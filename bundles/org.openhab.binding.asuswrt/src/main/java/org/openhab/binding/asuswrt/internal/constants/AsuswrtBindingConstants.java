/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.asuswrt.internal.constants;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link AsuswrtBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Christian Wild - Initial contribution
 */
@NonNullByDefault
public class AsuswrtBindingConstants {

    public static final String BINDING_ID = "asuswrt";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_ROUTER = new ThingTypeUID(BINDING_ID, "router");
    public static final ThingTypeUID THING_TYPE_CLIENT = new ThingTypeUID(BINDING_ID, "client");
    public static final ThingTypeUID THING_TYPE_INTERFACE = new ThingTypeUID(BINDING_ID, "interface");

    public static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(THING_TYPE_ROUTER, THING_TYPE_CLIENT,
            THING_TYPE_INTERFACE);

    /*** THINGS WITH CHANNEL GROUPS ***/
    public static final Set<ThingTypeUID> CHANNEL_GROUP_THING_SET = Collections
            .unmodifiableSet(Stream.of(SUPPORTED_THING_TYPES_UIDS).flatMap(Set::stream).collect(Collectors.toSet()));

    /***
     * CHANNEL LISTS
     * item channel names
     ***/

    // general event constats
    public static final String EVENT_STATE_CONNECTED = "connected";
    public static final String EVENT_STATE_GONE = "gone";
    public static final String EVENT_STATE_DISCONNECTED = "disconnected";

    // global channels
    public static final String CHANNELS_ALL = "anyChannel";

    // channel group system info
    public static final String CHANNEL_GROUP_SYSINFO = "sysInfo";
    public static final String CHANNEL_MEM_FREE = "memFree";
    public static final String CHANNEL_MEM_FREE_PERCENT = "memFreePercent";
    public static final String CHANNEL_MEM_TOTAL = "memTotal";
    public static final String CHANNEL_MEM_USED = "memUsed";
    public static final String CHANNEL_MEM_USED_PERCENT = "memUsedPercent";
    public static final String CHANNEL_CPU_USED_PERCENT = "cpuUsedPercent";

    // channel group interface information
    public static final String CHANNEL_GROUP_NETWORK = "networkInfo";
    public static final String CHANNEL_NETWORK_IP = "ipAddress";
    public static final String CHANNEL_NETWORK_MAC = "macAddress";
    public static final String CHANNEL_NETWORK_MASK = "subnet";
    public static final String CHANNEL_NETWORK_GATEWAY = "gateway";
    public static final String CHANNEL_NETWORK_METHOD = "ipMethod";
    public static final String CHANNEL_NETWORK_DNS = "dnsServers";
    public static final String CHANNEL_NETWORK_STATE = "networkState";
    public static final String CHANNEL_NETWORK_INTERNET = "internetState";
    public static final String EVENT_CONNECTION = "connectionEvent";

    // channel group clientList information
    public static final String CHANNEL_GROUP_CLIENTS = "clientList";
    public static final String CHANNEL_CLIENTS_KNOWN = "knownClients";
    public static final String CHANNEL_CLIENTS_ONLINE = "onlineClients";
    public static final String CHANNEL_CLIENTS_COUNT = "onlineClientsCount";
    public static final String CHANNEL_CLIENTS_ONLINE_MAC = "onlineMACs";
    public static final String EVENT_CLIENT_CONNECTION = "clientOnlineEvent";

    // channel group client information
    public static final String CHANNEL_GROUP_CLIENT = "client";
    public static final String CHANNEL_CLIENT_NICKNAME = "clientName";

    // channel group traffic
    public static final String CHANNEL_GROUP_TRAFFIC = "traffic";
    public static final String CHANNEL_TRAFFIC_TOTAL_RX = "totalRX";
    public static final String CHANNEL_TRAFFIC_TOTAL_TX = "totalTX";
    public static final String CHANNEL_TRAFFIC_TODAY_RX = "todayRX";
    public static final String CHANNEL_TRAFFIC_TODAY_TX = "todayTX";
    public static final String CHANNEL_TRAFFIC_CURRENT_RX = "curRX";
    public static final String CHANNEL_TRAFFIC_CURRENT_TX = "curTX";

    /**
     * PROPERTIES
     */

    // interface
    public static final String PROPERTY_INTERFACE_NAME = "interfaceName";
    public static final String NETWORK_REPRASENTATION_PROPERTY = "interfaceName";
    // client
    public static final String PROPERTY_CLIENT_NAME = "dnsName";
    public static final String CLIENT_REPRASENTATION_PROPERTY = "macAddress";

    /***
     * JSON REQUEST MEMBERNAMES
     * member-names of JSON response
     ***/
    public static final String JSON_MEMBER_TOKEN = "asus_token";
    // sysInfo
    public static final String JSON_MEMBER_PRODUCTID = "productid";
    public static final String JSON_MEMBER_FIRMWARE = "firmver";
    public static final String JSON_MEMBER_BUILD = "buildno";
    public static final String JSON_MEMBER_EXTENDNO = "extendo";
    public static final String JSON_MEMBER_MAC = "lan_hwaddr";

    // lanInfo
    public static final String JSON_MEMBER_LAN_IP = "lan_ipaddr";
    public static final String JSON_MEMBER_LAN_GATEWAY = "lan_gateway";
    public static final String JSON_MEMBER_LAN_NETMASK = "lan_netmask";
    public static final String JSON_MEMBER_LAN_PROTO = "lan_proto";

    // wanInfo
    public static final String JSON_MEMBER_WAN_IP = "wanlink-ipaddr";
    public static final String JSON_MEMBER_WAN_GATEWAY = "wanlink-gateway";
    public static final String JSON_MEMBER_WAN_NETMASK = "wanlink-netmask";
    public static final String JSON_MEMBER_WAN_PROTO = "wanlink-type";
    public static final String JSON_MEMBER_WAN_DNS_SERVER = "wanlink-dns";
    public static final String JSON_MEMBER_WAN_CONNECTED = "wanlink-status";

    // clientInfo
    public static final String JSON_MEMBER_CLIENTS = "get_clientlist";
    public static final String JSON_MEMBER_MACLIST = "maclist";
    public static final String JSON_MEMBER_API_LEVEL = "ClientAPILevel";
    public static final String JSON_MEMBER_CLIENT_RXCUR = "curRx";
    public static final String JSON_MEMBER_CLIENT_TXCUR = "curTx";
    public static final String JSON_MEMBER_CLIENT_DEFTYPE = "defaultType";
    public static final String JSON_MEMBER_CLIENT_DPIDEVICE = "dpiDevice";
    public static final String JSON_MEMBER_CLIENT_DPITYPE = "dpiType";
    public static final String JSON_MEMBER_CLIENT_IPFROM = "from";
    public static final String JSON_MEMBER_CLIENT_GROUP = "group";
    public static final String JSON_MEMBER_CLIENT_INETMODE = "internetMode";
    public static final String JSON_MEMBER_CLIENT_INETSTATE = "internetState";
    public static final String JSON_MEMBER_CLIENT_IP = "ip";
    public static final String JSON_MEMBER_CLIENT_IPMETHOD = "ipMethod";
    public static final String JSON_MEMBER_CLIENT_IPGATEWAY = "isGateway";
    public static final String JSON_MEMBER_CLIENT_GN = "isGN";
    public static final String JSON_MEMBER_CLIENT_ITUNES = "isITunes";
    public static final String JSON_MEMBER_CLIENT_LOGIN = "isLogin";
    public static final String JSON_MEMBER_CLIENT_ONLINE = "isOnline";
    public static final String JSON_MEMBER_CLIENT_PRINTER = "isPrinter";
    public static final String JSON_MEMBER_CLIENT_WEBSRV = "isWebServer";
    public static final String JSON_MEMBER_CLIENT_WIFI = "isWL";
    public static final String JSON_MEMBER_CLIENT_KEEPARP = "keeparp";
    public static final String JSON_MEMBER_CLIENT_MAC = "mac";
    public static final String JSON_MEMBER_CLIENT_MACREPEAT = "macRepeat";
    public static final String JSON_MEMBER_CLIENT_NAME = "name";
    public static final String JSON_MEMBER_CLIENT_NICK = "nickName";
    public static final String JSON_MEMBER_CLIENT_MODE = "opMode";
    public static final String JSON_MEMBER_CLIENT_QOSLVL = "qosLevel";
    public static final String JSON_MEMBER_CLIENT_ROG = "ROG";
    public static final String JSON_MEMBER_CLIENT_RSSI = "rssi";
    public static final String JSON_MEMBER_CLIENT_SSID = "ssid";
    public static final String JSON_MEMBER_CLIENT_RXTOTAL = "totalRx";
    public static final String JSON_MEMBER_CLIENT_TXTOTAL = "totalTx";
    public static final String JSON_MEMBER_CLIENT_VENDOR = "vendor";
    public static final String JSON_MEMBER_CLIENT_CONNECTTIME = "wlConnectTime";
    public static final String JSON_MEMBER_CLIENT_WTFAST = "wtfast";

    // usage
    public static final String JSON_MEMBER_CPU_USAGE = "cpu_usage";
    public static final String JSON_MEMBER_CPU_TOTAL = "cpu{x}_total";
    public static final String JSON_MEMBER_CPU_USED = "cpu{x}_usage";
    public static final String JSON_MEMBER_MEM_USAGE = "memory_usage";
    public static final String JSON_MEMBER_MEM_TOTAL = "mem_total";
    public static final String JSON_MEMBER_MEM_USED = "mem_used";
    public static final String JSON_MEMBER_MEM_FREE = "mem_free";
    public static final Integer USAGE_CPU_COUNT = 4; // max count of cpu-cores

    // traffic
    public static final String JSON_MEMBER_TRAFFIC = "netdev";
    public static final String JSON_MEMBER_INET_RX = "INTERNET_rx";
    public static final String JSON_MEMBER_INET_TX = "INTERNET_tx";
    public static final String JSON_MEMBER_LAN_RX = "WIRED_rx";
    public static final String JSON_MEMBER_LAN_TX = "WIRED_tx";
    public static final String JSON_MEMBER_WLAN_RX = "WIRELESS{}_rx";
    public static final String JSON_MEMBER_WLAN_TX = "WIRELESS{}_tx";
}
