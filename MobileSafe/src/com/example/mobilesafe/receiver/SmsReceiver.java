package com.example.mobilesafe.receiver;

import com.example.mobilesafe.R;
import com.example.mobilesafe.activity.LockScreen;
import com.example.mobilesafe.service.LocationService;
import com.example.mobilesafe.service.LockService;
import com.example.mobilesafe.service.wipeService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] object = (Object[]) intent.getExtras().get("pdus");

		for (Object obj : object) {// 短信超过140个字节，会分多条短信发送
			SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);

			String originatingAddress = message.getOriginatingAddress();
			String messageBody = message.getMessageBody();
			System.out.println(messageBody);
			if ("#*alarm*#".equals(messageBody)) {
				// 播放报警音乐

				// 播放媒体音量与手机音量无关
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);// 设置音乐音量，基于系统音量的比例
				player.setLooping(true);// 重复播放
				player.start();// 开始播放

				// 4.4+版本上，无法拦截短信，调此方法没用，必须把当前应用设置成默认短信
				// 操作短信数据库，删除数据库相关短信内容，间接达到删除短信目的
				abortBroadcast();// 中断短信传递
			} else if ("#*location*#".equals(messageBody)) {
				System.out.println("手机定位");
				context.startService(new Intent(context, LocationService.class));
				System.out.println("1651656516机定位");
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(messageBody)) {
				// 一键锁屏
				context.startService(new Intent(context, LockService.class));
			} else if ("#*wipedata*#".equals(messageBody)) {
				// 清空数据
				
				context.startService(new Intent(context, wipeService.class));
			}
		}

	}

}
