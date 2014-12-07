import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public class Recommender {


	public boolean itemBased(String inputData, String unrated, String rated){
		System.out.println("Starting Item Based...");

		Long time = System.currentTimeMillis();


		try{
			DataModel model = new FileDataModel(new File(inputData));
 			ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
			GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);
			CachingRecommender cRecommender = new CachingRecommender(recommender);

			HashMap<Integer, ArrayList<Integer>> input = readInData(unrated);

			if(input == null){
				return false;
			}

			int count = 0;

			PrintWriter writer = new PrintWriter(rated, "UTF-8");
			for(int user : input.keySet()){


				for (int item : input.get(user)) {

					count++;
					try {

						float result = cRecommender.estimatePreference(user, item);

						System.out.println(count + " :: " + user + "," + item + "," + result);
						writer.println(user + "," + item + "," + result);

					}catch(NoSuchUserException nsu){
						nsu.printStackTrace();
						writer.println(user + "," + item + ",0");
						//Keep On Rolling!
					}
				}
			}

		}catch(Exception e){
			return false;
		}

		System.out.println("Item Based finished!");
		System.out.println("That took " + (System.currentTimeMillis() - time) / 1000 + " seconds, or " + ((System.currentTimeMillis() - time) / 1000)/60.0 + " minutes!");
		return true;
	}

	public boolean userBasedNeighborhood(String inputData, String unrated, String rated, int neighbourhood){

		System.out.println("Starting User Based - Neighborhood...");

		Long time = System.currentTimeMillis();


		try{
			DataModel model = new FileDataModel(new File(inputData));
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighbourhood, similarity, model);
			GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			CachingRecommender cRecommender = new CachingRecommender(recommender);

			HashMap<Integer, ArrayList<Integer>> input = readInData(unrated);

			if(input == null){
				return false;
			}

			int count = 0;

			PrintWriter writer = new PrintWriter(rated, "UTF-8");
			for(int user : input.keySet()){


				for (int item : input.get(user)) {

					count++;
					try {

						float result = cRecommender.estimatePreference(user, item);

						System.out.println(count + " :: " + user + "," + item + "," + result);
						writer.println(user + "," + item + "," + result);

					}catch(NoSuchUserException nsu){
						nsu.printStackTrace();
						writer.println(user + "," + item + ",0");
						//Keep On Rolling!
					}
				}
			}

		}catch(Exception e){
			return false;
		}
		System.out.println("User Based - Neightborhood finished!");
		System.out.println("That took " + (System.currentTimeMillis() - time) / 1000 + " seconds, or " + ((System.currentTimeMillis() - time) / 1000)/60.0 + " minutes!");
		return true;
	}

	public boolean userBasedThreshold(String inputData, String unrated, String rated, int threshold){
		System.out.println("Starting User Based - Threshold...");

		Long time = System.currentTimeMillis();

		try{
			DataModel model = new FileDataModel(new File(inputData));
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			ThresholdUserNeighborhood neighborhood = new ThresholdUserNeighborhood(threshold, similarity, model);
			GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			CachingRecommender cRecommender = new CachingRecommender(recommender);

			HashMap<Integer, ArrayList<Integer>> input = readInData(unrated);

			if(input == null){
				return false;
			}

			int count = 0;

			PrintWriter writer = new PrintWriter(rated, "UTF-8");
			for(int user : input.keySet()){


				for (int item : input.get(user)) {

					count++;
					try {

						float result = cRecommender.estimatePreference(user, item);

						System.out.println(count + " :: " + user + "," + item + "," + result);
						writer.println(user + "," + item + "," + result);

					}catch(NoSuchUserException nsu){
						nsu.printStackTrace();
						writer.println(user + "," + item + ",0");
						//Keep On Rolling!
					}
				}
			}

		}catch(Exception e){
			return false;
		}

		System.out.println("User Based - Threshold finished!");
		System.out.println("That took " + (System.currentTimeMillis() - time) / 1000 + " seconds, or " + ((System.currentTimeMillis() - time) / 1000)/60.0 + " minutes!");
		return true;
	}


	/*
	* Helper method
	* No need to call directly
	* @param file -  string reference to file location to read in
	* @return HashMap of data read from file
	* */
	private HashMap readInData(String file){

		HashMap<Integer, ArrayList<Integer>> input = new HashMap<>();

		String fileLocation = file;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int count = 1;


		try {

			br = new BufferedReader(new FileReader(fileLocation));
			while ((line = br.readLine()) != null) {



//                Read in data
				String[] entryData = line.split(cvsSplitBy);

				if(input.keySet().contains(Integer.parseInt(entryData[0]))){
					ArrayList result = input.get(Integer.parseInt(entryData[0]));
					result.add(Integer.parseInt(entryData[1]));
				}else{
					ArrayList<Integer> result = new ArrayList<>();
					result.add(Integer.parseInt(entryData[1]));
					input.put(Integer.parseInt(entryData[0]), result);
				}
				if(count % 10000 == 0){
					System.out.println("Input Count :: " + count);
				}
				count++;



			}

			return input;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}


	public static void main(String[] args){

		Recommender rec = new Recommender();

	}
}