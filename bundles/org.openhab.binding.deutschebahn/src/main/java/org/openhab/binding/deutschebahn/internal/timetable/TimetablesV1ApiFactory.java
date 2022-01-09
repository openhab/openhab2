/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.deutschebahn.internal.timetable;

import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.deutschebahn.internal.timetable.TimetablesV1Impl.HttpCallable;
import org.xml.sax.SAXException;

/**
 * Factory for {@link TimetablesV1Api}.
 * 
 * @author Sönke Küper - Initial contribution.
 */
@NonNullByDefault
public interface TimetablesV1ApiFactory {

    /**
     * Creates an new instance of the {@link TimetablesV1Api}.
     */
    public abstract TimetablesV1Api create(final String authToken, final HttpCallable httpCallable)
            throws JAXBException, SAXException, URISyntaxException;
}
