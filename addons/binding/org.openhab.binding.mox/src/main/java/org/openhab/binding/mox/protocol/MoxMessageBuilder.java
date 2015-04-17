/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mox.protocol;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


/**
 * @author Thomas Eichstaedt-Engelen (innoQ)
 * @since 2.0.0
 */
public class MoxMessageBuilder {

    private Logger logger = LoggerFactory.getLogger(MoxMessageBuilder.class);

	private static MoxMessageBuilder instance;
	private MoxMessage message; 
	
	
	private MoxMessageBuilder(MoxMessage message) {
		this.message = message;
	}
	
	public synchronized static MoxMessageBuilder messageBuilder(MoxMessage message) {
		if (instance == null) {
			instance = new MoxMessageBuilder(message);
		}
		return instance;
	}
	
	public MoxMessageBuilder withOid(int oid) {
		message.setOid(oid);
		return this;
	}
	
	public MoxMessageBuilder withSuboid(int suboid) {
		message.setSuboid(suboid);
		return this;
	}

	public MoxMessageBuilder withPriority(int priority) {
		message.setPriority(priority);
		return this;
	}

	public MoxMessageBuilder withCommandCode(MoxCommandCode commandCode) {
		message.setCommandCode(commandCode);
		return this;
	}

	public MoxMessageBuilder withValue(BigDecimal value) {
		message.setValue(value);
		return this;
	}
	
	
	public MoxMessageBuilder parseFrom(byte[] rawdata) {
		message.setHexString(new String(Hex.encodeHex(rawdata)));
		
		message.setPriority(readBytes(rawdata, 0, 1, false));
		message.setOid(readBytes(rawdata, 1, 3, false));
		message.setSuboid(readBytes(rawdata, 4, 1, false));
		message.setSubFunctionCode(readBytes(rawdata, 5, 1, false));
		message.setFunctionCode(readBytes(rawdata, 8, 2, false));
		
		MoxCommandCode code = 
			MoxCommandCode.valueOf(message.getSubFunctionCode(), message.getFunctionCode());
		message.setEventName(code.name());

		switch(code) {
			case POWER_ACTIVE:
			case POWER_REACTIVE:
			case POWER_APPARENT:
				message.setCommandCode(code);
				message.setValue(new BigDecimal(readBytes(rawdata, 10, 4, true) / 1000.0));
				break;
	
			case POWER_FACTOR:
			case POWER_ACTIVE_ENERGY:
				message.setCommandCode(code);
				message.setValue(new BigDecimal(readBytes(rawdata, 10, 4, true) / 1000.0));
				break;
	
			case LUMINOUS_GET:
				message.setCommandCode(code);
				message.setValue(new BigDecimal(readBytes(rawdata, 10, 1, false)));
				break;
	
			case LUMINOUS_SET:
				message.setDimmerTime(readBytes(rawdata, 12, 2, true));
			case INCREASE:
			case DECREASE:
			case STATUS:
			case ONOFF:
				message.setValue(new BigDecimal(readBytes(rawdata, 10, 1, false)));
				break;
		}
	
		return this;
	}

	public byte[] toBytes() {
		byte[] bytes;
		final MoxCommandCode commandCode = message.getCommandCode();
		int oid = message.getOid();

		switch (commandCode) {
			case LUMINOUS_SET:
				bytes = new byte[14];
				setBytes(bytes, 10, message.getValue().intValue());
				setBytes(bytes, 11, 0);
				setBytes(bytes, 12, 0x20, 0x3); // LSB 300ms = 0x320 TODO make dim speed configurable
				break;
			case ONOFF:
				bytes = new byte[11];
				setBytes(bytes, 10, message.getValue().intValue());
				break;
			case INCREASE:
			case DECREASE:
				bytes = new byte[14];
				setBytes(bytes, 10, 0x5, 0, 0xc8, 0); // TODO step = 5%, make this configurable
				break;
			default:
				bytes = new byte[15];
		}

		setBytes(bytes, 0, message.getPriority());
		setBytes(bytes, 1, oid/512, oid/256, oid%256);
		setBytes(bytes, 4, 0x11); // TODO suboid
		setBytes(bytes, 5, commandCode.getLow());
		setBytes(bytes, 6, 0, 0);
		setBytes(bytes, 8, commandCode.getHigh()/256);
		setBytes(bytes, 9, commandCode.getHigh() % 256);
		return bytes;
	}
	
	private static int readBytes(byte[] rawdata, int offset, int length, boolean le) {
	    int value = 0;
	    for (int i = 0; i < length; i++) {
	        int shift = le ? i * 8 : (length - 1 - i) * 8;
	        value += (rawdata[i + offset] & 0x000000FF) << shift;
	    }
	    return value;
	}

	private static byte[] setBytes(byte[] rawdata, int offset, int... values) {
		for (int i=0; i<values.length; i++) {
			byte currVal = (byte) (((byte) values[i]) & 0xff);
			rawdata[offset + i] = currVal;
		}
		return rawdata;
	}
	
	public static int[] getUnsignedIntArray(byte[] bytes) {
		if (bytes == null)
	        return null;

		int[] res = new int[bytes.length];

	    for (int i = 0; i < bytes.length; i++) {
		res[i] = bytes[i] & 0xff;
	    }
	    return res;
	}

    public MoxMessage build() {
        return this.message;
    }
    
}
