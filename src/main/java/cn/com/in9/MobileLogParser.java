package cn.com.in9;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class MobileLogParser
{
	private static final String MOBILE_INPUT_PATH = "/log/native/mobile";
	private static final String MOBILE_OUTPUT_PATH = "/test/shuxie/native/mobile";

	public static void main(String[] args)
	{
		// 获取集群配置
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://ns1");
		FileSystem filesystem = null;
		// 日期处理
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date startDate = null;
		Date endDate = null;
		List<Date> listDate = null;
		try
		{
			startDate = sdf.parse(args[0]);
			endDate = sdf.parse(args[1]);

			filesystem = FileSystem.get(new URI(MOBILE_INPUT_PATH), conf);

			listDate = ListDatesUtil.findDates(startDate, endDate);

			for (Date d : listDate)
			{
				String dStr = sdf.format(d);
				String sourcePath = MOBILE_INPUT_PATH + dStr;
				if (filesystem.exists(new Path(MOBILE_OUTPUT_PATH + dStr)))
				{
					filesystem.delete(new Path(MOBILE_OUTPUT_PATH + dStr), true);
				}

				FSDataInputStream fsDataInputStream = filesystem.open(new Path(sourcePath));

			}

		} catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
