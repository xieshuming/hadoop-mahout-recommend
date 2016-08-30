package cn.com.in9;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

public class SingleSVDDemo
{

	public static void main(String[] args) throws Exception
	{
		// 1.指定数据模型，使用评分数据集，格式 long::long::double
		DataModel dm = new FileDataModel(new File("ratings.dat"), "::");
		// 2.使用SVD奇异值分解推荐器，第二个参数是分解器，数值表示：2，分类数，0.05，正则化分解器特征，5，需要执行的训练步骤数
		Recommender recommender = new SVDRecommender(dm, new ALSWRFactorizer(dm, 2, 0.05, 5));

		// Recommender recommender = new SVDRecommender(dm, new
		// SVDPlusPlusFactorizer(dm, 0, 0));

		// Recommender recommender = new SVDRecommender(dm, new
		// ParallelSGDFactorizer(dm, 0, 0, 0));

		LongPrimitiveIterator userIDs = dm.getUserIDs();
		List<RecommendedItem> recommendations = null;

		while (userIDs.hasNext())
		{
			long userID = userIDs.nextLong();
			recommendations = recommender.recommend(userID, 10);// 为每个用户ID推荐排名前四的物品
			System.out.print(userID + ":");
			for (RecommendedItem r : recommendations)
			{
				// 得到的结果，按指定的分类数分好类，每类里面再根据评分排序，纵看，对比movies.bat,查看分类是否正确。
				System.out.print("[" + r.getItemID() + ":" + r.getValue() + "]");
			}

			System.out.println();

		}
	}

}
