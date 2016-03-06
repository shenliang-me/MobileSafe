package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.PrefUtils;
import com.example.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 设置中心页面
 * 
 * @author Shen
 *
 */
public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;
	// private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		// sharedPreferences = getSharedPreferences("auto_update",
		// MODE_PRIVATE);

		initUpdate();
	}

	// 初始化自动更新设置
	private void initUpdate() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);

		// sivUpdate.setTite("自动更新设置");
		boolean autoUpdate = PrefUtils.getBoolean("config", true, this);
		// if (autoUpdate) {
		// sivUpdate.setDesc("自动更新设置已开启");
		// sivUpdate.setChecked(true);
		// } else {
		// sivUpdate.setDesc("自动更新设置已关闭");
		// sivUpdate.setChecked(false);
		// }

		sivUpdate.setChecked(autoUpdate);

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("自动更新设置已关闭");
					// sharedPreferences.edit().putBoolean("config",
					// false).commit();
					PrefUtils.putBoolean("config", false, getApplicationContext());
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("自动更新设置已开启");
					PrefUtils.putBoolean("config", true, getApplicationContext());
				}

			}
		});
	}
}
