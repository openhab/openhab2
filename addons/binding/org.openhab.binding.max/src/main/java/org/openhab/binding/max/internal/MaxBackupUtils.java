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
package org.openhab.binding.max.internal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.smarthome.config.core.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MaxBackupUtils} class supports the backup of cube data
 *
 * @author Marcel Verpaalen - Initial contribution
 */
public class MaxBackupUtils {

    private final Logger logger = LoggerFactory.getLogger(MaxBackupUtils.class);
    private static final String BACKUP_PATH = "max";

    private String dbFolderName = "";
    private boolean inProgress = false;
    private StringBuilder msg;
    private String cube;
    private String backupId = "";

    public MaxBackupUtils(String backupId) {
        this.backupId = backupId;
        dbFolderName = ConfigConstants.getUserDataFolder() + File.separator + BACKUP_PATH;
        File folder = new File(dbFolderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public MaxBackupUtils() {
        this("auto" + new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
    }

    public void buildMsg(String msgLine) {
        if (msgLine.startsWith("H:")) {
            msg = new StringBuilder();
            cube = msgLine.substring(2).split(",")[0];
            inProgress = true;
        }
        if (inProgress) {
            msg.append(msgLine);
            msg.append(System.lineSeparator());
        }
        if (inProgress && msgLine.startsWith("L:")) {
            inProgress = false;
            saveMsg(msg.toString(), cube);
        }

    }

    private void saveMsg(String data, String cube) {
        File dataFile = new File(dbFolderName + File.separator + backupId + "-backup-" + cube + ".txt");
        try (FileWriter writer = new FileWriter(dataFile)) {
            writer.write(data);
            logger.debug("MAX! backup saved to " + dataFile.getAbsolutePath());
        } catch (IOException e) {
            logger.warn("MAX! Failed to write backup file '{}': {}", dataFile.getName(), e.getMessage());
        }
    }
}
