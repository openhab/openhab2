/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.km200.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;

/**
 * The KM200Comm class does the communication to the device and does any encryption/decryption/converting jobs
 *
 * @author Markus Eckhardt
 * @param <KM200BindingProvider>
 *
 */

public class KM200Comm<KM200BindingProvider> {

    private static final Logger logger = LoggerFactory.getLogger(KM200Comm.class);
    private HttpClient client = null;
    private KM200Device device = null;

    public KM200Comm(KM200Device device) {
        this.device = device;
    }

    /**
     * This function removes zero padding from a byte array.
     *
     */
    public static byte[] removeZeroPadding(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }

    /**
     * This function adds zero padding to a byte array.
     *
     */
    public static byte[] addZeroPadding(byte[] bdata, int bSize, String cSet) throws UnsupportedEncodingException {
        int encrypt_padchar = bSize - (bdata.length % bSize);
        byte[] padchars = new String(new char[encrypt_padchar]).getBytes(cSet);
        byte[] padded_data = new byte[bdata.length + padchars.length];
        System.arraycopy(bdata, 0, padded_data, 0, bdata.length);
        System.arraycopy(padchars, 0, padded_data, bdata.length, padchars.length);
        return padded_data;
    }

    /**
     * This function converts a hex string to a byte array
     *
     */
    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    /**
     * This function converts a byte array to a hex string
     *
     */
    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }

        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++) {
            if ((data[i] & 0xFF) < 16) {
                str = str + "0" + Integer.toHexString(data[i] & 0xFF);
            } else {
                str = str + Integer.toHexString(data[i] & 0xFF);
            }
        }
        return str;
    }

    /**
     * This function does the GET http communication to the device
     *
     */
    public byte[] getDataFromService(String service) {
        byte[] responseBodyB64 = null;
        int maxNbrGets = 3;
        int statusCode = 0;
        // Create an instance of HttpClient.
        if (client == null) {
            client = new HttpClient();
        }
        synchronized (client) {

            // Create a method instance.
            GetMethod method = new GetMethod("http://" + device.getIP4Address() + service);

            // Provide custom retry handler is necessary
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            // Set the right header
            method.setRequestHeader("Accept", "application/json");
            method.addRequestHeader("User-Agent", "TeleHeater/2.2.3");

            try {
                for (int i = 0; i < maxNbrGets && statusCode != HttpStatus.SC_OK; i++) {
                    // Execute the method.
                    statusCode = client.executeMethod(method);
                    // Check the status
                    switch (statusCode) {
                        case HttpStatus.SC_OK:
                            break;
                        case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                            /* Unknown problem with the device, wait and try again */
                            logger.warn("HTTP GET failed: 500, internal server error, repeating.. ");
                            Thread.sleep(2000L);
                            continue;
                        case HttpStatus.SC_FORBIDDEN:
                            /* Service is available but not readable */
                            byte[] test = new byte[1];
                            return test;
                        case HttpStatus.SC_NOT_FOUND:
                            /* Should only happen on discovery */
                            return null;
                        default:
                            logger.error("HTTP GET failed: {}", method.getStatusLine());
                            return null;
                    }
                }
                device.setCharSet(method.getResponseCharSet());
                // Read the response body.
                responseBodyB64 = ByteStreams.toByteArray(method.getResponseBodyAsStream());

            } catch (HttpException e) {
                logger.error("Fatal protocol violation: {}", e.getMessage());
            } catch (InterruptedException e) {
                logger.error("Sleep was interrupted: {}", e.getMessage());
            } catch (IOException e) {
                logger.error("Fatal transport error: {}", e.getMessage());
            } finally {
                // Release the connection.
                method.releaseConnection();
            }
            return responseBodyB64;
        }
    }

    /**
     * This function does the SEND http communication to the device
     *
     */
    public Integer sendDataToService(String service, byte[] data) {
        // Create an instance of HttpClient.
        Integer rCode = null;
        if (client == null) {
            client = new HttpClient();
        }
        synchronized (client) {

            // Create a method instance.
            PostMethod method = new PostMethod("http://" + device.getIP4Address() + service);

            // Provide custom retry handler is necessary
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));
            // Set the right header
            method.setRequestHeader("Accept", "application/json");
            method.addRequestHeader("User-Agent", "TeleHeater/2.2.3");
            method.setRequestEntity(new ByteArrayRequestEntity(data));

            try {
                rCode = client.executeMethod(method);

            } catch (Exception e) {
                logger.error("Failed to send data {}", e);

            } finally {
                // Release the connection.
                method.releaseConnection();
            }
            return rCode;
        }
    }

    /**
     * This function does the decoding for a new message from the device
     *
     */
    public String decodeMessage(byte[] encoded) {
        String retString = null;
        byte[] decodedB64 = null;

        try {
            decodedB64 = Base64.decodeBase64(encoded);
        } catch (Exception e) {
            logger.error("Message is not in valid Base64 scheme: {}", e.getMessage());
            return null;
        }
        try {
            /* Check whether the length of the decryptData is NOT multiplies of 16 */
            if ((decodedB64.length & 0xF) != 0) {
                /* Return the data */
                retString = new String(decodedB64, device.getCharSet());
                return retString;
            }
            // --- create cipher
            final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(device.getCryptKeyPriv(), "AES"));
            final byte[] decryptedData = cipher.doFinal(decodedB64);
            byte[] decryptedDataWOZP = removeZeroPadding(decryptedData);
            retString = new String(decryptedDataWOZP, device.getCharSet());
            return retString;
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            // failure to authenticate
            logger.error("Exception on encoding: {}", e);
            return null;
        }
    }

    /**
     * This function does the encoding for a new message to the device
     *
     */
    public byte[] encodeMessage(String data) {
        byte[] encryptedDataB64 = null;

        try {
            // --- create cipher
            byte[] bdata = data.getBytes(device.getCharSet());
            final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(device.getCryptKeyPriv(), "AES"));
            logger.debug("Create padding..");
            int bsize = cipher.getBlockSize();
            logger.debug("Add Padding and Encrypt AES..");
            final byte[] encryptedData = cipher.doFinal(addZeroPadding(bdata, bsize, device.getCharSet()));
            logger.debug("Encrypt B64..");
            try {
                encryptedDataB64 = Base64.encodeBase64(encryptedData);
            } catch (Exception e) {
                logger.error("Base64encoding not possible: {}", e.getMessage());
            }
            return encryptedDataB64;
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            // failure to authenticate
            logger.error("Exception on encoding: {}", e);
            return null;
        }
    }

    /**
     * This function checks the capabilities of a service on the device
     *
     */
    public void initObjects(String service, KM200CommObject parent) {
        String id = null, type = null, decodedData = null;
        Integer writeable = 0;
        Integer recordable = 0;
        Integer readable = 1;
        JSONObject nodeRoot = null;
        KM200CommObject newObject = null;
        logger.debug("Init: {}", service);
        if (device.blacklistMap.contains(service)) {
            logger.debug("Service on blacklist: {}", service);
            return;
        }
        byte[] recData = getDataFromService(service.toString());
        try {
            if (recData == null) {
                logger.error("Communication is not possible!");
                return;
            }
            if (recData.length == 0) {
                logger.error("No reply from KM200!");
                return;
            }
            /* Look whether the communication was forbidden */
            if (recData.length == 1) {
                readable = 0;
                id = service;
                type = "$$PROTECTED$$";
            }
            if (readable == 1) {
                decodedData = decodeMessage(recData);

                if (decodedData == null) {
                    logger.error("Decoding of the KM200 message is not possible!");
                    return;
                }
                if (decodedData.length() > 0) {
                    nodeRoot = new JSONObject(decodedData);
                    type = nodeRoot.getString("type");
                    id = nodeRoot.getString("id");
                } else {
                    logger.warn("Get empty reply");
                    return;
                }

                /* Check the service features and set the flags */
                if (nodeRoot.has("writeable")) {
                    Integer val = nodeRoot.getInt("writeable");
                    logger.debug(val.toString());
                    writeable = val;
                }
                if (nodeRoot.has("recordable")) {
                    Integer val = nodeRoot.getInt("recordable");
                    logger.debug(val.toString());
                    recordable = val;
                }
                logger.debug("Typ: {}", type);
            }

            newObject = new KM200CommObject(id, type, readable, writeable, recordable, parent);
            newObject.setJSONData(decodedData);

            Object valObject = null;
            switch (type) {
                case "stringValue": /* Check whether the type is a single value containing a string value */
                    logger.debug("initDevice: type string value: {}", decodedData);
                    valObject = new String(nodeRoot.getString("value"));
                    newObject.setValue(valObject);
                    if (nodeRoot.has("allowedValues")) {
                        List<String> valParas = new ArrayList<String>();
                        JSONArray paras = nodeRoot.getJSONArray("allowedValues");
                        for (int i = 0; i < paras.length(); i++) {
                            String subJSON = (String) paras.get(i);
                            valParas.add(subJSON);
                        }
                        newObject.setValueParameter(valParas);
                    }
                    break;

                case "floatValue": /* Check whether the type is a single value containing a float value */
                    logger.debug("initDevice: type float value: {}", decodedData);
                    valObject = nodeRoot.getBigDecimal("value");
                    newObject.setValue(valObject);
                    if (nodeRoot.has("minValue") && nodeRoot.has("maxValue")) {
                        List<BigDecimal> valParas = new ArrayList<BigDecimal>();
                        valParas.add(nodeRoot.getBigDecimal("minValue"));
                        valParas.add(nodeRoot.getBigDecimal("maxValue"));
                        newObject.setValueParameter(valParas);
                    }
                    break;

                case "switchProgram": /* Check whether the type is a switchProgram */
                    logger.debug("initDevice: type switchProgram {}", decodedData);
                    KM200SwitchProgramService sPService = new KM200SwitchProgramService();
                    sPService.setMaxNbOfSwitchPoints(nodeRoot.getInt("maxNbOfSwitchPoints"));
                    sPService.setMaxNbOfSwitchPointsPerDay(nodeRoot.getInt("maxNbOfSwitchPointsPerDay"));
                    sPService.setSwitchPointTimeRaster(nodeRoot.getInt("switchPointTimeRaster"));
                    JSONObject propObject = nodeRoot.getJSONObject("setpointProperty");
                    sPService.setSetpointProperty(propObject.getString("id"));
                    newObject.setValueParameter(sPService);
                    newObject.setJSONData(decodedData);
                    device.virtualList.add(newObject);
                    break;

                case "errorList": /* Check whether the type is a errorList */
                    logger.debug("initDevice: type errorList: {}", decodedData);
                    KM200ErrorService eService = new KM200ErrorService();
                    eService.updateErrors(nodeRoot);
                    newObject.setValueParameter(eService);
                    newObject.setJSONData(decodedData);
                    device.virtualList.add(newObject);
                    break;

                case "refEnum": /* Check whether the type is a refEnum */
                    logger.debug("initDevice: type refEnum: {}", decodedData);
                    JSONArray refers = nodeRoot.getJSONArray("references");
                    for (int i = 0; i < refers.length(); i++) {
                        JSONObject subJSON = refers.getJSONObject(i);
                        id = subJSON.getString("id");
                        initObjects(id, newObject);
                    }
                    break;

                case "moduleList": /* Check whether the type is a moduleList */
                    logger.debug("initDevice: type moduleList: {}", decodedData);
                    JSONArray vals = nodeRoot.getJSONArray("values");
                    for (int i = 0; i < vals.length(); i++) {
                        JSONObject subJSON = vals.getJSONObject(i);
                        id = subJSON.getString("id");

                        initObjects(id, newObject);
                    }
                    break;

                case "yRecording": /* Check whether the type is a yRecording */
                    logger.debug("initDevice: type yRecording: {}", decodedData);
                    /* have to be completed */
                    break;

                case "systeminfo": /* Check whether the type is a systeminfo */
                    logger.debug("initDevice: type systeminfo: {}", decodedData);
                    JSONArray sInfo = nodeRoot.getJSONArray("values");
                    newObject.setValue(sInfo);
                    /* have to be completed */
                    break;
                case "arrayData":
                    logger.debug("initDevice: type arrayData: {}", decodedData);
                    newObject.setJSONData(decodedData);
                    /* have to be completed */
                    break;

                case "$$PROTECTED$$":
                    logger.debug("initDevice: readonly");
                    newObject.setJSONData(decodedData);
                    break;

                default: /* Unknown type */
                    logger.info("initDevice: type unknown for service: {} Data: {}", service, decodedData);
            }
        } catch (

        JSONException e) {
            logger.error("Parsingexception in JSON: {} data: {}", e.getMessage(), decodedData);
        }
        String[] servicePath = service.split("/");
        if (parent == null) {
            device.serviceTreeMap.put(servicePath[servicePath.length - 1], newObject);
        } else {
            parent.serviceTreeMap.put(servicePath[servicePath.length - 1], newObject);
        }
    }

    /**
     * This function creates the virtual services
     *
     */
    public void initVirtualObjects() {
        KM200CommObject newObject = null;
        for (KM200CommObject object : device.virtualList) {
            logger.debug(object.getFullServiceName());
            String id = object.getFullServiceName();
            String type = object.getServiceType();
            switch (type) {
                case "switchProgram":
                    KM200SwitchProgramService sPService = ((KM200SwitchProgramService) object.getValueParameter());
                    sPService.determineSwitchNames(device);
                    JSONObject nodeRoot = new JSONObject(object.getJSONData());
                    sPService.updateSwitches(nodeRoot);
                    newObject = new KM200CommObject(id + "/weekday", type, 1, 0, 1, id, object);
                    object.serviceTreeMap.put("weekday", newObject);
                    newObject = new KM200CommObject(id + "/nbrCycles", type, 0, 0, 1, id, object);
                    object.serviceTreeMap.put("nbrCycles", newObject);
                    newObject = new KM200CommObject(id + "/cycle", type, 1, 0, 1, id, object);
                    object.serviceTreeMap.put("cycle", newObject);
                    logger.debug("On: {}  Of: {}", id + "/" + sPService.getPositiveSwitch(),
                            id + "/" + sPService.getNegativeSwitch());
                    newObject = new KM200CommObject(id + "/" + sPService.getPositiveSwitch(), type,
                            object.getWriteable(), object.getRecordable(), 1, id, object);
                    object.serviceTreeMap.put(sPService.getPositiveSwitch(), newObject);
                    newObject = new KM200CommObject(id + "/" + sPService.getNegativeSwitch(), type,
                            object.getWriteable(), object.getRecordable(), 1, id, object);
                    object.serviceTreeMap.put(sPService.getNegativeSwitch(), newObject);

                    break;
                case "errorList":
                    newObject = new KM200CommObject(id + "/nbrErrors", type, 0, 0, 1, id, object);
                    object.serviceTreeMap.put("nbrErrors", newObject);
                    newObject = new KM200CommObject(id + "/error", type, 1, 0, 1, id, object);
                    object.serviceTreeMap.put("error", newObject);
                    newObject = new KM200CommObject(id + "/errorString", type, 0, 0, 1, id, object);
                    object.serviceTreeMap.put("errorString", newObject);
                    break;
            }
        }
    }

    /**
     * This function checks the state of a service on the device
     *
     */
    public State getProvidersState(String service, String itemType) {
        synchronized (device) {
            String decodedData = null;
            String type = null;
            byte[] recData = null;
            KM200CommObject object = null;

            logger.debug("Check state of: {} type: {} item: {}", service, type, itemType);
            if (device.blacklistMap.contains(service)) {
                logger.debug("Service on blacklist: {}", service);
                return null;
            }
            if (device.containsService(service)) {
                object = device.getServiceObject(service);
                if (object.getReadable() == 0) {
                    logger.warn("Service is listed as protected (reading is not possible): {}", service);
                    return null;
                }
                type = object.getServiceType();
            } else {
                logger.warn("Service is not in the determined device service list: {}", service);
                return null;
            }
            /* For using of virtual services only one receive on the parent service is needed */
            if (!object.getUpdated()
                    || (object.getVirtual() == 1 && !device.getServiceObject(object.getParent()).getUpdated())) {

                if (object.getVirtual() == 1) {
                    /* If it's a virtual service then receive the data from parent service */
                    recData = getDataFromService(object.getParent());
                } else {
                    recData = getDataFromService(service);
                }

                if (recData == null) {
                    logger.error("Communication is not possible!");
                    return null;
                }
                if (recData.length == 0) {
                    logger.error("No reply from KM200!");
                    return null;
                }
                /* Look whether the communication was forbidden */
                if (recData.length == 1) {
                    logger.error("Service is listed as readable but communication is forbidden: {}", service);
                    return null;
                }
                decodedData = decodeMessage(recData);
                logger.debug("Check state of data: {}", decodedData);
                if (decodedData == null) {
                    logger.error("Decoding of the KM200 message is not possible!");
                    return null;
                }
                if (object.getVirtual() == 1) {
                    device.getServiceObject(object.getParent()).setJSONData(decodedData);
                    device.getServiceObject(object.getParent()).setUpdated(true);
                } else {
                    object.setJSONData(decodedData);
                }
                object.setUpdated(true);

            } else {
                /* If already updated then use the saved data */
                if (object.getVirtual() == 1) {
                    decodedData = device.getServiceObject(object.getParent()).getJSONData();
                } else {
                    decodedData = object.getJSONData();
                }
            }
            /* Data is received, now parsing it */
            return parseJSONData(decodedData, type, service, itemType);
        }
    }

    /**
     * This function parses the receviced JSON Data and return the right state
     *
     */
    public State parseJSONData(String decodedData, String type, String service, String itemType) {
        JSONObject nodeRoot = null;
        State state = null;
        KM200CommObject object = device.getServiceObject(service);
        logger.debug("parseJSONData service: {}, data: {}", service, decodedData);
        /* Now parsing of the JSON String depending on its type and the type of binding item */
        try {
            if (decodedData.length() > 0) {
                nodeRoot = new JSONObject(decodedData);
            } else {
                logger.warn("Get empty reply");
                return null;
            }

            switch (type) {
                case "stringValue": /* Check whether the type is a single value containing a string value */
                    logger.debug("initDevice: type string value: {}", decodedData);
                    String sVal = nodeRoot.getString("value");
                    object.setValue(sVal);
                    /* NumberItem Binding */
                    if (itemType.equals("Number")) {
                        try {
                            state = new DecimalType(Float.parseFloat(sVal));
                        } catch (NumberFormatException e) {
                            logger.error(
                                    "Conversion of the string value to Decimal wasn't possible, data: {} error: {}",
                                    decodedData, e);
                            return null;
                        }
                        /* DateTimeItem Binding */
                    } else if (itemType.equals("DateTime")) {
                        try {
                            state = new DateTimeType(sVal);
                        } catch (IllegalArgumentException e) {
                            logger.error(
                                    "Conversion of the string value to DateTime wasn't possible, data: {} error: {}",
                                    decodedData, e);
                            return null;
                        }

                        /* StringItem Binding */
                    } else if (itemType.equals("String")) {
                        state = new StringType(sVal);

                    } else {
                        logger.warn("Bindingtype not supported for string values: {}", itemType.getClass());
                        return null;
                    }
                    return state;

                case "floatValue": /* Check whether the type is a single value containing a float value */
                    logger.debug("state of type float value: {}", decodedData);
                    BigDecimal bdVal = nodeRoot.getBigDecimal("value");
                    object.setValue(bdVal);
                    /* NumberItem Binding */
                    if (itemType.equals("Number")) {
                        state = new DecimalType(bdVal.floatValue());

                        /* StringItem Binding */
                    } else if (itemType.equals("String")) {
                        state = new StringType(bdVal.toString());
                    } else {
                        logger.warn("Bindingtype not supported for float values: {}", itemType.getClass());
                        return null;
                    }
                    return state;

                case "switchProgram": /* Check whether the type is a switchProgram */
                    KM200SwitchProgramService sPService = null;
                    logger.debug("state of type switchProgram: {}", decodedData);
                    /* Get the KM200SwitchProgramService class object with all specific parameters */
                    if (object.getVirtual() == 0) {
                        sPService = ((KM200SwitchProgramService) object.getValueParameter());
                    } else {
                        sPService = ((KM200SwitchProgramService) device.getServiceObject(object.getParent())
                                .getValueParameter());
                    }
                    /* Update the switches insode the KM200SwitchProgramService */
                    sPService.updateSwitches(nodeRoot);

                    /* the parsing of switch program-services have to be outside, using json in strings */
                    if (object.getVirtual() == 1) {
                        return this.getVirtualState(object, itemType, service);
                    } else {
                        /* if access to the parent non virtual service the return the switchPoints jsonarray */
                        if (itemType.equals("String")) {
                            state = new StringType(nodeRoot.getJSONArray("switchPoints").toString());
                        } else {
                            logger.warn(
                                    "Bindingtype not supported for switchProgram, only json over strings supported: {}",
                                    itemType.getClass());
                            return null;
                        }
                        return state;
                    }

                case "errorList": /* Check whether the type is a errorList */
                    KM200ErrorService eService = null;
                    logger.debug("state of type errorList: {}", decodedData);
                    /* Get the KM200ErrorService class object with all specific parameters */
                    if (object.getVirtual() == 0) {
                        eService = ((KM200ErrorService) object.getValueParameter());
                    } else {
                        eService = ((KM200ErrorService) device.getServiceObject(object.getParent())
                                .getValueParameter());
                    }
                    /* Update the switches insode the KM200SwitchProgramService */
                    eService.updateErrors(nodeRoot);

                    /* the parsing of switch program-services have to be outside, using json in strings */
                    if (object.getVirtual() == 1) {
                        return this.getVirtualState(object, itemType, service);
                    } else {
                        /* if access to the parent non virtual service the return the switchPoints jsonarray */
                        if (itemType.equals("String")) {
                            state = new StringType(nodeRoot.getJSONArray("values").toString());
                        } else {
                            logger.warn(
                                    "Bindingtype not supported for error list, only json over strings is supported: {}",
                                    itemType.getClass());
                            return null;
                        }
                        return state;
                    }

                case "yRecording": /* Check whether the type is a yRecording */
                    logger.info("state of: type yRecording is not supported yet: {}", decodedData);
                    /* have to be completed */
                    break;

                case "systeminfo": /* Check whether the type is a systeminfo */
                    logger.info("state of: type systeminfo is not supported yet: {}", decodedData);
                    /* have to be completed */
                    break;

                case "arrayData": /* Check whether the type is a arrayData */
                    logger.info("state of: type arrayData is not supported yet: {}", decodedData);
                    /* have to be completed */
                    break;
            }
        } catch (JSONException e) {
            logger.error("Parsingexception in JSON, data: {} error: {} ", decodedData, e.getMessage());
        }
        return null;
    }

    /**
     * This function checks the virtual state of a service
     *
     */
    public State getVirtualState(KM200CommObject object, String itemType, String service) {
        State state = null;
        String type = object.getServiceType();
        logger.debug("Check virtual state of: {} type: {} item: {}", service, type, itemType);

        switch (type) {
            case "switchProgram":
                KM200SwitchProgramService sPService = ((KM200SwitchProgramService) device
                        .getServiceObject(object.getParent()).getValueParameter());
                String[] servicePath = service.split("/");
                String virtService = servicePath[servicePath.length - 1];
                if (virtService.equals("weekday")) {
                    if (itemType.equals("String")) {
                        String val = sPService.getActiveDay();
                        if (val == null) {
                            return null;
                        }
                        state = new StringType(val);
                    } else {
                        logger.warn("Bindingtype not supported for day service: {}", itemType.getClass());
                        return null;
                    }
                } else if (virtService.equals("nbrCycles")) {
                    if (itemType.equals("Number")) {
                        Integer val = sPService.getNbrCycles();
                        if (val == null) {
                            return null;
                        }
                        state = new DecimalType(val);
                    } else {
                        logger.warn("Bindingtype not supported for nbrCycles service: {}", itemType.getClass());
                        return null;
                    }
                } else if (virtService.equals("cycle")) {
                    if (itemType.equals("Number")) {
                        Integer val = sPService.getActiveCycle();
                        if (val == null) {
                            return null;
                        }
                        state = new DecimalType(val);
                    } else {
                        logger.warn("Bindingtype not supported for cycle service: {}", itemType.getClass());
                        return null;
                    }
                } else if (virtService.equals(sPService.getPositiveSwitch())) {
                    if (itemType.equals("Number")) {
                        Integer val = sPService.getActivePositiveSwitch();
                        if (val == null) {
                            return null;
                        }
                        state = new DecimalType(val);
                    } else if (itemType.equals("DateTime")) {
                        Integer val = sPService.getActivePositiveSwitch();
                        if (val == null) {
                            return null;
                        }
                        Calendar rightNow = Calendar.getInstance();
                        Integer hour = val % 60;
                        Integer minute = val - (hour * 60);
                        rightNow.set(Calendar.HOUR_OF_DAY, hour);
                        rightNow.set(Calendar.MINUTE, minute);
                        state = new DateTimeType(rightNow);
                    } else {
                        logger.warn("Bindingtype not supported for cycle service: {}", itemType);
                        return null;
                    }
                } else if (virtService.equals(sPService.getNegativeSwitch())) {
                    if (itemType.equals("Number")) {
                        Integer val = sPService.getActiveNegativeSwitch();
                        if (val == null) {
                            return null;
                        }
                        state = new DecimalType(val);
                    } else if (itemType.equals("DateTime")) {
                        Integer val = sPService.getActiveNegativeSwitch();
                        if (val == null) {
                            return null;
                        }
                        Calendar rightNow = Calendar.getInstance();
                        Integer hour = val % 60;
                        Integer minute = val - (hour * 60);
                        rightNow.set(Calendar.HOUR_OF_DAY, hour);
                        rightNow.set(Calendar.MINUTE, minute);
                        state = new DateTimeType(rightNow);
                    } else {
                        logger.warn("Bindingtype not supported for cycle service: {}", itemType.getClass());
                        return null;
                    }
                }
                break;
            case "errorList":
                KM200ErrorService eService = ((KM200ErrorService) device.getServiceObject(object.getParent())
                        .getValueParameter());
                String[] nServicePath = service.split("/");
                String nVirtService = nServicePath[nServicePath.length - 1];
                /* Go through the parameters and read the values */
                switch (nVirtService) {
                    case "nbrErrors":
                        if (itemType.equals("Number")) {
                            Integer val = eService.getNbrErrors();
                            if (val == null) {
                                return null;
                            }
                            state = new DecimalType(val);
                        } else {
                            logger.warn("Bindingtype not supported for error number service: {}", itemType.getClass());
                            return null;
                        }
                        break;
                    case "error":
                        if (itemType.equals("Number")) {
                            Integer val = eService.getActiveError();
                            if (val == null) {
                                return null;
                            }
                            state = new DecimalType(val);
                        } else {
                            logger.warn("Bindingtype not supported for error service: {}", itemType.getClass());
                            return null;
                        }
                        break;
                    case "errorString":
                        if (itemType.equals("String")) {
                            String val = eService.getErrorString();
                            if (val == null) {
                                return null;
                            }
                            state = new StringType(val);
                        } else {
                            logger.warn("Bindingtype not supported for error string service: {}", itemType.getClass());
                            return null;
                        }
                        break;
                }
                break;
        }
        return state;

    }

    /**
     * This function sets the state of a service on the device
     *
     */
    public byte[] sendProvidersState(String service, Command command, String itemType) {
        synchronized (device) {
            String type = null;
            String dataToSend = null;
            KM200CommObject object = null;

            logger.debug("Prepare item for send: {} type: {} item: {}", service, type, itemType);
            if (device.blacklistMap.contains(service)) {
                logger.debug("Service on blacklist: {}", service);
                return null;
            }
            if (device.containsService(service)) {
                if (device.getServiceObject(service).getWriteable() == 0) {
                    logger.error("Service is listed as read-only: {}", service);
                    return null;
                }
                object = device.getServiceObject(service);
                type = object.getServiceType();
            } else {
                logger.error("Service is not in the determined device service list: {}", service);
                return null;
            }
            /* The service is availible, set now the values depeding on the item and binding type */
            logger.debug("state of: {} type: {}", command, type);
            /* Binding is a NumberItem */
            if (itemType.equals("Number")) {
                BigDecimal bdVal = ((DecimalType) command).toBigDecimal();
                logger.debug("val: {}", bdVal);
                /* Check the capabilities of this service */
                if (object.getValueParameter() != null) {
                    @SuppressWarnings("unchecked")
                    List<BigDecimal> valParas = (List<BigDecimal>) object.getValueParameter();
                    BigDecimal minVal = valParas.get(0);
                    BigDecimal maxVal = valParas.get(1);
                    if (bdVal.compareTo(minVal) < 0) {
                        bdVal = minVal;
                    }
                    if (bdVal.compareTo(maxVal) > 0) {
                        bdVal = maxVal;
                    }
                }
                if (type.equals("floatValue")) {
                    dataToSend = new JSONObject().put("value", bdVal).toString();
                } else if (type.equals("stringValue")) {
                    dataToSend = new JSONObject().put("value", bdVal.toString()).toString();
                } else if (type.equals("switchProgram") && object.getVirtual() == 1) {
                    /* A switchProgram as NumberItem is always virtual */
                    dataToSend = sendVirtualState(object, service, command, itemType);
                } else if (type.equals("errorList") && object.getVirtual() == 1) {
                    /* A errorList as NumberItem is always virtual */
                    dataToSend = sendVirtualState(object, service, command, itemType);
                } else {
                    logger.warn("Not supported type for numberItem: {}", type);
                }
                /* Binding is a StringItem */
            } else if (itemType.equals("String")) {
                String val = ((StringType) command).toString();
                /* Check the capabilities of this service */
                if (object.getValueParameter() != null) {
                    @SuppressWarnings("unchecked")
                    List<String> valParas = (List<String>) object.getValueParameter();
                    if (!valParas.contains(val)) {
                        logger.warn("Parameter is not in the service parameterlist: {}", val);
                        return null;
                    }
                }
                if (type.equals("stringValue")) {
                    dataToSend = new JSONObject().put("value", val).toString();
                } else if (type.equals("floatValue")) {
                    dataToSend = new JSONObject().put("value", Float.parseFloat(val)).toString();
                } else if (type.equals("switchProgram")) {
                    if (object.getVirtual() == 1) {
                        dataToSend = sendVirtualState(object, service, command, itemType);
                    } else {
                        /* The JSONArray of switch items can be sended directly */
                        try {
                            /* Check whether ths input string is a valid JSONArray */
                            JSONArray userArray = new JSONArray(val);
                            dataToSend = userArray.toString();
                        } catch (Exception e) {
                            logger.warn("The input for the switchProgram is not a valid JSONArray : {}",
                                    e.getMessage());
                            return null;
                        }
                    }
                } else {
                    logger.warn("Not supported type for stringItem: {}", type);
                }

                /* Binding is a DateTimeItem */
            } else if (itemType.equals("DateTime")) {
                String val = ((DateTimeType) command).toString();
                if (type.equals("stringValue")) {
                    dataToSend = new JSONObject().put("value", val).toString();
                } else if (type.equals("switchProgram")) {
                    dataToSend = sendVirtualState(object, service, command, itemType);
                } else {
                    logger.warn("Not supported type for dateTimeItem: {}", type);
                }

            } else {
                logger.warn("Bindingtype not supported: {}", itemType.getClass());
                return null;
            }
            /* If some data is availible then we have to send it to device */
            if (dataToSend != null) {
                /* base64 + encoding */
                logger.debug("Encoding: {}", dataToSend);
                byte[] encData = encodeMessage(dataToSend);
                if (encData == null) {
                    logger.error("Couldn't encrypt data");
                    return null;
                }
                return encData;
            } else {
                return null;
            }
        }
    }

    /**
     * This function sets the state of a virtual service
     *
     */
    public String sendVirtualState(KM200CommObject object, String service, Command command, String itemType) {
        String dataToSend = null;
        String type = null;
        logger.debug("Check virtual state of: {} type: {} item: {}", service, type, itemType);
        object = device.getServiceObject(service);
        KM200CommObject parObject = device.getServiceObject(object.getParent());
        type = object.getServiceType();
        /* Binding is a StringItem */
        if (itemType.equals("String")) {
            String val = ((StringType) command).toString();
            switch (type) {
                case "switchProgram":
                    KM200SwitchProgramService sPService = ((KM200SwitchProgramService) parObject.getValueParameter());
                    String[] servicePath = service.split("/");
                    String virtService = servicePath[servicePath.length - 1];
                    if (virtService.equals("weekday")) {
                        /* Only parameter changing without communication to device */
                        sPService.setActiveDay(val);
                    }
                    break;
            }
            /* Binding is a NumberItem */
        } else if (itemType.equals("Number")) {
            Integer val = ((DecimalType) command).intValue();
            switch (type) {
                case "switchProgram":
                    KM200SwitchProgramService sPService = ((KM200SwitchProgramService) parObject.getValueParameter());
                    String[] servicePath = service.split("/");
                    String virtService = servicePath[servicePath.length - 1];
                    if (virtService.equals("cycle")) {
                        /* Only parameter changing without communication to device */
                        sPService.setActiveCycle(val);
                    } else if (virtService.equals(sPService.getPositiveSwitch())) {
                        sPService.setActivePositiveSwitch(val);
                        /* Create a JSON Array from current switch configuration */
                        dataToSend = sPService.getUpdatedJSONData(parObject);
                    } else if (virtService.equals(sPService.getNegativeSwitch())) {
                        sPService.setActiveNegativeSwitch(val);
                        /* Create a JSON Array from current switch configuration */
                        dataToSend = sPService.getUpdatedJSONData(parObject);
                    }
                    break;
                case "errorList":
                    KM200ErrorService eService = ((KM200ErrorService) device.getServiceObject(object.getParent())
                            .getValueParameter());
                    String[] nServicePath = service.split("/");
                    String nVirtService = nServicePath[nServicePath.length - 1];
                    if (nVirtService.equals("error")) {
                        /* Only parameter changing without communication to device */
                        eService.setActiveError(val);
                    }
                    break;
            }
        } else if (itemType.equals("DateTime")) {
            Calendar cal = ((DateTimeType) command).getCalendar();
            KM200SwitchProgramService sPService = ((KM200SwitchProgramService) parObject.getValueParameter());
            String[] servicePath = service.split("/");
            String virtService = servicePath[servicePath.length - 1];
            Integer minutes;
            if (virtService.equals(sPService.getPositiveSwitch())) {
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
                minutes = (minutes % sPService.getSwitchPointTimeRaster()) * sPService.getSwitchPointTimeRaster();
                sPService.setActivePositiveSwitch(minutes);
                /* Create a JSON Array from current switch configuration */
                dataToSend = sPService.getUpdatedJSONData(parObject);
            }
            if (virtService.equals(sPService.getNegativeSwitch())) {
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
                minutes = (minutes % sPService.getSwitchPointTimeRaster()) * sPService.getSwitchPointTimeRaster();
                sPService.setActiveNegativeSwitch(minutes);
                /* Create a JSON Array from current switch configuration */
                dataToSend = sPService.getUpdatedJSONData(parObject);
            }
        }
        return dataToSend;
    }
}
