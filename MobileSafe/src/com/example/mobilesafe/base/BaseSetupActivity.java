package com.example.mobilesafe.base;

import com.example.mobilesafe.R;
import com.example.mobilesafe.Utils.ToastUtils;
import com.example.mobilesafe.activity.SetupOneActivity;
import com.example.mobilesafe.activity.SetupThreeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 设置向导的基类
 * 
 * @author Shen
 *
 */
public abstract class BaseSetupActivity extends Activity {

	private GestureDetector mDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

			// 快速滑动
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

				if (Math.abs(e2.getRawY() - e1.getRawY()) > 200) {

					ToastUtils.showToast(getApplicationContext(), "不能这样滑动哦！");
					return true;
				}

				if (Math.abs(velocityX) < 200) {
					ToastUtils.showToast(getApplicationContext(), "滑动太慢了哦！");
					return true;
				}

				if (e2.getRawX() - e1.getRawX() > 200) {
					// 上一页
					showPrevious();
					return true;
				}
				if (e1.getRawX() - e2.getRawX() > 200) {
					// 下一页
					showNext();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});
	}

	/**
	 * 下一个页面
	 */
	public void next(View view) {
		showNext();
	}

	/**
	 * 显示上一个页面
	 */
	public abstract void showNext();

	/**
	 * 上一个页面
	 */
	public void previous(View view) {
		showPrevious();
	}

	/**
	 * 显示上一个页面
	 */
	public abstract void showPrevious();

	/**
	 * 当前界面被触摸时，走此方法
	 * 
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 将事件委托给手势识别器处理
		mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
