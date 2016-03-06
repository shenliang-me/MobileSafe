package com.example.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.PrefUtils;
import com.example.mobilesafe.Utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 闪屏页面 展示logo 公司品牌检查版本更新 项目初始化 校验合法性(检查是否有网络, 检查是否登录)
 * 
 * @author Shen
 *
 */
public class SplashActivity extends Activity {

	private static final int CODE_UPDATE_DIALOG = 1;
	private static final int CODE_ENTER_HOME = 2;
	private static final int CODE_URL_ERROR = 3;
	private static final int CODE_NETWORK_ERROR = 4;
	private static final int CODE_JSON_ERROR = 5;
	private TextView tvName;

	private String mVersionName;
	private int mVersionCode;
	private String mDes;
	private String mUrl;
	private Message message;
	private TextView tvProcess;

	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (message.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(getApplicationContext(), "网络连接异常", 0).show();
				enterHome();
				break;
			case CODE_NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "网络异常", 0).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(getApplicationContext(), "数据解析异常", 0).show();
				enterHome();
				break;

			default:
				break;
			}
		};
	};
	private RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tvName = (TextView) findViewById(R.id.tv_name);
		tvName.setText("版本：" + getVersionName());

		tvProcess = (TextView) findViewById(R.id.tv_process);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

		// SharedPreferences sharedPreferences =
		// getSharedPreferences("auto_update", MODE_PRIVATE);
		boolean autoUpdate = PrefUtils.getBoolean("config", true, this);
		if (autoUpdate) {
			// 检查版本
			checkVersion();
		} else {

			new Handler().postDelayed(new Runnable() {// 延时两秒跳主页面
				public void run() {
					enterHome();
				}
			}, 2000);
		}

		// 闪屏页面渐变

		AlphaAnimation animation = new AlphaAnimation(0.2f, 1);
		animation.setDuration(2000);

		rlRoot.startAnimation(animation);

		copyDb("address.db");// 拷贝归属地数据库

	}

	/**
	 * 跳主页面
	 */
	protected void enterHome() {
		startActivity(new Intent(this, HomeActivity.class));
		finish();

	}

	/**
	 * 检查版本
	 */
	private void checkVersion() {
		new Thread() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				message = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					URL url = new URL("http://192.168.1.102:8080/update.json");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(2000);// 连接超时
					connection.setReadTimeout(2000);// 读取超时
					connection.connect();
					int responseCode = connection.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.StreatToString(inputStream);
						// 解析Json
						JSONObject jsonObject = new JSONObject(result);
						mVersionName = jsonObject.getString("versionName");
						mVersionCode = jsonObject.getInt("versionCode");
						mDes = jsonObject.getString("des");
						mUrl = jsonObject.getString("url");

						if (getVersionCode() < mVersionCode) {
							System.out.println("有更新");
							// showUpdateDialog();
							message.what = CODE_UPDATE_DIALOG;
						} else {
							System.out.println("无更新");
							message.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					// url错误
					e.printStackTrace();
					message.what = CODE_URL_ERROR;
				} catch (IOException e) {
					// 网络异常
					e.printStackTrace();
					message.what = CODE_NETWORK_ERROR;
				} catch (JSONException e) {
					// Json解析失败
					e.printStackTrace();
					message.what = CODE_JSON_ERROR;
				} finally {
					if (connection != null) {
						connection.disconnect(); // 关闭网络连接
					}
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;// 访问网络使用时间
					try {
						if (timeUsed < 2000) {
							Thread.sleep(2000 - timeUsed); // 强制等待一段时间，凑够两秒钟
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mhandler.sendMessage(message);
				}
			}
		}.start();

	}

	/**
	 * 升级弹窗
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);// 这里必须要一个Activity对象
		dialog.setTitle("发现新版本");
		dialog.setMessage(mDes);
		// dialog.setCancelable(false); // 点击返回键，弹窗不消失 不建议使用，用户体验不好
		dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				downloadAPK();
			}
		});
		dialog.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});

		// 监听用户取消弹窗的操作，比如返回键
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});

		dialog.show();

	}

	/**
	 * 下载APK包 权限
	 */
	protected void downloadAPK() {

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断SD卡是否存在
			tvProcess.setVisibility(View.VISIBLE); // 显示进度条文字
			String path = Environment.getExternalStorageDirectory() + "/MobileSafe.apk";
			// xUtils
			HttpUtils utils = new HttpUtils();
			utils.download(mUrl, path, new RequestCallBack<File>() {

				// 在主线程运行
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					// 下载进度
					int percent = (int) (100 * current / total);
					tvProcess.setText("下载进度：" + percent + "%");

				}

				// 在主线程运行
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					// 下载成功
					String p = responseInfo.result.getAbsolutePath();
					System.out.println("下载成功" + p);

					// 跳系统安装界面
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}

				// 在主线程运行
				@Override
				public void onFailure(HttpException error, String msg) {
					// 下载失败
					error.printStackTrace();
					Toast.makeText(getApplicationContext(), msg, 0).show();
				}
			});
		} else {
			Toast.makeText(getApplicationContext(), "未找到SD卡", 0).show();
		}
	}

	/**
	 * 获取版本名称
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();// 包管理器
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);// 根据包名获取相关信息
			String versionName = packageInfo.versionName;// 版本名称
			return versionName;
		} catch (NameNotFoundException e) {
			// 包名未找到异常
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 获取版本号
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();// 包管理器
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);// 根据包名获取相关信息
			// String versionName = packageInfo.versionName;// 版本名称
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// 包名未找到异常
			e.printStackTrace();
		}
		return -1;

	}

	// 用户取消安装，会回调此方法
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		enterHome();
	}

	/**
	 * 拷贝数据库
	 */
	private void copyDb(String dbName) {

		// 先判断文件是否存在
		File filesDir = getFilesDir();

		File targetFile = new File(filesDir, dbName);

		if (targetFile.exists()) {
			// 数据库已经存在，无需拷贝
			return;
		}

		FileOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			AssetManager assets = getAssets();
			inputStream = assets.open(dbName);

			outputStream = new FileOutputStream(targetFile);

			int len = 0;
			byte[] buffer = new byte[1024];

			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
