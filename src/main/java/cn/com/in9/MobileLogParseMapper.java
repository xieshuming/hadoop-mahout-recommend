package cn.com.in9;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.json.JSONObject;

public class MobileLogParseMapper extends
		Mapper<LongWritable, Text, Text, Text>
{
	private MultipleOutputs mos;

	@Override
	public void map(LongWritable ikey, Text content, Context context)
			throws IOException, InterruptedException
	{
		String line = content.toString();
		String lineParse = URLDecoder.decode(line, "utf-8");

		Long uid2Long = null;
		Long itemid2Long = null;

		if (StringUtils.isBlank(lineParse))
		{
			return;
		}
		if (StringUtils.contains(lineParse, "launch"))
		{
			return;
		}
		if (StringUtils.contains(lineParse, "err"))
		{
			return;
		}
		lineParse = "{" + StringUtils.substringBetween(lineParse, "{", "}")
				+ "}";

		JSONObject jo = new JSONObject(lineParse);
		if (jo.isNull("uid") || jo.isNull("evt_para"))
		{
			return;
		}
		if ("0".equals(jo.getString("uid")))
		{
			return;
		}
		if (!jo.getString("evt_id").startsWith("Goods"))
		{
			return;
		}

		// uid2Long =
		// ConverTools.bytes2Long(jo.getString("uid").getBytes("utf-8"));

		// itemid2Long =
		// ConverTools.bytes2Long(jo.getString("evt_para").getBytes("utf-8"));

		String outStr = uid2Long.toString() + "," + itemid2Long.toString();
		context.write(new Text(outStr), new Text(""));

	}
}
