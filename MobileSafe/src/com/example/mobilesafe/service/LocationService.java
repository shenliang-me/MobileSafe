package com.example.mobilesafe.service;

import com.example.mobilesafe.Utils.PrefUtils;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;

public class LocationService extends Service {

	private LocationManager mLM;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mLM = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = mLM.getBestProvider(criteria, true);
		listener = new MyListener();
		mLM.requestLocationUpdates(bestProvider, 0, 0, listener);

	}

	class MyListener implements LocationListener {

		// 位置发生变化
		@Override
		public void onLocationChanged(Location location) {
			String j = "经度:" + location.getLongitude();
			String w = "纬度" + location.getLatitude();
			String accuracy = "精确度:" + location.getAccuracy();
			String altitude = "海拔:" + location.getAltitude();

			String result = j + "\n" + w + "\n" + accuracy + "\n" + altitude;

			System.out.println("hahhahahahahah");
			SmsManager sm = SmsManager.getDefault();
			String phone = PrefUtils.getString("safe_phone", "", getApplicationContext());
			sm.sendTextMessage(phone, null, result, null, null);

		}

		// 状态发生变化
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("onStatusChanged");
		}

		// 用户打开GPS
		@Override
		public void onProviderEnabled(String provider) {
			System.out.println("onProviderEnabled");
		}

		// 用户关闭GPS
		@Override
		public void onProviderDisabled(String provider) {
			System.out.println("onProviderDisabled");
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 停止位置监听
		mLM.removeUpdates(listener);
		listener = null;
	}

}
