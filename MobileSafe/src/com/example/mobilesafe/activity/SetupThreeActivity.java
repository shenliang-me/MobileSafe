package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.PrefUtils;
import com.example.mobilesafe.Utils.ToastUtils;
import com.example.mobilesafe.base.BaseSetupActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 设置向导3
 * 
 * @author Shen
 *
 */
public class SetupThreeActivity extends BaseSetupActivity {

	private EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_three);

		etPhone = (EditText) findViewById(R.id.et_phone);
		String phone = PrefUtils.getString("safe_phone", "", this);
		etPhone.setText(phone);
	}

	@Override
	public void showNext() {
		// 保存安全号码
		String phone = etPhone.getText().toString().trim();

		if (!TextUtils.isEmpty(phone)) {
			PrefUtils.putString("safe_phone", phone, this);

			startActivity(new Intent(this, SetupFourActivity.class));
			finish();

			// 两个Activity之间的动画 应该放在finish之后运行
			overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		} else {
			ToastUtils.showToast(this, "安全号码不能为空！");
		}

	}

	@Override
	public void showPrevious() {
		startActivity(new Intent(this, SetupTwoActivity.class));
		finish();

		// 两个Activity之间的动画 应该放在finish之后运行
		overridePendingTransition(R.anim.anim__previous_in, R.anim.anim_previous_out);

	}

	/**
	 * 选择联系人
	 * 
	 * @param view
	 */
	public void selectContact(View view) {
		startActivityForResult(new Intent(getApplicationContext(), ContactsActivity.class), 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {// 避免用户直接返回，导致空指针异常
			String phone = data.getStringExtra("phone");

			phone.replaceAll("-", "").replaceAll(" ", "");
			etPhone.setText(phone);
		}
	}
}
