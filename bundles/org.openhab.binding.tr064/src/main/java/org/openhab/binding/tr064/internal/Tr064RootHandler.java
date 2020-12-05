/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.tr064.internal;

import static org.openhab.binding.tr064.internal.Tr064BindingConstants.THING_TYPE_FRITZBOX;
import static org.openhab.binding.tr064.internal.Tr064BindingConstants.THING_TYPE_GENERIC;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Authentication;
import org.eclipse.jetty.client.api.AuthenticationStore;
import org.eclipse.jetty.client.util.DigestAuthentication;
import org.openhab.binding.tr064.internal.config.Tr064ChannelConfig;
import org.openhab.binding.tr064.internal.config.Tr064RootConfiguration;
import org.openhab.binding.tr064.internal.dto.scpd.root.SCPDDeviceType;
import org.openhab.binding.tr064.internal.dto.scpd.root.SCPDServiceType;
import org.openhab.binding.tr064.internal.dto.scpd.service.SCPDActionType;
import org.openhab.binding.tr064.internal.phonebook.Phonebook;
import org.openhab.binding.tr064.internal.phonebook.PhonebookProvider;
import org.openhab.binding.tr064.internal.phonebook.Tr064PhonebookImpl;
import org.openhab.binding.tr064.internal.soap.SOAPConnector;
import org.openhab.binding.tr064.internal.soap.SOAPValueConverter;
import org.openhab.binding.tr064.internal.util.SCPDUtil;
import org.openhab.binding.tr064.internal.util.Util;
import org.openhab.core.cache.ExpiringCacheMap;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link Tr064RootHandler} is responsible for handling commands, which are
 * sent to one of the channels and update channel values
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
public class Tr064RootHandler extends BaseBridgeHandler implements PhonebookProvider {
    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Set.of(THING_TYPE_GENERIC, THING_TYPE_FRITZBOX);
    private static final int RETRY_INTERVAL = 60;
    private static final Set<String> PROPERTY_ARGUMENTS = Set.of("NewSerialNumber", "NewSoftwareVersion",
            "NewModelName");

    private final Logger logger = LoggerFactory.getLogger(Tr064RootHandler.class);
    private final HttpClient httpClient;

    private Tr064RootConfiguration config = new Tr064RootConfiguration();
    private String deviceType = "";

    private @Nullable SCPDUtil scpdUtil;
    private SOAPConnector soapConnector;
    private String endpointBaseURL = "http://fritz.box:49000";

    private final Map<ChannelUID, Tr064ChannelConfig> channels = new HashMap<>();
    // caching is used to prevent excessive calls to the same action
    private final ExpiringCacheMap<ChannelUID, State> stateCache = new ExpiringCacheMap<>(2000);
    private Collection<Phonebook> phonebooks = Collections.emptyList();

    private @Nullable ScheduledFuture<?> connectFuture;
    private @Nullable ScheduledFuture<?> pollFuture;
    private @Nullable ScheduledFuture<?> phonebookFuture;

    Tr064RootHandler(Bridge bridge, HttpClient httpClient) {
        super(bridge);
        this.httpClient = httpClient;
        soapConnector = new SOAPConnector(httpClient, endpointBaseURL);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        Tr064ChannelConfig channelConfig = channels.get(channelUID);
        if (channelConfig == null) {
            logger.trace("Channel {} not supported.", channelUID);
            return;
        }

        if (command instanceof RefreshType) {
            SOAPConnector soapConnector = this.soapConnector;
            State state = stateCache.putIfAbsentAndGet(channelUID,
                    () -> soapConnector.getChannelStateFromDevice(channelConfig, channels, stateCache));
            if (state != null) {
                updateState(channelUID, state);
            }
            return;
        }

        if (channelConfig.getChannelTypeDescription().getSetAction() == null) {
            logger.debug("Discarding command {} to {}, read-only channel", command, channelUID);
            return;
        }
        scheduler.execute(() -> soapConnector.sendChannelCommandToDevice(channelConfig, command));
    }

    @Override
    public void initialize() {
        config = getConfigAs(Tr064RootConfiguration.class);
        if (!config.isValid()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "At least one mandatory configuration field is empty");
            return;
        }

        endpointBaseURL = "http://" + config.host + ":49000";
        updateStatus(ThingStatus.UNKNOWN);

        connectFuture = scheduler.scheduleWithFixedDelay(this::internalInitialize, 0, RETRY_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * internal thing initializer (sets SCPDUtil and connects to remote device)
     */
    private void internalInitialize() {
        try {
            scpdUtil = new SCPDUtil(httpClient, endpointBaseURL);
        } catch (SCPDException e) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "could not get device definitions from " + config.host);
            return;
        }

        if (establishSecureConnectionAndUpdateProperties()) {
            removeConnectScheduler();

            // connection successful, check channels
            ThingBuilder thingBuilder = editThing();
            thingBuilder.withoutChannels(thing.getChannels());
            final SCPDUtil scpdUtil = this.scpdUtil;
            if (scpdUtil != null) {
                Util.checkAvailableChannels(thing, thingBuilder, scpdUtil, "", deviceType, channels);
                updateThing(thingBuilder.build());
            }

            installPolling();
            updateStatus(ThingStatus.ONLINE, ThingStatusDetail.NONE);
        }
    }

    private void removeConnectScheduler() {
        final ScheduledFuture<?> connectFuture = this.connectFuture;
        if (connectFuture != null) {
            connectFuture.cancel(true);
            this.connectFuture = null;
        }
    }

    @Override
    public void dispose() {
        removeConnectScheduler();
        uninstallPolling();
        stateCache.clear();

        super.dispose();
    }

    /**
     * poll remote device for channel values
     */
    private void poll() {
        channels.forEach((channelUID, channelConfig) -> {
            if (isLinked(channelUID)) {
                State state = stateCache.putIfAbsentAndGet(channelUID,
                        () -> soapConnector.getChannelStateFromDevice(channelConfig, channels, stateCache));
                if (state != null) {
                    updateState(channelUID, state);
                }
            }
        });
    }

    /**
     * establish the connection - get secure port (if avallable), install authentication, get device properties
     *
     * @return true if successful
     */
    private boolean establishSecureConnectionAndUpdateProperties() {
        final SCPDUtil scpdUtil = this.scpdUtil;
        if (scpdUtil != null) {
            try {
                SCPDDeviceType device = scpdUtil.getDevice("")
                        .orElseThrow(() -> new SCPDException("Root device not found"));
                SCPDServiceType deviceService = device.getServiceList().stream()
                        .filter(service -> service.getServiceId().equals("urn:DeviceInfo-com:serviceId:DeviceInfo1"))
                        .findFirst().orElseThrow(() -> new SCPDException(
                                "service 'urn:DeviceInfo-com:serviceId:DeviceInfo1' not found"));

                this.deviceType = device.getDeviceType();

                // try to get security (https) port
                SOAPMessage soapResponse = soapConnector.doSOAPRequest(deviceService, "GetSecurityPort",
                        Collections.emptyMap());
                if (!soapResponse.getSOAPBody().hasFault()) {
                    SOAPValueConverter soapValueConverter = new SOAPValueConverter(httpClient);
                    soapValueConverter.getStateFromSOAPValue(soapResponse, "NewSecurityPort", null)
                            .ifPresentOrElse(port -> {
                                endpointBaseURL = "https://" + config.host + ":" + port.toString();
                                soapConnector = new SOAPConnector(httpClient, endpointBaseURL);
                                logger.debug("endpointBaseURL is now '{}'", endpointBaseURL);
                            }, () -> logger.warn("Could not determine secure port, disabling https"));
                } else {
                    logger.warn("Could not determine secure port, disabling https");
                }

                // clear auth cache and force re-auth
                httpClient.getAuthenticationStore().clearAuthenticationResults();
                AuthenticationStore auth = httpClient.getAuthenticationStore();
                auth.addAuthentication(new DigestAuthentication(new URI(endpointBaseURL), Authentication.ANY_REALM,
                        config.user, config.password));

                // check & update properties
                SCPDActionType getInfoAction = scpdUtil.getService(deviceService.getServiceId())
                        .orElseThrow(() -> new SCPDException(
                                "Could not get service definition for 'urn:DeviceInfo-com:serviceId:DeviceInfo1'"))
                        .getActionList().stream().filter(action -> action.getName().equals("GetInfo")).findFirst()
                        .orElseThrow(() -> new SCPDException("Action 'GetInfo' not found"));
                SOAPMessage soapResponse1 = soapConnector.doSOAPRequest(deviceService, getInfoAction.getName(),
                        Collections.emptyMap());
                SOAPValueConverter soapValueConverter = new SOAPValueConverter(httpClient);
                Map<String, String> properties = editProperties();
                PROPERTY_ARGUMENTS.forEach(argumentName -> getInfoAction.getArgumentList().stream()
                        .filter(argument -> argument.getName().equals(argumentName)).findFirst()
                        .ifPresent(argument -> soapValueConverter
                                .getStateFromSOAPValue(soapResponse1, argumentName, null).ifPresent(value -> properties
                                        .put(argument.getRelatedStateVariable(), value.toString()))));
                properties.put("deviceType", device.getDeviceType());
                updateProperties(properties);

                return true;
            } catch (SCPDException | SOAPException | Tr064CommunicationException | URISyntaxException e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * get all sub devices of this root device (used for discovery)
     *
     * @return the list
     */
    public List<SCPDDeviceType> getAllSubDevices() {
        final SCPDUtil scpdUtil = this.scpdUtil;
        return (scpdUtil == null) ? Collections.emptyList() : scpdUtil.getAllSubDevices();
    }

    /**
     * get the SOAP connector (used by sub devices for communication with the remote device)
     *
     * @return the SOAP connector
     */
    public SOAPConnector getSOAPConnector() {
        return soapConnector;
    }

    /**
     * get the SCPD processing utility
     *
     * @return the SCPD utility (or null if not available)
     */
    public @Nullable SCPDUtil getSCPDUtil() {
        return scpdUtil;
    }

    /**
     * uninstall the polling
     */
    private void uninstallPolling() {
        final ScheduledFuture<?> pollFuture = this.pollFuture;
        if (pollFuture != null) {
            pollFuture.cancel(true);
            this.pollFuture = null;
        }
        final ScheduledFuture<?> phonebookFuture = this.phonebookFuture;
        if (phonebookFuture != null) {
            phonebookFuture.cancel(true);
            this.phonebookFuture = null;
        }
    }

    /**
     * install the polling
     */
    private void installPolling() {
        uninstallPolling();
        pollFuture = scheduler.scheduleWithFixedDelay(this::poll, 0, config.refresh, TimeUnit.SECONDS);
        if (config.phonebookInterval > 0) {
            phonebookFuture = scheduler.scheduleWithFixedDelay(this::retrievePhonebooks, 0, config.phonebookInterval,
                    TimeUnit.SECONDS);
        }
    }

    @SuppressWarnings("unchecked")
    private Collection<Phonebook> processPhonebookList(SOAPMessage soapMessagePhonebookList,
            SCPDServiceType scpdService) {
        SOAPValueConverter soapValueConverter = new SOAPValueConverter(httpClient);
        return (Collection<Phonebook>) soapValueConverter
                .getStateFromSOAPValue(soapMessagePhonebookList, "NewPhonebookList", null)
                .map(phonebookList -> Arrays.stream(phonebookList.toString().split(","))).orElse(Stream.empty())
                .map(index -> {
                    try {
                        SOAPMessage soapMessageURL = soapConnector.doSOAPRequest(scpdService, "GetPhonebook",
                                Map.of("NewPhonebookID", index));
                        return soapValueConverter.getStateFromSOAPValue(soapMessageURL, "NewPhonebookURL", null)
                                .map(url -> (Phonebook) new Tr064PhonebookImpl(httpClient, url.toString()));
                    } catch (Tr064CommunicationException e) {
                        logger.warn("Failed to get phonebook with index {}:", index, e);
                    }
                    return Optional.empty();
                }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    private void retrievePhonebooks() {
        String serviceId = "urn:X_AVM-DE_OnTel-com:serviceId:X_AVM-DE_OnTel1";
        SCPDUtil scpdUtil = this.scpdUtil;
        if (scpdUtil == null) {
            logger.warn("Cannot find SCPDUtil. This is most likely a programming error.");
            return;
        }
        Optional<SCPDServiceType> scpdService = scpdUtil.getDevice("").flatMap(deviceType -> deviceType.getServiceList()
                .stream().filter(service -> service.getServiceId().equals(serviceId)).findFirst());

        phonebooks = scpdService.map(service -> {
            try {
                return processPhonebookList(
                        soapConnector.doSOAPRequest(service, "GetPhonebookList", Collections.emptyMap()), service);
            } catch (Tr064CommunicationException e) {
                return Collections.<Phonebook> emptyList();
            }
        }).orElse(Collections.emptyList());

        if (phonebooks.isEmpty()) {
            logger.warn("Could not get phonebooks for thing {}", thing.getUID());
        }
    }

    @Override
    public Optional<Phonebook> getPhonebookByName(String name) {
        return phonebooks.stream().filter(p -> name.equals(p.getName())).findAny();
    }

    @Override
    public Collection<Phonebook> getPhonebooks() {
        return phonebooks;
    }

    @Override
    public ThingUID getUID() {
        return thing.getUID();
    }

    @Override
    public String getFriendlyName() {
        String friendlyName = thing.getLabel();
        return friendlyName != null ? friendlyName : getUID().getId();
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Set.of(Tr064DiscoveryService.class);
    }
}
