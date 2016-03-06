package com.example.mobilesafe.service;

import com.example.mobilesafe.receiver.AdminReceiver;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class LockService extends Service {

	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// 设备策略管理器
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		// 初始化组件
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
		mDPM.lockNow();
	}

}
