package org.openhab.binding.mqtt.handler;

import static org.openhab.binding.mqtt.MqttBindingConstants.*;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.io.transport.mqtt.MqttService;
import org.openhab.binding.mqtt.internal.MqttMessageSubscriber;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MqttBridgeHandler} is responsible for handling connection to MQTT service
 *
 * @author Marcus of Wetware Labs - Initial contribution
 */
public class MqttBridgeHandler extends BaseBridgeHandler {

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(THING_TYPE_BRIDGE);

    private Logger logger = LoggerFactory.getLogger(MqttBridgeHandler.class);

    private List<MqttBridgeListener> mqttBridgeListeners = new CopyOnWriteArrayList<>();

    private String broker;

    /** MqttService for sending/receiving messages **/
    private MqttService mqttService;

    public MqttBridgeHandler(Bridge mqttBridge) {
        super(mqttBridge);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // TODO Auto-generated method stub

    }

    /**
     * Setter for Declarative Services. Adds the MqttService instance.
     *
     * @param mqttService
     *            Service.
     */
    // public void setMqttService(MqttService mqttService) {
    // this.mqttService = mqttService;
    // }

    /**
     * Unsetter for Declarative Services.
     *
     * @param mqttService
     *            MqttService to remove.
     */
    // public void unsetMqttService(MqttService mqttService) {
    // this.mqttService = null;
    // }

    String getBroker() {
        return broker;
    }

    public void registerMessageConsumer(MqttMessageSubscriber subscriber) {
        mqttService.registerMessageConsumer(broker, subscriber);
    }

    @Override
    public void initialize() {
        logger.debug("Initializing MQTT bridge handler.");

        // get reference to org.eclipse.smarthome.io.transport.mqtt service
        ServiceReference<ManagedService> mqttServiceReference = (ServiceReference<ManagedService>) bundleContext
                .getServiceReference(MqttService.class.getName());
        mqttService = (MqttService) bundleContext.getService(mqttServiceReference);

        // final String broker = this.getThing().getBridgeUID().segments[2];
        broker = (String) getConfig().get(BROKER);

        if (broker != null) {
            try {

                // get reference to ConfigurationAdmin and update the configuration of io.transport.mqtt service (PID is
                // actually org.eclipse.smarthome.mqtt)
                ServiceReference<ConfigurationAdmin> configurationAdminReference = (ServiceReference<ConfigurationAdmin>) bundleContext
                        .getServiceReference(ConfigurationAdmin.class.getName());
                if (configurationAdminReference != null) {
                    ConfigurationAdmin confAdmin = bundleContext.getService(configurationAdminReference);

                    Configuration mqttServiceConf = confAdmin.getConfiguration(MQTT_SERVICE_PID);
                    Dictionary<String, Object> properties = mqttServiceConf.getProperties();
                    if (properties == null) {
                        properties = new Hashtable<String, Object>();
                        properties.put("service.pid", MQTT_SERVICE_PID); // initialize the PID. Is this necessary?
                    }

                    if (getConfig().get(URL) != null) {
                        properties.put(BROKER + ":" + URL, getConfig().get(URL));
                    }
                    if (getConfig().get(USER) != null) {
                        properties.put(BROKER + ":" + USER, getConfig().get(USER));
                    }
                    if (getConfig().get(PWD) != null) {
                        properties.put(BROKER + ":" + PWD, getConfig().get(PWD));
                    }
                    if (getConfig().get(CLIENTID) != null) {
                        properties.put(BROKER + ":" + CLIENTID, getConfig().get(CLIENTID));
                    }
                    mqttServiceConf.update(properties);
                    updateStatus(ThingStatus.ONLINE);
                }
            } catch (Exception e) {
                logger.error("Failed to set MQTT broker properties");
            }

        }

    }

    @Override
    public void dispose() {
        logger.debug("Handler disposed.");
    }

    private synchronized void onUpdate() {
    }

    public boolean registerMqttBridgeListener(MqttBridgeListener mqttBridgeListener) {
        if (mqttBridgeListener == null) {
            throw new NullPointerException("It's not allowed to pass a null mqttBridgeListener.");
        }
        boolean result = mqttBridgeListeners.add(mqttBridgeListener);
        if (result) {
            onUpdate();
        }
        return result;
    }

}
