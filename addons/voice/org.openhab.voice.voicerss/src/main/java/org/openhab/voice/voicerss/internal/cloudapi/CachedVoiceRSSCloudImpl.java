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
package org.openhab.voice.voicerss.internal.cloudapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a cache for the retrieved audio data. It will preserve
 * them in file system, as audio files with an additional .txt file to indicate
 * what content is in the audio file.
 *
 * @author Jochen Hiller - Initial contribution
 */
public class CachedVoiceRSSCloudImpl extends VoiceRSSCloudImpl {

    private final Logger logger = LoggerFactory.getLogger(CachedVoiceRSSCloudImpl.class);

    private final File cacheFolder;

    /**
     * Stream buffer size
     */
    private static final int READ_BUFFER_SIZE = 4096;

    public CachedVoiceRSSCloudImpl(String cacheFolderName) {
        if (cacheFolderName == null) {
            throw new IllegalStateException("Folder for cache must be defined");
        }
        // Lazy create the cache folder
        cacheFolder = new File(cacheFolderName);
        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }
    }

    public File getTextToSpeechAsFile(String apiKey, String text, String locale, String audioFormat)
            throws IOException {
        String fileNameInCache = getUniqueFilenameForText(text, locale);
        // check if in cache
        File audioFileInCache = new File(cacheFolder, fileNameInCache + "." + audioFormat.toLowerCase());
        if (audioFileInCache.exists()) {
            return audioFileInCache;
        }

        // if not in cache, get audio data and put to cache
        try (InputStream is = super.getTextToSpeech(apiKey, text, locale, audioFormat);
                FileOutputStream fos = new FileOutputStream(audioFileInCache);) {
            copyStream(is, fos);
            // write text to file for transparency too
            // this allows to know which contents is in which audio file
            File txtFileInCache = new File(cacheFolder, fileNameInCache + ".txt");
            writeText(txtFileInCache, text);
            // return from cache
            return audioFileInCache;
        } catch (FileNotFoundException ex) {
            logger.warn("Could not write {} to cache", audioFileInCache, ex);
            return null;
        } catch (IOException ex) {
            logger.error("Could not write {} to cache", audioFileInCache, ex);
            return null;
        }
    }

    /**
     * Gets a unique filename for a give text, by creating a MD5 hash of it. It
     * will be preceded by the locale.
     *
     * Sample: "en-US_00a2653ac5f77063bc4ea2fee87318d3"
     */
    private String getUniqueFilenameForText(String text, String locale) {
        try {
            byte[] bytesOfMessage = text.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Hash = md.digest(bytesOfMessage);
            BigInteger bigInt = new BigInteger(1, md5Hash);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return locale + "_" + hashtext;
        } catch (UnsupportedEncodingException ex) {
            // should not happen
            logger.error("Could not create MD5 hash for '{}'", text, ex);
            return null;
        } catch (NoSuchAlgorithmException ex) {
            // should not happen
            logger.error("Could not create MD5 hash for '{}'", text, ex);
            return null;
        }
    }

    // helper methods

    private void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bytes = new byte[READ_BUFFER_SIZE];
        int read = inputStream.read(bytes, 0, READ_BUFFER_SIZE);
        while (read > 0) {
            outputStream.write(bytes, 0, read);
            read = inputStream.read(bytes, 0, READ_BUFFER_SIZE);
        }
    }

    private void writeText(File file, String text) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(text.getBytes("UTF-8"));
        }
    }
}
