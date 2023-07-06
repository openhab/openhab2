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
package org.openhab.binding.kodi.internal;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import org.openhab.binding.kodi.internal.handler.KodiHandler;
import org.openhab.core.audio.AudioFormat;
import org.openhab.core.audio.AudioHTTPServer;
import org.openhab.core.audio.AudioSink;
import org.openhab.core.audio.AudioSinkAsync;
import org.openhab.core.audio.AudioStream;
import org.openhab.core.audio.StreamServed;
import org.openhab.core.audio.URLAudioStream;
import org.openhab.core.audio.UnsupportedAudioFormatException;
import org.openhab.core.audio.UnsupportedAudioStreamException;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This makes Kodi to serve as an {@link AudioSink}.
 *
 * @author Kai Kreuzer - Initial contribution and API
 * @author Paul Frank - Adapted for Kodi
 * @author Christoph Weitkamp - Improvements for playing audio notifications
 */
public class KodiAudioSink extends AudioSinkAsync {

    private final Logger logger = LoggerFactory.getLogger(KodiAudioSink.class);

    private static final Set<AudioFormat> SUPPORTED_AUDIO_FORMATS = Set.of(AudioFormat.MP3, AudioFormat.WAV);
    private static final Set<Class<? extends AudioStream>> SUPPORTED_AUDIO_STREAMS = Set.of(AudioStream.class);
    // Needed because Kodi does multiple requests for the stream
    private static final int STREAM_TIMEOUT = 10;

    private final KodiHandler handler;
    private final AudioHTTPServer audioHTTPServer;
    private final String callbackUrl;

    public KodiAudioSink(KodiHandler handler, AudioHTTPServer audioHTTPServer, String callbackUrl) {
        this.handler = handler;
        this.audioHTTPServer = audioHTTPServer;
        this.callbackUrl = callbackUrl;
    }

    @Override
    public String getId() {
        return handler.getThing().getUID().toString();
    }

    @Override
    public String getLabel(Locale locale) {
        return handler.getThing().getLabel();
    }

    @Override
    public void processAsynchronously(AudioStream audioStream)
            throws UnsupportedAudioFormatException, UnsupportedAudioStreamException {
        if (audioStream == null) {
            // in case the audioStream is null, this should be interpreted as a request to end any currently playing
            // stream.
            logger.trace("Stop currently playing stream.");
            handler.stop();
        } else {
            AudioFormat format = audioStream.getFormat();
            if (!AudioFormat.MP3.isCompatible(format) && !AudioFormat.WAV.isCompatible(format)) {
                throw new UnsupportedAudioFormatException("Currently only MP3 and WAV formats are supported.", format);
            }

            if (audioStream instanceof URLAudioStream) {
                // it is an external URL, the speaker can access it itself and play it
                String url = ((URLAudioStream) audioStream).getURL();
                logger.trace("Processing audioStream URL {} of format {}.", url, format);
                handler.playURI(new StringType(url));
            } else if (callbackUrl != null) {
                // we serve it on our own HTTP server for 10 seconds as Kodi requests the stream several times
                // Form the URL for streaming the notification from the OH web server
                try {
                    StreamServed streamServed = audioHTTPServer.serve(audioStream, STREAM_TIMEOUT, true);
                    // note : KODI is not very compatible with our HTTP server. It keeps requesting the URL again and
                    // again, resetting the time we label as the "start". Our server cannot reliably detect duration.
                    streamServed.playEnd().thenRun(() -> this.playbackFinished(audioStream));
                    String url = callbackUrl + streamServed.url();
                    logger.trace("Processing audioStream URL {} of format {}.", url, format);
                    handler.playNotificationSoundURI(new StringType(url), false);
                } catch (IOException e) {
                    logger.warn("Kodi binding was not able to handle the audio stream (cache on disk failed)", e);
                }
            } else {
                logger.warn("We do not have any callback url, so Kodi cannot play the audio stream!");
            }
        }
    }

    @Override
    public Set<AudioFormat> getSupportedFormats() {
        return SUPPORTED_AUDIO_FORMATS;
    }

    @Override
    public Set<Class<? extends AudioStream>> getSupportedStreams() {
        return SUPPORTED_AUDIO_STREAMS;
    }

    @Override
    public PercentType getVolume() {
        return handler.getVolume();
    }

    @Override
    public void setVolume(PercentType volume) {
        handler.setVolume(volume);
    }
}
