package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.ToastUtils;
import com.example.mobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddressActivity extends Activity {

	private EditText etNumber;
	private Button btnStart;
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);

		etNumber = (EditText) findViewById(R.id.et_number);
		btnStart = (Button) findViewById(R.id.btn_start);
		tvResult = (TextView) findViewById(R.id.tv_result);

		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String number = etNumber.getText().toString().trim();
				if (!TextUtils.isEmpty(number)) {
					String address = (String) AddressDao.getAddress(number);

					tvResult.setText(address);
				}else {
					ToastUtils.showToast(getApplicationContext(), "输入内容不能为空");
				}
			}
		});
	}
}
