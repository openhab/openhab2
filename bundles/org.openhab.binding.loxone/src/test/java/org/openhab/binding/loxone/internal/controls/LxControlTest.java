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
package org.openhab.binding.loxone.internal.controls;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.type.ChannelKind;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.StateDescription;
import org.eclipse.smarthome.core.types.StateOption;
import org.openhab.binding.loxone.internal.types.LxCategory;
import org.openhab.binding.loxone.internal.types.LxContainer;
import org.openhab.binding.loxone.internal.types.LxState;
import org.openhab.binding.loxone.internal.types.LxUuid;

/**
 * Common test framework class for all (@link LxControl} objects
 *
 * @author Pawel Pieczul - initial contribution
 *
 */
class LxControlTest {
    LxServerHandlerDummy handler;
    LxUuid controlUuid;
    LxUuid roomUuid;
    LxUuid categoryUuid;
    String controlName;

    void setupControl(String controlUuid, String roomUuid, String categoryUuid, String controlName) {
        this.controlUuid = new LxUuid(controlUuid);
        this.roomUuid = new LxUuid(roomUuid);
        this.categoryUuid = new LxUuid(categoryUuid);
        this.controlName = controlName;
        handler = new LxServerHandlerDummy();
        handler.loadConfiguration();
    }

    private <T> void testControlCreation(LxControl c, int numberOfControls, int numberOfSubcontrols,
            int numberOfChannels, int numberOfChannelsWithSubs, int numberOfStates) {
        assertNotNull(c);
        Map<LxUuid, LxControl> subC = c.getSubControls();
        assertNotNull(subC);
        assertEquals(numberOfSubcontrols, subC.size());
        assertEquals(controlUuid, c.getUuid());
        String[] segments = controlName.split("/");
        String name = segments[segments.length - 1].trim();
        assertEquals(name, c.getName());
        assertEquals(name, c.getLabel());
        LxContainer room = c.getRoom();
        assertNotNull(room);
        assertEquals(roomUuid, room.getUuid());
        LxCategory cat = c.getCategory();
        assertNotNull(cat);
        assertEquals(categoryUuid, cat.getUuid());
        assertEquals(numberOfChannels, c.getChannels().size());
        assertEquals(numberOfChannelsWithSubs, c.getChannelsWithSubcontrols().size());
        assertEquals(numberOfStates, c.getStates().size());
    }

    <T> void testControlCreation(Class<T> testClass, int numberOfControls, int numberOfSubcontrols,
            int numberOfChannels, int numberOfChannelsWithSubs, int numberOfStates) {
        assertEquals(numberOfControls, numberOfControls(testClass));
        LxControl c = getControl(controlUuid);
        assertNotNull(c);
        testControlCreation(c, numberOfControls, numberOfSubcontrols, numberOfChannels, numberOfChannelsWithSubs,
                numberOfStates);
    }

    <T> void testSubControlCreation(Class<T> testClass, int numberOfControls, int numberOfSubcontrols,
            int numberOfChannels, int numberOfChannelsWithSubs, int numberOfStates) {
        assertEquals(numberOfControls, numberOfSubControls(testClass));
        LxControl c = getSubControl(controlUuid);
        assertNotNull(c);
        testControlCreation(c, numberOfControls, numberOfSubcontrols, numberOfChannels, numberOfChannelsWithSubs,
                numberOfStates);
    }

    void testChannel(String channelLabel, LxControl ctrl, String itemType, String namePostFix, BigDecimal min,
            BigDecimal max, BigDecimal step, String format, Boolean readOnly, List<StateOption> options,
            Set<String> tags) {
        assertNotNull(ctrl);
        Channel c = getChannel(getExpectedName(channelLabel == null ? controlName : channelLabel,
                ctrl.getRoom().getName(), namePostFix), ctrl);
        assertNotNull(c);
        assertNotNull(c.getUID());
        assertNotNull(c.getDescription());
        assertEquals(itemType, c.getAcceptedItemType());
        assertEquals(ChannelKind.STATE, c.getKind());
        StateDescription d = handler.stateDescriptions.get(c.getUID());
        if (readOnly != null || min != null || max != null || step != null || format != null || options != null) {
            assertNotNull(d);
            assertEquals(min, d.getMinimum());
            assertEquals(max, d.getMaximum());
            assertEquals(step, d.getStep());
            assertEquals(format, d.getPattern());
            assertEquals(readOnly, d.isReadOnly());
            List<StateOption> opts = d.getOptions();
            if (options == null) {
                assertTrue(opts.isEmpty());
            } else {
                assertNotNull(opts);
                assertEquals(options.size(), opts.size());
                options.forEach(o -> {
                    String label = o.getLabel();
                    long num = opts.stream().filter(
                            f -> label != null && label.equals(f.getLabel()) && o.getValue().equals(f.getValue()))
                            .collect(Collectors.counting());
                    assertEquals(1, num);
                });
            }
        } else {
            assertNull(d);
        }
        if (tags != null) {
            assertThat(c.getDefaultTags(), hasItems(tags.toArray(new String[0])));
        } else {
            assertThat(c.getDefaultTags(), empty());
        }
    }

    void testChannel(String itemType, String namePostFix, BigDecimal min, BigDecimal max, BigDecimal step,
            String format, Boolean readOnly, List<StateOption> options, Set<String> tags) {
        LxControl ctrl = getControl(controlUuid);
        testChannel(null, ctrl, itemType, namePostFix, min, max, step, format, readOnly, options, tags);
    }

    void testChannel(String itemType) {
        testChannel(itemType, null, null, null, null, null, null, null, null);
    }

    void testChannel(String itemType, Set<String> tags) {
        testChannel(itemType, null, null, null, null, null, null, null, tags);
    }

    void testChannel(LxControl ctrl, String itemType) {
        testChannel(null, ctrl, itemType, null, null, null, null, null, null, null, null);
    }

    void testChannel(LxControl ctrl, String itemType, Set<String> tags) {
        testChannel(null, ctrl, itemType, null, null, null, null, null, null, null, tags);
    }

    void testChannel(String label, LxControl ctrl, String itemType, Set<String> tags) {
        testChannel(label, ctrl, itemType, null, null, null, null, null, null, null, tags);
    }

    void testChannel(String itemType, String namePostFix) {
        testChannel(itemType, namePostFix, null, null, null, null, null, null, null);
    }

    void testChannel(String itemType, String namePostFix, Set<String> tags) {
        testChannel(itemType, namePostFix, null, null, null, null, null, null, tags);
    }

    void testChannel(String itemType, String namePostFix, BigDecimal min, BigDecimal max, BigDecimal step,
            String format, Boolean readOnly, List<StateOption> options) {
        testChannel(itemType, namePostFix, min, max, step, format, readOnly, options, null);
    }

    State getChannelState(String channelLabel, LxControl ctrl, String namePostFix) {
        assertNotNull(ctrl);
        Channel c = getChannel(getExpectedName(channelLabel == null ? controlName : channelLabel,
                ctrl.getRoom().getName(), namePostFix), ctrl);
        assertNotNull(c);
        return ctrl.getChannelState(c.getUID());
    }

    State getChannelState(LxControl ctrl, String namePostFix) {
        return getChannelState(null, ctrl, namePostFix);
    }

    State getChannelState(String namePostFix) {
        LxControl ctrl = getControl(controlUuid);
        return getChannelState(ctrl, namePostFix);
    }

    void testChannelState(LxControl ctrl, String namePostFix, State expectedValue) {
        testChannelState(null, ctrl, namePostFix, expectedValue);
    }

    void testChannelState(String channelLabel, LxControl ctrl, String namePostFix, State expectedValue) {
        State current = getChannelState(channelLabel, ctrl, namePostFix);
        if (expectedValue != null) {
            assertNotNull(current);
        }
        assertEquals(expectedValue, current);
    }

    void testChannelState(String namePostFix, State expectedValue) {
        LxControl ctrl = getControl(controlUuid);
        testChannelState(ctrl, namePostFix, expectedValue);
    }

    void testChannelState(State expectedValue) {
        testChannelState((String) null, expectedValue);
    }

    void testChannelState(LxControl ctrl, State expectedValue) {
        testChannelState(ctrl, null, expectedValue);
    }

    void testChannelState(String channelLabel, LxControl ctrl, State expectedValue) {
        testChannelState(channelLabel, ctrl, null, expectedValue);
    }

    void changeLoxoneState(String stateName, Object value) {
        LxControl ctrl = getControl(controlUuid);
        assertNotNull(ctrl);
        LxState state = ctrl.getStates().get(stateName);
        assertNotNull(state);
        state.setStateValue(value);
    }

    void executeCommand(String channelLabel, LxControl ctrl, String namePostFix, Command command) {
        assertNotNull(ctrl);
        Channel c = getChannel(getExpectedName(channelLabel == null ? controlName : channelLabel,
                ctrl.getRoom().getName(), namePostFix), ctrl);
        assertNotNull(c);
        try {
            ctrl.handleCommand(c.getUID(), command);
        } catch (IOException e) {
            fail("This exception should never happen in test environment.");
        }
    }

    void executeCommand(LxControl ctrl, String namePostFix, Command command) {
        executeCommand(null, ctrl, namePostFix, command);
    }

    void executeCommand(String namePostFix, Command command) {
        LxControl ctrl = getControl(controlUuid);
        executeCommand(ctrl, namePostFix, command);
    }

    void executeCommand(String channelLabel, LxControl ctrl, Command command) {
        executeCommand(channelLabel, ctrl, null, command);
    }

    void executeCommand(LxControl ctrl, Command command) {
        executeCommand(ctrl, null, command);
    }

    void executeCommand(Command command) {
        executeCommand((String) null, command);
    }

    void testAction(String expectedAction, int numberOfActions) {
        assertEquals(numberOfActions, handler.actionQueue.size());
        if (numberOfActions > 0) {
            String action = handler.actionQueue.poll();
            assertNotNull(action);
            assertEquals(controlUuid + "/" + expectedAction, action);
        }
    }

    void testAction(String expectedAction) {
        if (expectedAction == null) {
            testAction(null, 0);
        } else {
            testAction(expectedAction, 1);
        }
    }

    void testSubControl(Type type, String name) {
        LxControl ctrl = getControl(controlUuid);
        assertNotNull(ctrl);
        long n = ctrl.getSubControls().values().stream().filter(c -> name.equals(c.getName()))
                .collect(Collectors.counting());
        assertEquals(1L, n);
    }

    private Channel getChannel(String name, LxControl c) {
        List<Channel> channels = c.getChannels();
        List<Channel> filtered = channels.stream().filter(a -> name.equals(a.getLabel())).collect(Collectors.toList());
        assertEquals(1, filtered.size());
        return filtered.get(0);
    }

    private <T> long numberOfControls(Collection<LxControl> collection, Class<T> c) {
        return collection.stream().filter(o -> c.equals(o.getClass())).collect(Collectors.counting());
    }

    private <T> long numberOfControls(Class<T> c) {
        return numberOfControls(handler.controls.values(), c);
    }

    private <T> long numberOfSubControls(Class<T> c) {
        return numberOfControls(handler.subControls.values(), c);
    }

    private LxControl getControl(LxUuid uuid) {
        LxControl c = handler.controls.get(uuid);
        if (c == null) {
            c = handler.subControls.get(uuid);
        }
        return c;
    }

    private LxControl getSubControl(LxUuid uuid) {
        return handler.subControls.get(uuid);
    }

    private String getExpectedName(String controlName, String roomName, String postFix) {
        return roomName + " / " + controlName + (postFix != null ? postFix : "");
    }
}
