package cn.com.in9;

public class ConverTools
{
	public static byte[] long2Bytes(long num)
	{
		byte[] byteNum = new byte[8];
		for (int ix = 0; ix < byteNum.length; ++ix)
		{
			int offset = 64 - (ix + 1) * 8;
			byteNum[ix] = (byte) ((num >> offset) & 0xff);
		}
		return byteNum;
	}

	public static long bytes2Long(byte[] byteNum)
	{
		long num = 0;
		for (int ix = 0; ix < 8; ++ix)
		{
			num <<= 8;
			num |= (byteNum[ix] & 0xff);
		}
		return num;
	}
}
