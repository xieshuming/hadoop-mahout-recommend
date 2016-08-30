package cn.com.in9;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MobileLogParseDriver
{
	private static final String MOBILE_INPUT_PATH = "hdfs://ns1/9inlog/";
	private static final String MOBILE_OUTPUT_PATH = "hdfs://ns1/9inoutput/";

	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.err.println("Usage:hadoop jar xxx.jar startdate enddate");
			System.exit(2);
		}
		Configuration conf = new Configuration();// 加载集群配置文件

		SimpleDateFormat sdf = null;
		Date startDate = null;
		Date endDate = null;
		List<Date> listDate = null;

		try
		{
			FileSystem filesystem = FileSystem.get(new URI(MOBILE_INPUT_PATH),
					conf);
			sdf = new SimpleDateFormat("yyyyMMdd");
			startDate = sdf.parse(args[0]);
			endDate = sdf.parse(args[1]);
			listDate = ListDatesUtil.findDates(startDate, endDate);

			for (Date d : listDate)
			{
				String dateToStr = sdf.format(d);

				Job job = Job.getInstance(conf, "MobileLogParse " + dateToStr);// 创建一个job实例,每个日期对应的job都是一个实例，注意了！
				job.setJarByClass(MobileLogParseDriver.class);

				job.setMapperClass(MobileLogParseMapper.class);
				job.setReducerClass(MobileLogParseReducer.class);

				job.setOutputKeyClass(Text.class);
				job.setOutputValueClass(Text.class);

				String sourcePath = MOBILE_INPUT_PATH + dateToStr;
				String desPath = MOBILE_OUTPUT_PATH + dateToStr;

				if (filesystem.exists(new Path(MOBILE_OUTPUT_PATH + dateToStr)))
				{
					filesystem.delete(new Path(MOBILE_OUTPUT_PATH + dateToStr),
							true);
				}

				Path inputPath = new Path(sourcePath);
				Path outputPath = new Path(desPath);
				FileInputFormat.setInputPaths(job, inputPath);
				FileOutputFormat.setOutputPath(job, outputPath);

				job.waitForCompletion(true);

			}

		} catch (ParseException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

}
