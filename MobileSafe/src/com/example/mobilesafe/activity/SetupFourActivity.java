package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.PrefUtils;
import com.example.mobilesafe.base.BaseSetupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 设置向导4
 * 
 * @author Shen
 *
 */
public class SetupFourActivity extends BaseSetupActivity {

	private CheckBox cbProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_four);

		cbProtect = (CheckBox) findViewById(R.id.cb_protect);

		boolean protect = PrefUtils.getBoolean("protect", false, getApplicationContext());
		if (protect) {
			cbProtect.setChecked(true);
			cbProtect.setText("防盗保护已经开启");
		} else {
			cbProtect.setChecked(false);
			cbProtect.setText("您没有开启防盗保护");
		}

		cbProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					cbProtect.setText("防盗保护已经开启");
					PrefUtils.putBoolean("protect", true, getApplicationContext());
				} else {
					cbProtect.setText("您没有开启防盗保护");
					PrefUtils.remove("protect", getApplicationContext());
				}

			}
		});

	}

	@Override
	public void showNext() {
		PrefUtils.putBoolean("configed", true, this);
		startActivity(new Intent(this, LostAndFindActivity.class));
		finish();
		
		//startActivity(new Intent(this, LockScreen.class));

	}

	@Override
	public void showPrevious() {
		startActivity(new Intent(this, SetupThreeActivity.class));
		finish();

		// 两个Activity之间的动画 应该放在finish之后运行
		overridePendingTransition(R.anim.anim__previous_in, R.anim.anim_previous_out);

	}
}
