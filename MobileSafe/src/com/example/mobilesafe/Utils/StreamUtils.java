package com.example.mobilesafe.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	public static String StreatToString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		int len = 0;
		byte[] buffer = new byte[1024];

		while ((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		inputStream.close();
		outputStream.close();

		return outputStream.toString();

	}
}
