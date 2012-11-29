package com.maro.cgmdemo;

import static com.maro.cgmdemo.CommonUtilities.REG_ID;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	MainActivity ma = new MainActivity();

	@Override
	protected void onError(Context context, String errorId) {
		Toast.makeText(getApplicationContext(), "onError " + "errorId",
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.v("GCMIntentService", "onMessage");
		final String str = String.format("no: %s \ntitle: %s \nmsg: %s",
				intent.getStringExtra("no"), intent.getStringExtra("title"),
				intent.getStringExtra("msg"));
		MainActivity.mHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.v("GCMIntentService", "onRegistered - " + regId);
		Toast.makeText(getApplicationContext(), "onRegistered",
				Toast.LENGTH_LONG).show();
		REG_ID = regId;
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		Toast.makeText(getApplicationContext(), "onUnregistered",
				Toast.LENGTH_LONG).show();
	}

}
