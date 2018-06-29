package org.openhab.binding.ihc.internal.converters;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.types.Type;
import org.junit.Test;
import org.openhab.binding.ihc.ws.projectfile.IhcEnumValue;
import org.openhab.binding.ihc.ws.resourcevalues.WSEnumValue;
import org.openhab.binding.ihc.ws.resourcevalues.WSResourceValue;

/**
 * Test for IHC / ELKO binding
 *
 * @author Pauli Anttila
 */
public class StringTypeWSEnumValueConverterTest {

    @Test
    public void Test() {
        ArrayList<IhcEnumValue> enumValues = new ArrayList<>();
        enumValues.add(new IhcEnumValue(101, "testA"));
        enumValues.add(new IhcEnumValue(102, "testB"));
        enumValues.add(new IhcEnumValue(103, "testC"));
        enumValues.add(new IhcEnumValue(104, "testD"));

        WSEnumValue val = new WSEnumValue(12345, 5555, 0, "");

        val = convertFromOHType(val, new StringType("testC"), new ConverterAdditionalInfo(enumValues, false));
        assertEquals(12345, val.getResourceID());
        assertEquals(5555, val.getDefinitionTypeID());
        assertEquals(103, val.getEnumValueID());
        assertEquals("testC", val.getEnumName());

        StringType type = convertFromResourceValue(val, new ConverterAdditionalInfo(enumValues, false));
        assertEquals(new StringType("testC"), type);
    }

    private WSEnumValue convertFromOHType(WSEnumValue IHCvalue, Type OHval,
            ConverterAdditionalInfo converterAdditionalInfo) {
        Converter<WSResourceValue, Type> converter = ConverterFactory.getInstance().getConverter(IHCvalue.getClass(),
                StringType.class);
        return (WSEnumValue) converter.convertFromOHType(OHval, IHCvalue, converterAdditionalInfo);
    }

    private StringType convertFromResourceValue(WSEnumValue IHCvalue, ConverterAdditionalInfo converterAdditionalInfo) {
        Converter<WSResourceValue, Type> converter = ConverterFactory.getInstance().getConverter(IHCvalue.getClass(),
                StringType.class);
        return (StringType) converter.convertFromResourceValue(IHCvalue, converterAdditionalInfo);
    }
}
