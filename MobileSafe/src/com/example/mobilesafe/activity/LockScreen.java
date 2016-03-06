package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class LockScreen extends Activity {

	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_screen);

		// 设备策略管理器
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		// 初始化组件
		mDeviceAdminSample = new ComponentName(this, com.example.mobilesafe.receiver.AdminReceiver.class);

		// mDPM.resetPassword("123456", 0);// 重置密码, 如果要取消密码,传空串""
		// mDPM.lockNow();
		// finish();
	}

	public void lock(View view) {
		mDPM.lockNow();// 立即锁屏
	}
	
	public void lockScreen(){
		mDPM.lockNow();// 立即锁屏
	}

}
