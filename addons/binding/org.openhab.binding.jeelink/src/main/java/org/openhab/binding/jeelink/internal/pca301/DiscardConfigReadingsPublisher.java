/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jeelink.internal.pca301;

import org.openhab.binding.jeelink.internal.ReadingPublisher;

/**
 * Discards all PCA301 config readings and propagates remaining readings to underlying publisher.
 *
 * @author Volker Bier - Initial contribution
 */
public class DiscardConfigReadingsPublisher implements ReadingPublisher<Pca301Reading> {
    private ReadingPublisher<Pca301Reading> fDelegate;

    public DiscardConfigReadingsPublisher(ReadingPublisher<Pca301Reading> publisher) {
        fDelegate = publisher;
    }

    @Override
    public void publish(Pca301Reading reading) {
        if (!reading.isConfigReading()) {
            fDelegate.publish(reading);
        }
    }

    @Override
    public void dispose() {
        fDelegate.dispose();
    }
}
