package com.example.mobilesafe.activity;

import org.w3c.dom.Text;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.MD5Utils;
import com.example.mobilesafe.Utils.PrefUtils;
import com.example.mobilesafe.Utils.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主页面
 * 
 * @author Shen
 *
 */
public class HomeActivity extends Activity {

	private GridView gvHome;

	private String[] mHomeNames = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具",
			"设置中心" };

	private int[] mImageIds = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
			R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings, };

	private String savedPassWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());

		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {

				case 0:
					showSafeDialog();
					break;
				case 8:
					startActivity(new Intent(getApplicationContext(), SettingActivity.class));
					break;
				case 7:
					startActivity(new Intent(getApplicationContext(), AToolsActivity.class));
					break;

				default:
					break;
				}
			}
		});

	}

	/**
	 * 手机防盗弹窗
	 */
	protected void showSafeDialog() {

		savedPassWord = PrefUtils.getString("password", null, getApplicationContext());
		if (TextUtils.isEmpty(savedPassWord)) {
			showSetPWDialog();
		} else {
			showInputDialog();
		}

	}

	/**
	 * 弹输入密码框
	 */
	private void showInputDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(getApplicationContext(), R.layout.diaolog_input_pwd, null);
		dialog.setView(view, 0, 0, 0, 0);// 设置弹窗边距为0

		final EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);

		Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);

		dialog.show();

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String Pwd = etPwd.getText().toString().trim();

				if (!TextUtils.isEmpty(Pwd)) {

					if (MD5Utils.encoding(Pwd).equals(savedPassWord)) {
						// 密码正确
						dialog.dismiss();
						// 跳手机防盗页面
						startActivity(new Intent(getApplicationContext(), LostAndFindActivity.class));
					} else {
						// 密码错误
						ToastUtils.showToast(getApplicationContext(), "密码错误");
						etPwd.setText("");
					}

				} else {
					ToastUtils.showToast(getApplicationContext(), "输入内容不能为空");
				}

			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

	}

	/**
	 * 弹设置密码弹窗
	 */
	private void showSetPWDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(getApplicationContext(), R.layout.diaolog_set_pwd, null);
		dialog.setView(view, 0, 0, 0, 0);// 设置弹窗边距为0

		final EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
		final EditText etPwdConfirm = (EditText) view.findViewById(R.id.et_pwd_confirm);

		Button btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		Button btnOk = (Button) view.findViewById(R.id.btn_ok);

		dialog.show();

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String Pwd = etPwd.getText().toString().trim();
				String PwdConfirm = etPwdConfirm.getText().toString().trim();

				if (!TextUtils.isEmpty(Pwd) && !TextUtils.isEmpty(PwdConfirm)) {

					if (Pwd.equals(PwdConfirm)) {
						PrefUtils.putString("password", MD5Utils.encoding(PwdConfirm), getApplicationContext());
						dialog.dismiss();
						startActivity(new Intent(getApplicationContext(), LostAndFindActivity.class));
					} else {
						ToastUtils.showToast(getApplicationContext(), "两次输入密码不相同");
						etPwd.setText("");
						etPwdConfirm.setText("");
					}

				} else {
					ToastUtils.showToast(getApplicationContext(), "输入内容不能为空");
				}

			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mHomeNames.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.list_item_home, null);

			ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tvName = (TextView) view.findViewById(R.id.tv_name);

			ivIcon.setImageResource(mImageIds[position]);
			tvName.setText(mHomeNames[position]);

			return view;
		}

	}

}
