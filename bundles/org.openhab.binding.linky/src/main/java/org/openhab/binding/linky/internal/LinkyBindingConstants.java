/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
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
package org.openhab.binding.linky.internal;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link LinkyBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Gaël L'hopital - Initial contribution
 * @author Laurent Arnal - Rewrite addon to use official dataconect API *
 */
@NonNullByDefault
public class LinkyBindingConstants {

    public static final String BINDING_ID = "linky";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_API_ENEDIS_BRIDGE = new ThingTypeUID(BINDING_ID, "enedis");
    public static final ThingTypeUID THING_TYPE_API_WEB_ENEDIS_BRIDGE = new ThingTypeUID(BINDING_ID, "enedis-web");
    public static final ThingTypeUID THING_TYPE_API_MYELECTRICALDATA_BRIDGE = new ThingTypeUID(BINDING_ID,
            "my-electrical-data");
    public static final ThingTypeUID THING_TYPE_LINKY = new ThingTypeUID(BINDING_ID, "linky");

    public static final Set<ThingTypeUID> SUPPORTED_DEVICE_THING_TYPES_UIDS = Set.of(THING_TYPE_API_ENEDIS_BRIDGE,
            THING_TYPE_API_WEB_ENEDIS_BRIDGE, THING_TYPE_API_MYELECTRICALDATA_BRIDGE, THING_TYPE_LINKY);

    // Thing properties
    // List of all Channel groups id's
    public static final String PUISSANCE = "puissance";
    public static final String PRM_ID = "prmId";
    public static final String USER_ID = "customerId";
    public static final String AV2_ID = "av2_interne_id";

    public static final String DAILY_GROUP = "daily";
    public static final String WEEKLY_GROUP = "weekly";
    public static final String MONTHLY_GROUP = "monthly";
    public static final String YEARLY_GROUP = "yearly";

    public static final String MAIN_GROUP = "main";
    public static final String TEMPO_GROUP = "tempo";
    public static final String LOAD_CURVE_GROUP = "loadCurve";

    // List of all Channel id's
    public static final String CONSUMPTION_CHANNEL = "consumption";
    public static final String MAX_POWER_CHANNEL = "maxPower";
    public static final String POWER_CHANNEL = "power";
    public static final String TIMESTAMP_CHANNEL = "power";

    public static final String DAY_MINUS_1 = "yesterday";
    public static final String DAY_MINUS_2 = "day-2";
    public static final String DAY_MINUS_3 = "day-3";

    public static final String PEAK_POWER_DAY_MINUS_1 = "power";
    public static final String PEAK_POWER_TS_DAY_MINUS_1 = "timestamp";

    public static final String PEAK_POWER_DAY_MINUS_2 = "power-2";
    public static final String PEAK_POWER_TS_DAY_MINUS_2 = "timestamp-2";

    public static final String PEAK_POWER_DAY_MINUS_3 = "power-3";
    public static final String PEAK_POWER_TS_DAY_MINUS_3 = "timestamp-3";

    public static final String WEEK_MINUS_0 = "thisWeek";
    public static final String WEEK_MINUS_1 = "lastWeek";
    public static final String WEEK_MINUS_2 = "week-2";

    public static final String MONTH_MINUS_0 = "thisMonth";
    public static final String MONTH_MINUS_1 = "lastMonth";
    public static final String MONTH_MINUS_2 = "month-2";

    public static final String YEAR_MINUS_0 = "thisYear";
    public static final String YEAR_MINUS_1 = "lastYear";
    public static final String YEAR_MINUS_2 = "year-2";

    public static final String TEMPO_TODAY_INFO = "tempoInfoToday";
    public static final String TEMPO_TOMORROW_INFO = "tempoInfoTomorrow";
    public static final String TEMPO_TEMPO_INFO_TIME_SERIES = "tempoInfoTimeSeries";

    public static final String MAIN_IDENTITY = "identity";

    public static final String MAIN_CONTRACT_SUBSCRIBED_POWER = "contractSubscribedPower";
    public static final String MAIN_CONTRACT_LAST_ACTIVATION_DATE = "contractLastActivationDate";
    public static final String MAIN_CONTRACT_DISTRIBUTION_TARIFF = "contractDistributionTariff";
    public static final String MAIN_CONTRACT_OFF_PEAK_HOURS = "contractOffpeakHours";
    public static final String MAIN_CONTRACT_CONTRACT_STATUS = "contractStatus";
    public static final String MAIN_CONTRACT_CONTRACT_TYPE = "contractType";
    public static final String MAIN_CONTRACT_LAST_DISTRIBUTION_TARIFF_CHANGE_DATE = "contractLastDistributionTariffChangeDate";
    public static final String MAIN_CONTRACT_SEGMENT = "contractSegment";

    public static final String MAIN_USAGEPOINT_ID = "usagePointId";
    public static final String MAIN_USAGEPOINT_STATUS = "usagePointStatus";
    public static final String MAIN_USAGEPOINT_METER_TYPE = "usagePointMeterType";

    public static final String MAIN_USAGEPOINT_METER_ADDRESS_CITY = "usagePointAddressCity";
    public static final String MAIN_USAGEPOINT_METER_ADDRESS_COUNTRY = "usagePointAddressCountry";
    public static final String MAIN_USAGEPOINT_METER_ADDRESS_INSEE_CODE = "usagePointAddressInseeCode";
    public static final String MAIN_USAGEPOINT_METER_ADDRESS_POSTAL_CODE = "usagePointAddressPostalCode";
    public static final String MAIN_USAGEPOINT_METER_ADDRESS_STREET = "usagePointAddressStreet";

    public static final String MAIN_CONTACT_MAIL = "contactMail";
    public static final String MAIN_CONTACT_PHONE = "contactPhone";

    // Authorization related Servlet and resources aliases.
    public static final String LINKY_ALIAS = "/connectlinky";
    public static final String LINKY_IMG_ALIAS = "/img";

    /**
     * Smartthings scopes needed by this binding to work.
     */
    public static final String LINKY_SCOPES = "am_application_scope default";

    public static final String ENEDIS_ACCOUNT_URL_PROD = "https://mon-compte-particulier.enedis.fr/";
    public static final String ENEDIS_AUTHORIZE_URL_PROD = ENEDIS_ACCOUNT_URL_PROD + "dataconnect/v1/oauth2/authorize";
    public static final String ENEDIS_API_TOKEN_URL_PROD = ENEDIS_ACCOUNT_URL_PROD + "oauth2/v3/token";

    public static final String ENEDIS_ACCOUNT_URL_PREPROD = "https://ext.prod-sandbox.api.enedis.fr/";
    public static final String ENEDIS_AUTHORIZE_URL_PREPROD = ENEDIS_ACCOUNT_URL_PROD
            + "dataconnect/v1/oauth2/authorize?duration=P36M";
    public static final String ENEDIS_API_TOKEN_URL_PREPROD = ENEDIS_ACCOUNT_URL_PREPROD + "oauth2/v3/token";

    // List of Linky services related urls, information
    public static final String LINKY_MYELECTRICALDATA_ACCOUNT_URL = "https://www.myelectricaldata.fr/";
    // public static final String LINKY_MYELECTRICALDATA_AUTHORIZE_URL = LINKY_MYELECTRICALDATA_ACCOUNT_URL
    // + "v1/oauth2/authorize?usage_points_id=21454992660003&user_type=aa&person_id=-1";
    public static final String LINKY_MYELECTRICALDATA_AUTHORIZE_URL = ENEDIS_AUTHORIZE_URL_PROD;
    public static final String LINKY_MYELECTRICALDATA_API_TOKEN_URL = LINKY_MYELECTRICALDATA_ACCOUNT_URL
            + "v1/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=na&user_type=na&state=na&person_id=-1&usage_points_id=%s";

    public static final String LINKY_MYELECTRICALDATA_CLIENT_ID = "_h7zLaRr2INxqBI8jhDUQXsa_G4a";
}
