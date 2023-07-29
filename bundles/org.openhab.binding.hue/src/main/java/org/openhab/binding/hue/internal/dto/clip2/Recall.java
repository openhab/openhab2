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
package org.openhab.binding.hue.internal.dto.clip2;

import java.time.Duration;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.hue.internal.dto.clip2.enums.RecallAction;

/**
 * DTO for scene recall.
 *
 * @author Andrew Fiddian-Green - Initial contribution
 */
@NonNullByDefault
public class Recall {
    private @Nullable @SuppressWarnings("unused") String action;
    private @Nullable @SuppressWarnings("unused") String status;
    private @Nullable @SuppressWarnings("unused") Long duration;

    public Recall setAction(RecallAction action) {
        this.action = action.name().toLowerCase();
        return this;
    }

    public Recall setDuration(Duration duration) {
        this.duration = duration.toMillis();
        return this;
    }
}
