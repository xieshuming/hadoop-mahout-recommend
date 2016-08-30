package cn.com.in9;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class SingleitemCFDemo
{

	public static void main(String[] args) throws IOException, TasteException
	{
		// 1. 创建数据模型，数据格式：long,long,double or long long double
		DataModel model = new FileDataModel(new File("item.csv"));// long,long,double

		// 2. 计算物品相似度.对比使用不同的相似度算法。
		ItemSimilarity isy = new EuclideanDistanceSimilarity(model);

		// 3.推荐 有评分
		Recommender recommender = new GenericItemBasedRecommender(model, isy);
		// 推荐 无评分
		// Recommender recommender1 = new
		// GenericBooleanPrefItemBasedRecommender(model, isy);
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

			recommendations = recommender.recommend(userID, 4);// 为每个用户ID推荐排名前四的物品
			// System.out.println("为用户 " + userID + "推荐" + ":");
			System.out.println("推荐结果:" + "看了又看:");
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

		/*
		 * List<RecommendedItem> recommendations = recommender.recommend(3,
		 * 3);// usrid,推荐个数
		 * 
		 * for (RecommendedItem r : recommendations) { System.out.println(r); }
		 */
	}
}
