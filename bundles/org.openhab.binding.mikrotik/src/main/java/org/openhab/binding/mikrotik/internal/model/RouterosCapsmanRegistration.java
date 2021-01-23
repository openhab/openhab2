package org.openhab.binding.mikrotik.internal.model;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.openhab.binding.mikrotik.internal.util.Converter;

import java.math.BigInteger;
import java.util.Map;

@NonNullByDefault
public class RouterosCapsmanRegistration extends RouterosRegistrationBase {
    public RouterosCapsmanRegistration(Map<String, String> props) {
        super(props);
        postProcess();
    }

    private void postProcess(){
        String[] bytes = propMap.get("bytes").split(",");
        propMap.put("tx-byte", bytes[0]);
        propMap.put("rx-byte", bytes[1]);

        String[] packets = propMap.get("packets").split(",");
        propMap.put("tx-packet", packets[0]);
        propMap.put("rx-packet", packets[1]);
    }

    public String getIdentity(){ return propMap.get("eap-identity"); }
    public String getInterfaceName(){ return propMap.get("interface"); }
    public int getRxSignal(){ return Integer.parseInt(propMap.get("rx-signal")); }

    public BigInteger getTxBytes(){ return new BigInteger(propMap.get("tx-byte")); }
    public BigInteger getRxBytes(){ return new BigInteger(propMap.get("rx-byte")); }
    public BigInteger getTxPackets(){ return new BigInteger(propMap.get("tx-packet")); }
    public BigInteger getRxPackets(){ return new BigInteger(propMap.get("rx-packet")); }

    public String getUptime(){ return propMap.get("uptime"); }
    public @Nullable DateTime getUptimeStart(){
        if(propMap.containsKey("uptime")){
            Period uptime = Converter.fromRouterosPeriod(propMap.get("uptime"));
            return DateTime.now().minus(uptime);
        }
        return null;
    }

}
