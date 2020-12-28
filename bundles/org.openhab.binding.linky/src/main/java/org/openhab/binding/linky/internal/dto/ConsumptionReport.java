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
package org.openhab.binding.linky.internal.dto;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openhab.binding.linky.internal.LinkyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link ConsumptionReport} is responsible for holding values
 * returned by API calls
 *
 * @author Gaël L'hopital - Initial contribution
 */
public class ConsumptionReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumptionReport.class);

    public class Period {
        public String grandeurPhysiqueEnum;
        public ZonedDateTime dateDebut;
        public ZonedDateTime dateFin;
    }

    public class Aggregate {
        public List<String> labels;
        public List<Period> periodes;
        public List<Double> datas;

        public void log(String title, boolean withDateFin, DateTimeFormatter dateTimeFormatter, boolean onlyLast) {
            if (LOGGER.isDebugEnabled()) {
                int size = (datas == null || periodes == null) ? 0
                        : (datas.size() <= periodes.size() ? datas.size() : periodes.size());
                if (onlyLast) {
                    if (size > 0) {
                        log(size - 1, title, withDateFin, dateTimeFormatter);
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        log(i, title, withDateFin, dateTimeFormatter);
                    }
                }
            }
        }

        private void log(int index, String title, boolean withDateFin, DateTimeFormatter dateTimeFormatter) {
            if (withDateFin) {
                LOGGER.debug("{} {} {} value {}", title, periodes.get(index).dateDebut.format(dateTimeFormatter),
                        periodes.get(index).dateFin.format(dateTimeFormatter), datas.get(index));
            } else {
                LOGGER.debug("{} {} value {}", title, periodes.get(index).dateDebut.format(dateTimeFormatter),
                        datas.get(index));
            }
        }
    }

    public class ChronoData {
        @SerializedName("JOUR")
        public Aggregate days;
        @SerializedName("SEMAINE")
        public Aggregate weeks;
        @SerializedName("MOIS")
        public Aggregate months;
        @SerializedName("ANNEE")
        public Aggregate years;
    }

    public class Consumption {
        public ChronoData aggregats;
        public String grandeurMetier;
        public String grandeurPhysique;
        public String unite;
    }

    public class FirstLevel {
        @SerializedName("CONS")
        public Consumption consumptions;
    }

    @SerializedName("1")
    public FirstLevel firstLevel;

    public void checkData() throws LinkyException {
        Consumption cons = firstLevel.consumptions;
        if (cons.aggregats.days.periodes.size() == 0) {
            throw new LinkyException("invalid consumptions data: no day period");
        }
        if (cons.aggregats.days.periodes.size() != cons.aggregats.days.datas.size()) {
            throw new LinkyException("invalid consumptions data: not one data for each day period");
        }
        if (cons.aggregats.weeks.periodes.size() == 0) {
            throw new LinkyException("invalid consumptions data: no week period");
        }
        if (cons.aggregats.weeks.periodes.size() != cons.aggregats.weeks.datas.size()) {
            throw new LinkyException("invalid consumptions data: not one data for each week period");
        }
        if (cons.aggregats.months.periodes.size() == 0) {
            throw new LinkyException("invalid consumptions data: no month period");
        }
        if (cons.aggregats.months.periodes.size() != cons.aggregats.months.datas.size()) {
            throw new LinkyException("invalid consumptions data: not one data for each month period");
        }
        if (cons.aggregats.years.periodes.size() == 0) {
            throw new LinkyException("invalid consumptions data: no year period");
        }
        if (cons.aggregats.years.periodes.size() != cons.aggregats.years.datas.size()) {
            throw new LinkyException("invalid consumptions data: not one data for each year period");
        }
    }
}
