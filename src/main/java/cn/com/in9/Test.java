package cn.com.in9;


public class Test
{

	public static void main(String agrs[])
	{

		long aa = 8679093351360908877L;
		System.out.println(aa);

		System.out.println(new String(long2Bytes(aa)));

	}

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