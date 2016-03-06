package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 归属地查询
 * 
 * @author Shen
 *
 */
public class AddressDao {

	private static final String PATH = "data/data/com.example.mobilesafe/files/address.db";

	/**
	 * 根据电话号码返回归属地 使用之前必须在闪屏页拷贝数据库
	 * 
	 * @param number
	 */
	public static String getAddress(String number) {
		String address = "未知号码";

		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);// 只支持从data/data目录下打开，不能从assets目录打开

		// 正则表达式匹配手机号码
		if (number.matches("^1[3-8]\\d{9}$")) {
			Cursor cursor = database.rawQuery(
					"select location from data2 where id = (select outkey from data1 where id = ?)",
					new String[] { number.substring(0, 7) });

			if (cursor.moveToFirst()) {
				address = cursor.getString(0);
			}
			cursor.close();
		}

		database.close();
		return address;
	}
}
