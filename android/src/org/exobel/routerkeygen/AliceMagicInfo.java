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

import android.os.Parcel;
import android.os.Parcelable;

public class AliceMagicInfo implements Parcelable{
	private final String alice;
	private final int [] magic;
	private final String serial;
	private final String mac;
	public AliceMagicInfo(String alice,  int[] magic,
			String serial, String mac) {
		this.alice = alice;
		this.magic = magic;
		this.serial = serial;
		this.mac = mac;
	}
    public int [] getMagic() {
        return magic;
    }
    public String getSerial() {
        return serial;
    }
    public String getAlice() {
        return alice;
    }
    public String getMac() {
        return mac;
    }
    
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(magic.length);
		dest.writeIntArray(magic);
		dest.writeInt(alice != null ? 1 : 0);
		if (alice != null)
			dest.writeString(alice);
		dest.writeInt(serial != null ? 1 : 0);
		if (serial != null)
			dest.writeString(serial);
		dest.writeInt(mac != null ? 1 : 0);
		if (mac != null)
			dest.writeString(mac);
	}

	public static final Parcelable.Creator<AliceMagicInfo> CREATOR = new Parcelable.Creator<AliceMagicInfo>() {
		public AliceMagicInfo createFromParcel(Parcel in) {
			return new AliceMagicInfo(in);
		}

		public AliceMagicInfo[] newArray(int size) {
			return new AliceMagicInfo[size];
		}
	};

	private AliceMagicInfo(Parcel in) {
			magic = new int[in.readInt()];
			in.readIntArray(magic);
			if ( in.readInt() == 1 )
				alice = in.readString();
			else
				alice = "";
			if ( in.readInt() == 1 )
				serial = in.readString();
			else
				serial = "";
			if ( in.readInt() == 1 )
				mac = in.readString();
			else
				mac = "";
	}
}
