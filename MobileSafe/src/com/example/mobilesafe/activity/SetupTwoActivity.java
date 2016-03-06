package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.PrefUtils;
import com.example.mobilesafe.Utils.ToastUtils;
import com.example.mobilesafe.base.BaseSetupActivity;
import com.example.mobilesafe.view.SettingItemView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 设置向导2
 * 
 * @author Shen
 *
 */
public class SetupTwoActivity extends BaseSetupActivity {

	private SettingItemView sivBind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_two);

		sivBind = (SettingItemView) findViewById(R.id.siv_bind);
		String bindSim = PrefUtils.getString("bind_sim", null, this);
		if (TextUtils.isEmpty(bindSim)) {
			sivBind.setChecked(false);
		} else {
			sivBind.setChecked(true);
		}

		sivBind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivBind.isChecked()) {
					sivBind.setChecked(false);
					PrefUtils.remove("bind_sim", getApplicationContext());
					ToastUtils.showToast(getApplicationContext(), "sim卡已解绑");
				} else {
					sivBind.setChecked(true);
					// 保存sim卡信息
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();// 获取sim卡序列号，需要权限

					PrefUtils.putString("bind_sim", simSerialNumber, getApplicationContext());
					ToastUtils.showToast(getApplicationContext(), "sim卡已绑定");

				}

			}
		});

	}

	@Override
	public void showNext() {
		String bindSim = PrefUtils.getString("bind_sim", null, this);
		if (TextUtils.isEmpty(bindSim)) {
			// 强制绑定sim卡
			ToastUtils.showToast(getApplicationContext(), "请绑定sim卡");
			return;
		}

		startActivity(new Intent(this, SetupThreeActivity.class));
		finish();
		// 两个Activity之间的动画 应该放在finish之后运行
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

	}

	@Override
	public void showPrevious() {

		startActivity(new Intent(this, SetupOneActivity.class));
		finish();
		// 两个Activity之间的动画 应该放在finish之后运行
		overridePendingTransition(R.anim.anim__previous_in, R.anim.anim_previous_out);

	}
}
