/*
 * Copyright 2012 Rui Araújo, Luís Fonseca
 *
 * This file is part of Router Keygen.
 *
 * Router Keygen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Router Keygen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Router Keygen.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exobel.routerkeygen;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class WifiNetwork implements Comparable<WifiNetwork>, Parcelable{
	
	private final String ssid;
	private final String mac;
	private final String ssidSubpart;
	private final String encryption;
	private final boolean supported;
	private final boolean newThomson;
	private final int level;
	private final List <AliceMagicInfo> supportedAlice;
	private final TYPE type;
	public static enum TYPE {
		THOMSON , DLINK , DISCUS , VERIZON ,
		EIRCOM , PIRELLI , TELSEY , ALICE ,
		WLAN4 , HUAWEI, WLAN2 , ONO_WEP ,
		SKY_V1 , WLAN6 ,TECOM , INFOSTRADA };
		
    public WifiNetwork(String ssid, String subpart, String mac, int level , String enc , boolean supported , TYPE type ){
        this.ssid = ssid;
        this.ssidSubpart = subpart;
        this.mac = mac;
        this.level  = level;
        if ( enc.equals(""))
            this.encryption = "Open";
        else
            this.encryption = enc;
        this.newThomson = false;
        this.supported = supported;
        this.type = type;
        supportedAlice = null;
    }
    
    public WifiNetwork(String ssid, String subpart, String mac, int level , String enc , boolean supported , TYPE type , List <AliceMagicInfo> supportedAlice){
        this.ssid = ssid;
        this.ssidSubpart = subpart;
        this.mac = mac;
        this.level  = level;
        if ( enc.equals(""))
            this.encryption = "Open";
        else
            this.encryption = enc;
        this.newThomson = false;
        this.supported = supported;
        this.type = type;
        this.supportedAlice = supportedAlice;
    }
	
	public int getLevel(){
		return level;
	}
	
	public String getSSIDsubpart(){
		return ssidSubpart;
	}
		
	public String getMacClean(){
		return  mac.replace(":", "");
	}
	
    public String getMac(){
        return  mac;
    }
    
	public int compareTo(WifiNetwork another) {
		if ( another.level == this.level && this.getSsid().equals(another.getSsid()) && this.mac.equals(another.mac) )
			return 0;
		if ( this.isSupported() && !this.newThomson )
			return -1;
		return 1;
	}

    public String getSsid() {
        return ssid;
    }
    public List <AliceMagicInfo> getSupportedAlice() {
        return supportedAlice;
    }

    public TYPE getType() {
        return type;
    }

    public String getEncryption() {
        return encryption;
    }

    public boolean isSupported() {
        return supported;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        //TODO
    }

}
