package cn.com.in9;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;

public class IREvaluatorIntro
{

	public static void main(String[] args) throws Exception
	{
		RandomUtils.useTestSeed();
		final DataModel model = new FileDataModel(new File("item.csv"));
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder()
		{
			public Recommender buildRecommender(DataModel model) throws TasteException
			{
				/*
				 * UserSimilarity similarity = new
				 * PearsonCorrelationSimilarity(model); UserNeighborhood
				 * neighborhood = new NearestNUserNeighborhood(2, similarity,
				 * model); return new GenericUserBasedRecommender(model,
				 * neighborhood, similarity);
				 */
				ItemSimilarity isy = new EuclideanDistanceSimilarity(model);
				return new GenericItemBasedRecommender(model, isy);
			}

		};
		IRStatistics stats = evaluator.evaluate(recommenderBuilder, null, model, null, 2,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);

		System.out.println(stats.getPrecision());// 查准率，值为0.5，说明平均有一半的推荐结果是好的。
		System.out.println(stats.getRecall());// 查全率、召回率，值为1，说明所有好的推荐都包含在这些推荐结果中。
	}

}
