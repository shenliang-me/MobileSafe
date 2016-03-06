package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 手机防盗主页面
 * 
 * @author Shen
 *
 */
public class LostAndFindActivity extends Activity {

	private TextView tvSafePhone;
	private ImageView ivLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean configed = PrefUtils.getBoolean("configed", false, this);
		if (!configed) {
			// 跳设置向导页面
			startActivity(new Intent(this, SetupOneActivity.class));
			finish();
		} else {
			setContentView(R.layout.activity_lost_and_find);

			tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			ivLock = (ImageView) findViewById(R.id.iv_lock);

			String phone = PrefUtils.getString("safe_phone", "", this);
			tvSafePhone.setText(phone);

			boolean protect = PrefUtils.getBoolean("protect", false, this);
			if (protect) {
				ivLock.setImageResource(R.drawable.lock);
			} else {
				ivLock.setImageResource(R.drawable.unlock);
			}
		}
	}

	/**
	 * 重新进入设置向导页面
	 * 
	 * @param view
	 */
	public void reSetup(View view) {
		startActivity(new Intent(this, SetupOneActivity.class));
		finish();
	}
}
