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

import android.content.Context;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WifiListAdapter extends BaseAdapter {
    private final WifiNetwork[] listNetworks;
    private Context context;

    public WifiListAdapter(WifiNetwork[] list, Context context) {
        if (list != null)
            this.listNetworks = list;
        else
            this.listNetworks = new WifiNetwork[0];
        this.context = context;
    }

    public int getCount() {
        return listNetworks.length;
    }

    public Object getItem(int position) {
        return listNetworks[position];
    }

    public long getItemId(int position) {
        return listNetworks[position].hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final WifiNetwork wifi = listNetworks[position];
        final int strenght = listNetworks[position].getLevel();
        final RelativeLayout itemLayout = (RelativeLayout) LayoutInflater.from(
                context).inflate(R.layout.item_list_wifi, parent, false);

        TextView ssid = (TextView) itemLayout.findViewById(R.id.wifiName);
        ssid.setText(wifi.getSsid());

        TextView bssid = (TextView) itemLayout.findViewById(R.id.wifiMAC);
        bssid.setText(wifi.getMacClean());

        ImageView icon = (ImageView) itemLayout.findViewById(R.id.icon);
        if (wifi.isSupported())
            icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_possible));

        ImageView networkS = (ImageView) itemLayout.findViewById(R.id.strenght);
        int pic = WifiManager.calculateSignalLevel(strenght, 4);
        switch (pic) {
        case 0:
            networkS.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_wifi_weak));
            break;
        case 1:
            networkS.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_wifi_medium));
            break;
        case 2:
            networkS.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_wifi_strong));
            break;
        case 3:
            networkS.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_wifi_verystrong));
            break;
        }
        return itemLayout;
    }

}
