package com.example.mobilesafe.receiver;

import com.example.mobilesafe.Utils.PrefUtils;
import com.example.mobilesafe.Utils.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		String savedSim = PrefUtils.getString("bind_sim", null, context);
		boolean protect = PrefUtils.getBoolean("protect", false, context);
		if (!protect) {
			return;
		}
		if (!TextUtils.isEmpty(savedSim)) {

			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = tm.getSimSerialNumber() + "ddddddd";
			if (currentSim.equals(savedSim)) {
				System.out.println("手机安全");
			} else {
				System.out.println("sim已经改变，发送报警短信");
				String phone = PrefUtils.getString("safe_phone", "", context);
				SmsManager sm = SmsManager.getDefault();
				sm.sendTextMessage(phone, null, "SIM卡已经改变，请确保您的手机安全", null, null);
			}
		}
	}

}
