/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upnpcontrol.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 *
 * @author Mark Herwege - Initial contribution
 * @author Karel Goderis - Based on UPnP logic in Sonos binding
 */
@NonNullByDefault
public class UpnpEntry {

    private String id;
    private String refId;
    private String parentId;
    private String upnpClass;
    private String title = "";
    private List<UpnpEntryRes> resList = new ArrayList<>();;
    private String album = "";
    private String albumArtUri = "";
    private String creator = "";
    private String artist = "";
    private String publisher = "";
    private String genre = "";
    private @Nullable Integer originalTrackNumber;

    public UpnpEntry(String id, String refId, String parentId, String upnpClass) {
        this.id = id;
        this.refId = refId;
        this.parentId = parentId;
        this.upnpClass = upnpClass;
    }

    public UpnpEntry withTitle(String title) {
        this.title = title;
        return this;
    }

    public UpnpEntry withAlbum(String album) {
        this.album = album;
        return this;
    }

    public UpnpEntry withAlbumArtUri(String albumArtUri) {
        this.albumArtUri = albumArtUri;
        return this;
    }

    public UpnpEntry withCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public UpnpEntry withArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public UpnpEntry withPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public UpnpEntry withGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public UpnpEntry withResList(List<UpnpEntryRes> resList) {
        this.resList = resList;
        return this;
    }

    public UpnpEntry withTrackNumber(@Nullable Integer originalTrackNumber) {
        this.originalTrackNumber = originalTrackNumber;
        return this;
    }

    /**
     * @return the title of the entry.
     */
    @Override
    public String toString() {
        return title;
    }

    /**
     * @return the unique identifier of this entry.
     */
    public String getId() {
        return id;
    }

    /**
     * @return the title of the entry.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the identifier of the entry this reference intry refers to.
     */
    public String getRefId() {
        return refId;
    }

    /**
     * @return the unique identifier of the parent of this entry.
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @return a URI for this entry. Thumbnail resources are not considered.
     */
    public String getRes() {
        UpnpEntryRes resource;
        try {
            resource = resList.stream().filter(res -> !res.isThumbnailRes()).findFirst().get();
        } catch (NoSuchElementException e) {
            return "";
        }
        return resource.getRes();
    }

    public List<String> getProtocolList() {
        List<String> protocolList = new ArrayList<>();
        for (UpnpEntryRes entryRes : resList) {
            protocolList.add(entryRes.getProtocolInfo());
        }
        return protocolList;
    }

    /**
     * @return the UPnP classname for this entry.
     */
    public String getUpnpClass() {
        return upnpClass;
    }

    public boolean isContainer() {
        Pattern pattern = Pattern.compile("object.container");
        Matcher matcher = pattern.matcher(getUpnpClass());
        return (matcher.find());
    }

    /**
     * @return the name of the album.
     */
    public String getAlbum() {
        return album;
    }

    /**
     * @return the URI for the album art.
     */
    public String getAlbumArtUri() {
        return StringEscapeUtils.unescapeXml(albumArtUri);
    }

    /**
     * @return the name of the artist who created the entry.
     */
    public String getCreator() {
        return creator;
    }

    public String getArtist() {
        return artist;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getGenre() {
        return genre;
    }

    public @Nullable Integer getOriginalTrackNumber() {
        return originalTrackNumber;
    }
}
