/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.spotify.internal.api.model;

import java.util.List;

/**
 * Spotify Api Album data class.
 *
 * @author Andreas Stenlund - Initial contribution
 * @author Hilbrand Bouwkamp - Moved to it's own class
 */
public class Album {

    private String albumType;
    private List<Artist> artists;
    private List<String> availableMarkets;
    private ExternalUrl externalUrls;
    private String href;
    private String id;
    private List<Image> images;
    private String name;
    private String type;
    private String uri;

    public String getAlbumType() {
        return albumType;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public ExternalUrl getExternalUrls() {
        return externalUrls;
    }

    public String getHref() {
        return href;
    }

    public String getId() {
        return id;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }
}
