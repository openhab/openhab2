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
package org.openhab.binding.astro.internal.model;

import java.util.Calendar;

import org.openhab.binding.astro.internal.util.DateTimeUtils;

/**
 * Holds the season dates of the year and the current name.
 *
 * @author Gerhard Riegler - Initial contribution
 */
public class Season {
    private Calendar spring;
    private Calendar summer;
    private Calendar autumn;
    private Calendar winter;

    private SeasonName name;

    /**
     * Returns the date of the beginning of spring.
     */
    public Calendar getSpring() {
        return spring;
    }

    /**
     * Sets the date of the beginning of spring.
     */
    public void setSpring(Calendar spring) {
        this.spring = spring;
    }

    /**
     * Returns the date of the beginning of summer.
     */
    public Calendar getSummer() {
        return summer;
    }

    /**
     * Sets the date of the beginning of summer.
     */
    public void setSummer(Calendar summer) {
        this.summer = summer;
    }

    /**
     * Returns the date of the beginning of autumn.
     */
    public Calendar getAutumn() {
        return autumn;
    }

    /**
     * Sets the date of the beginning of autumn.
     */
    public void setAutumn(Calendar autumn) {
        this.autumn = autumn;
    }

    /**
     * Returns the date of the beginning of winter.
     */
    public Calendar getWinter() {
        return winter;
    }

    /**
     * Returns the date of the beginning of winter.
     */
    public void setWinter(Calendar winter) {
        this.winter = winter;
    }

    /**
     * Returns the current season name.
     */
    public SeasonName getName() {
        return name;
    }

    /**
     * Sets the current season name.
     */
    public void setName(SeasonName name) {
        this.name = name;
    }

    /**
     * Returns the next season.
     */
    public Calendar getNextSeason() {
        return DateTimeUtils.getNext(spring, summer, autumn, winter);
    }
}
