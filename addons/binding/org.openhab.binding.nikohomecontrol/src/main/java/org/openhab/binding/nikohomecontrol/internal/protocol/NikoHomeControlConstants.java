/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.nikohomecontrol.internal.protocol;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link NikoHomeControlConstants} class defines common constants used in the Niko Home Control communication.
 *
 * @author Mark Herwege - Initial Contribution
 */
@NonNullByDefault
public class NikoHomeControlConstants {

    // Action types abstracted from NhcI and NhcII action types
    public static enum ActionType {
        TRIGGER,
        RELAY,
        DIMMER,
        ROLLERSHUTTER,
        GENERIC
    };

    // switch and dimmer constants in the Nhc layer
    public static final String NHCON = "On";
    public static final String NHCOFF = "Off";

    public static final String NHCTRIGGERED = "Triggered";

    // rollershutter constants in the Nhc layer
    public static final String NHCDOWN = "Down";
    public static final String NHCUP = "Up";
    public static final String NHCSTOP = "Stop";

    // NhcII thermostat modes
    public static final String[] THERMOSTATMODES = { "Day", "Night", "Eco", "Off", "Cool", "Prog1", "Prog2", "Prog3" };

    // Certificates for local MQTT SSL connection with NhcII CoCo
    public static final String CERTNEWINTERMEDIATE = "-----BEGIN CERTIFICATE-----\r\n"
            + "MIIF7jCCA9agAwIBAgICEA4wDQYJKoZIhvcNAQELBQAwgYExCzAJBgNVBAYTAkJF"
            + "MRgwFgYDVQQIDA9Pb3N0LVZsYWFuZGVyZW4xFTATBgNVBAcMDFNpbnQtTmlrbGFh"
            + "czENMAsGA1UECgwETmlrbzEVMBMGA1UECwwMSG9tZSBDb250cm9sMRswGQYJKoZI"
            + "hvcNAQkBFgxpbmZvQG5pa28uYmUwHhcNNzAwMTAxMDAwMDAwWhcNMzcwMTAxMDAw"
            + "MDAwWjCBiTELMAkGA1UEBhMCQkUxGDAWBgNVBAgMD09vc3QtVmxhYW5kZXJlbjEN"
            + "MAsGA1UECgwETmlrbzEVMBMGA1UECwwMSG9tZSBDb250cm9sMR0wGwYDVQQDDBRO"
            + "aWtvIEludGVybWVkaWF0ZSBDQTEbMBkGCSqGSIb3DQEJARYMaW5mb0BuaWtvLmJl"
            + "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAuSDk7ob45c78+b/SSfMl"
            + "TOY82nJ/RQjNrIFRTdMUwrt18GMz2TDXJnaz+N5bkxC4L2CkPWZE3eOr3l10al+r"
            + "hZ+m55AhZZcoHHN9vFIul8pw86mVAY8uxr3pM72/270L9yJ+Ra8d+qwM6+L8zWUc"
            + "S/RoGokyutkzfuC20tC1u8IOsUgNHuHwh2dWA0OrI+GWZ6k+Mr/Ojsj7YL5xIrOK"
            + "eZHIN0jy6/hSnWDN1GTxIKpiKCOoFUGAj5Wwpf3Z3mpmSIvAG048fczX2ZdcjCcg"
            + "Iaiw5yeK77G5iMYtzPxJwZRKBVfo+Kf0sPn7QSOJwMJZ8KRgO1KAysuCtspUsemg"
            + "mA0I0pzXOwFJI5dIquMj/2vO+JFB+T8XeoPdeaOc9RJA5Wj2ENIjHTu/W86ElJwU"
            + "8Aw3Z6Gc63mto4FGkM7kN7VQyQVX7EbTmuMC5gHDltrYpsnlKz2d0pShBg++x6IY"
            + "Hd321i8HGqg7NyfG6jZpISQSKKzPZKG++9l2/w7eQ8qJYpGZ6zqiUphygKdx9q2s"
            + "sP8AUbKYZzRBK0u4XDwtJtYAaNw5arKGH4qLHn+EEYTruC1fo9SAGqkPoACd0Oze"
            + "3w8tjsHwwzD8NXJzEpnUyjDmtvi1VfUzKlc82CrNW6iePzR0lGzEQtVBI4rfqbfJ"
            + "RvQ9Hq9HaCrX1P6M5s/ZfisCAwEAAaNmMGQwHQYDVR0OBBYEFHoJvtyYZ7/j4nDe"
            + "kGT2q+xKCWE/MB8GA1UdIwQYMBaAFOa0NGf2t36uYioWVapmm073eJBZMBIGA1Ud"
            + "EwEB/wQIMAYBAf8CAQAwDgYDVR0PAQH/BAQDAgGGMA0GCSqGSIb3DQEBCwUAA4IC"
            + "AQBsl6Y9A5qhTMQHL+Z7S0+t1xjRuzYtzgddEGIWK9nsx1RmH6NDv4KoziaUX3Tx"
            + "YMlOFqx6Q+P0Tyl+x+wbPC3stVa8h4hmr5mOd1ph15hecAK0VbcWeRfMAEqg9o47"
            + "6MheSjXwwIp/oRx3YCX3MhiBaWj1IgLElOA99Xtlksk6VsR6QVoSmTKUNDR0T3TF"
            + "AKq6AH+IIa4wsMlXdkK7LQFGnArmYwXuTyVpDoaYbYP9F5sXslfa294oqPp0kfUl"
            + "niyzX0jLYKAL7CqEBzMXVtLPo2Be6X6uagBIz6MV8s1FGmETf++pWKsuvR9EOoh8"
            + "Cm0xozW9WlPm0dBeMyT991QqDkfaMyOtFT6KZwkD3HxAiTBOZ1LI/P00kaPjpJwt"
            + "+8OKGjqQcXBn6p4ZxF6AmZ9fMCWkYyG37HwSeQYJM/zqrbP+Opfl6dgGJ+Qa5P6k"
            + "1f8YzBkE1gG1V9YcAAWOGPMOgqBE0V0uZfPVctp4wcC4WBqti4pYC28+iHdewQzl"
            + "9LB6RwIJmWNrhRLY+fdutV8NgTVb44vtkaQ+ewyc8y01Fk/G0HXarPt3UYgO6oqa"
            + "FpEU/wi2o9qMVgvHmkXdR1yQLSYZs2R/yzE1KDUSOmxa5T+XFfW7KQ07fhwk27Gk"
            + "y7Ob3mU1LT25MO7yLXUjGqNj9k9aa5FLUTyoh1JGGM64Zw==\r\n" + "-----END CERTIFICATE-----\r\n";
    public static final String CERTNEWCA = "-----BEGIN CERTIFICATE-----\r\n"
            + "MIIF6jCCA9KgAwIBAgIJANTA8rXGnhG7MA0GCSqGSIb3DQEBCwUAMIGBMQswCQYD"
            + "VQQGEwJCRTEYMBYGA1UECAwPT29zdC1WbGFhbmRlcmVuMRUwEwYDVQQHDAxTaW50"
            + "LU5pa2xhYXMxDTALBgNVBAoMBE5pa28xFTATBgNVBAsMDEhvbWUgQ29udHJvbDEb"
            + "MBkGCSqGSIb3DQEJARYMaW5mb0BuaWtvLmJlMB4XDTcwMDEwMTAwMDAwNVoXDTM3"
            + "MDEyOTAwMDAwNVowgYExCzAJBgNVBAYTAkJFMRgwFgYDVQQIDA9Pb3N0LVZsYWFu"
            + "ZGVyZW4xFTATBgNVBAcMDFNpbnQtTmlrbGFhczENMAsGA1UECgwETmlrbzEVMBMG"
            + "A1UECwwMSG9tZSBDb250cm9sMRswGQYJKoZIhvcNAQkBFgxpbmZvQG5pa28uYmUw"
            + "ggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCpKNKKHC0fCND19D96G78G"
            + "Zdj+OGLvy/DRJswbLepG8cPedqEZwXjn762fvLdTlcTX/ohkeG4QPb1mxPzjpEgl"
            + "M5aNmp2rmlAFVtLWILQx7mWir5FjG5eTyYi2fbYHnPQpx8XuVk2INENd85818R4j"
            + "RfouYLaZWSd8wc7LP20N0rVtjg5RJ/zAkQ6A7KzdgeOkKhn07wSGBWu9vDw7gCdL"
            + "+Oyeo4LQmABXB7up8nIDCl+o23QL4/aSzdrS5cBCXoPWwto7OiXw0RRcEbpumQyW"
            + "mTGS8jT2FCUNAIWAxC3pKEIXbzf03pLo7EMfFcmjsLDcvcnkB+EJX0fuATwl5CLz"
            + "SneUFY7MNTpv9xgZFX83LhoiFbycZwzWEUr/Q0pmHYZdmezm84+W6EA3E9qH+oR8"
            + "V09bwEMAMSQpbebEB8JmvvwykQHxowkpnV01bmimBEOaquAmyfiW3YSO90vJu+kg"
            + "Zrkihc0AEMFcDbLRCEKvx/u6Hs2xMmVPz0W9mPW37t5zKOV0vcrHmFgMp+9EyDAQ"
            + "vfNofLx790lD1LFp3qvD/H0+IbydQoEc7Q1/tTQDjL45TLNXwwBWQVQLIEQY5sqN"
            + "n8p2ita3MPpSnu5XU93pBcns8jUNlc6/wFIMSBDWK40RiJKzTsr/2jTGVqZX8PXA"
            + "rDnIoa0Eapt0nq87qnkQzQIDAQABo2MwYTAdBgNVHQ4EFgQU5rQ0Z/a3fq5iKhZV"
            + "qmabTvd4kFkwHwYDVR0jBBgwFoAU5rQ0Z/a3fq5iKhZVqmabTvd4kFkwDwYDVR0T"
            + "AQH/BAUwAwEB/zAOBgNVHQ8BAf8EBAMCAYYwDQYJKoZIhvcNAQELBQADggIBAFLw"
            + "6lbxex6hElSrqOZljoFQzQg78dmtUFm4BgKu5EAqn1Ug/OHOKk8LBbNMf2X0Y+4i"
            + "SO4g8yO/C9M/YPxjnM5As1ouTu7UzQL4hEJynyHoPuCBD8nZxuaKeGMX7nQYm7GD"
            + "0iaL0iP9gFwv/A2O/isQUB/sTAclhm1zKAw4f/SaBq8t22wf59e8na0xIfHui0PD"
            + "s8PfRbC4xIOKMxHkHFv+DHeMGjCbR4x20RV/z4JNx1ALEBGo6Oh7Dph/maAQWbje"
            + "x9BCstNR3V1Bhx9rUe7BjIMyJUGEItpZXG+N+qnQr2K7xDdloJl4X0flIa74sdUE"
            + "K4s0X7p+JixLMSxbu5oS6W+d3g6EG0ZgEUwwwc98D1fsm1ziNqwcnYMkI6P2601G"
            + "kEaK/54kYqCxvw6fu5+PNmsDD8ptdazoO3/UOxWvspI1U3drcpnaEHuNclEF7WeL"
            + "yqTfi+8UiL9xJgq9ivjKjZdchkdaD2THgrnzs0XxLbZnwAPeh3cHooUJQkInmKp3"
            + "O05Gv0rnSr29bH8vh/sy4/yJJCUd036pF9C8mPHAYsvNDVGaGYVmNt5P28z3PO16"
            + "YKNJCOJ0x333F6PJaqWAQQP9bGMuJThX8ZQ9Fd8KMXVUfFVKICEkb4erWpL2RIz3"
            + "9JFSC56ZtXv2losfASTyXJwCpyib7FcTZ1rJze+l\r\n" + "-----END CERTIFICATE-----\r\n";
    public static final String CERTINTERMEDIATE = "-----BEGIN CERTIFICATE-----\r\n"
            + "MIIF2jCCA8KgAwIBAgICEAAwDQYJKoZIhvcNAQELBQAwdzELMAkGA1UEBhMCQkUx"
            + "GDAWBgNVBAgMD09vc3QtVmxhYW5kZXJlbjEVMBMGA1UEBwwMU2ludC1OaWtsYWFz"
            + "MQ0wCwYDVQQKDAROaWtvMQwwCgYDVQQLDANSJkQxGjAYBgkqhkiG9w0BCQEWC2pn"
            + "b0BuaWtvLmJlMB4XDTE3MDExNjEwMDA1N1oXDTI3MDExNDEwMDA1N1owgYAxCzAJ"
            + "BgNVBAYTAkJFMRgwFgYDVQQIDA9Pb3N0LVZsYWFuZGVyZW4xDTALBgNVBAoMBE5p"
            + "a28xDDAKBgNVBAsMA1ImRDEeMBwGA1UEAwwVaW50ZXJtZWRpYXRlIEFQSSBjZXJ0"
            + "MRowGAYJKoZIhvcNAQkBFgtqZ29Abmlrby5iZTCCAiIwDQYJKoZIhvcNAQEBBQAD"
            + "ggIPADCCAgoCggIBAPRSUHw3o3F46vfkSD19O3vqW6GRVofDLWjTz9tFIGUlT0wK"
            + "0MkrjSzfuAmUEU8EqS24aSSk8NOyM1aCUPOs8TCq72otxskad4SUmvJo+azVALgB"
            + "7aiK4pZEoAcCyb3xzJDTSeyetx7Nd4udHwzi7Eez9cE41ZGlbgPWl+mr0FwNr+bV"
            + "EZ6tZYqri88xGFVgWxP0Gu/IMRW08g3Zp7MGaQNRv6ygOIc+aeiUwXFWjF8n2Tpu"
            + "h893+uB6HBDCLfW7DonJaoUZWvl84c8HTGg9bFpp76PC8b61jCqu4ehdYTVxltIY"
            + "VyKwUpibd5e9PA+1d0l21cQ1Pu9nl7jCGSJupvuUwYnh190szDZcAdGJ/ufSfTcj"
            + "d+/UOfBAVe7wsQS8yZCM/BZv58n/yPfkv2JRFVuYrsRzJ3v+8uYBw0zmjps245oe"
            + "YI//okITAUF8m669+CRG6w4QhLk4PjjA7nLXfHCEOMMEP1P+tVEoDj4qOuPKRPh2"
            + "6fNvF6c3V9VPwg8gRNn647c+rnY/nXm0RsR5KG6+eVhJJK2XQUdzvBGsjqYOXGEP"
            + "1AJia8T1HRzDxjkZ+XOJLvm7lvU2QALspUiHkSujuLUtiLjq9PVTcQYnPe2drOdh"
            + "umveD20bKgxKpQMrUDEfDSjypI2tYb76M0qJazyEAoamE7a14ZQf6s655p6nAgMB"
            + "AAGjZjBkMB0GA1UdDgQWBBSJysj201o5jl4vz36kZyaoPnFvvjAfBgNVHSMEGDAW"
            + "gBS1GTXKMka186SljwL8tG78B7hqVjASBgNVHRMBAf8ECDAGAQH/AgEAMA4GA1Ud"
            + "DwEB/wQEAwIBhjANBgkqhkiG9w0BAQsFAAOCAgEAmOS2Jag9WFSEfVr1iXux5ZsV"
            + "5DFY7dMrmRdjmdjBMKe51bwN3wrEiWM+dJfJ8VtSAhPxpNmcbLAzlSamPHZ3hXUe"
            + "3X2wY8xFxAwZy7c5zZmh4pnqDFbzuqRZ6L+Hc7S/t7rG1wZB/2tnot+BZaPnEaFA"
            + "jU/N2EYRHjj6zGz1xRTVO4ycmNfRtYrYCe2c9/aDB3f37ekRofDxu4d8rT70qAci"
            + "Ycm1zkRadg9u0KPVQ0XaQAsxZ1eI/EJowlkC18oHdQF/MpfhCP6EcudvskaLFNmA"
            + "pv4L/i+VO3ovRRyt+U3UpEzsA66X5XzFZ58pOikilIVV8odVRRVKvGhUKnK+CU8h"
            + "nnb+RKxsTGP2mKMXiSEUG3peFQupJ18eE6VXlOPbSgtcU2dPUVZUDrRlAaQAZLiI"
            + "gicNnGb6EnB8jqqIXcFfuLOYId8PQcEsLu42Nz+DtAzQvzhsXgb2HQZfunDwsIwW"
            + "Z6HL/AfE/gCPqLlYZqb2l2NZeKjzPPGE1ohX5NIvDl0/tHsNw1Yb23xntx5s29EX"
            + "FUmXnAuS1u5M44VQPNZ11RuzlWxyj/gfDGIKYl2K9YO1etVTVRQHcS4yro3M+9yy"
            + "ysRrjZo7jtZl7iZ2mHLAfEP3FJucHe8diCY/vMxSxvn+89aP4k0x051MNw3xW7Mn" + "422w/GItAjwcLjagfxA=\r\n"
            + "-----END CERTIFICATE-----\r\n";
    public static final String CERTCA = "-----BEGIN CERTIFICATE-----\r\n"
            + "MIIF1DCCA7ygAwIBAgIJAMwcTOyTL+pMMA0GCSqGSIb3DQEBCwUAMHcxCzAJBgNV"
            + "BAYTAkJFMRgwFgYDVQQIDA9Pb3N0LVZsYWFuZGVyZW4xFTATBgNVBAcMDFNpbnQt"
            + "TmlrbGFhczENMAsGA1UECgwETmlrbzEMMAoGA1UECwwDUiZEMRowGAYJKoZIhvcN"
            + "AQkBFgtqZ29Abmlrby5iZTAeFw0xNzAxMTYwOTU0MDJaFw0zNzAxMTEwOTU0MDJa"
            + "MHcxCzAJBgNVBAYTAkJFMRgwFgYDVQQIDA9Pb3N0LVZsYWFuZGVyZW4xFTATBgNV"
            + "BAcMDFNpbnQtTmlrbGFhczENMAsGA1UECgwETmlrbzEMMAoGA1UECwwDUiZEMRow"
            + "GAYJKoZIhvcNAQkBFgtqZ29Abmlrby5iZTCCAiIwDQYJKoZIhvcNAQEBBQADggIP"
            + "ADCCAgoCggIBANu1DzZLGgEQgmW9PD8KR4Seskp3HJbgbgUcN98/wlbCK6Zd/l9L"
            + "sKz16shaJfcw45kTYF4eDcSgORtI8SUc0pgKEKKtvLE8R19BBayPb/FkiGV7yFPa"
            + "Q41eCySOdrWC5vrm2cP7pLroLqXuJefR0e0ajy3HpBoxkOSmUXt5Q8dKIuYQCV3I"
            + "I8pE1Jl8f0Xc0AhQm2g3Gpdsn0+98zwc90meoICRQf1nzl+T1Cs2n6f0YqRkbjzQ"
            + "zXN7TJz5Z0bYM5XvHvBOBr/70afIX6eAUE4bsimP6dHSpdfKnCdc+Wx+KefHG3Or"
            + "w5pmUVfZWIDoQVMuEsUw4s0QAHWNLULnB+ARj6joNCzRttj1Jkybq+qb3x626/d3"
            + "TrcwUk1yMPJPcvQyv73pJga6zfi5vap+PBROB7SvKFvqvHxJi0EMfddGiMhqYwLe"
            + "4pr1vZj2nn60mYNr4mhFfSXArvoPstf0pSX5M3o/B/+qXyJMuX4gcWSeq75BOZ/4"
            + "3bBGtYfdj+9Xx0rflG6hK8c6BDzSoox9iQHCbq6DgGTpNTunoBhQdNvP53XbzMuF"
            + "an7xjaJbjtjaJV88XPOFW7eZmAdjgTC1qxgMLvZl6uZbrTJkcwyT3gsAszV2/Mlu"
            + "tfor9ngrsrVIjGnzmho87EYhPRpZKD7rzoCDiv5pQHJleYIzYskv8wpJAgMBAAGj"
            + "YzBhMB0GA1UdDgQWBBS1GTXKMka186SljwL8tG78B7hqVjAfBgNVHSMEGDAWgBS1"
            + "GTXKMka186SljwL8tG78B7hqVjAPBgNVHRMBAf8EBTADAQH/MA4GA1UdDwEB/wQE"
            + "AwIBhjANBgkqhkiG9w0BAQsFAAOCAgEAgTbEQ4Vd85/2WvAy/5fD4u2u6xtFaKR/"
            + "OOkZ/nlUAuqheDCEXVOlkRNUzqqXL2nn95yiUtYKK9xXEAiClq9TtvLT5WM4XagH"
            + "zoWcXOGzZFwxOZkFO1bla8x3GtsSDfiPIMw7eJIIWEewXNWs01InbCvzQaNyYBTL"
            + "tdTABcGKIlPivUwfBfhYXRwN5/ZRN0Gbca6jLoF9xZog9+1rTnbWh8cASWJwodc+"
            + "nvmmTO34zAHbXXBcBeJwWjFKtA6L08C3lJ2dON/u9eSL8CesICZnaSRDPR0qBIhU"
            + "uAhHER/Ps8a9POYzrev7P37v/e7nqGbFzZZFAZM+yM4kAcntzj9yFX/sdx+YFTqI"
            + "ZYQsLFOv1RISYGJK32mA851/oLGHCR0/5x3Mc2eZktH7lmJ0N8NmQ7k2gLoACbXF"
            + "mUwfEVm8RATKgi9afJRCYbeyi1zzFAwpib3IddRQZf4NguTn0vrpxVlSeF+AHr1A"
            + "Xn0gwutwNB07mi2vRl7teC69AFdv3ZyLsLt7bXnCvO0vNMRBWGFv9iUh48piAGjA"
            + "8QHuxo55OOtopg5WNR3Moz+ebsq33B6mCws5ov8V8X8slU6+4xoerID4js/Kre1d"
            + "PW0VwFd8ok2ocQ/g8wpFEtyWlzdQVWJe8FUyR97kjlKsy+H4AO6twuhi3GYLZoqr" + "K1GCaaZcI1Q=\r\n"
            + "-----END CERTIFICATE-----\r\n";
}
