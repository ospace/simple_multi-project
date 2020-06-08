package com.tistory.ospace.core.util;

public class MjpgUtils {
	private static final byte[]  JPEG_BOI = { (byte)0xff, (byte)0xd8 };
	private static final byte[]  JPEG_EOI = { (byte)0xff, (byte)0xd9 };
	
	public static int findJpegBegin(byte[] data, int offset) {
		for(int i=offset, n=data.length-1; i<n; ++i) {
			if(isJpegBoi(data[i], data[i+1])) {
				return i;
			}
		}
		return -1;
	}
	
	public static int findJpegEnd(byte[] data, int offset) {
		for(int i=offset, n=data.length-1; i<n; ++i) {
			if(isJpegEoi(data[i], data[i+1])) {
				return i + 2;
			}
		}
		return -1;
	}
	
	public static boolean isJpegBoi(byte l, byte r) {
		return JPEG_BOI[0] == l && JPEG_BOI[1] == r;
	}
	
	public static boolean isJpegEoi(byte l, byte r) {
		return JPEG_EOI[0] == l && JPEG_EOI[1] == r;
	}
}
