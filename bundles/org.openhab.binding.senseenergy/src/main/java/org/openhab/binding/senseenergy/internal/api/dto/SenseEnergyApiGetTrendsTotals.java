/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
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
package org.openhab.binding.senseenergy.internal.api.dto;

import com.google.gson.annotations.SerializedName;

/**
 * {@link SenseEnergyApiGetTrendsTotals }
 *
 * @author Jeff James - Initial contribution
 */
public class SenseEnergyApiGetTrendsTotals {
    @SerializedName("total")
    public float totalPower;
    @SerializedName("totals")
    public float[] totalsPower;
    public SenseEnergyApiGetTrendsDevice[] devices;
    @SerializedName("total_cost")
    public float totalCost;
    @SerializedName("total_costs")
    public float[] totalCosts;
}

/* @formatter:off
"consumption":{
"total":18.861498,
"totals":[
   1.179842,
   0.83928496,
   0.860291,
   1.140934,
   0.544447,
   0.562177,
   0.804201,
   0.544805,
   0.558279,
   0.846418,
   2.193837,
   1.217603,
   1.260953,
   1.5024201,
   1.303495,
   0.503124,
   0.832463,
   0.52909696,
   0.53581405,
   1.102013,
   0.0,
   0.0,
   0.0,
   0.0
],
"devices":[
   {
      "id":"LpzF0vGG",
      "name":"Wine Cooler",
      "icon":"ac",
      "tags":{
         "Alertable":"true",
         "AlwaysOn":"false",
         "DateCreated":"2024-04-11T09:28:45.000Z",
         "DateFirstUsage":"2024-04-01",
         "DefaultUserDeviceType":"AC",
         "DeployToMonitor":"true",
         "DeviceListAllowed":"true",
         "MergedDevices":"db3fc2f4,e329c895",
         "ModelCreatedVersion":"12",
         "ModelUpdatedVersion":"28",
         "name_useredit":"true",
         "NameUserGuess":"false",
         "OriginalName":"AC",
         "PeerNames":[

         ],
         "Pending":"false",
         "Revoked":"false",
         "TimelineAllowed":"true",
         "TimelineDefault":"true",
         "Type":"WindowAC",
         "UserDeletable":"true",
         "UserDeviceType":"AC",
         "UserDeviceTypeDisplayString":"AC",
         "UserEditable":"true",
         "UserEditableMeta":"true",
         "UserMergeable":"true",
         "UserShowBubble":"true",
         "UserShowInDeviceList":"true",
         "Virtual":"true"
      },
      "history":[
         0.37245202,
         0.01909,
         0.020947,
         0.368776,
         0.019066999,
         0.025436,
         0.28254902,
         0.019898001,
         0.019055,
         0.170864,
         0.14370401,
         0.019107,
         0.059858,
         0.296629,
         0.019107,
         0.019064,
         0.345176,
         0.018984,
         0.040691003,
         0.364613,
         0.0,
         0.0,
         0.0,
         0.0
      ],
      "avgw":0.110211134,
      "total_kwh":2.6450672,
      "total_cost":53,
      "pct":14.0,
      "cost_history":[
         8,
         0,
         0,
         8,
         0,
         1,
         6,
         0,
         0,
         4,
         3,
         0,
         1,
         6,
         0,
         0,
         7,
         0,
         1,
         8,
         0,
         0,
         0,
         0
      ]
   },
   {
      "id":"wIpu0Jjw",
      "name":"Entertainment Center",
      "icon":"plug",
      "tags":{
         "Alertable":"true",
         "ControlCapabilities":[
            "OnOff",
            "StandbyThreshold"
         ],
         "DateCreated":"2024-08-22T19:23:30.000Z",
         "DefaultUserDeviceType":"SmartPlug",
         "DeviceListAllowed":"true",
         "DUID":"D8:44:89:30:93:F6",
         "IntegratedDeviceType":"IntegratedSmartPlug",
         "IntegrationType":"TPLink",
         "name_useredit":"false",
         "OriginalName":"Entertainment Center",
         "Revoked":"false",
         "SmartPlugModel":"TP-Link Kasa KP115",
         "SSIEnabled":"true",
         "SSIModel":"SelfReporting",
         "TimelineAllowed":"true",
         "TimelineDefault":"true",
         "UserControlLock":"true",
         "UserDeletable":"false",
         "UserDeviceTypeDisplayString":"Smart Plug",
         "UserEditable":"true",
         "UserEditableMeta":"true",
         "UserMergeable":"false",
         "UserShowInDeviceList":"true",
         "UserVisibleDeviceId":"D8:44:89:30:93:F6"
      },
      "history":[
         0.010637,
         0.010685001,
         0.01068,
         0.010636,
         0.010737,
         0.010778,
         0.010765,
         0.010706999,
         0.04493,
         0.151627,
         0.081801005,
         0.14708,
         0.14865199,
         0.148901,
         0.099185996,
         0.010794,
         0.010876001,
         0.010772,
         0.010753999,
         0.010878,
         0.0,
         0.0,
         0.0,
         0.0
      ],
      "avgw":0.040078163,
      "total_kwh":0.96187586,
      "total_cost":17,
      "pct":5.1,
      "cost_history":[
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         1,
         3,
         2,
         3,
         3,
         3,
         2,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0
      ]
   },
   {
      "id":"always_on",
      "name":"Always On",
      "icon":"alwayson",
      "tags":{
         "DefaultUserDeviceType":"AlwaysOn",
         "DeviceListAllowed":"true",
         "TimelineAllowed":"false",
         "UserDeleted":"false",
         "UserDeviceTypeDisplayString":"Always On",
         "UserEditable":"false",
         "UserMergeable":"false"
      },
      "history":[
         0.402,
         0.403366,
         0.405,
         0.4053,
         0.425181,
         0.443,
         0.444666,
         0.440596,
         0.40559798,
         0.404,
         0.404,
         0.404,
         0.404,
         0.404,
         0.4007,
         0.385716,
         0.388228,
         0.385812,
         0.38265,
         0.382,
         0.0,
         0.0,
         0.0,
         0.0
      ],
      "avgw":0.3383255,
      "total_kwh":8.119812,
      "total_cost":164,
      "pct":43.0,
      "cost_history":[
         8,
         8,
         8,
         8,
         9,
         9,
         9,
         9,
         8,
         8,
         8,
         8,
         8,
         8,
         8,
         8,
         8,
         8,
         8,
         8,
         0,
         0,
         0,
         0
      ]
   },
   {
      "id":"pZCoCHcc",
      "name":"Garage Freezer",
      "icon":"plug",
      "tags":{
         "Alertable":"true",
         "ControlCapabilities":[
            "OnOff",
            "StandbyThreshold"
         ],
         "DateCreated":"2024-08-22T19:23:30.000Z",
         "DefaultUserDeviceType":"SmartPlug",
         "DeviceListAllowed":"true",
         "DUID":"D8:44:89:30:81:D9",
         "IntegratedDeviceType":"IntegratedSmartPlug",
         "IntegrationType":"TPLink",
         "name_useredit":"false",
         "OriginalName":"Garage Freezer",
         "Revoked":"false",
         "SmartPlugModel":"TP-Link Kasa KP115",
         "SSIEnabled":"true",
         "SSIModel":"SelfReporting",
         "TimelineAllowed":"true",
         "TimelineDefault":"true",
         "UserControlLock":"true",
         "UserDeletable":"false",
         "UserDeviceTypeDisplayString":"Smart Plug",
         "UserEditable":"true",
         "UserEditableMeta":"true",
         "UserMergeable":"false",
         "UserShowInDeviceList":"true",
         "UserVisibleDeviceId":"D8:44:89:30:81:D9"
      },
      "history":[
         0.040856,
         0.032984,
         0.040568,
         0.032936,
         0.032405,
         0.038964,
         0.038643003,
         0.032064,
         0.038653,
         0.038432,
         0.031378,
         0.038960997,
         0.039219003,
         0.03901,
         0.039935,
         0.042015,
         0.041991003,
         0.047634996,
         0.036985002,
         0.047472,
         0.0,
         0.0,
         0.0,
         0.0
      ],
      "avgw":0.032129414,
      "total_kwh":0.77110595,
      "total_cost":20,
      "pct":4.1,
      "cost_history":[
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         1,
         0,
         0,
         0,
         0
      ]
   },
   {
      "id":"a6b4aa4d",
      "name":"Backyard Spotlight",
      "icon":"lightbulb",
      "tags":{
         "Alertable":"true",
         "AlwaysOn":"false",
         "DateCreated":"2024-06-07T12:10:01.000Z",
         "DateFirstUsage":"2024-04-06",
         "DefaultUserDeviceType":"Light",
         "DeployToMonitor":"true",
         "DeviceListAllowed":"true",
         "ModelCreatedVersion":"24",
         "ModelUpdatedVersion":"29",
         "name_useredit":"true",
         "OriginalName":"Light 1",
         "PeerNames":[
            {
               "Name":"Light",
               "UserDeviceType":"Light",
               "Percent":91.0,
               "Icon":"lightbulb",
               "UserDeviceTypeDisplayString":"Light"
            },
            {
               "Name":"Appliance Light",
               "UserDeviceType":"ApplianceLight",
               "Percent":3.0,
               "Icon":"lightbulb",
               "UserDeviceTypeDisplayString":"Appliance Light"
            },
            {
               "Name":"TV/Monitor",
               "UserDeviceType":"TV",
               "Percent":3.0,
               "Icon":"tv",
               "UserDeviceTypeDisplayString":"TV/Monitor"
            },
            {
               "Name":"Microwave",
               "UserDeviceType":"Microwave",
               "Percent":2.0,
               "Icon":"microwave",
               "UserDeviceTypeDisplayString":"Microwave"
            }
         ],
         "Pending":"false",
         "Revoked":"false",
         "TimelineAllowed":"true",
         "TimelineDefault":"true",
         "Type":"Lighting",
         "UserDeletable":"true",
         "UserDeviceTypeDisplayString":"Light",
         "UserEditable":"true",
         "UserEditableMeta":"true",
         "UserMergeable":"true",
         "UserShowInDeviceList":"true"
      },
      "history":[
         0.0,
         0.0,
         0.015547,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0
      ],
      "avgw":6.4779166E-4,
      "total_kwh":0.015547001,
      "total_cost":0,
      "pct":0.1,
      "cost_history":[
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0
      ]
   },
   {
      "id":"Pgz8OdRP",
      "name":"Oven",
      "icon":"stove",
      "tags":{
         "Alertable":"true",
         "AlwaysOn":"false",
         "DateCreated":"2024-05-09T04:16:08.000Z",
         "DateFirstUsage":"2024-04-02",
         "DefaultUserDeviceType":"MysteryHeat",
         "DeployToMonitor":"true",
         "DeviceListAllowed":"true",
         "MergedDevices":"0988f51e,fc5b120b",
         "ModelCreatedVersion":"21",
         "name_useredit":"true",
         "OriginalName":"Heat 3",
         "PeerNames":[

         ],
         "Pending":"false",
         "Revoked":"false",
         "TimelineAllowed":"true",
         "TimelineDefault":"true",
         "Type":"UnknownHeat",
         "UserDeletable":"true",
         "UserDeviceType":"Oven",
         "UserDeviceTypeDisplayString":"Oven",
         "UserEditable":"true",
         "UserEditableMeta":"true",
         "UserMergeable":"true",
         "UserShowInDeviceList":"true",
         "Virtual":"true"
      },
      "make":"Decor",
      "history":[
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0017309999,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0,
         0.0
      ],
      "avgw":7.2124996E-5,
      "total_kwh":0.0017309999,
      "total_cost":0,
      "pct":0.0,
      "cost_history":[
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         0
      ],
      "given_make":"Decor"
   },
   {
      "id":"unknown",
      "name":"Unknown",
      "icon":"home",
      "monitor_id":869850,
      "tags":{

      },
      "history":[
         0.35389698,
         0.37315995,
         0.367549,
         0.32328606,
         0.057057012,
         0.04399902,
         0.027577966,
         0.04153996,
         0.050042972,
         0.08149502,
         1.5312228,
         0.6084549,
         0.60922396,
         0.61388,
         0.74456716,
         0.045534983,
         0.046192013,
         0.06589394,
         0.06473405,
         0.29704997,
         0.0,
         0.0,
         0.0,
         0.0
      ],
      "avgw":0.2644316,
      "total_kwh":6.3463583,
      "total_cost":133,
      "pct":33.6,
      "cost_history":[
         7,
         8,
         8,
         7,
         1,
         1,
         1,
         1,
         1,
         2,
         32,
         13,
         13,
         13,
         15,
         1,
         1,
         1,
         1,
         6,
         0,
         0,
         0,
         0
      ]
   }
],
"total_cost":392,
"total_costs":[
   25,
   17,
   18,
   24,
   11,
   12,
   17,
   11,
   12,
   18,
   46,
   25,
   26,
   31,
   27,
   10,
   17,
   11,
   11,
   23,
   0,
   0,
   0,
   0
]
},
"production":{
"total":42.46737,
"totals":[
   -0.007275,
   -0.005593,
   -0.003241,
   -0.003823,
   -0.003187,
   -0.003171,
   0.016122999,
   0.334525,
   1.1068189,
   4.277293,
   5.843486,
   6.346029,
   6.397974,
   6.078468,
   5.257767,
   3.728918,
   2.0833168,
   0.840636,
   0.191979,
   -0.009675,
   0.0,
   0.0,
   0.0,
   0.0
],
"devices":[
   {
      "id":"solar",
      "name":"Solar",
      "icon":"solar_alt",
      "tags":{
         "DefaultUserDeviceType":"Solar",
         "DeviceListAllowed":"false",
         "TimelineAllowed":"false",
         "UserDeleted":"false",
         "UserDeviceTypeDisplayString":"Solar",
         "UserEditable":"false",
         "UserMergeable":"false"
      },
      "history":[
         -0.007275,
         -0.005593,
         -0.003241,
         -0.003823,
         -0.003187,
         -0.003171,
         0.016122999,
         0.334525,
         1.1068189,
         4.277293,
         5.843486,
         6.346029,
         6.397974,
         6.078468,
         5.257767,
         3.728918,
         2.0833168,
         0.840636,
         0.191979,
         -0.009675,
         0.0,
         0.0,
         0.0,
         0.0
      ],
      "avgw":1.7694739,
      "total_cost":881,
      "pct":100.0,
      "cost_history":[
         0,
         0,
         0,
         0,
         0,
         0,
         0,
         7,
         23,
         89,
         121,
         132,
         133,
         126,
         109,
         77,
         43,
         17,
         4,
         0,
         0,
         0,
         0,
         0
      ]
   }
],
"total_cost":881,
"total_costs":[
   0,
   0,
   0,
   0,
   0,
   0,
   0,
   7,
   23,
   89,
   121,
   132,
   133,
   126,
   109,
   77,
   43,
   17,
   4,
   0,
   0,
   0,
   0,
   0
]
}
 * @formatter:on
 */
