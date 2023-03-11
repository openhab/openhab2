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
package org.openhab.voice.picotts.internal;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.audio.AudioException;
import org.openhab.core.audio.AudioFormat;
import org.openhab.core.audio.AudioStream;
import org.openhab.core.voice.TTSException;
import org.openhab.core.voice.TTSService;
import org.openhab.core.voice.Voice;
import org.osgi.service.component.annotations.Component;

/**
 * @author Florian Schmidt - Initial Contribution
 * @author Laurent Garnier - Code to generate the audio file is moved into that class
 */
@Component
@NonNullByDefault
public class PicoTTSService implements TTSService {
    private final Set<Voice> voices = Stream
            .of(new PicoTTSVoice("de-DE"), new PicoTTSVoice("en-US"), new PicoTTSVoice("en-GB"),
                    new PicoTTSVoice("es-ES"), new PicoTTSVoice("fr-FR"), new PicoTTSVoice("it-IT"))
            .collect(Collectors.toSet());

    private final Set<AudioFormat> audioFormats = Collections.singleton(
            new AudioFormat(AudioFormat.CONTAINER_WAVE, AudioFormat.CODEC_PCM_SIGNED, false, 16, null, 16000L));

    @Override
    public Set<Voice> getAvailableVoices() {
        return this.voices;
    }

    @Override
    public Set<AudioFormat> getSupportedFormats() {
        return this.audioFormats;
    }

    @Override
    public AudioStream synthesize(String text, Voice voice, AudioFormat requestedFormat) throws TTSException {
        if (text.isEmpty()) {
            throw new TTSException("The passed text can not be empty");
        }

        if (!this.voices.contains(voice)) {
            throw new TTSException("The passed voice is unsupported");
        }

        boolean isAudioFormatSupported = this.audioFormats.stream().anyMatch(audioFormat -> {
            return audioFormat.isCompatible(requestedFormat);
        });

        if (!isAudioFormatSupported) {
            throw new TTSException("The passed AudioFormat is unsupported");
        }

        try {
            return new PicoTTSAudioStream(generateFile(text, voice), requestedFormat);
        } catch (AudioException e) {
            throw new TTSException(e);
        }
    }

    @Override
    public String getId() {
        return "picotts";
    }

    @Override
    public String getLabel(@Nullable Locale locale) {
        return "PicoTTS";
    }

    private File generateFile(String text, Voice voice) throws AudioException {
        File outputFile;
        try {
            outputFile = File.createTempFile(Integer.toString(text.hashCode()), ".wav");
            outputFile.deleteOnExit();
        } catch (IOException e) {
            throw new AudioException("Unable to create temp file.", e);
        }

        String[] command = new String[] { "pico2wave", "-l=" + voice.getLabel(), "-w=" + outputFile.getAbsolutePath(),
                text };

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if (outputFile.length() == 0) {
                throw new AudioException("Generated file '" + outputFile + "' has no content.'");
            }
            return outputFile;
        } catch (IOException e) {
            throw new AudioException("Error while executing '" + command + "'", e);
        } catch (InterruptedException e) {
            throw new AudioException("The '" + command + "' has been interrupted", e);
        }
    }
}
