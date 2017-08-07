/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahareceiver.internal.protocol.xml;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.yamahareceiver.YamahaReceiverBindingConstants;
import org.openhab.binding.yamahareceiver.internal.protocol.AbstractConnection;
import org.openhab.binding.yamahareceiver.internal.protocol.ReceivedMessageParseException;
import org.openhab.binding.yamahareceiver.internal.protocol.ZoneControl;
import org.openhab.binding.yamahareceiver.internal.state.ZoneControlState;
import org.openhab.binding.yamahareceiver.internal.state.ZoneControlStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * The zone protocol class is used to control one zone of a Yamaha receiver with HTTP/xml.
 * No state will be saved in here, but in {@link ZoneControlState} instead.
 *
 * @author David Gräff - Refactored
 * @author Eric Thill
 * @author Ben Jones
 */
public class ZoneControlXML implements ZoneControl {
    private ZoneControlStateListener observer;
    private Logger logger = LoggerFactory.getLogger(ZoneControlXML.class);
    private final WeakReference<AbstractConnection> comReference;
    private final YamahaReceiverBindingConstants.Zone zone;

    public ZoneControlXML(AbstractConnection xml, YamahaReceiverBindingConstants.Zone zone,
            ZoneControlStateListener observer) {
        this.comReference = new WeakReference<AbstractConnection>(xml);
        this.zone = zone;
        this.observer = observer;
    }

    /**
     * Return the zone
     */
    public YamahaReceiverBindingConstants.Zone getZone() {
        return zone;
    }

    @Override
    public void setPower(boolean on) throws IOException, ReceivedMessageParseException {
        if (on) {
            comReference.get().send(XMLUtils.wrZone(zone, "<Power_Control><Power>On</Power></Power_Control>"));
        } else {
            comReference.get().send(XMLUtils.wrZone(zone, "<Power_Control><Power>Standby</Power></Power_Control>"));
        }
        update();
    }

    /**
     * Sets the absolute volume in decibel.
     *
     * @param volume Absolute value in decibel ([-80,+12]).
     * @throws IOException
     */
    @Override
    public void setVolumeDB(float volume) throws IOException, ReceivedMessageParseException {
        if (volume < YamahaReceiverBindingConstants.VOLUME_MIN) {
            volume = YamahaReceiverBindingConstants.VOLUME_MIN;
        }
        if (volume > YamahaReceiverBindingConstants.VOLUME_MAX) {
            volume = YamahaReceiverBindingConstants.VOLUME_MAX;
        }
        int vol = (int) volume * 10;
        comReference.get().send(XMLUtils.wrZone(zone,
                "<Volume><Lvl><Val>" + String.valueOf(vol) + "</Val><Exp>1</Exp><Unit>dB</Unit></Lvl></Volume>"));

        update();
    }

    /**
     * Sets the volume in percent
     *
     * @param volume
     * @throws IOException
     */
    @Override
    public void setVolume(float volume) throws IOException, ReceivedMessageParseException {
        if (volume < 0) {
            volume = 0;
        }
        if (volume > 100) {
            volume = 100;
        }
        // Compute value in db
        setVolumeDB(volume * YamahaReceiverBindingConstants.VOLUME_RANGE / 100.0f
                + YamahaReceiverBindingConstants.VOLUME_MIN);
    }

    /**
     * Increase or decrease the volume by the given percentage.
     *
     * @param percent
     * @throws IOException
     */
    @Override
    public void setVolumeRelative(ZoneControlState state, float percent)
            throws IOException, ReceivedMessageParseException {
        setVolume(state.volume + percent);
    }

    @Override
    public void setMute(boolean mute) throws IOException, ReceivedMessageParseException {
        if (mute) {
            comReference.get().send(XMLUtils.wrZone(zone, "<Volume><Mute>On</Mute></Volume>"));
        } else {
            comReference.get().send(XMLUtils.wrZone(zone, "<Volume><Mute>Off</Mute></Volume>"));
        }
        update();
    }

    @Override
    public void setInput(String name) throws IOException, ReceivedMessageParseException {
        comReference.get().send(XMLUtils.wrZone(zone, "<Input><Input_Sel>" + name + "</Input_Sel></Input>"));
        update();
    }

    @Override
    public void setSurroundProgram(String name) throws IOException, ReceivedMessageParseException {
        if (name.toLowerCase().equals("straight")) {
            comReference.get().send(XMLUtils.wrZone(zone,
                    "<Surround><Program_Sel><Current><Straight>On</Straight></Current></Program_Sel></Surround>"));
        } else {
            comReference.get().send(XMLUtils.wrZone(zone, "<Surround><Program_Sel><Current><Sound_Program>" + name
                    + "</Sound_Program></Current></Program_Sel></Surround>"));
        }

        update();
    }

    @Override
    public void update() throws IOException, ReceivedMessageParseException {
        if (observer == null) {
            return;
        }

        AbstractConnection com = comReference.get();
        String response = com.sendReceive(XMLUtils.wrZone(zone, "<Basic_Status>GetParam</Basic_Status>"));
        Document doc = XMLUtils.xml(response);
        if (doc.getFirstChild() == null) {
            throw new ReceivedMessageParseException("<Basic_Status>GetParam failed: " + response);
        }
        Node basicStatus = XMLUtils.getNode(doc.getFirstChild(), zone + "/Basic_Status");

        Node node;
        String value;

        ZoneControlState state = new ZoneControlState();

        node = XMLUtils.getNode(basicStatus, "Power_Control/Power");
        value = node != null ? node.getTextContent() : "";
        state.power = "On".equalsIgnoreCase(value);

        node = XMLUtils.getNode(basicStatus, "Input/Input_Sel");
        value = node != null ? node.getTextContent() : "";
        state.inputID = XMLUtils.convertNameToID(value);

        if (StringUtils.isBlank(state.inputID)) {
            throw new ReceivedMessageParseException(
                    "Expected inputID. Failed to read Input/Input_Sel_Item_Info/Src_Name");
        }

        // Some receivers may use Src_Name instead?
        node = XMLUtils.getNode(basicStatus, "Input/Input_Sel_Item_Info/Title");
        value = node != null ? node.getTextContent() : "";
        state.inputName = value;

        node = XMLUtils.getNode(basicStatus, "Surround/Program_Sel/Current/Sound_Program");
        value = node != null ? node.getTextContent() : "";
        state.surroundProgram = value;

        node = XMLUtils.getNode(basicStatus, "Volume/Lvl/Val");
        value = node != null ? node.getTextContent() : String.valueOf(YamahaReceiverBindingConstants.VOLUME_MIN);
        state.volume = Float.parseFloat(value) * .1f; // in DB
        state.volume = (state.volume + -YamahaReceiverBindingConstants.VOLUME_MIN) * 100.0f
                / YamahaReceiverBindingConstants.VOLUME_RANGE; // in percent
        if (state.volume < 0 || state.volume > 100) {
            logger.error("Received volume is out of range: {}", state.volume);
            state.volume = 0;
        }

        node = XMLUtils.getNode(basicStatus, "Volume/Mute");
        value = node != null ? node.getTextContent() : "";
        state.mute = "On".equalsIgnoreCase(value);

        observer.zoneStateChanged(state);
    }
}
