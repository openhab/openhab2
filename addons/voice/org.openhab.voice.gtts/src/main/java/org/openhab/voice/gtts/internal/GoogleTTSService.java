/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.voice.gtts.internal;

import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import org.eclipse.smarthome.config.core.ConfigConstants;
import org.eclipse.smarthome.config.core.ConfigOptionProvider;
import org.eclipse.smarthome.config.core.ConfigurableService;
import org.eclipse.smarthome.config.core.ParameterOption;
import org.eclipse.smarthome.core.audio.AudioException;
import org.eclipse.smarthome.core.audio.AudioFormat;
import org.eclipse.smarthome.core.audio.AudioStream;
import org.eclipse.smarthome.core.audio.FileAudioStream;
import org.eclipse.smarthome.core.voice.TTSException;
import org.eclipse.smarthome.core.voice.TTSService;
import org.eclipse.smarthome.core.voice.Voice;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.*;

import static org.openhab.voice.gtts.internal.GoogleTTSService.*;

/**
 * Voice service implementation.
 *
 * @author Gabor Bicskei - Initial contribution
 */
@Component(configurationPid = SERVICE_PID, property = {
        Constants.SERVICE_PID + "=" + SERVICE_PID,
        ConfigurableService.SERVICE_PROPERTY_LABEL + "=" + SERVICE_NAME,
        ConfigurableService.SERVICE_PROPERTY_DESCRIPTION_URI + "=" + SERVICE_CATEGORY + ":" + SERVICE_ID,
        ConfigurableService.SERVICE_PROPERTY_CATEGORY + "=" + SERVICE_CATEGORY })
public class GoogleTTSService implements TTSService, ConfigOptionProvider {
    /**
     * Service name
     */
    static final String SERVICE_NAME = "Google Cloud TTS Service";

    /**
     * Service id
     */
    static final String SERVICE_ID = "gtts";

    /**
     * Service category
     */
    static final String SERVICE_CATEGORY = "voice";

    /**
     * Service pid
     */
    static final String SERVICE_PID = "org.openhab." + SERVICE_CATEGORY + "." + SERVICE_ID;

    /**
     * Configuration parameters
     */
    private static final String PARAM_SERVICE_KEY_FILE_NAME = "serviceAccountKeyName";
    private static final String PARAM_PITCH = "pitch";
    private static final String PARAM_SPEAKING_RATE = "speakingRate";
    private static final String PARAM_VOLUME_GAIN_DB = "volumeGainDb";

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(GoogleTTSService.class);

    /**
     * Set of supported audio formats
     */
    private Set<AudioFormat> audioFormats = new HashSet<>();

    /**
     * Google Cloud TTS API implementation
     */
    private GoogleCloudAPI apiImpl;

    /**
     * All voices for all supported locales
     */
    private Set<Voice> allVoices = new HashSet<>();

    /**
     * Audio format.
     */
    private GoogleTTSConfig config = new GoogleTTSConfig();

    /**
     * DS activate, with access to ConfigAdmin
     */
    protected void activate(Map<String, Object> config) {
        try {
            //create home folder
            File userData = new File(ConfigConstants.getUserDataFolder());
            File homeFolder = new File(userData, SERVICE_ID);
            if (!homeFolder.exists()) {
                //noinspection ResultOfMethodCallIgnored
                homeFolder.mkdirs();
            }
            logger.info("Home folder created: {}", homeFolder.getAbsolutePath());
            apiImpl = new GoogleCloudAPI(homeFolder);
            updateConfig(config);
        } catch (Throwable t) {
            logger.error("Failed to activate Google Cloud TTS Service: {}", t.getMessage(), t);
        }
    }

    /**
     * Initializing audio formats. Google supports 3 formats:
     * LINEAR16
     * Uncompressed 16-bit signed little-endian samples (Linear PCM). Audio content returned as LINEAR16
     * also contains a WAV header.
     * MP3
     * MP3 audio.
     * OGG_OPUS
     * Opus encoded audio wrapped in an ogg container. This is not supported by openHAB.
     *
     * @return Set of supported AudioFormats
     */
    private Set<AudioFormat> initAudioFormats() {
        logger.trace("Initializing audio formats");
        Set<AudioFormat> ret = new HashSet<>();
        Set<String> formats = apiImpl.getSupportedAudioFormats();
        for (String format : formats) {
            AudioFormat audioFormat = getAudioFormat(format);
            if (audioFormat != null) {
                ret.add(audioFormat);
                logger.trace("Audio format supported: {}", format);
            } else {
                logger.trace("Audio format not supported: {}", format);
            }
        }
        return ret;
    }

    /**
     * Loads available voices from Google API
     *
     * @return Set of available voices.
     */
    private Set<Voice> initVoices() {
        logger.trace("Initializing voices");
        Set<Voice> ret = new HashSet<>();
        for (Locale l : apiImpl.getSupportedLocales()) {
            ret.addAll(apiImpl.getVoicesForLocale(l));
        }
        if (logger.isTraceEnabled()) {
            for (Voice v : ret) {
                logger.trace("Google Cloud TTS voice: {}", v.getLabel());
            }
        }
        return ret;
    }

    /**
     * Called by the framework when the configuration was updated.
     *
     * @param newConfig Updated configuration
     */
    @Modified
    private void updateConfig(Map<String, Object> newConfig) {
        logger.debug("Updating configuration");
        if (newConfig != null) {
            //account key
            String param = newConfig.containsKey(PARAM_SERVICE_KEY_FILE_NAME) ? newConfig.get(PARAM_SERVICE_KEY_FILE_NAME).toString() : null;
            if (param != null) {
                config.setServiceKeyFileName(param);

                apiImpl.setConfig(config);
                if (apiImpl.isInitialized()) {
                    allVoices = initVoices();
                    audioFormats = initAudioFormats();
                }
            } else {
                logger.error("Missing service account key configuration to access Google Cloud TTS API.");
            }

            //pitch
            param = newConfig.containsKey(PARAM_PITCH) ? newConfig.get(PARAM_PITCH).toString() : null;
            if (param != null) {
                config.setPitch(Double.parseDouble(param));
            }

            //speakingRate
            param = newConfig.containsKey(PARAM_SPEAKING_RATE) ? newConfig.get(PARAM_SPEAKING_RATE).toString() : null;
            if (param != null) {
                config.setSpeakingRate(Double.parseDouble(param));
            }

            //volumeGainDb
            param = newConfig.containsKey(PARAM_VOLUME_GAIN_DB) ? newConfig.get(PARAM_VOLUME_GAIN_DB).toString() : null;
            if (param != null) {
                config.setVolumeGainDb(Double.parseDouble(param));
            }
            logger.debug("New configuration: {}", config.toString());
        } else {
            logger.error("Missing Google Cloud TTS configuration.");
        }
    }

    @Override
    public String getId() {
        return SERVICE_ID;
    }

    @Override
    public String getLabel(Locale locale) {
        return SERVICE_NAME;
    }

    @Override
    public Set<Voice> getAvailableVoices() {
        return allVoices;
    }

    @Override
    public Set<AudioFormat> getSupportedFormats() {
        return audioFormats;
    }

    /**
     * Helper to create AudioFormat objects from Google names.
     *
     * @param format Google audio format.
     * @return Audio format object.
     */
    private AudioFormat getAudioFormat(String format) {
        Integer bitDepth = 16;
        Long frequency = 44100L;

        AudioEncoding encoding = AudioEncoding.valueOf(format);

        switch (encoding) {
            case MP3:
                // we use by default: MP3, 44khz_16bit_mono with bitrate 64 kbps
                return new AudioFormat(AudioFormat.CONTAINER_NONE, AudioFormat.CODEC_MP3, null, bitDepth,
                        64000, frequency);
            case LINEAR16:
                // we use by default: wav, 44khz_16bit_mono
                return new AudioFormat(AudioFormat.CONTAINER_WAVE, AudioFormat.CODEC_PCM_SIGNED, null, bitDepth,
                        null, frequency);
            default:
                logger.warn("Audio format {} is not yet supported.", format);
                return null;
        }
    }

    /**
     * Checks parameters and calls the API to synthesize voice.
     *
     * @param text            Input text.
     * @param voice           Selected voice.
     * @param requestedFormat Format that is supported by the target sink as well.
     * @return Output audio stream
     * @throws TTSException in case the service is unavailable or a parameter is invalid.
     */
    @Override
    public AudioStream synthesize(String text, Voice voice, AudioFormat requestedFormat) throws TTSException {
        logger.debug("Synthesize '{}' for voice '{}' in format {}", text, voice.getUID(), requestedFormat);
        // Validate known api key
        if (!apiImpl.isInitialized()) {
            throw new TTSException("Missing service configuration.");
        }
        // Validate arguments
        // trim text
        text = text.trim();
        if (text.isEmpty()) {
            throw new TTSException("The passed text is null or empty");
        }
        if (!this.allVoices.contains(voice)) {
            throw new TTSException("The passed voice is unsupported");
        }
        boolean isAudioFormatSupported = false;
        for (AudioFormat currentAudioFormat : this.audioFormats) {
            if (currentAudioFormat.isCompatible(requestedFormat)) {
                isAudioFormatSupported = true;
                break;
            }
        }
        if (!isAudioFormatSupported) {
            throw new TTSException("The passed AudioFormat is unsupported");
        }

        // now create the input stream for given text, locale, format. There is
        // only a default voice
        try {
            File audioFile = apiImpl.synthesizeSpeech(text, new GoogleTTSVoice(voice), requestedFormat.getCodec());
            if (audioFile == null) {
                throw new TTSException("Could not read from Google Cloud TTS Service");
            }
            return new FileAudioStream(audioFile, requestedFormat);
        } catch (AudioException ex) {
            throw new TTSException("Could not create AudioStream", ex);
        }
    }

    @Override
    public Collection<ParameterOption> getParameterOptions(URI uri, String s, Locale locale) {
        return getParameterOptions(uri, s, null, locale);
    }

    /**
     * Implementing ConfigOptionProvider to help the configuration by listing all JSON files under service home.
     *
     * @param uri Configuration URI
     * @param param Param name.
     * @param context Context.
     * @param locale Locale
     *
     * @return Collection of options.
     */
    @Override
    public Collection<ParameterOption> getParameterOptions(URI uri, String param, String context, Locale locale) {
        if (uri.toString().equals("voice:gtts")) {
            if (PARAM_SERVICE_KEY_FILE_NAME.equals(param)) {
                List<ParameterOption> options = new ArrayList<>();
                String userDataFolder = ConfigConstants.getUserDataFolder();
                File homeFolder = new File(userDataFolder, SERVICE_ID);

                String[] jsonFiles = homeFolder.list((dir, name) -> name.endsWith(".json"));

                options.add(new ParameterOption("", "Select the service account key"));

                if (jsonFiles != null) {
                    for (String keyFile : jsonFiles) {
                        ParameterOption option = new ParameterOption(keyFile, keyFile);
                        options.add(option);
                    }
                }

                return options;
            }
        }
        return null;
    }
}
