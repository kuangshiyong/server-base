package cn.bjzfgcjs.idefense.common.utils;

import java.util.StringTokenizer;

/**
 * TCP/IP Address Utility Class:
 *
 * 模拟C#：
 *
 * IPAddress.TryParse(IpStr, out ipaddress)
 * BitConverter.ToUInt32(ipaddress.GetAddressBytes(), 0)
 *
 */
public class IPAddress {

    public final static long parseAddress(String ipaddr) {
        //  Check if the string is valid
        if ( ipaddr == null || ipaddr.length() < 7 || ipaddr.length() > 15)
            return 0;

        //  Check the address string, should be n.n.n.n format
        StringTokenizer token = new StringTokenizer(ipaddr,".");
        if ( token.countTokens() != 4)
            return 0;

        long ipInt = 0;
        int count = 0;
        while ( token.hasMoreTokens()) {
            //  Get the current token and convert to an integer value
            String ipNum = token.nextToken();
            try {
                Long ipVal = Long.valueOf(ipNum);
                if ( ipVal < 0 || ipVal > 255)
                    return 0;

                ipInt |= ipVal << ( count * 8);
            }
            catch (NumberFormatException ex) {
                return 0;
            }
            count ++;
        }

        //  Return the integer address
        return ipInt;
    }
}
