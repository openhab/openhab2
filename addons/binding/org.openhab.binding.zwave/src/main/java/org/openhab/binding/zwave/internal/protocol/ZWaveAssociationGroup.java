/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openhab.binding.zwave.internal.HexToIntegerConverter;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * This class provides a storage class for zwave association groups
 * within the node class. This is then serialised to XML.
 *
 * The class consolidates information from the different association classes -
 * ASSOCIATION, MULTI_INSTANCE_ASSOCIATION, and ASSOCIATION_GROUP_INFO.
 *
 * This is necessary since ASSOCIATION, MULTI_INSTANCE_ASSOCIATION provide the same
 * information and overlap in their responses
 *
 * @author Chris Jackson
 */
@XStreamAlias("associationGroup")
public class ZWaveAssociationGroup {
    private int index;
    private String name;

    @XStreamConverter(HexToIntegerConverter.class)
    private Integer profile;
    private Set<CommandClass> commands;

    List<ZWaveAssociation> associations = new ArrayList<ZWaveAssociation>();

    public ZWaveAssociationGroup(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int newIndex) {
        index = newIndex;
    }

    public void addAssociation(int node) {
        addAssociation(node, 0);
    }

    public void addAssociation(int node, int endpoint) {
        // Check if we're already associated
        if (isAssociated(node, endpoint)) {
            return;
        }

        // No - add a new association
        ZWaveAssociation newAssociation = new ZWaveAssociation(node, endpoint);
        associations.add(newAssociation);
    }

    public boolean removeAssociation(int node) {
        return removeAssociation(node, 0);
    }

    public boolean removeAssociation(int node, int endpoint) {
        int associationCnt = associations.size();
        for (int index = 0; index < associationCnt; index++) {
            ZWaveAssociation association = associations.get(index);
            if (association.getNode() == node && association.getEndpoint() == endpoint) {
                associations.remove(index);
                return true;
            }
        }

        return false;
    }

    public boolean isAssociated(int node) {
        return isAssociated(node, 0);
    }

    public boolean isAssociated(int node, int endpoint) {
        int associationCnt = associations.size();
        for (int index = 0; index < associationCnt; index++) {
            ZWaveAssociation association = associations.get(index);
            if (association.getNode() == node && association.getEndpoint() == endpoint) {
                return true;
            }
        }

        return false;
    }

    public List<ZWaveAssociation> getAssociations() {
        return associations;
    }

    public void setAssociations(List<ZWaveAssociation> associations) {
        this.associations = associations;
    }

    public int getAssociationCnt() {
        return associations.size();
    }

    public Set<CommandClass> getCommands() {
        return commands;
    }

    public void setCommands(Set<CommandClass> commands) {
        this.commands = commands;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
