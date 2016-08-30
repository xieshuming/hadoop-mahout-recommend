package cn.com.in9;

import org.apache.hadoop.conf.Configuration;
import org.apache.mahout.cf.taste.hadoop.item.RecommenderJob;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.VectorSimilarityMeasures;

public class ItemCFhadoopDemo
{
	private static final String HDFSPATH = "hdfs://ns1";

	public static void main(String[] args) throws Exception
	{
		String inputpath = HDFSPATH + "/test/shuxie/native/mobile/ir.csv";
		String outputpath = HDFSPATH + "/test/shuxie/native/mobile/itemcfoutput";
		String temppath = HDFSPATH + "/test/shuxie/itemtemp" + System.currentTimeMillis();
		// 封装命令行参数
		StringBuffer sb = new StringBuffer();
		sb.append("--input ").append(inputpath);
		sb.append(" --output ").append(outputpath);
		sb.append(" --booleanData true");// true是无评分，默认false，有评分
		// <li>--booleanData (boolean):
		// Treat input data as having no
		// pref values (false) 如果输入数据不包含偏好数值，则将该参数设置为true，默认为false </li>
		sb.append(" --similarityClassname " + VectorSimilarityMeasures.SIMILARITY_EUCLIDEAN_DISTANCE);// 相似度算法选择
		sb.append(" --tempDir ").append(temppath);
		args = sb.toString().split(" ");

		Configuration conf = new Configuration();
		// jobconf.setJobName(ItemCFhadoopDemo.class.getName());
		// 封装整个推荐job
		RecommenderJob rjob = new RecommenderJob();
		rjob.setConf(conf);
		rjob.run(args);

	}

}
