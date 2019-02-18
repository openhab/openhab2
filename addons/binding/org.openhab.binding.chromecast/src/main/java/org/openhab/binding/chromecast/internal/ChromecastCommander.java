/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.chromecast.internal;

import static org.eclipse.smarthome.core.thing.ThingStatusDetail.COMMUNICATION_ERROR;
import static org.openhab.binding.chromecast.internal.ChromecastBindingConstants.*;

import java.io.IOException;

import org.eclipse.smarthome.core.library.types.IncreaseDecreaseType;
import org.eclipse.smarthome.core.library.types.NextPreviousType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.PlayPauseType;
import org.eclipse.smarthome.core.library.types.StopMoveType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.litvak.chromecast.api.v2.Application;
import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.MediaStatus;
import su.litvak.chromecast.api.v2.Status;

/**
 * This sends the various commands to the Chromecast.
 *
 * @author Jason Holmes - Initial contribution
 */
public class ChromecastCommander {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ChromeCast chromeCast;
    private final ChromecastScheduler scheduler;
    private final ChromecastStatusUpdater statusUpdater;

    private static final int VOLUMESTEP = 10;

    public ChromecastCommander(ChromeCast chromeCast, ChromecastScheduler scheduler,
            ChromecastStatusUpdater statusUpdater) {
        this.chromeCast = chromeCast;
        this.scheduler = scheduler;
        this.statusUpdater = statusUpdater;
    }

    public void handleCommand(final ChannelUID channelUID, final Command command) {
        if (chromeCast == null) {
            return;
        }

        if (command instanceof RefreshType) {
            scheduler.scheduleRefresh();
            return;
        }

        switch (channelUID.getId()) {
            case CHANNEL_CONTROL:
                handleControl(command);
                break;
            case CHANNEL_VOLUME:
                handleVolume(command);
                break;
            case CHANNEL_MUTE:
                handleMute(command);
                break;
            case CHANNEL_PLAY_URI:
                handlePlayUri(command);
                break;
            default:
                logger.debug("Received command {} for unknown channel: {}", command, channelUID);
                break;
        }
    }

    public void handleRefresh() {
        if (!chromeCast.isConnected()) {
            scheduler.cancelRefresh();
            scheduler.scheduleConnect();
            return;
        }

        Status status;
        try {
            status = chromeCast.getStatus();
            statusUpdater.processStatusUpdate(status);

            if (status == null) {
                scheduler.cancelRefresh();
            }
        } catch (IOException ex) {
            logger.debug("Failed to request status: {}", ex.getMessage());
            statusUpdater.updateStatus(ThingStatus.OFFLINE, COMMUNICATION_ERROR, ex.getMessage());
            scheduler.cancelRefresh();
            return;
        }

        try {
            if (status != null && status.getRunningApp() != null) {
                MediaStatus mediaStatus = chromeCast.getMediaStatus();
                statusUpdater.updateMediaStatus(mediaStatus);

                if (mediaStatus != null && mediaStatus.playerState == MediaStatus.PlayerState.IDLE
                        && mediaStatus.idleReason != null
                        && mediaStatus.idleReason != MediaStatus.IdleReason.INTERRUPTED) {
                    stopMediaPlayerApp();
                }
            }
        } catch (IOException ex) {
            logger.debug("Failed to request media status with a running app: {}", ex.getMessage());
            // We were just able to request status, so let's not put the device OFFLINE.
        }
    }

    private void handlePlayUri(Command command) {
        if (command instanceof StringType) {
            playMedia(null, command.toString(), null);
        }
    }

    private void handleControl(final Command command) {
        try {
            if (command instanceof NextPreviousType) {
                // I can't find a way to control next/previous from the API. The Google app doesn't seem to
                // allow it either, so I suspect there isn't a way.
                logger.info("{} command not yet implemented", command);
                return;
            }

            Application app = chromeCast.getRunningApp();
            statusUpdater.updateStatus(ThingStatus.ONLINE);
            if (app == null) {
                logger.debug("{} command ignored because media player app is not running", command);
                return;
            }

            if (command instanceof StopMoveType) {
                final StopMoveType stopMoveType = (StopMoveType) command;
                if (stopMoveType == StopMoveType.STOP) {
                    chromeCast.stopApp();
                    statusUpdater.updateMediaStatus(null);
                }
                return;
            }

            if (command instanceof PlayPauseType) {
                MediaStatus mediaStatus = chromeCast.getMediaStatus();
                logger.debug("mediaStatus {}", mediaStatus);
                if (mediaStatus == null || mediaStatus.playerState == MediaStatus.PlayerState.IDLE) {
                    logger.debug("{} command ignored because media is not loaded", command);
                    return;
                }

                final PlayPauseType playPause = (PlayPauseType) command;
                if (playPause == PlayPauseType.PLAY) {
                    chromeCast.play();
                } else if (playPause == PlayPauseType.PAUSE
                        && ((mediaStatus.supportedMediaCommands & 0x00000001) == 0x1)) {
                    chromeCast.pause();
                } else {
                    logger.info("{} command not supported by current media", command);
                }
            }
        } catch (final Exception e) {
            logger.debug("{} command failed: {}", command, e.getMessage());
            statusUpdater.updateStatus(ThingStatus.OFFLINE, COMMUNICATION_ERROR, e.getMessage());
        }
    }

    public void handleVolume(final Command command) {
        if (command instanceof PercentType) {
            setVolumeInternal((PercentType) command);
        } else if (command == IncreaseDecreaseType.INCREASE) {
            setVolumeInternal(new PercentType(
                    Math.max(statusUpdater.getVolume().intValue() + VOLUMESTEP, PercentType.ZERO.intValue())));
        } else if (command == IncreaseDecreaseType.DECREASE) {
            setVolumeInternal(new PercentType(
                    Math.min(statusUpdater.getVolume().intValue() - VOLUMESTEP, PercentType.HUNDRED.intValue())));
        }
    }

    private void setVolumeInternal(PercentType volume) {
        try {
            chromeCast.setVolumeByIncrement(volume.floatValue() / 100);
            statusUpdater.updateStatus(ThingStatus.ONLINE);
        } catch (final IOException ex) {
            logger.debug("Set volume failed: {}", ex.getMessage());
            statusUpdater.updateStatus(ThingStatus.OFFLINE, COMMUNICATION_ERROR, ex.getMessage());
        }
    }

    private void handleMute(final Command command) {
        if (command instanceof OnOffType) {
            final boolean mute = command == OnOffType.ON;
            try {
                chromeCast.setMuted(mute);
                statusUpdater.updateStatus(ThingStatus.ONLINE);
            } catch (final IOException ex) {
                logger.debug("Mute/unmute volume failed: {}", ex.getMessage());
                statusUpdater.updateStatus(ThingStatus.OFFLINE, COMMUNICATION_ERROR, ex.getMessage());
            }
        }
    }

    void playMedia(String title, String url, String mimeType) {
        try {
            if (chromeCast.isAppAvailable(MEDIA_PLAYER)) {
                if (!chromeCast.isAppRunning(MEDIA_PLAYER)) {
                    final Application app = chromeCast.launchApp(MEDIA_PLAYER);
                    statusUpdater.setAppSessionId(app.sessionId);
                    logger.debug("Application launched: {}", app);
                }
                if (url != null) {
                    // If the current track is paused, launching a new request results in nothing happening, therefore
                    // resume current track.
                    MediaStatus ms = chromeCast.getMediaStatus();
                    if (ms != null && MediaStatus.PlayerState.PAUSED == ms.playerState && url.equals(ms.media.url)) {
                        logger.debug("Current stream paused, resuming");
                        chromeCast.play();
                    } else {
                        chromeCast.load(title, null, url, mimeType);
                    }
                }
            } else {
                logger.warn("Missing media player app - cannot process media.");
            }
            statusUpdater.updateStatus(ThingStatus.ONLINE);
        } catch (final IOException e) {
            logger.debug("Failed playing media: {}", e.getMessage());
            statusUpdater.updateStatus(ThingStatus.OFFLINE, COMMUNICATION_ERROR, e.getMessage());
        }
    }

    private void stopMediaPlayerApp() {
        try {
            Application app = chromeCast.getRunningApp();
            if (app.id.equals(MEDIA_PLAYER) && app.sessionId.equals(statusUpdater.getAppSessionId())) {
                chromeCast.stopApp();
                logger.debug("Media player app stopped");
            }
        } catch (final Exception e) {
            logger.debug("Failed stopping media player app: {}", e);
        }
    }
}
