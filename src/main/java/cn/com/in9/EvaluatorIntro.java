package cn.com.in9;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;

public class EvaluatorIntro
{

	public static void main(String[] args) throws Exception
	{
		// 强制每次选择相同的随机量(测试用)
		RandomUtils.useTestSeed();
		final DataModel model = new FileDataModel(new File("item.csv"));
		// 1. 创建推荐评估器
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		// 2. 基于给定的model builder出一个推荐器
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder()
		{
			public Recommender buildRecommender(DataModel model) throws TasteException
			{
				// 跟推荐程序一样
				/*
				 * UserSimilarity similarity = new
				 * PearsonCorrelationSimilarity(model); UserNeighborhood
				 * neighorhood = new NearestNUserNeighborhood(2, similarity,
				 * model); return new GenericUserBasedRecommender(model,
				 * neighorhood, similarity);
				 */
				ItemSimilarity isy = new EuclideanDistanceSimilarity(model);
				return new GenericItemBasedRecommender(model, isy);
			}

		};
		// 3.
		// 用推荐评估器评估推荐器。第二个参数是datamodelbuilder对象，默认空即可。1.0表示用来控制总共使用多少输入数据。这里指的是100%的数据。0.7表示训练70%的数据，测试30%
		double score = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
		// 评价的结果socre，是一个显示recommender表现如何的分数。表示平均而言推荐程序所给出的估计值与实际值的偏差
		System.out.println(score);// 值为0.8，评分在1~5分之间。说明推荐程序给出的估计值比较接近实际值，偏差在0.8

	}
}
