package cn.com.in9;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MobileLogParseReducer extends Reducer<Text, Text, Text, Text>
{

	public void reduce(Text _key, Iterable<Text> values, Context context) throws IOException, InterruptedException
	{

		// key去重后直接输出就可以了。如果还是保持for这样默认输出，数据会像map那样原样输出的，
		// 虽然key去重了但是v2s还是for输出key v key v
		// 注意：map端只是输出k2v2,reduce前还有每个map的洗牌。比如map输出
		// word1,1，word1,1,洗牌后才是word1{1,1}，作为reduce的输入，如果reduce原样for输出，输出还是
		// word1,1，word1,1
		context.write(_key, new Text(""));

	}

}
