package cn.com.in9;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.json.JSONObject;

public class FileTools
{

	public static void main(String[] args) throws Exception
	{
		LineIterator lineIterator = FileUtils.lineIterator(new File("D:\\9inlog"), "UTF-8");
		System.out.println("start....");
		BufferedWriter bw = null;
		OutputStreamWriter osw = null;

		while (lineIterator.hasNext())
		{
			String line = lineIterator.nextLine();

			String deline = URLDecoder.decode(line, "utf-8");

			if (StringUtils.isBlank(line))
			{
				continue;
			}
			if (StringUtils.contains(line, "launch"))
			{
				continue;
			}

			if (StringUtils.contains(line, "err"))
			{
				continue;
			}

			deline = "{" + StringUtils.substringBetween(deline, "{", "}") + "}";

			JSONObject jo = new JSONObject(deline);

			if (jo.isNull("uid"))
			{
				continue;
			}
			if (jo.isNull("evt_id") || !jo.getString("evt_id").startsWith("Goods"))
			{
				continue;
			}
			if (jo.isNull("evt_para"))
			{
				continue;
			}
			MemoryIDMigrator thing2long = new MemoryIDMigrator();
			Long uid = thing2long.toLongID(jo.getString("uid"));
			Long itemid = thing2long.toLongID(jo.getString("evt_para"));

			String uid1 = thing2long.toStringID(uid);
			String itemid1 = thing2long.toStringID(itemid);
			System.out.println(jo.getString("uid") + " " + uid + "  " + uid1 + "  " + jo.getString("evt_para") + " "
					+ itemid + "  " + itemid1);

		}
		System.out.println("end....");

	}
}
