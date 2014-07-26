

/*
* Copyright (C) 2014 Ganapathi Kamath 
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.ganasoft.lockscreenWidget; 


import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



public class DeviceAdminSample extends DeviceAdminReceiver {
	private static final String TAG = "LockScreen Admin Receiver";

	 @Override
	 public void onEnabled(Context context, Intent intent) {
		 Log.i(TAG, "Sample Device Admin: enabled");
	 }
	 @Override
	 public void onDisabled(Context context, Intent intent) {
		 Log.i(TAG, "Sample Device Admin: disabled");
	 }
	// An empty class that is a place holder for being the device admin receiver

	// An Inner Class put here simple for keeping things together

	public static class Controller extends Activity {
		public static DevicePolicyManager mDPM;
		public static ActivityManager mAM;
		public static ComponentName mDeviceAdminSample;
		final static int ACTIVATION_REQUEST = 1;
		private static final String TAG = "LockScreen Controller Activity";
		
		 
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Log.i(TAG,"controller created");
			mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
			mAM = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			mDeviceAdminSample = new ComponentName(Controller.this, DeviceAdminSample.class);
		
			
			boolean active=mDPM.isAdminActive(mDeviceAdminSample); 
			if (!active){
				Log.i(TAG,"getting admin access first");
				get_admin();
				//mDPM.lockNow();
			}else{
				//mDPM.lockNow();
			}		
			// kill my own pid
			//android.os.Process.killProcess(android.os.Process.myPid());
			finish();
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			switch (requestCode) {
			case ACTIVATION_REQUEST:
				if (resultCode == Activity.RESULT_OK) {
					Log.i(TAG, "Administration enabled!");
				} else {
					Log.i(TAG, "Administration enable FAILED!");
				}
				return;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

		public void get_admin(){
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					mDeviceAdminSample);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"to lock screen");
			intent.putExtra("force-locked",
					DeviceAdminInfo.USES_POLICY_FORCE_LOCK);

			startActivityForResult(intent, 1);
		}

 
	}
}