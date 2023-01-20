/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.freeboxos.internal.api.rest;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.freeboxos.internal.api.FreeboxException;
import org.openhab.binding.freeboxos.internal.api.Response;
import org.openhab.binding.freeboxos.internal.api.rest.FreeboxOsSession.BoxModel;
import org.openhab.binding.freeboxos.internal.api.rest.LoginManager.Session.Permission;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails.AudioTrack;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails.MediaState;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails.Metadata;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails.Metadata.PlaybackState;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails.Metadata.SubtitleTrack;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails.Metadata.SubtitleTrack.Type;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails.Metadata.VideoTrack;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.PlayerContext.PlayerDetails.Metadata.VideoTrack.Framerate;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.Status.PowerState;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.TvContext.Channel;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.TvContext.Channel.BouquetType;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.TvContext.Channel.ChannelType;
import org.openhab.binding.freeboxos.internal.api.rest.PlayerManager.TvContext.Channel.Service;
import org.openhab.binding.freeboxos.internal.api.rest.SystemManager.ModelInfo;

import com.google.gson.annotations.SerializedName;

import inet.ipaddr.mac.MACAddress;

/**
 * The {@link PlayerManager} is the Java class used to handle api requests related to player
 *
 * @author Gaël L'hopital - Initial contribution
 */
@NonNullByDefault
public class PlayerManager extends ListableRest<PlayerManager.Player, PlayerManager.PlayerResponse> {
    public static final String STATUS_PATH = "status";
    private static final String PATH = "player";

    public static class PlayerResponse extends Response<Player> {
    }

    public static record Player(MACAddress mac, StbType stbType, int id, ZonedDateTime lastTimeReachable,
            boolean apiAvailable, String deviceName, BoxModel deviceModel, boolean reachable, String uid,
            @Nullable String apiVersion, List<String> lanGids) {
        private static enum StbType {
            STB_ANDROID,
            STB_V6,
            STB_V7,
            STB_V8,
            UNKNOWN;
        }

        /**
         * @return a string like eg : '17/api/v8'
         */
        public String baseUrl() {
            return "%d/api/v%s/".formatted(id, apiVersion.split("\\.")[0]);
        }
    }

    private static class StatusResponse extends Response<Status> {
    }

    public static record Status(PowerState powerState, StatusInformation player,
            @Nullable ForegroundApp foregroundApp) {
        public static enum PowerState {
            STANDBY,
            RUNNING,
            UNKNOWN;
        }

        public @Nullable ForegroundApp foregroundApp() {
            return foregroundApp;
        }
    }

    public static record ForegroundApp(int packageId, @Nullable String curlUrl, @Nullable Object context,
            @SerializedName(value = "package") String _package) {
    }

    public static record StatusInformation(String name, ZonedDateTime lastActivity) {
    }

    private static class ConfigurationResponse extends Response<Configuration> {
    }

    public static record Configuration(String boardName, boolean configured, String firmwareVersion,
            @Nullable ModelInfo modelInfo, String serial, String uptime, long uptimeVal) {
    }

    public static record PlayerContext(@Nullable PlayerDetails player) {

        public static record PlayerDetails(@SerializedName("audioIndex") int audioIndex,
                @SerializedName("audioList") List<AudioTrack> audioList, @SerializedName("curPos") long curPos,
                int duration, @SerializedName("livePos") long livePos, @SerializedName("maxPos") long maxPos,
                @SerializedName("mediaState") MediaState mediaState, @Nullable Metadata metadata,
                @SerializedName("minPos") long minPos, @SerializedName("playbackState") PlaybackState playbackState,
                long position, @Nullable String source, @SerializedName("subtitleIndex") int subtitleIndex,
                @SerializedName("subtitleList") List<SubtitleTrack> subtitleList,
                @SerializedName("videoIndex") int videoIndex, @SerializedName("videoList") List<VideoTrack> videoList) {

            public static record AudioTrack(int bitrate, @SerializedName("channelCount") int channelCount,
                    @Nullable String codec, @SerializedName("codecId") @Nullable String codecId,
                    @Nullable String language, @SerializedName("metadataId") @Nullable String metadataId, int pid,
                    int samplerate, long uid) {
            }

            public enum MediaState {
                READY,
                UNKNOWN;
            }

            public static record Metadata(@Nullable String album,
                    @SerializedName("albumArtist") @Nullable String albumArtist, @Nullable String artist,
                    @Nullable String author, int bpm, @Nullable String comment, boolean compilation,
                    @Nullable String composer, @Nullable String container, @Nullable String copyright, long date,
                    @SerializedName("discId") @Nullable String discId, @SerializedName("discNumber") int discNumber,
                    @SerializedName("discTotal") int discTotal, @Nullable String genre,
                    @SerializedName("musicbrainzDiscId") @Nullable String musicbrainzDiscId, @Nullable String performer,
                    @Nullable String title, @SerializedName("trackNumber") int trackNumber,
                    @SerializedName("trackTotal") int trackTotal, @Nullable String url) {

                public static enum PlaybackState {
                    PLAY,
                    PAUSE,
                    UNKNOWN;
                }

                public static record SubtitleTrack(@Nullable String codec, @Nullable String language,
                        @Nullable String pid, Type type, @Nullable String uid) {
                    public static enum Type {
                        NORMAL,
                        HEARINGIMPAIRED,
                        UNKNOWN;
                    }

                }

                public static record VideoTrack(int bitrate, @Nullable String codec, @Nullable Framerate framerate,
                        int height, int pid, int uid, int width) {

                    public static record Framerate(int den, int num) {
                    }
                }
            }

        }

    }

    public static record TvContext(@Nullable Channel channel, @Nullable PlayerDetails player) {

        public static record Channel(@SerializedName("bouquetId") long bouquetId,
                @SerializedName("bouquetName") @Nullable String bouquetName,
                @SerializedName("bouquetType") BouquetType bouquetType,
                @SerializedName("channelName") @Nullable String channelName,
                @SerializedName("channelNumber") int channelNumber,
                @SerializedName("channelSubNumber") int channelSubNumber,
                @SerializedName("channelType") ChannelType channelType,
                @SerializedName("channelUuid") @Nullable String channelUuid,
                @SerializedName("currentServiceIndex") int currentServiceIndex,
                @SerializedName("isTimeShifting") boolean isTimeShifting, List<Service> services,
                @SerializedName("videoIsVisible") boolean videoIsVisible) {

            public static enum BouquetType {
                ADSL,
                UNKNOWN;
            }

            public static enum ChannelType {
                REGULAR,
                UNKNOWN;
            }

            public static record Service(long id, @Nullable String name,
                    @SerializedName("qualityLabel") @Nullable String qualityLabel,
                    @SerializedName("qualityName") @Nullable String qualityName,
                    @SerializedName("sortInfo") int sortInfo, @SerializedName("typeLabel") @Nullable String typeLabel,
                    @SerializedName("typeName") @Nullable String typeName, @Nullable String url) {
            }

        }

    }

    private final Map<Integer, String> subPaths = new HashMap<>();

    public PlayerManager(FreeboxOsSession session) throws FreeboxException {
        super(session, Permission.PLAYER, PlayerResponse.class, session.getUriBuilder().path(PATH));
        getDevices().stream().filter(Player::apiAvailable).forEach(player -> subPaths.put(player.id, player.baseUrl()));
    }

    public Status getPlayerStatus(int id) throws FreeboxException {
        return getSingle(StatusResponse.class, subPaths.get(id), STATUS_PATH);
    }

    // The player API does not allow to directly request a given player like others api parts
    @Override
    public Player getDevice(int id) throws FreeboxException {
        return getDevices().stream().filter(player -> player.id == id).findFirst().orElse(null);
    }

    public Configuration getConfig(int id) throws FreeboxException {
        return getSingle(ConfigurationResponse.class, subPaths.get(id), SYSTEM_PATH);
    }

    public void sendKey(String ip, String code, String key, boolean longPress, int count) {
        UriBuilder uriBuilder = UriBuilder.fromPath("pub").scheme("http").host(ip).path("remote_control");
        uriBuilder.queryParam("code", code).queryParam("key", key);
        if (longPress) {
            uriBuilder.queryParam("long", true);
        }
        if (count > 1) {
            uriBuilder.queryParam("repeat", count);
        }
        try {
            session.execute(uriBuilder.build(), HttpMethod.GET, GenericResponse.class, null);
        } catch (FreeboxException ignore) {
            // This call does not return anything, we can safely ignore
        }
    }

    public void reboot(int id) throws FreeboxException {
        post(subPaths.get(id), SYSTEM_PATH, REBOOT_ACTION);
    }
}
