package cn.com.in9;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class SingleUserCFDemo
{

	public static void main(String[] args) throws Exception
	{
		// 1.封装数据模型
		DataModel model = new FileDataModel(new File("intro.csv"));
		// 2.用户相似度
		UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
		// 3.用户近邻选择
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(4, similarity, model);
		// 4.基于用户推荐
		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

		// 得到所有用户的ID
		LongPrimitiveIterator userIDs = model.getUserIDs();
		List<RecommendedItem> recommendations = null;
		// 4.为每一个用户做推荐
		while (userIDs.hasNext())
		{
			long userID = userIDs.nextLong();

			FastIDSet itemIDs = model.getItemIDsFromUser(userID);
			LongPrimitiveIterator itemIDfrommodels = itemIDs.iterator();

			System.out.println("用户" + userID + "浏览了:");

			while (itemIDfrommodels.hasNext())
			{
				long itemIDfrommodel = itemIDfrommodels.next();
				LineIterator it = FileUtils.lineIterator(new File("D:\\itemtest.csv"), "UTF-8");

				try
				{
					while (it.hasNext())
					{
						String line = it.nextLine();
						String itemID = line.split(",")[0];
						String itemname = line.split(",")[1];
						if (itemIDfrommodel == Long.parseLong(itemID))
						{
							System.out.println(itemname);
						}
					}
				} finally
				{
					LineIterator.closeQuietly(it);
				}
			}

			recommendations = recommender.recommend(userID, 5);// 为每个用户ID推荐排名前四的物品
			// System.out.print(userID + ":");
			System.out.println("推荐结果:" + "喜欢这些商品的还喜欢:");
			for (RecommendedItem r : recommendations)
			{
				// System.out.print("[" + r.getItemID() + ":" + r.getValue() +
				// "]");

				LineIterator it = FileUtils.lineIterator(new File("D:\\itemtest.csv"), "UTF-8");
				try
				{
					while (it.hasNext())
					{
						String line = it.nextLine();
						String itemID = line.split(",")[0];
						String itemname = line.split(",")[1];
						if (r.getItemID() == Long.parseLong(itemID))
						{
							System.out.println(itemname + ":" + "评分:" + r.getValue());
						}
					}
				} finally
				{
					LineIterator.closeQuietly(it);
				}
			}

			System.out.println();

		}

	}

}
