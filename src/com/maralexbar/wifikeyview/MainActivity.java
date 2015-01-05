/*
 * Copyright (C) 2014
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.maralexbar.wifikeyview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Int2;
import android.webkit.WebView;

public class MainActivity extends Activity {
	
	WebView w;
	String pwd="";
	String ssid="";
	String type="";
	String open="";
	String error="";
	String ret;
	int id=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		w = (WebView) this.findViewById(R.id.w1);
		pwd=getResources().getString(R.string.pwd);
		ssid=getResources().getString(R.string.ssid);
		type=getResources().getString(R.string.type);
		open=getResources().getString(R.string.open);
		error=getResources().getString(R.string.error);
		//RunAsRoot("busybox cp /data/misc/wifi/wpa_supplicant.conf /sdcard/wpa_supplicant.conf");
		String aa;
		aa=RunAsRoot("su -c cat /data/misc/wifi/wpa_supplicant.conf | busybox grep -E \"key_mgmt|ssid|psk\""); 
		aa=aa.replace("\t", "<br>");
		aa=aa.replace("key_mgmt=NONE", "<font color=00ff00>"+type+": "+open+"</font><hr>");
		aa=aa.replace("key_mgmt=WPA-PSK", "<font color=ff0000>"+type+": "+"WPA-PSK</font><hr>");
		aa=aa.replace("ssid=", ssid+": ");
		aa=aa.replace("psk=", pwd+": ");
		aa=aa.replace("\"", "");
		aa=aa+"<p>Wifi Key Viey 1.0<br>Created By <a href=\"https://github.com/Maralexbar\">Maralexbar<br></a>Github: <a href=\"https://github.com/Maralexbar/Wifi-Key-View\">https://github.com/Maralexbar/Wifi-Key-View</a></p>";
		w.loadData(aa, "text/html", null);
						
	}

	private String RunAsRoot(String cmd) {
		try {
		    // Executes the command.
		    Process process = Runtime.getRuntime().exec(cmd);
		    
		    // Reads stdout.
		    // NOTE: You can write to stdin of the command using
		    //       process.getOutputStream().
		    BufferedReader reader = new BufferedReader(
			new InputStreamReader(process.getInputStream()));
		    int read;
		    String aa="";
		    id=0;
		    char[] buffer = new char[4096];
		    StringBuffer output = new StringBuffer();
		    while ((read = reader.read(buffer)) > 0) {
		        output.append(buffer, 0, read);
		        aa += output.toString();
		        ++id;
		    }
		    reader.close();
		    
		    // Waits for the command to finish.
		    process.waitFor();
		    
		    ret = id == 0 ? error+"<hr>" : aa;
		} catch (IOException e) {
		    throw new RuntimeException(e);
		} catch (InterruptedException e) {
		    throw new RuntimeException(e);
		}
		return ret;
	}
}
