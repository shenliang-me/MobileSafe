package com.example.mobilesafe.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

public class PrefUtils {

	public static void putBoolean(String key, boolean value, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
		preferences.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(String key, boolean defValue, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return preferences.getBoolean(key, defValue);
	}

	public static void putInt(String key, int value, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
		preferences.edit().putInt(key, value).commit();
	}

	public static int getInt(String key, int defValue, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return preferences.getInt(key, defValue);
	}

	public static void putString(String key, String value, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
		preferences.edit().putString(key, value).commit();
	}

	public static String getString(String key, String defValue, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
		return preferences.getString(key, defValue);
	}

	public static void remove(String key, Context context) {
		SharedPreferences preferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
		preferences.edit().remove(key).commit();
	}

}
