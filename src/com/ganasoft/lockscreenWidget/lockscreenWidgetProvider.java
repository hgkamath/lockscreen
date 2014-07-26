

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

import com.ganasoft.lockscreenWidget.DeviceAdminSample;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class lockscreenWidgetProvider extends AppWidgetProvider {
	private static final String TAG = "LockScreen Widget Provider";

	final static int ACTIVATION_REQUEST = 1;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		

		// Get all id s
		ComponentName thisWidget = new ComponentName(context,
				lockscreenWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {
			Log.w(TAG, "onUpdate1:"+String.valueOf(widgetId));
			// Register an onClickListener
			Intent intent = new Intent(context, lockscreenWidgetProvider.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			int[] ids = { widgetId };// ids are the widgets to send update to
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids); 
			intent.putExtra("b_onclicked", widgetId); 
			// b_onclicked is used as an identifier if triggered by onclick

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.lockbtn, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);

		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.w(TAG, "onreceive1:" + String.valueOf(intent == null));
		int onclicktriggered = intent.getIntExtra("b_onclicked",
				AppWidgetManager.INVALID_APPWIDGET_ID);
		//
		if (onclicktriggered != AppWidgetManager.INVALID_APPWIDGET_ID) {
			Log.w(TAG, "onreceive2:" + String.valueOf(onclicktriggered));
			DevicePolicyManager mDPM = (DevicePolicyManager) context
					.getSystemService(Context.DEVICE_POLICY_SERVICE);
			ComponentName mDeviceAdminSample = new ComponentName(context, DeviceAdminSample.class);
			boolean active=mDPM.isAdminActive(mDeviceAdminSample); 
			if (!active){
				Intent intent2 = new Intent(context, DeviceAdminSample.Controller.class);
				intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent2);
			}else{
			    mDPM.lockNow();
			}
		}
		super.onReceive(context, intent);
	}
}
