package com.example.mobilesafe.view;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbCheck;
	private String descOff;
	private String title;
	private String descOn;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		// int count = attrs.getAttributeCount();
		// for (int i = 0; i < count; i++) {
		// String attributeName = attrs.getAttributeName(i);
		// String attributeValue = attrs.getAttributeValue(i);
		// //System.out.println(attributeName + "=" + attributeValue);
		// }

		title = attrs.getAttributeValue(NAMESPACE, "title");
		descOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		descOn = attrs.getAttributeValue(NAMESPACE, "desc_on");

		setTite(title);
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		View child = View.inflate(getContext(), R.layout.setting_item_view, null);// 初始化组合控件布局

		tvTitle = (TextView) child.findViewById(R.id.tv_title);
		tvDesc = (TextView) child.findViewById(R.id.tv_desc);
		cbCheck = (CheckBox) child.findViewById(R.id.cb_check);

		this.addView(child);// 将布局添加给当前RelativeLayout对象
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTite(String title) {
		tvTitle.setText(title);
	}

	/**
	 * 设置描述
	 * 
	 * @param desc
	 */
	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}

	/**
	 * 判断是否勾选
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cbCheck.isChecked();
	}

	/**
	 * 设置选中状态
	 * 
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		cbCheck.setChecked(checked);
		// 更新描述信息
		if (checked) {
			setDesc(descOn);
		} else {
			setDesc(descOff);
		}
	}

}
