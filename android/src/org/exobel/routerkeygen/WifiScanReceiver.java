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
import java.util.Set;
import java.util.TreeSet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

class WiFiScanReceiver extends BroadcastReceiver {
    final ScanListener listener;
    final NetworkMatcher matcher;
    final WifiManager wifiManager;

    public WiFiScanReceiver(ScanListener listener, WifiManager wifiManager,
            NetworkMatcher matcher) {
        this.listener = listener;
        this.matcher = matcher;
        this.wifiManager = wifiManager;
    }

    public void onReceive(Context context, Intent intent) {
        List<ScanResult> results = wifiManager.getScanResults();
        if (results == null)/*
                             * He have had reports of this returning null
                             * instead of empty
                             */
            return;
        final Set<WifiNetwork> set = new TreeSet<WifiNetwork>();
        // Removing repeated results
        for (int i = 0; i < results.size() - 1; ++i)
            for (int j = i + 1; j < results.size(); ++j)
                if (results.get(i).SSID.equals(results.get(j).SSID))
                    results.remove(j--);

        for (ScanResult result : results) {
            set.add(matcher.matchNetwork(result));
        }

        if (set.isEmpty()) {
            Toast.makeText(context, R.string.msg_nowifidetected,
                    Toast.LENGTH_SHORT).show();
        }
        listener.setScannedNetworks(set.toArray(new WifiNetwork[0]));
        try {
            context.unregisterReceiver(this);
        } catch (Exception e) {
        }

    }
}
