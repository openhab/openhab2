/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.icloud.internal;

import org.openhab.binding.icloud.internal.json.google.AddressComponent;
import org.openhab.binding.icloud.internal.json.google.JSONRootObject;
import org.openhab.binding.icloud.internal.json.google.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Parses a google API address lookup response (json).
 *
 * @author Patrik Gfeller - Initial Contribution
 *
 */
public class AddressLookupParser {
    private final Logger logger = LoggerFactory.getLogger(AddressLookupParser.class);
    private JSONRootObject data;

    public AddressLookupParser(String json) {
        try {
            Gson gson = new GsonBuilder().create();
            data = gson.fromJson(json, JSONRootObject.class);
        } catch (Exception e) {
            logger.warn("Unable to parse address lookup response: {}", json, e);
        }
    }

    public Address getAddress() {
        Address address = new Address();
        Result result = data.getResults().get(0);

        String street = "";
        String streetNumber = "";
        String postalCode = "";
        String city = "";

        address.FormattedAddress = result.getFormattedAddress();

        for (AddressComponent component : result.getAddressComponents()) {
            String componentType = component.getTypes().get(0).toString();

            switch (componentType) {
                case "street_number":
                    streetNumber = component.getLongName();
                    break;
                case "route":
                    street = component.getLongName();
                    break;
                case "locality":
                    city = component.getLongName();
                    break;
                case "country":
                    address.Country = component.getLongName();
                    break;
                case "postal_code":
                    postalCode = component.getLongName();
                    break;
            }
        }

        address.Street = street + " " + streetNumber;
        address.City = postalCode + " " + city;

        return address;
    }
}
