package cn.com.in9;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

public class ClusterHadoopDemo
{
	private static final String INPUTTEXTPATH = "hdfs://ns1/user/root/短外套";
	private static final String OUTPUTSEQPATH = "hdfs://ns1/user/root/seq-out";
	private static final String INPUTSEQPATH = "hdfs://ns1/user/root/seq-out";
	private static final String OUTPUTVECPATH = "hdfs://ns1/user/root/vec-out";
	private static final String CANOPYOUTDIR = "hdfs://ns1/user/root/canopy-out";
	private static final String FINALOUTDIR = "hdfs://ns1/user/root/final-out";

	public static void main(String[] args) throws Exception, Exception
	{
		Configuration conf = new Configuration();
		// 1.文本文件转成序列化文件
		// transformToSeqFile(INPUTTEXTPATH, OUTPUTSEQPATH);
		// 结果查看：hadoop fs -text hdfs路径 或者 mahout seqdumper -i
		// hdfs路径(加不加hdfs://ns1无所谓)
		// 2.序列化文件转成向量文件
		// transformToVector(INPUTSEQPATH, OUTPUTVECPATH);
		/*
		 * 结果查看：向量文件用mahout vectordump -i hdfs路径
		 */

		KMeanwithCanopy(conf, OUTPUTVECPATH + "/tfidf-vectors", CANOPYOUTDIR, FINALOUTDIR);
	}

	// 1.文本文件转成序列化文件
	public static void transformToSeqFile(String inputtextpath, String outputseqpath)
	{
		// SequenceFilesFromDirectory 实现将某个文件目录下的所有文件写入一个 SequenceFiles 的功能
		// 它其实本身是一个工具类，可以直接用命令行调用，这里直接调用了它的 main 方法
		String[] params =
		{ "-c", "UTF-8", "-i", inputtextpath, "-o", outputseqpath };
		// 解释一下参数的意义：
		// -c: 指定文件的编码形式，这里用的是"UTF-8"
		// -i: 指定输入的文件目录，这里指到我们刚刚导出文件的目录
		// -o: 指定输出的文件目录
		// -xm 指定单机还是MR 默认MR运行 sequential是单机顺序执行

		try
		{
			SequenceFilesFromDirectory.main(params);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// 2.序列化文件转成向量文件
	public static void transformToVector(String inputseqpath, String outputvecpath)
	{
		// SparseVectorsFromSequenceFiles 实现将 SequenceFiles 中的数据进行向量化。
		// 它其实本身是一个工具类，可以直接用命令行调用，这里直接调用了它的 main 方法
		String[] args =
		{ "-i", inputseqpath, "-o", outputvecpath, "-a", "org.wltea.analyzer.lucene.IKAnalyzer", "-chunk", "128",
				"-wt", "tfidf", "-s", "5", "-md", "3", "-x", "90", "-ng", "2", "-ml", "50", "-seq" };
		// 解释一下参数的意义：
		// -i: 指定输入的文件目录，这里指到我们刚刚生成 SequenceFiles 的目录
		// -o: 指定输出的文件目录
		// -a: 指定使用的 Analyzer，这里用的是 lucene 的空格分词的 Analyzer
		// -chunk: 指定 Chunk 的大小，单位是 M。对于大的文件集合，我们不能一次 load 所有文件，所以需要
		// 对数据进行切块
		// -wt: 指定分析时采用的计算权重的模式，这里选了 tfidf
		// -s: 指定词语在整个文本集合出现的最低频度，低于这个频度的词汇将被丢掉
		// -md: 指定词语在多少不同的文本中出现的最低值，低于这个值的词汇将被丢掉
		// -x: 指定高频词汇和无意义词汇（例如 is，a，the 等）的出现频率上限，高于上限的将被丢掉
		// -ng: 指定分词后考虑词汇的最大长度，例如 1-gram 就是，coca，cola，这是两个词，
		// 2-gram 时，coca cola 是一个词汇，2-gram 比 1-gram 在一定情况下分析的更准确。
		// -ml: 指定判断相邻词语是不是属于一个词汇的相似度阈值，当选择 >1-gram 时才有用，其实计算的是
		// Minimum Log Likelihood Ratio 的阈值
		// -seq: 指定生成的向量是 SequentialAccessSparseVectors，没设置时默认生成还是
		// RandomAccessSparseVectors

		try
		{
			SparseVectorsFromSequenceFiles.main(args);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void KMeanwithCanopy(Configuration conf, String canopyinput, String canopyout, String finaloutdir)
			throws Exception, IOException
	{
		HadoopUtil.delete(conf, new Path(canopyout));
		HadoopUtil.delete(conf, new Path(finaloutdir));

		CanopyDriver.run(conf, new Path(canopyinput), new Path(canopyout), new CosineDistanceMeasure(), 250, 120,
				false, 0, false);

		KMeansDriver.run(conf, new Path(canopyinput), new Path(canopyout, "clusters-0-final"), new Path(finaloutdir),
				0.01, 10, true, 0, false);
	}

}
