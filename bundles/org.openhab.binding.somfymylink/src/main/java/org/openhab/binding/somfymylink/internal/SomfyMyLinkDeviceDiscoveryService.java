package org.openhab.binding.somfymylink.internal;

import static org.openhab.binding.somfymylink.internal.SomfyMyLinkBindingConstants.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SomfyMyLinkDeviceDiscoveryService extends AbstractDiscoveryService {

    private static final int DISCOVERY_TIMEOUT_SEC = 10;
    private static final int DISCOVERY_REFRESH_SEC = 1800;

    private final Logger logger = LoggerFactory.getLogger(SomfyMyLinkDeviceDiscoveryService.class);

    private ScheduledFuture<?> scanTask;
    private SomfyMyLinkHandler mylinkHandler;

    @Nullable
    private ScheduledFuture<?> discoveryJob;

    public SomfyMyLinkDeviceDiscoveryService(SomfyMyLinkHandler mylinkHandler) throws IllegalArgumentException {
        super(SomfyMyLinkHandlerFactory.DISCOVERABLE_DEVICE_TYPES_UIDS, 10);

        this.mylinkHandler = mylinkHandler;
    }

    @Override
    @Activate
    public void activate(@Nullable Map<@NonNull String, @Nullable Object> configProperties) {
        super.activate(configProperties);
    }

    @Override
    @Deactivate
    public void deactivate() {
        super.deactivate();
    }

    @Override
    protected void startBackgroundDiscovery() {
        logger.debug("Starting Somfy My Link background discovery");

        if (discoveryJob == null || discoveryJob.isCancelled()) {
            discoveryJob = scheduler.scheduleWithFixedDelay(this::runDiscovery, 10, DISCOVERY_REFRESH_SEC,
                    TimeUnit.SECONDS);
        }

    }

    @Override
    protected void stopBackgroundDiscovery() {
        logger.debug("Stopping Somfy MyLink background discovery");
        if (discoveryJob != null && !discoveryJob.isCancelled()) {
            discoveryJob.cancel(true);
            discoveryJob = null;
        }
    }

    @Override
    protected void startScan() {
        runDiscovery();
    }

    private synchronized void runDiscovery() {
        logger.debug("Starting scanning for things...");

        if (this.scanTask == null || this.scanTask.isDone()) {
            this.scanTask = scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        discoverDevices();
                    } catch (Exception e) {
                        logger.error("Error scanning for devices", e);

                        if (scanListener != null) {
                            scanListener.onErrorOccurred(e);
                        }
                    }
                }
            }, 0, TimeUnit.SECONDS);
        }
    }

    private void discoverDevices() throws IOException, SomfyMyLinkException {
        // get the shade list
        SomfyMyLinkShade[] shades = this.mylinkHandler.getShadeList();

        if (shades != null) {
            for (SomfyMyLinkShade shade : shades) {
                String id = shade.getTargetID();
                String label = "Somfy Shade " + shade.getName();
                logger.info("Adding device {}", id);
                notifyShadeDiscovery(THING_TYPE_SHADE, id, label);
            }
        }

        SomfyMyLinkScene[] scenes = this.mylinkHandler.getSceneList();

        if (scenes != null) {
            for (SomfyMyLinkScene scene : scenes) {
                String id = scene.getTargetID();
                String label = "Somfy Scene " + scene.getName();
                logger.info("Adding device {}", id);
                notifySceneDiscovery(THING_TYPE_SCENE, id, label);
            }
        }

    }

    private void notifyShadeDiscovery(ThingTypeUID thingTypeUID, String targetId, String label) {
        if (targetId == null) {
            logger.info("Discovered {} with no integration ID", label);
            return;
        }

        ThingUID bridgeUID = this.mylinkHandler.getThing().getUID();
        ThingUID uid = new ThingUID(thingTypeUID, bridgeUID, targetId);

        Map<String, Object> properties = new HashMap<>();

        properties.put(TARGET_ID, targetId);

        DiscoveryResult result = DiscoveryResultBuilder.create(uid).withBridge(bridgeUID).withLabel(label)
                .withProperties(properties).withRepresentationProperty(TARGET_ID).build();

        thingDiscovered(result);

        logger.debug("Discovered {}", uid);
    }

    private void notifySceneDiscovery(ThingTypeUID thingTypeUID, String sceneId, String label) {
        if (sceneId == null) {
            logger.info("Discovered {} with no scene ID", label);
            return;
        }

        ThingUID bridgeUID = this.mylinkHandler.getThing().getUID();
        ThingUID uid = new ThingUID(thingTypeUID, bridgeUID, sceneId);

        Map<String, Object> properties = new HashMap<>();

        properties.put(SCENE_ID, sceneId);

        DiscoveryResult result = DiscoveryResultBuilder.create(uid).withBridge(bridgeUID).withLabel(label)
                .withProperties(properties).withRepresentationProperty(SCENE_ID).build();

        thingDiscovered(result);

        logger.debug("Discovered {}", uid);
    }
}
