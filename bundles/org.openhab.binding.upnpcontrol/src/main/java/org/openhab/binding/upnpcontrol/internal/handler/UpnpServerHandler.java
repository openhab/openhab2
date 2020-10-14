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
package org.openhab.binding.upnpcontrol.internal.handler;

import static org.openhab.binding.upnpcontrol.internal.UpnpControlBindingConstants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jupnp.model.meta.RemoteDevice;
import org.openhab.binding.upnpcontrol.internal.UpnpControlHandlerFactory;
import org.openhab.binding.upnpcontrol.internal.UpnpDynamicCommandDescriptionProvider;
import org.openhab.binding.upnpcontrol.internal.UpnpDynamicStateDescriptionProvider;
import org.openhab.binding.upnpcontrol.internal.config.UpnpControlBindingConfiguration;
import org.openhab.binding.upnpcontrol.internal.config.UpnpControlServerConfiguration;
import org.openhab.binding.upnpcontrol.internal.queue.UpnpEntry;
import org.openhab.binding.upnpcontrol.internal.queue.UpnpEntryQueue;
import org.openhab.binding.upnpcontrol.internal.util.UpnpControlUtil;
import org.openhab.binding.upnpcontrol.internal.util.UpnpProtocolMatcher;
import org.openhab.binding.upnpcontrol.internal.util.UpnpXMLParser;
import org.openhab.core.io.transport.upnp.UpnpIOService;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.types.Command;
import org.openhab.core.types.CommandOption;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.openhab.core.types.StateOption;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UpnpServerHandler} is responsible for handling commands sent to the UPnP Server. It implements UPnP
 * ContentDirectory service actions.
 *
 * @author Mark Herwege - Initial contribution
 * @author Karel Goderis - Based on UPnP logic in Sonos binding
 */
@NonNullByDefault
public class UpnpServerHandler extends UpnpHandler {

    static final String DIRECTORY_ROOT = "0";
    static final String UP = "..";

    private final Logger logger = LoggerFactory.getLogger(UpnpServerHandler.class);

    ConcurrentMap<String, UpnpRendererHandler> upnpRenderers;
    private volatile @Nullable UpnpRendererHandler currentRendererHandler;
    private volatile List<StateOption> rendererStateOptionList = Collections.synchronizedList(new ArrayList<>());

    private volatile List<CommandOption> playlistCommandOptionList = Collections.synchronizedList(new ArrayList<>());

    private @NonNullByDefault({}) ChannelUID rendererChannelUID;
    private @NonNullByDefault({}) ChannelUID currentSelectionChannelUID;
    private @NonNullByDefault({}) ChannelUID playlistSelectChannelUID;

    private volatile @Nullable CompletableFuture<Boolean> isBrowsing;
    private volatile boolean browseUp = false; // used to avoid automatically going down a level if only one container
                                               // entry found when going up in the hierarchy

    private static final UpnpEntry ROOT_ENTRY = new UpnpEntry(DIRECTORY_ROOT, DIRECTORY_ROOT, DIRECTORY_ROOT,
            "object.container");
    volatile UpnpEntry currentEntry = ROOT_ENTRY;
    // current entry list in selection
    List<UpnpEntry> entries = Collections.synchronizedList(new ArrayList<>());
    // store parents in hierarchy separately to be able to move up in directory structure
    private ConcurrentMap<String, UpnpEntry> parentMap = new ConcurrentHashMap<>();

    private volatile String playlistName = "";

    protected @NonNullByDefault({}) UpnpControlServerConfiguration config;

    public UpnpServerHandler(Thing thing, UpnpIOService upnpIOService,
            ConcurrentMap<String, UpnpRendererHandler> upnpRenderers,
            UpnpDynamicStateDescriptionProvider upnpStateDescriptionProvider,
            UpnpDynamicCommandDescriptionProvider upnpCommandDescriptionProvider,
            UpnpControlBindingConfiguration configuration) {
        super(thing, upnpIOService, configuration, upnpStateDescriptionProvider, upnpCommandDescriptionProvider);
        this.upnpRenderers = upnpRenderers;

        // put root as highest level in parent map
        parentMap.put(ROOT_ENTRY.getId(), ROOT_ENTRY);
    }

    @Override
    public void initialize() {
        super.initialize();
        config = getConfigAs(UpnpControlServerConfiguration.class);

        logger.debug("Initializing handler for media server device {}", thing.getLabel());

        Channel rendererChannel = thing.getChannel(UPNPRENDERER);
        if (rendererChannel != null) {
            rendererChannelUID = rendererChannel.getUID();
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Channel " + UPNPRENDERER + " not defined");
            return;
        }
        Channel selectionChannel = thing.getChannel(BROWSE);
        if (selectionChannel != null) {
            currentSelectionChannelUID = selectionChannel.getUID();
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Channel " + BROWSE + " not defined");
            return;
        }
        Channel playlistSelectChannel = thing.getChannel(PLAYLIST_SELECT);
        if (playlistSelectChannel != null) {
            playlistSelectChannelUID = playlistSelectChannel.getUID();
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "Channel " + PLAYLIST_SELECT + " not defined");
            return;
        }

        initDevice();
    }

    @Override
    public void dispose() {
        CompletableFuture<Boolean> browsingFuture = isBrowsing;
        if (browsingFuture != null) {
            browsingFuture.complete(false);
            isBrowsing = null;
        }

        super.dispose();
    }

    @Override
    protected void initJob() {
        synchronized (jobLock) {
            if (!ThingStatus.ONLINE.equals(thing.getStatus())) {
                rendererStateOptionList = Collections.synchronizedList(new ArrayList<>());
                synchronized (rendererStateOptionList) {
                    upnpRenderers.forEach((key, value) -> {
                        StateOption stateOption = new StateOption(key, value.getThing().getLabel());
                        rendererStateOptionList.add(stateOption);
                    });
                }
                updateStateDescription(rendererChannelUID, rendererStateOptionList);

                getProtocolInfo();

                browse(currentEntry.getId(), "BrowseDirectChildren", "*", "0", "0", config.sortcriteria);

                playlistsListChanged();

                RemoteDevice device = getDevice();
                if (device != null) {
                    updateDeviceConfig(device);
                }

                updateStatus(ThingStatus.ONLINE);
            }
        }
    }

    /**
     * Method that does a UPnP browse on a content directory. Results will be retrieved in the {@link onValueReceived}
     * method.
     *
     * @param objectID content directory object
     * @param browseFlag BrowseMetaData or BrowseDirectChildren
     * @param filter properties to be returned
     * @param startingIndex starting index of objects to return
     * @param requestedCount number of objects to return, 0 for all
     * @param sortCriteria sort criteria, example: +dc:title
     */
    protected void browse(String objectID, String browseFlag, String filter, String startingIndex,
            String requestedCount, String sortCriteria) {
        CompletableFuture<Boolean> browsing = isBrowsing;
        boolean browsed = true;
        try {
            if (browsing != null) {
                // wait for maximum 2.5s until browsing is finished
                browsed = browsing.get(config.responsetimeout, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.debug("Exception, previous server query on {} interrupted or timed out, trying new browse anyway",
                    thing.getLabel());
        }

        if (browsed) {
            isBrowsing = new CompletableFuture<Boolean>();

            Map<String, String> inputs = new HashMap<>();
            inputs.put("ObjectID", objectID);
            inputs.put("BrowseFlag", browseFlag);
            inputs.put("Filter", filter);
            inputs.put("StartingIndex", startingIndex);
            inputs.put("RequestedCount", requestedCount);
            inputs.put("SortCriteria", sortCriteria);

            invokeAction("ContentDirectory", "Browse", inputs);
        } else {
            logger.debug("Cannot browse, cancelled querying server {}", thing.getLabel());
        }
    }

    /**
     * Method that does a UPnP search on a content directory. Results will be retrieved in the {@link onValueReceived}
     * method.
     *
     * @param containerID content directory container
     * @param searchCriteria search criteria, examples:
     *            dc:title contains "song"
     *            dc:creator contains "Springsteen"
     *            upnp:class = "object.item.audioItem"
     *            upnp:album contains "Born in"
     * @param filter properties to be returned
     * @param startingIndex starting index of objects to return
     * @param requestedCount number of objects to return, 0 for all
     * @param sortCriteria sort criteria, example: +dc:title
     */
    protected void search(String containerID, String searchCriteria, String filter, String startingIndex,
            String requestedCount, String sortCriteria) {
        CompletableFuture<Boolean> browsing = isBrowsing;
        boolean browsed = true;
        try {
            if (browsing != null) {
                // wait for maximum 2.5s until browsing is finished
                browsed = browsing.get(config.responsetimeout, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.debug("Exception, previous server query on {} interrupted or timed out, trying new search anyway",
                    thing.getLabel());
        }

        if (browsed) {
            isBrowsing = new CompletableFuture<Boolean>();

            Map<String, String> inputs = new HashMap<>();
            inputs.put("ContainerID", containerID);
            inputs.put("SearchCriteria", searchCriteria);
            inputs.put("Filter", filter);
            inputs.put("StartingIndex", startingIndex);
            inputs.put("RequestedCount", requestedCount);
            inputs.put("SortCriteria", sortCriteria);

            invokeAction("ContentDirectory", "Search", inputs);
        } else {
            logger.debug("Cannot search, cancelled querying server {}", thing.getLabel());
        }
    }

    protected void updateServerState(ChannelUID channelUID, State state) {
        updateState(channelUID, state);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handle command {} for channel {} on server {}", command, channelUID, thing.getLabel());

        switch (channelUID.getId()) {
            case UPNPRENDERER:
                handleCommandUpnpRenderer(channelUID, command);
                break;
            case CURRENTID:
                handleCommandCurrentId(channelUID, command);
                break;
            case BROWSE:
<<<<<<< Upstream, based on main
                if (command instanceof StringType) {
                    String browseTarget = command.toString();
                    if (browseTarget != null) {
                        if (!UP.equals(browseTarget)) {
                            final String target = browseTarget;
                            synchronized (entries) {
                                Optional<UpnpEntry> current = entries.stream()
                                        .filter(entry -> target.equals(entry.getId())).findFirst();
                                if (current.isPresent()) {
                                    currentEntry = current.get();
                                } else {
                                    logger.info("Trying to browse invalid target {}", browseTarget);
                                    browseTarget = UP; // move up on invalid target
                                }
                            }
                        }
                        if (UP.equals(browseTarget)) {
                            // Move up in tree
                            browseTarget = currentEntry.getParentId();
                            if (browseTarget.isEmpty()) {
                                // No parent found, so make it the root directory
                                browseTarget = DIRECTORY_ROOT;
                            }
                            UpnpEntry entry = parentMap.get(browseTarget);
                            if (entry == null) {
                                logger.info("Browse target not found. Exiting.");
                                return;
                            }
                            currentEntry = entry;

                        }
                        updateState(CURRENTID, StringType.valueOf(currentEntry.getId()));
                        logger.debug("Browse target {}", browseTarget);
                        browse(browseTarget, "BrowseDirectChildren", "*", "0", "0", config.sortcriteria);
                    }
                }
=======
                handleCommandBrowse(command);
>>>>>>> 9c67025 Bring development to OH3.
                break;
            case SEARCH:
                handleCommandSearch(command);
                break;
            case PLAYLIST_SELECT:
                handleCommandPlaylistSelect(channelUID, command);
                break;
            case PLAYLIST:
                handleCommandPlaylist(channelUID, command);
                break;
            case PLAYLIST_ACTION:
                handleCommandPlaylistAction(command);
                break;
            case VOLUME:
            case MUTE:
            case CONTROL:
            case STOP:
                // Pass these on to the media renderer thing if one is selected
                handleCommandInRenderer(channelUID, command);
                break;
        }
    }

    private void handleCommandUpnpRenderer(ChannelUID channelUID, Command command) {
        UpnpRendererHandler renderer = null;
        UpnpRendererHandler previousRenderer = currentRendererHandler;
        if (command instanceof StringType) {
            renderer = (upnpRenderers.get(((StringType) command).toString()));
            currentRendererHandler = renderer;
            if (config.filter) {
                // only refresh title list if filtering by renderer capabilities
                browse(currentEntry.getId(), "BrowseDirectChildren", "*", "0", "0", config.sortcriteria);
            } else {
                serveMedia();
            }
        }

        if ((renderer != null) && !renderer.equals(previousRenderer)) {
            if (previousRenderer != null) {
                previousRenderer.unsetServerHandler();
            }
            renderer.setServerHandler(this);

            Channel channel;
            if ((channel = thing.getChannel(VOLUME)) != null) {
                handleCommand(channel.getUID(), RefreshType.REFRESH);
            }
            if ((channel = thing.getChannel(MUTE)) != null) {
                handleCommand(channel.getUID(), RefreshType.REFRESH);
            }
            if ((channel = thing.getChannel(CONTROL)) != null) {
                handleCommand(channel.getUID(), RefreshType.REFRESH);
            }
        }

        if ((renderer = currentRendererHandler) != null) {
            updateState(channelUID, StringType.valueOf(renderer.getThing().getUID().toString()));
        } else {
            updateState(channelUID, UnDefType.UNDEF);
        }
    }

    private void handleCommandCurrentId(ChannelUID channelUID, Command command) {
        String currentId = "";
        if (command instanceof StringType) {
            currentId = String.valueOf(command);
            logger.debug("Server {}, setting currentId to {}", thing.getLabel(), currentId);
            if (!currentId.isEmpty()) {
                if (parentMap.containsKey(currentId)) {
                    currentEntry = parentMap.get(currentId);
                } else {
                    // The real entry is not in the parentMap yet, so construct a default one
                    currentEntry = new UpnpEntry(currentId, currentId, DIRECTORY_ROOT, "object.container");
                }
                browse(currentId, "BrowseDirectChildren", "*", "0", "0", config.sortcriteria);
            }
        } else if (command instanceof RefreshType) {
            currentId = currentEntry.getId();
        }
        updateState(channelUID, StringType.valueOf(currentId));
    }

    private void handleCommandBrowse(Command command) {
        if (command instanceof StringType) {
            String browseTarget = command.toString();
            if (browseTarget != null) {
                if (!UP.equals(browseTarget)) {
                    final String target = browseTarget;
                    synchronized (entries) {
                        Optional<UpnpEntry> current = entries.stream().filter(entry -> target.equals(entry.getId()))
                                .findFirst();
                        if (current.isPresent()) {
                            currentEntry = current.get();
                        } else {
                            logger.info("Server {}, trying to browse invalid target {}", thing.getLabel(),
                                    browseTarget);
                            browseTarget = UP; // move up on invalid target
                        }
                    }
                }
                if (UP.equals(browseTarget)) {
                    // Move up in tree
                    browseTarget = currentEntry.getParentId();
                    if (browseTarget.isEmpty()) {
                        // No parent found, so make it the root directory
                        browseTarget = DIRECTORY_ROOT;
                    }
                    if (parentMap.containsKey(browseTarget)) {
                        currentEntry = parentMap.get(browseTarget);
                    } else {
                        // The real entry is not in the parentMap yet, so construct a default one
                        currentEntry = new UpnpEntry(browseTarget, browseTarget, DIRECTORY_ROOT, "object.container");
                    }
                    browseUp = true;
                }

                logger.debug("Navigating to node {} on server {}", currentEntry.getId(), thing.getLabel());
                updateState(CURRENTID, StringType.valueOf(currentEntry.getId()));
                logger.debug("Browse target {}", browseTarget);
                browse(browseTarget, "BrowseDirectChildren", "*", "0", "0", config.sortcriteria);
            }
        }
    }

    private void handleCommandSearch(Command command) {
        if (command instanceof StringType) {
            String criteria = command.toString();
            if (criteria != null) {
                String searchContainer = "";
                if (currentEntry.isContainer()) {
                    searchContainer = currentEntry.getId();
                } else {
                    searchContainer = currentEntry.getParentId();
                }
                if (config.searchfromroot || searchContainer.isEmpty()) {
                    // Config option search from root or no parent found, so make it the root directory
                    searchContainer = DIRECTORY_ROOT;
                }
                if (parentMap.containsKey(searchContainer)) {
                    currentEntry = parentMap.get(searchContainer);
                } else {
                    // The real entry is not in the parentMap yet, so construct a default one
                    currentEntry = new UpnpEntry(searchContainer, searchContainer, DIRECTORY_ROOT, "object.container");
                }

                logger.debug("Navigating to node {} on server {}", searchContainer, thing.getLabel());
                updateState(CURRENTID, StringType.valueOf(currentEntry.getId()));
                logger.debug("Search container {} for {}", searchContainer, criteria);
                search(searchContainer, criteria, "*", "0", "0", config.sortcriteria);
            }
        }
    }

    private void handleCommandPlaylistSelect(ChannelUID channelUID, Command command) {
        if (command instanceof StringType) {
            playlistName = command.toString();
            updateState(PLAYLIST, StringType.valueOf(playlistName));
        }
    }

    private void handleCommandPlaylist(ChannelUID channelUID, Command command) {
        if (command instanceof StringType) {
            playlistName = command.toString();
        }
        updateState(channelUID, StringType.valueOf(playlistName));
    }

    private void handleCommandPlaylistAction(Command command) {
        if (command instanceof StringType) {
            switch (command.toString()) {
                case RESTORE:
                    handleCommandPlaylistRestore();
                    break;
                case SAVE:
                    handleCommandPlaylistSave(false);
                    break;
                case APPEND:
                    handleCommandPlaylistSave(true);
                    break;
                case DELETE:
                    handleCommandPlaylistDelete();
                    break;
            }
        }
    }

    private void handleCommandPlaylistRestore() {
        if (!playlistName.isEmpty()) {
            // Don't immediately restore a playlist if a browse or search is still underway, or it could get overwritten
            CompletableFuture<Boolean> browsing = isBrowsing;
            try {
                if (browsing != null) {
                    // wait for maximum 2.5s until browsing is finished
                    browsing.get(config.responsetimeout, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logger.debug(
                        "Exception, previous server on {} query interrupted or timed out, restoring playlist anyway",
                        thing.getLabel());
            }

            UpnpEntryQueue queue = new UpnpEntryQueue();
            queue.restoreQueue(playlistName, config.udn, bindingConfig.path);
            updateTitleSelection(queue.getEntryList());

            String parentId;
            UpnpEntry current = queue.get(0);
            if (current != null) {
                parentId = current.getParentId();
                if (parentMap.containsKey(parentId)) {
                    currentEntry = parentMap.get(parentId);
                } else {
                    // The real entry is not in the parentMap yet, so construct a default one
                    currentEntry = new UpnpEntry(parentId, parentId, DIRECTORY_ROOT, "object.container");
                }
            } else {
                parentId = DIRECTORY_ROOT;
                currentEntry = ROOT_ENTRY;
            }

            logger.debug("Restoring playlist to node {} on server {}", parentId, thing.getLabel());
            updateState(CURRENTID, StringType.valueOf(currentEntry.getId()));
        }
    }

    private void handleCommandPlaylistSave(boolean append) {
        if (!playlistName.isEmpty()) {
            List<UpnpEntry> mediaQueue = new ArrayList<>();
            mediaQueue.addAll(entries);
            if (mediaQueue.isEmpty() && !currentEntry.isContainer()) {
                mediaQueue.add(currentEntry);
            }
            UpnpEntryQueue queue = new UpnpEntryQueue(mediaQueue, config.udn);
            queue.persistQueue(playlistName, append, bindingConfig.path);
            UpnpControlUtil.updatePlaylistsList(bindingConfig.path);
        }
    }

    private void handleCommandPlaylistDelete() {
        if (!playlistName.isEmpty()) {
            UpnpControlUtil.deletePlaylist(playlistName, bindingConfig.path);
            UpnpControlUtil.updatePlaylistsList(bindingConfig.path);
            updateState(PLAYLIST, UnDefType.UNDEF);
        }
    }

    private void handleCommandInRenderer(ChannelUID channelUID, Command command) {
        String channelId = channelUID.getId();
        UpnpRendererHandler handler = currentRendererHandler;
        Channel channel;
        if ((handler != null) && (channel = handler.getThing().getChannel(channelId)) != null) {
            handler.handleCommand(channel.getUID(), command);
        } else if (!STOP.equals(channelId)) {
            updateState(channelId, UnDefType.UNDEF);
        }
    }

    /**
     * Add a renderer to the renderer channel state option list.
     * This method is called from the {@link UpnpControlHandlerFactory} class when creating a renderer handler.
     *
     * @param key
     */
    public void addRendererOption(String key) {
        synchronized (rendererStateOptionList) {
            rendererStateOptionList.add(new StateOption(key, upnpRenderers.get(key).getThing().getLabel()));
        }
        updateStateDescription(rendererChannelUID, rendererStateOptionList);
        logger.debug("Renderer option {} added to {}", key, thing.getLabel());
    }

    /**
     * Remove a renderer from the renderer channel state option list.
     * This method is called from the {@link UpnpControlHandlerFactory} class when removing a renderer handler.
     *
     * @param key
     */
    public void removeRendererOption(String key) {
        UpnpRendererHandler handler = currentRendererHandler;
        if ((handler != null) && (handler.getThing().getUID().toString().equals(key))) {
            currentRendererHandler = null;
            updateState(rendererChannelUID, UnDefType.UNDEF);
        }
        synchronized (rendererStateOptionList) {
            rendererStateOptionList.removeIf(stateOption -> (stateOption.getValue().equals(key)));
        }
        updateStateDescription(rendererChannelUID, rendererStateOptionList);
        logger.debug("Renderer option {} removed from {}", key, thing.getLabel());
    }

    @Override
    public void playlistsListChanged() {
        playlistCommandOptionList = UpnpControlUtil.playlists().stream().map(p -> (new CommandOption(p, p)))
                .collect(Collectors.toList());
        updateCommandDescription(playlistSelectChannelUID, playlistCommandOptionList);
    }

    private void updateTitleSelection(List<UpnpEntry> titleList) {
        // Optionally, filter only items that can be played on the renderer
        logger.debug("Filtering content on server {}: {}", thing.getLabel(), config.filter);
        List<UpnpEntry> resultList = config.filter ? filterEntries(titleList, true) : titleList;

        List<CommandOption> commandOptionList = new ArrayList<>();
        // Add a directory up selector if not in the directory root
        if ((!resultList.isEmpty() && !(DIRECTORY_ROOT.equals(resultList.get(0).getParentId())))
                || (resultList.isEmpty() && !DIRECTORY_ROOT.equals(currentEntry.getId()))) {
            CommandOption commandOption = new CommandOption(UP, UP);
            commandOptionList.add(commandOption);
            logger.debug("UP added to selection list on server {}", thing.getLabel());
        }

        synchronized (entries) {
            entries.clear(); // always only keep the current selection in the entry map to keep memory usage down
            resultList.forEach((value) -> {
                CommandOption commandOption = new CommandOption(value.getId(), value.getTitle());
                commandOptionList.add(commandOption);
                logger.trace("{} added to selection list on server {}", value.getId(), thing.getLabel());

                // Keep the entries in a map so we can find the parent and container for the current selection to go
                // back up
                if (value.isContainer()) {
                    parentMap.put(value.getId(), value);
                }
                entries.add(value);
            });
        }

        logger.debug("{} entries added to selection list on server {}", commandOptionList.size(), thing.getLabel());
        updateCommandDescription(currentSelectionChannelUID, commandOptionList);

        serveMedia();
    }

    /**
     * Filter a list of media and only keep the media that are playable on the currently selected renderer. Return all
     * if no renderer is selected.
     *
     * @param resultList
     * @param includeContainers
     * @return
     */
    private List<UpnpEntry> filterEntries(List<UpnpEntry> resultList, boolean includeContainers) {
        logger.debug("Server {}, raw result list {}", thing.getLabel(), resultList);

        UpnpRendererHandler handler = currentRendererHandler;
        List<String> sink = (handler != null) ? handler.getSink() : null;
        List<UpnpEntry> list = resultList.stream()
                .filter(entry -> ((includeContainers && entry.isContainer()) || (sink == null) && !entry.isContainer())
                        || ((sink != null) && UpnpProtocolMatcher.testProtocolList(entry.getProtocolList(), sink)))
                .collect(Collectors.toList());

        logger.debug("Server {}, filtered result list {}", thing.getLabel(), list);
        return list;
    }

<<<<<<< Upstream, based on main
    private void updateStateDescription(ChannelUID channelUID, List<StateOption> stateOptionList) {
        StateDescription stateDescription = StateDescriptionFragmentBuilder.create().withReadOnly(false)
                .withOptions(stateOptionList).build().toStateDescription();
        upnpStateDescriptionProvider.setDescription(channelUID, stateDescription);
    }

    private void updateCommandDescription(ChannelUID channelUID, List<CommandOption> commandOptionList) {
        CommandDescription commandDescription = CommandDescriptionBuilder.create().withCommandOptions(commandOptionList)
                .build();
        upnpCommandDescriptionProvider.setDescription(channelUID, commandDescription);
    }

    /**
     * Method that does a UPnP browse on a content directory. Results will be retrieved in the
     * {@link #onValueReceived(String, String, String)} method.
     *
     * @param objectID content directory object
     * @param browseFlag BrowseMetaData or BrowseDirectChildren
     * @param filter properties to be returned
     * @param startingIndex starting index of objects to return
     * @param requestedCount number of objects to return, 0 for all
     * @param sortCriteria sort criteria, example: +dc:title
     */
    public void browse(String objectID, String browseFlag, String filter, String startingIndex, String requestedCount,
            String sortCriteria) {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("ObjectID", objectID);
        inputs.put("BrowseFlag", browseFlag);
        inputs.put("Filter", filter);
        inputs.put("StartingIndex", startingIndex);
        inputs.put("RequestedCount", requestedCount);
        inputs.put("SortCriteria", sortCriteria);

        invokeAction("ContentDirectory", "Browse", inputs);
    }

    /**
     * Method that does a UPnP search on a content directory. Results will be retrieved in the
     * {@link #onValueReceived(String, String, String)} method.
     *
     * @param containerID content directory container
     * @param searchCriteria search criteria, examples:
     *            dc:title contains "song"
     *            dc:creator contains "Springsteen"
     *            upnp:class = "object.item.audioItem"
     *            upnp:album contains "Born in"
     * @param filter properties to be returned
     * @param startingIndex starting index of objects to return
     * @param requestedCount number of objects to return, 0 for all
     * @param sortCriteria sort criteria, example: +dc:title
     */
    public void search(String containerID, String searchCriteria, String filter, String startingIndex,
            String requestedCount, String sortCriteria) {
        Map<String, String> inputs = new HashMap<>();
        inputs.put("ContainerID", containerID);
        inputs.put("SearchCriteria", searchCriteria);
        inputs.put("Filter", filter);
        inputs.put("StartingIndex", startingIndex);
        inputs.put("RequestedCount", requestedCount);
        inputs.put("SortCriteria", sortCriteria);

        invokeAction("ContentDirectory", "Search", inputs);
    }

    @Override
    public void onStatusChanged(boolean status) {
        logger.debug("Server status changed to {}", status);
        if (status) {
            initServer();
        } else {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    "Communication lost with " + thing.getLabel());
        }
        super.onStatusChanged(status);
    }

=======
>>>>>>> 9c67025 Bring development to OH3.
    @Override
    public void onValueReceived(@Nullable String variable, @Nullable String value, @Nullable String service) {
        logger.debug("UPnP device {} received variable {} with value {} from service {}", thing.getLabel(), variable,
                value, service);
        if (variable == null) {
            return;
        }
        switch (variable) {
            case "Result":
                onValueReceivedResult(value);
                break;
            case "NumberReturned":
            case "TotalMatches":
            case "UpdateID":
                break;
            default:
                super.onValueReceived(variable, value, service);
                break;
        }
    }

    private void onValueReceivedResult(@Nullable String value) {
        CompletableFuture<Boolean> browsing = isBrowsing;
        if (!((value == null) || (value.isEmpty()))) {
            List<UpnpEntry> list = UpnpXMLParser.getEntriesFromXML(value);
            if (config.browsedown && (list.size() == 1) && list.get(0).isContainer() && !browseUp) {
                // We only received one container entry, so we immediately browse to the next level if config.browsedown
                // = true
                if (browsing != null) {
                    browsing.complete(true); // Clear previous browse flag before starting new browse
                }
                currentEntry = list.get(0);
                String browseTarget = currentEntry.getId();
                parentMap.put(browseTarget, currentEntry);
                updateState(CURRENTID, StringType.valueOf(currentEntry.getId()));
                logger.debug("Server {}, browsing down one level to the unique container result {}", thing.getLabel(),
                        browseTarget);
                browse(browseTarget, "BrowseDirectChildren", "*", "0", "0", config.sortcriteria);
            } else {
                updateTitleSelection(removeDuplicates(list));
            }
        } else {
            updateTitleSelection(new ArrayList<UpnpEntry>());
        }
        browseUp = false;
        if (browsing != null) {
            browsing.complete(true); // We have received browse or search results, so can launch new browse or
                                     // search
        }
    }

    @Override
    protected void updateProtocolInfo(String value) {
    }

    /**
     * Remove double entries by checking the refId if it exists as Id in the list and only keeping the original entry if
     * available. If the original entry is not in the list, only keep one referring entry.
     *
     * @param list
     * @return filtered list
     */
    private List<UpnpEntry> removeDuplicates(List<UpnpEntry> list) {
        List<UpnpEntry> newList = new ArrayList<>();
        Set<String> refIdSet = new HashSet<>();
        list.forEach(entry -> {
            String refId = entry.getRefId();
            if (refId.isEmpty() || !refIdSet.contains(refId)) {
                newList.add(entry);
                refIdSet.add(refId);
            }
        });
        return newList;
    }

    private void serveMedia() {
        UpnpRendererHandler handler = currentRendererHandler;
        if (handler != null) {
            List<UpnpEntry> mediaQueue = Collections.synchronizedList(new ArrayList<>());
            mediaQueue.addAll(filterEntries(entries, false));
            if (mediaQueue.isEmpty() && !currentEntry.isContainer()) {
                mediaQueue.add(currentEntry);
            }
            if (mediaQueue.isEmpty()) {
                logger.debug("Nothing to serve from server {} to renderer {}", thing.getLabel(),
                        handler.getThing().getLabel());
            } else {
                UpnpEntryQueue queue = new UpnpEntryQueue(mediaQueue, getUDN());
                handler.registerQueue(queue);
                logger.debug("Serving media queue {} from server {} to renderer {}", mediaQueue, thing.getLabel(),
                        handler.getThing().getLabel());

                // always keep a copy of current list that is being served
                queue.persistQueue(bindingConfig.path);
                UpnpControlUtil.updatePlaylistsList(bindingConfig.path);
            }
        } else {
            logger.warn("Cannot serve media from server {}, no renderer selected", thing.getLabel());
        }
    }
}
