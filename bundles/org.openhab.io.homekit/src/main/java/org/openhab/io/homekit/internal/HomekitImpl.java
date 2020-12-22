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
package org.openhab.io.homekit.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.common.ThreadPoolManager;
import org.openhab.core.config.core.ConfigurableService;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.MetadataRegistry;
import org.openhab.core.net.NetworkAddressService;
import org.openhab.core.storage.StorageService;
import org.openhab.io.homekit.Homekit;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.hapjava.accessories.HomekitAccessory;
import io.github.hapjava.server.impl.HomekitRoot;
import io.github.hapjava.server.impl.HomekitServer;
import io.github.hapjava.server.impl.crypto.HAPSetupCodeUtils;

/**
 * Provides access to openHAB items via the HomeKit API
 *
 * @author Andy Lintner - Initial contribution
 */
@Component(service = { Homekit.class }, configurationPid = HomekitSettings.CONFIG_PID, property = {
        Constants.SERVICE_PID + "=org.openhab.homekit", "port:Integer=9123" })
@ConfigurableService(category = "io", label = "HomeKit Integration", description_uri = "io:homekit")
@NonNullByDefault
public class HomekitImpl implements Homekit {
    private final Logger logger = LoggerFactory.getLogger(HomekitImpl.class);

    @Reference
    protected @NonNullByDefault({}) NetworkAddressService networkAddressService;
    @Reference
    protected @NonNullByDefault({}) ConfigurationAdmin configAdmin;
    @Reference
    protected @NonNullByDefault({}) ItemRegistry itemRegistry;
    @Reference
    protected @NonNullByDefault({}) StorageService storageService;
    @Reference
    protected @NonNullByDefault({}) MetadataRegistry metadataRegistry;

    private @NonNullByDefault({}) HomekitAuthInfoImpl authInfo;
    private @NonNullByDefault({}) HomekitSettings settings;
    private @Nullable InetAddress networkInterface;
    private @Nullable HomekitServer homekitServer;
    private @Nullable HomekitRoot bridge;
    private @NonNullByDefault({}) HomekitChangeListener changeListener;

    private final ScheduledExecutorService scheduler = ThreadPoolManager
            .getScheduledPool(ThreadPoolManager.THREAD_POOL_NAME_COMMON);

    public HomekitImpl() {
    }

    @Activate
    protected void activate(Map<String, Object> properties) {
        this.settings = processConfig(properties);
        this.changeListener = new HomekitChangeListener(itemRegistry, settings, metadataRegistry, storageService);
        try {
            authInfo = new HomekitAuthInfoImpl(storageService.getStorage(HomekitAuthInfoImpl.STORAGE_KEY), settings.pin,
                    settings.setupId);
            startHomekitServer();
        } catch (IOException | InvalidAlgorithmParameterException e) {
            logger.warn("Cannot activate HomeKit binding. {}", e.getMessage());
        }
    }

    private HomekitSettings processConfig(Map<String, Object> properties) {
        HomekitSettings settings = (new Configuration(properties)).as(HomekitSettings.class);
        org.osgi.service.cm.Configuration config = null;
        Dictionary<String, Object> props = null;
        try {
            config = configAdmin.getConfiguration(HomekitSettings.CONFIG_PID);
            props = config.getProperties();
        } catch (IOException e) {
            logger.warn("Cannot retrieve config admin {}", e.getMessage());
        }

        if (props == null) { // if null, the configuration is new
            props = new Hashtable<>();
        }
        if (settings.networkInterface == null) {
            settings.networkInterface = networkAddressService.getPrimaryIpv4HostAddress();
            props.put("networkInterface", settings.networkInterface);
        }
        if (settings.setupId == null) { // generate setupId very first time
            settings.setupId = HAPSetupCodeUtils.generateSetupId();
            props.put("setupId", settings.setupId);
        }

        // QR Code setup URI is always generated from PIN, setup ID and accessory category (1 = bridge)
        String setupURI = HAPSetupCodeUtils.getSetupURI(settings.pin.replaceAll("-", ""), settings.setupId, 1);
        if ((settings.qrCode == null) || (!settings.qrCode.equals(setupURI))) { // QR code was changed
            settings.qrCode = setupURI;
            props.put("qrCode", settings.qrCode);
        }

        if (config != null) {
            try {
                config.updateIfDifferent(props);
            } catch (IOException e) {
                logger.warn("Cannot update configuration {}", e.getMessage());
            }
        }
        return settings;
    }

    @Modified
    protected synchronized void modified(Map<String, Object> config) {
        try {
            HomekitSettings oldSettings = settings;
            settings = processConfig(config);
            changeListener.updateSettings(settings);
            if (!oldSettings.networkInterface.equals(settings.networkInterface) || oldSettings.port != settings.port) {
                // the HomeKit server settings changed. we do a complete re-init
                stopHomekitServer();
                startHomekitServer();
            } else if (!oldSettings.name.equals(settings.name) || !oldSettings.pin.equals(settings.pin)
                    || !oldSettings.setupId.equals(settings.setupId)) {
                stopHomekitServer();
                authInfo.setPin(settings.pin);
                authInfo.setSetupId(settings.setupId);
                startHomekitServer();
            }
        } catch (IOException e) {
            logger.warn("Could not initialize HomeKit connector: {}", e.getMessage());
        }
    }

    private void stopBridge() {
        final @Nullable HomekitRoot bridge = this.bridge;
        if (bridge != null) {
            changeListener.unsetBridge();
            bridge.stop();
            this.bridge = null;
        }
    }

    private void startBridge() throws IOException {
        final @Nullable HomekitServer homekitServer = this.homekitServer;
        if (homekitServer != null && bridge == null) {
            final HomekitRoot bridge = homekitServer.createBridge(authInfo, settings.name, HomekitSettings.MANUFACTURER,
                    HomekitSettings.MODEL, HomekitSettings.SERIAL_NUMBER,
                    FrameworkUtil.getBundle(getClass()).getVersion().toString(), HomekitSettings.HARDWARE_REVISION);
            changeListener.setBridge(bridge);
            this.bridge = bridge;
            bridge.setConfigurationIndex(changeListener.getConfigurationRevision());

            final int lastAccessoryCount = changeListener.getLastAccessoryCount();
            int currentAccessoryCount = changeListener.getAccessories().size();
            if (currentAccessoryCount < lastAccessoryCount) {
                logger.debug(
                        "It looks like not all items were initialized yet. Old configuration had {} accessories, the current one has only {} accessories. Delay HomeKit bridge start for {} seconds.",
                        lastAccessoryCount, currentAccessoryCount, settings.startDelay);
                scheduler.schedule(() -> {
                    if (currentAccessoryCount < lastAccessoryCount) {
                        // the number of items is still different, maybe it is desired.
                        // make new configuration revision.
                        changeListener.makeNewConfigurationRevision();
                    }
                    bridge.start();
                }, settings.startDelay, TimeUnit.SECONDS);
            } else { // start bridge immediately.
                bridge.start();
            }
        } else {
            logger.warn(
                    "trying to start bridge but HomeKit server is not initialized or bridge is already initialized");
        }
    }

    private void startHomekitServer() throws IOException {
        if (homekitServer == null) {
            networkInterface = InetAddress.getByName(settings.networkInterface);
            homekitServer = new HomekitServer(networkInterface, settings.port);
            startBridge();
        } else {
            logger.warn("trying to start HomeKit server but it is already initialized");
        }
    }

    private void stopHomekitServer() {
        final @Nullable HomekitServer homekit = this.homekitServer;
        if (homekit != null) {
            if (bridge != null) {
                stopBridge();
            }
            homekit.stop();
            this.homekitServer = null;
        }
    }

    @Deactivate
    protected void deactivate() {
        changeListener.clearAccessories();
        stopHomekitServer();
        changeListener.stop();
    }

    @Override
    public void refreshAuthInfo() throws IOException {
        final @Nullable HomekitRoot bridge = this.bridge;
        if (bridge != null) {
            bridge.refreshAuthInfo();
        }
    }

    @Override
    public void allowUnauthenticatedRequests(boolean allow) {
        final @Nullable HomekitRoot bridge = this.bridge;
        if (bridge != null) {
            bridge.allowUnauthenticatedRequests(allow);
        }
    }

    @Override
    public List<HomekitAccessory> getAccessories() {
        return new ArrayList<>(this.changeListener.getAccessories().values());
    }

    @Override
    public void clearHomekitPairings() {
        try {
            authInfo.clear();
            refreshAuthInfo();
        } catch (Exception e) {
            logger.warn("Could not clear HomeKit pairings", e);
        }
    }
}
