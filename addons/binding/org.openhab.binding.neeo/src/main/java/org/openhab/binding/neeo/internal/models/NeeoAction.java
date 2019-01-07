/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neeo.internal.models;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * The model representing an forward actions result (serialize/deserialize json use only).
 *
 * @author Tim Roberts - Initial contribution
 *
 */
@NonNullByDefault
public class NeeoAction {
    /** The action - can be null */
    @Nullable
    private String action;

    /** The action parameter - generally null */
    @Nullable
    @SerializedName("actionparameter")
    private String actionParameter;

    /** The recipe name - only valid on launch of recipe */
    @Nullable
    private String recipe;

    /** The device name - usually filled */
    @Nullable
    private String device;

    /** The room name - usually filled */
    @Nullable
    private String room;

    /**
     * Returns the action
     *
     * @return a possibly null, possibly empty action
     */
    @Nullable
    public String getAction() {
        return action;
    }

    /**
     * Returns the action parameter
     *
     * @return a possibly null, possibly empty action parameter
     */
    @Nullable
    public String getActionParameter() {
        return actionParameter;
    }

    /**
     * Returns the recipe name
     *
     * @return a possibly null, possibly empty recipe name
     */
    @Nullable
    public String getRecipe() {
        return recipe;
    }

    /**
     * Returns the device name
     *
     * @return a possibly null, possibly empty device name
     */
    @Nullable
    public String getDevice() {
        return device;
    }

    /**
     * Returns the room name
     *
     * @return a possibly null, possibly room name
     */
    @Nullable
    public String getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "NeeoAction [action=" + action + ", actionParameter=" + actionParameter + ", recipe=" + recipe
                + ", device=" + device + ", room=" + room + "]";
    }

}
