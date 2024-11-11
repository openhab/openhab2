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
package org.openhab.binding.linky.internal.dto;

import org.eclipse.jetty.jaas.spi.UserInfo;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link UserInfo} holds informations about energy delivery point
 *
 * @author Gaël L'hopital - Initial contribution
 * @author Laurent Arnal - Rewrite addon to use official dataconect API
 */

public class UsagePoint {
    @SerializedName("usage_point_id")
    public String usagePointId;

    @SerializedName("usage_point_status")
    public String usagePointStatus;

    @SerializedName("meter_type")
    public String meterType;

    @SerializedName("usage_point_addresses")
    public AddressInfo usagePointAddresses;

    public class AddressInfo {
        public String street;
        public String locality;

        @SerializedName("postal_code")
        public String postalCode;

        @SerializedName("insee_code")
        public String inseeCode;
        public String city;
        public String country;
    }

    public static UsagePoint convertFromPrmDetail(PrmDetail prmDetail) {
        UsagePoint result = new UsagePoint();

        result.usagePointId = "";
        result.usagePointStatus = "";
        result.meterType = prmDetail.situationComptageDto.dispositifComptage().typeComptage().code();
        result.usagePointAddresses = result.new AddressInfo();

        result.usagePointAddresses.street = prmDetail.adresse.ligne4();
        result.usagePointAddresses.locality = prmDetail.adresse.ligne6();
        result.usagePointAddresses.city = prmDetail.adresse.ligne6();
        result.usagePointAddresses.postalCode = prmDetail.adresse.ligne6();
        result.usagePointAddresses.inseeCode = "";
        result.usagePointAddresses.country = "";

        return result;
    }
}
