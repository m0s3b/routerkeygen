package org.exobel.routerkeygen;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.exobel.routerkeygen.WifiNetwork.TYPE;

import android.content.Context;
import android.net.wifi.ScanResult;

public class NetworkMatcher {

    private final List<AliceMagicInfo> supportedAlice;

    private final static String THOMSON_ROUTER = "(Thomson|SpeedTouch|O2Wireless|Orange-|INFINITUM|BigPond|Otenet|Bbox-|DMAX|privat|TN_private_|CYTA|Blink)[0-9a-fA-F]{6}";
    private final static String DLINK_ROUTER = "DLink-[0-9a-fA-F]{6}";
    private final static String TELSEY_ROUTERS = "FASTWEB-(1|2)-(002196|00036F)[0-9A-Fa-f]{6}";
    private final static String PIRELLI_ROUTERS = "FASTWEB-1-(000827|0013C8|0017C2|00193E|001CA2|001D8B|002233|00238E|002553|00A02F|080018|3039F2|38229D|6487D7)[0-9A-Fa-f]{6}";
    private final static String WLAN4_ROUTERS = "(WLAN|JAZZTEL)_[0-9a-fA-F]{4}";
    private final static String WLAN6_ROUTERS = "WLAN[0-9a-zA-Z]{6}|WiFi[0-9a-zA-Z]{6}|YaCom[0-9a-zA-Z]{6}";
    private final static String TECOM_ROUTERS = "TECOM-AH4(021|222)-[0-9a-zA-Z]{6}";
    private final static String HUAWEI_ROUTERS = "INFINITUM[0-9a-zA-Z]{4}";
    private final static String SKY_ROUTERS = "SKY[0-9]{5}";
    private final static String INFOSTRADA_ROUTERS = "InfostradaWiFi-[0-9a-zA-Z]{6}";
    private final static String ONO_ROUTERS = "[Pp]1[0-9]{6}0{4}[0-9]";
    
    
    public NetworkMatcher(Context con) {
        final AliceHandle aliceReader = new AliceHandle();
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser;
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(con.getResources().openRawResource(R.raw.alice),
                    aliceReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        supportedAlice = aliceReader.getSupportedAlice();
    }
    

    public WifiNetwork matchNetwork(ScanResult result) {
        if (result == null)
            return null;
        final TYPE type;
        final String ssid = result.SSID;
        final String mac = result.BSSID.toUpperCase();
        if (ssid.matches(THOMSON_ROUTER)) {
            final String ssidSubpart = ssid.substring(ssid.length() - 6);
            if (!mac.equals("")) {
                if (ssidSubpart.equals(mac.replace(":", "").substring(6))) {
                    return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                            result.capabilities, false, null);
                }
            }
            type = TYPE.THOMSON;
            return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                    result.capabilities, true, type);
        }
        if (ssid.matches(DLINK_ROUTER)) {
            final String ssidSubpart = ssid.substring(ssid.length() - 6);
            type = TYPE.DLINK;
            return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                    result.capabilities, true, type);
        }
        if (ssid.matches("Discus--?[0-9a-fA-F]{6}")) {
            final String ssidSubpart = ssid.substring(ssid.length() - 6);
            type = TYPE.DISCUS;
            return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                    result.capabilities, true, type);
        }
        if ((ssid.matches("eircom[0-7]{8}|eircom[0-7]{4} [0-7]{4}"))) {
            final String ssidSubpart;
            if (ssid.length() == 14)
                ssidSubpart = ssid.substring(ssid.length() - 8);
            else
                ssidSubpart = ssid.substring(6, 10)
                        + ssid.substring(ssid.length() - 4);
            type = TYPE.EIRCOM;
            if (mac.equals(""))
                return new WifiNetwork(ssid, ssidSubpart,
                        calcEircomMAC(ssidSubpart), result.level,
                        result.capabilities, true, type);
            else
                return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                        result.capabilities, true, type);
        }
        if (ssid.length() == 5
                && (mac.startsWith("00:1F:90") || mac.startsWith("A8:39:44")
                        || mac.startsWith("00:18:01")
                        || mac.startsWith("00:20:E0")
                        || mac.startsWith("00:0F:B3")
                        || mac.startsWith("00:1E:A7")
                        || mac.startsWith("00:15:05")
                        || mac.startsWith("00:24:7B")
                        || mac.startsWith("00:26:62") || mac
                            .startsWith("00:26:B8"))) {
            type = TYPE.VERIZON;
            return new WifiNetwork(ssid, ssid, mac, result.level,
                    result.capabilities, true, type);
        }

        if (ssid.matches(PIRELLI_ROUTERS)) {
            final String ssidSubpart = ssid.substring(ssid.length() - 12);
            type = TYPE.PIRELLI;
            if (mac.equals(""))
                return new WifiNetwork(ssid, ssidSubpart,
                        calcFastwebMAC(ssidSubpart), result.level,
                        result.capabilities, true, type);
            else
                return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                        result.capabilities, true, type);
        }
        if (ssid.matches(TELSEY_ROUTERS)) {
            final String ssidSubpart = ssid.substring(ssid.length() - 12);
            type = TYPE.TELSEY;
            if (mac.equals(""))
                return new WifiNetwork(ssid, ssidSubpart,
                        calcFastwebMAC(ssidSubpart), result.level,
                        result.capabilities, true, type);
            else
                return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                        result.capabilities, true, type);
        }
        if (ssid.matches("[aA]lice-[0-9]{8}")) {
            final String ssidSubpart = ssid.substring(ssid.length() - 8);
            final List<AliceMagicInfo> magicInfos = new ArrayList<AliceMagicInfo>();
            for ( AliceMagicInfo info : supportedAlice )
            {
                if ( info.getAlice().equalsIgnoreCase(ssidSubpart) )
                    magicInfos.add(info);
            }
            type = TYPE.ALICE;
            if ( magicInfos.size() > 0 )
            {
                if (mac.equals(""))
                    return new WifiNetwork(ssid, ssidSubpart,
                            magicInfos.get(0).getMac(), result.level,
                            result.capabilities, true, type,magicInfos);
                else
                    return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                            result.capabilities, true, type,magicInfos);
            }
            else
                return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                        result.capabilities, true, type);
        }
        if (ssid.matches(WLAN4_ROUTERS)
                && (mac.startsWith("00:1F:A4") || mac.startsWith("64:68:0C") || mac
                        .startsWith("00:1D:20"))) {
            final String ssidSubpart = ssid.substring(ssid.length() - 4);
            type = TYPE.WLAN4;
            return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                    result.capabilities, true, type);
        }
        if (ssid.matches(HUAWEI_ROUTERS)
                && (mac.startsWith("00:25:9E") || mac.startsWith("00:25:68")
                        || mac.startsWith("00:22:A1")
                        || mac.startsWith("00:1E:10")
                        || mac.startsWith("00:18:82")
                        || mac.startsWith("00:0F:F2")
                        || mac.startsWith("00:E0:FC")
                        || mac.startsWith("28:6E:D4")
                        || mac.startsWith("54:A5:1B")
                        || mac.startsWith("F4:C7:14")
                        || mac.startsWith("28:5F:DB")
                        || mac.startsWith("30:87:30")
                        || mac.startsWith("4C:54:99")
                        || mac.startsWith("40:4D:8E")
                        || mac.startsWith("64:16:F0")
                        || mac.startsWith("78:1D:BA")
                        || mac.startsWith("84:A8:E4")
                        || mac.startsWith("04:C0:6F")
                        || mac.startsWith("5C:4C:A9")
                        || mac.startsWith("1C:1D:67")
                        || mac.startsWith("CC:96:A0") || mac
                            .startsWith("20:2B:C1"))) {
            final String ssidSubpart;
            if (ssid.startsWith("INFINITUM"))
                ssidSubpart = ssid.substring(ssid.length() - 4);
            else
                ssidSubpart = "";
            type = TYPE.HUAWEI;
            return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                    result.capabilities, true, type);
        }
        if (ssid.startsWith("WLAN_")
                && ssid.length() == 7
                && (mac.startsWith("00:01:38") || mac.startsWith("00:16:38")
                        || mac.startsWith("00:01:13")
                        || mac.startsWith("00:01:1B") || mac
                            .startsWith("00:19:5B"))) {
            final String ssidSubpart = ssid.substring(ssid.length() - 2);
            type = TYPE.WLAN2;
            return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                    result.capabilities, true, type);
        }
        /* ssid must be of the form P1XXXXXX0000X or p1XXXXXX0000X */
        if (ssid.matches(ONO_ROUTERS)) {
            type = TYPE.ONO_WEP;
            return new WifiNetwork(ssid, "", mac, result.level,
                    result.capabilities, true, type);
        }
        if (ssid.matches(WLAN6_ROUTERS)) {
            final String ssidSubpart = ssid.substring(ssid.length() - 6);
            type = TYPE.WLAN6;
            return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                    result.capabilities, true, type);
        }
        if (ssid.matches(SKY_ROUTERS)
                && (mac.startsWith("C4:3D:C7") || mac.startsWith("E0:46:9A")
                        || mac.startsWith("E0:91:F5")
                        || mac.startsWith("00:09:5B")
                        || mac.startsWith("00:0F:B5")
                        || mac.startsWith("00:14:6C")
                        || mac.startsWith("00:18:4D")
                        || mac.startsWith("00:26:F2")
                        || mac.startsWith("C0:3F:0E")
                        || mac.startsWith("30:46:9A")
                        || mac.startsWith("00:1B:2F")
                        || mac.startsWith("A0:21:B7")
                        || mac.startsWith("00:1E:2A")
                        || mac.startsWith("00:1F:33")
                        || mac.startsWith("00:22:3F") || mac
                            .startsWith("00:24:B2"))) {
            final String ssidSubpart = ssid.substring(ssid.length() - 5);
            type = TYPE.SKY_V1;
            return new WifiNetwork(ssid, ssidSubpart, mac, result.level,
                    result.capabilities, true, type);
        }
        
        if (ssid.matches(TECOM_ROUTERS)) {
            type = TYPE.TECOM;
            return new WifiNetwork(ssid, ssid, mac, result.level,
                    result.capabilities, true, type);
        }
        if (ssid.matches(INFOSTRADA_ROUTERS)) {
            type = TYPE.INFOSTRADA;
            return new WifiNetwork(ssid, ssid, mac, result.level,
                    result.capabilities, true, type);
        }
        return new WifiNetwork(ssid, "", mac, result.level,
                result.capabilities, false, null);
    }

    private String calcEircomMAC(String ssidSubpart) {
        String end = Integer
                .toHexString(Integer.parseInt(ssidSubpart, 8) ^ 0x000fcc);
        return "00:0F:CC" + ":" + end.substring(0, 2) + ":"
                + end.substring(2, 4) + ":" + end.substring(4, 6);
    }

    private String calcFastwebMAC(String ssidSubpart) {
        return ssidSubpart.substring(0, 2) + ":" + ssidSubpart.substring(2, 4)
                + ":" + ssidSubpart.substring(4, 6) + ":"
                + ssidSubpart.substring(6, 8) + ":"
                + ssidSubpart.substring(8, 10) + ":"
                + ssidSubpart.substring(10, 12);
    }
}
