/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.nanoleaf.internal.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents layout of the light panels
 *
 * @author Martin Raepple - Initial contribution
 * @author Stefan Höhn - further improvements
 */
@NonNullByDefault
public class Layout {

    private int numPanels;

    private final Logger logger = LoggerFactory.getLogger(Layout.class);

    private @Nullable List<PositionDatum> positionData = null;

    public int getNumPanels() {
        return numPanels;
    }

    public void setNumPanels(int numPanels) {
        this.numPanels = numPanels;
    }

    public @Nullable List<PositionDatum> getPositionData() {
        return positionData;
    }

    public void setPositionData(List<PositionDatum> positionData) {
        this.positionData = positionData;
    }

    /**
     * Returns an text representation for a canvas layout.
     *
     * Note only canvas supported currently due to its easy geometry
     *
     * @return a String containing the layout
     */
    public String getLayoutView() {
        if (positionData != null) {
            String view = "";

            int minx = Integer.MAX_VALUE;
            int maxx = Integer.MIN_VALUE;
            int miny = Integer.MAX_VALUE;
            int maxy = Integer.MIN_VALUE;
            int sideLength = Integer.MIN_VALUE;

            @SuppressWarnings("null")
            final int noofDefinedPanels = positionData.size();

            /*
             * Since 5.0.0 sidelengths are panelspecific and not delivered per layout but only the individual panel.
             * The only approximation we can do then is to derive the max-sidelength
             * the other issue is that panel sidelength have become fix per paneltype which has to be retrieved in a
             * hardcoded way.
             */
            for (int index = 0; index < noofDefinedPanels; index++) {
                if (positionData != null) {

                    @SuppressWarnings("null")
                    PositionDatum panel = positionData.get(index);
                    logger.debug("Layout: Panel position data x={} y={}", panel.getPosX(), panel.getPosY());

                    if (panel != null) {
                        if (panel.getPosX() < minx) {
                            minx = panel.getPosX();
                        }
                        if (panel.getPosX() > maxx) {
                            maxx = panel.getPosX();
                        }
                        if (panel.getPosY() < miny) {
                            miny = panel.getPosY();
                        }
                        if (panel.getPosY() > maxy) {
                            maxy = panel.getPosY();
                        }
                        if (panel.getPanelSize() > sideLength) {
                            sideLength = panel.getPanelSize();
                        }
                    }
                }
            }

            int shiftWidth = sideLength / 2;

            if (shiftWidth == 0) {
                // seems we do not have squares here
                return "Cannot render layout. Please note that layout views are only supported for square panels.";
            }

            int lineY = maxy;
            Map<Integer, PositionDatum> map;

            while (lineY >= miny) {
                map = new TreeMap<>();
                for (int index = 0; index < noofDefinedPanels; index++) {

                    if (positionData != null) {
                        @SuppressWarnings("null")
                        PositionDatum panel = positionData.get(index);

                        if (panel != null && panel.getPosY() == lineY) {
                            map.put(panel.getPosX(), panel);
                        }
                    }
                }
                lineY -= shiftWidth;
                for (int x = minx; x <= maxx; x += shiftWidth) {
                    if (map.containsKey(x)) {
                        PositionDatum panel = map.get(x);
                        @SuppressWarnings("null")
                        int panelId = panel.getPanelId();
                        view += String.format("%5s ", panelId);
                    } else {
                        view += "      ";
                    }
                }
                view += "\n";
            }

            return view;
        } else {
            return "";
        }
    }
}
