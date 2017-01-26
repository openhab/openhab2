/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.amazondashbutton.internal.pcap;

import java.util.List;

import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;

import com.google.common.base.Function;
import com.google.common.base.Objects;

/**
 * This wrapper is needed as {@link PcapNetworkInterface#equals(Object)} and {@link PcapNetworkInterface#hashCode()} are
 * not implemented in an appropriate way. The wrapper delegates all methods calls except {@link #equals(Object)} and
 * {@link #hashCode()} to {@link #pcapNetworkInterface}.
 *
 * @author Oliver Libutzki - Initial contribution
 *
 */
public class PcapNetworkInterfaceWrapper {

    /**
     * The wrapped object
     */
    private final PcapNetworkInterface pcapNetworkInterface;

    /**
     * Use this Guava function in order to create a {@link PcapNetworkInterfaceWrapper} instance.
     */
    public final static Function<PcapNetworkInterface, PcapNetworkInterfaceWrapper> TRANSFORMER = new Function<PcapNetworkInterface, PcapNetworkInterfaceWrapper>() {

        @Override
        public PcapNetworkInterfaceWrapper apply(PcapNetworkInterface pcapNetworkInterface) {
            return new PcapNetworkInterfaceWrapper(pcapNetworkInterface);
        }
    };

    private PcapNetworkInterfaceWrapper(PcapNetworkInterface pcapNetworkInterface) {
        if (pcapNetworkInterface == null) {
            throw new NullPointerException("Don't pass null.");
        }
        this.pcapNetworkInterface = pcapNetworkInterface;
    }

    public List<PcapAddress> getAddresses() {
        return pcapNetworkInterface.getAddresses();
    }

    public String getName() {
        return pcapNetworkInterface.getName();
    }

    public String getDescription() {
        return pcapNetworkInterface.getDescription();
    }

    public PcapHandle openLive(int arg0, PromiscuousMode arg1, int arg2) throws PcapNativeException {
        return pcapNetworkInterface.openLive(arg0, arg1, arg2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PcapNetworkInterfaceWrapper other = (PcapNetworkInterfaceWrapper) obj;
        return Objects.equal(this.getName(), other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getName());
    }

    @Override
    public String toString() {
        return pcapNetworkInterface.toString();
    }

}
