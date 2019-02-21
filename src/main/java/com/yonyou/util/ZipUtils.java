package com.yonyou.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**字符串压缩与解压缩
 * @author XIANGJIAN
 * @date 创建时间：2017年3月10日
 * @version 1.0
 */
public class ZipUtils {

	/**
	 * 
	 * 使用gzip进行压缩
	 */
	public static String gzip(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return new sun.misc.BASE64Encoder().encode(out.toByteArray());
	}

	/**
	 *
	 * <p>
	 * Description:使用gzip进行解压缩
	 * </p>
	 * 
	 * @param compressedStr
	 * @return
	 */
	public static String gunzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = new sun.misc.BASE64Decoder()
					.decodeBuffer(compressedStr);
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return decompressed;
	}

	/**
	 * 使用zip进行压缩
	 * 
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String str) {
		if (str == null)
			return null;
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new sun.misc.BASE64Encoder()
					.encodeBuffer(compressed);
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = new sun.misc.BASE64Decoder()
					.decodeBuffer(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}
	
	
	public static void main(String[] args) {
		
		String str = "Wm5WdVkzUnBiMjRsTWpCaWRYUjBiMjVmZFc1aWFXNWtYMjl1WTJ4cFkyc29kQ2tsTjBJbE1qQWxNRGxwWmloelpXeGxZM1JOWlc1MVZtRnNkV1VvS1NrbE4wSWxNakJ5WlhSMWNtNGxNMElsTWpBbE4wUWxNMElsTWpBbE1EbDJZWElsTWpCaWRYUjBiMjVVYjJ0bGJpVXlNQ1V6UkNVeU1DVXlOQ2gwS1M1aGRIUnlLQ1V5TW1KMWRIUnZibFJ2YTJWdUpUSXlLU1V6UWlVeU1DVXdPWFpoY2lVeU1ITmxiR1ZqZEdWa0pUSXdKVE5FSlRJd1NsTlBUaTV3WVhKelpTaG5aWFJUWld4bFkzUnBiMjV6S0NrcEpUTkNKVEl3SlRBNWFXWW9jMlZzWldOMFpXUXViR1Z1WjNSb0pUSXdKVE5FSlRORUpUSXdNQ2tsTjBJbE1qQWxNRGtsTURsdlZHRmliR1V1YzJodmQwMXZaR0ZzS0NkdGIyUmhiQ2NsTWtNbE1qQWxNaklsUlRnbFFVWWxRamNsUlRnbE9EY2xRak1sUlRVbFFqQWxPVEVsUlRrbE9EQWxPRGtsUlRZbE9FSWxRVGtsUlRRbFFqZ2xPREFsUlRZbE9VUWxRVEVsUlRZbE9UVWxRakFsUlRZbE9FUWxRVVVsUlRnbFFUY2xRVE1sUlRjbFFrSWxPVEVsTWpJcEpUTkNKVEl3SlRBNUpUQTVjbVYwZFhKdUpUTkNKVEl3SlRBNUpUZEVKVEl3SlRBNWRtRnlKVEl3YldWemMyRm5aU1V5TUNVelJDVXlNSFJ5WVc1elZHOVRaWEoyWlhJb1ptbHVaRUoxYzFWeWJFSjVVSFZpYkdsalVHRnlZVzBvZENVeVF5Y25KVEpEWDJSaGRHRlRiM1Z5WTJWRGIyUmxLU1V5UTJkbGRGTmxiR1ZqZEdsdmJuTW9LU2tsTTBJbE1qQWxNRGx2VkdGaWJHVXVjMmh2ZDAxdlpHRnNLQ2R0YjJSaGJDY2xNa01sTWpCdFpYTnpZV2RsS1NVelFpVXlNQ1V3T1hGMVpYSjVWR0ZpYkdWQ2VVUmhkR0ZUYjNWeVkyVkRiMlJsS0hRbE1rTmZaR0YwWVZOdmRYSmpaVU52WkdVcEpUTkNKVEl3SlRkRUpUSXdKVEl3ZG1GeUpUSXdhWE5DZFhSMGIyNGxNakFsTTBSbVlXeHpaU1V6UWlVeU1HWjFibU4wYVc5dUpUSXdZblYwZEc5dVgySnBibVJmYjI1amJHbGpheWdwSlRkQ0pUSXdKVEE1YVdZb2MyVnNaV04wVFdWdWRWWmhiSFZsS0NrcEpUZENKVEl3Y21WMGRYSnVKVE5DSlRJd0pUZEVKVE5DSlRJd0pUQTVhWE5DZFhSMGIyNGxNakFsTTBSMGNuVmxKVE5DSlRJd0pUQTVZMmhsWTJ0U1pXWmxjbVZ1WTJVb0oxSkZSaWhTVFY5Q1ZWUlVUMDRsTWtOQ1ZWUlVUMDVmVGtGTlJTVXpRVk5GUVZKRFNDMU1TVXRGTFVKVlZGUlBUbDlPUVUxRkpUSkRNQ2tuS1NVelFpVXlNQ1UzUkNVeU1DVXlNR1oxYm1OMGFXOXVKVEl3Y21WbVgzZHlhWFJsWDJwemIyNG9jbVZxYzI5dVFYSnlZWGtwSlRkQ0pUSXdKVEE1YVdZb2NtVnFjMjl1UVhKeVlYa3ViR1Z1WjNSb0pUSXdKVE5FSlRORUpUSXdNQ2tsTjBJbE1qQWxNRGtsTURsdlZHRmliR1V1YzJodmQwMXZaR0ZzS0NkdGIyUmhiQ2NsTWtNbE1qQWxNaklsUlRnbFFVWWxRamNsUlRnbE9EY2xRak1sUlRVbFFqQWxPVEVsUlRrbE9EQWxPRGtsUlRZbE9FSWxRVGtsUlRRbFFqZ2xPREFsUlRZbE9VUWxRVEVsUlRZbE9UVWxRakFsUlRZbE9FUWxRVVVsUlRnbFFrWWxPVUlsUlRnbFFURWxPRU1sUlRjbFFrSWxPVEVsUlRVbFFVVWxPVUVsTWpJcEpUTkNKVEl3SlRBNUpUQTVjbVYwZFhKdUpUTkNKVEl3SlRBNUpUZEVKVEl3SlRBNWFXWW9hWE5DZFhSMGIyNHBKVGRDSlRJd0pUQTVKVEE1YVhOQ2RYUjBiMjRsTTBSbVlXeHpaU1V6UWlVeU1DVXdPU1V3T1haaGNpVXlNR0oxZEhSdmJsOXBaSE1sTWpBbE0wUWxNakFuSnlVelFpVXlNQ1V3T1NVd09YWmhjaVV5TUdKMWRIUnZibFJ2YTJWdUpUSXdKVE5FSlRJd0pUSXlZblYwWDNKbGJGOXRaVzUxWDJKcGJtUWxNaklsTTBJbE1qQWxNRGtsTURsMllYSWxNakJ0Wlc1MVgybGtKVEl3SlRORUpUSXdKVEkwS0NVeU1pVXlNMDFGVGxWZlNVUWxNaklwTG5aaGJDZ3BKVEl3SlRBNUpUQTVabTl5S0haaGNpVXlNR2tsTWpBbE0wUWxNakF3SlROQ2FTVXpReVV5TUhKbGFuTnZia0Z5Y21GNUxteGxibWQwYUNVeU1DVXpRaVV5TUdrbE1rSWxNa0lwSlRkQ0pUSXdKVEE1SlRBNUpUQTVZblYwZEc5dVgybGtjeVV5TUNVeVFpVXpSQ1V5TUhKbGFuTnZia0Z5Y21GNUpUVkNhU1UxUkNVMVFpZEpSQ2NsTlVRbE1rSW5KVEpESnlVelFpVXlNQ1V3T1NVd09TVTNSQ1V5TUNVd09TVXdPV0oxZEhSdmJsOXBaSE1sTWpBbE0wUWxNakJpZFhSMGIyNWZhV1J6TG5OMVluTjBjbWx1Wnlnd0pUSkRZblYwZEc5dVgybGtjeTVzWlc1bmRHZ3RNU2tsTTBJbE1qQWxNRGtsTURsMllYSWxNakJpYVc1a1ZYSnNKVEl3SlRORUpUSXdZMjl1ZEdWNGRDVXlRaWNsTWtaaWRYUjBiMjVDWVhObEpUTkdZMjFrSlRORVluVjBkRzl1SlRJMmJXVnVkVjlwWkNVelJDY2xNa0p0Wlc1MVgybGtKVEpDSnlVeU5tSjFkSFJ2Ymw5cFpITWxNMFFuSlRKQ1luVjBkRzl1WDJsa2N5VXlRaWNsTWpaaWRYUjBiMjVVYjJ0bGJpVXpSQ2NsTWtKaWRYUjBiMjVVYjJ0bGJpVXpRaVV5TUNVd09TVXdPVzlVWVdKc1pTNXphRzkzVFc5a1lXd29KMjF2WkdGc0p5VXlReVV5TUhSeVlXNXpWRzlUWlhKMlpYSW9ZbWx1WkZWeWJDVXlRMHBUVDA0dWMzUnlhVzVuYVdaNUtISmxhbk52YmtGeWNtRjVLU2twSlROQ0pUSXdKVEE1SlRkRVpXeHpaU1UzUWlVeU1DVXdPU1V3T1hKbGRIVnliaVV5TUhSeWRXVWxNMElsTWpBbE1Ea2xOMFFsTWpBbE4wUWxNakFsTWpCbWRXNWpkR2x2YmlVeU1ISmxabDlsYm1Rb0tTVTNRaVV5TUNVd09TVXlOQ2dsTWpJbE1qTnhkV1Z5ZVY5aWRYUjBiMjUwYjJ0bGJpVXlNaWt1ZG1Gc0tDVXlNbkYxWlhKNUpUSXlLU1V6UWlVeU1DVXdPVzlVWVdKc1pTNXhkV1Z5ZVZSaFlteGxLQ1V5TkhSaFlteGxKVEpESlRJd1ptbHVaRUoxYzFWeWJFSjVRblYwZEc5dVZHOXVhMlZ1S0NVeU1uRjFaWEo1SlRJeUpUSkRKeWNsTWtOZlpHRjBZVk52ZFhKalpVTnZaR1VwS1NVelFpVXlNQ1UzUkNVeU1DVXlNR1oxYm1OMGFXOXVKVEl3YzJGMlpTaDBLU1UzUWlVeU1DVXdPWFpoY2lVeU1HMWxjM05oWjJVbE1qQWxNMFFsTWpJbE1qSWxNMElsTWpBbE1EbDJZWElsTWpCaWRYUjBiMjVVYjJ0bGJpVXlNQ1V6UkNVeU5DZ2xNaklsTWpOcGJuTmZiM0pmZFhCZlluVjBkRzl1ZEc5clpXNGxNaklwTG5aaGJDZ3BKVE5DSlRJd0pUQTVhV1lvSVc1dmRFNTFiR3gyWlhJb0tTa2xOMElsTWpBbE1Ea2xNRGx5WlhSMWNtNGxNakFsTTBJbE1qQWxNakFsTURrbE4wUWxNakFsTURsdFpYTnpZV2RsSlRJd0pUTkVKVEl3ZEhKaGJuTlViMU5sY25abGNpaG1hVzVrUW5WelZYSnNRbmxDZFhSMGIyNVViMjVyWlc0b1luVjBkRzl1Vkc5clpXNGxNa01uSnlVeVExOWtZWFJoVTI5MWNtTmxRMjlrWlNrbE1rTm5aWFJLYzI5dUtDVXlOQ2dsTWpJbE1qTmlkV3hwWkZCaFoyVWxNakFsTTBGdWIzUW9hVzV3ZFhRbE5VSnBaQ1V6UkNkSlJDY2xOVVFwSlRJeUtTa3BKVE5DSlRJd0pUQTViMVJoWW14bExuTm9iM2ROYjJSaGJDZ25iVzlrWVd3bkpUSkRKVEl3YldWemMyRm5aU2tsTTBJbE1qQWxNRGxpWVdOcktIUXBKVE5DSlRJd0pUQTVjWFZsY25sVVlXSnNaVUo1UkdGMFlWTnZkWEpqWlVOdlpHVW9kQ1V5UTE5a1lYUmhVMjkxY21ObFEyOWtaU2tsTTBJbE1qQWxOMFElM0Q";
		System.out.println("-------gzip:"+gzip(str));
		System.out.println("-------gunzi:p"+gunzip(gzip(str)));
		System.out.println("-------str:"+str.length());
		System.out.println("-------gzip:"+gzip(str).length());
	}
	
	
}