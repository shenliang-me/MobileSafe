package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.base.BaseSetupActivity;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 设置向导1
 * 
 * @author Shen
 *
 */
public class SetupOneActivity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_one);

	}

	@Override
	public void showNext() {
		startActivity(new Intent(this, SetupTwoActivity.class));
		finish();

		// 两个Activity之间的动画 应该放在finish之后运行
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

	}

	@Override
	public void showPrevious() {
		// TODO Auto-generated method stub

	}
}
