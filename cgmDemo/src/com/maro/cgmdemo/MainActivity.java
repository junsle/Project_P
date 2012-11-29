package com.maro.cgmdemo;

import static com.maro.cgmdemo.CommonUtilities.API_KEY;
import static com.maro.cgmdemo.CommonUtilities.REG_ID;
import static com.maro.cgmdemo.CommonUtilities.SENDER_ID;
import static com.maro.cgmdemo.CommonUtilities.TAG;

import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class MainActivity extends Activity {

	static Handler mHandler = null;
	AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkNotNull(API_KEY);
		checkNotNull(SENDER_ID);
		mHandler = new Handler();
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		REG_ID = GCMRegistrar.getRegistrationId(this);
		if (REG_ID.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			Log.v(TAG,
					"Already registered ("
							+ GCMRegistrar.getRegistrationId(this) + ")");
		}
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (REG_ID == "") {
							Log.v(TAG, "Not yet register");
							return;
						}
						// collapseKey 중복유무판단
						// delayWhileIdle : 메세지과부하시 대기유무 (false로 설정할 경우 성공확률 UP)
						// timeToLive : 기기가 비활성화 상태일때 GCM가 메시지를 유효화하는 시간(메시지
						// 유지시간을 늘려도 성공확률 UP)
						// sender.send()의 3번째 인자. 메시지 전송실패시 재시도 횟수
						Sender sender = new Sender(API_KEY);
						Message message = new Message.Builder()
								.collapseKey(
										String.valueOf(Math.random() % 100 + 1))
								.delayWhileIdle(true).timeToLive(3)
								.addData("no", "1").addData("title", "MARO")
								.addData("msg", "GCM MARO TEST").build();
						try {
							Log.d(TAG, "send!");
							Result result = sender.send(message, REG_ID, 5);

							Log.v(TAG,
									"result : "
											+ result.getCanonicalRegistrationId());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		if (!GCMRegistrar.getRegistrationId(this).equals("")) {
			GCMRegistrar.unregister(this);
		}
		super.onDestroy();
	}

	private void checkNotNull(Object reference) {
		if (reference == null) {
			throw new NullPointerException(
					"Please check the value and recompile the app.");
		}
	}
}
