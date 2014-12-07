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

//	private DataModel model;
//	private ItemSimilarity itemSimilarity;
//	private UserSimilarity userSimilarity;
//	private UserNeighborhood neighborhood;
//	private GenericUserBasedRecommender recommender;
//	private GenericItemBasedRecommender itemRecommender;
//	private CachingRecommender cachingRecommender;
//	private HashMap<Integer, ArrayList<Integer>> input;

	public void init(){
		System.out.println("INITIALISING...");
		try {

//			System.out.println("Pre-Model");
//			model = new FileDataModel(new File("C:\\Users\\George\\Uni\\COMP3208\\TrainingSetData\\subset-data-input.csv"));
//			System.out.println("Model Done");
//			//itemSimilarity = new PearsonCorrelationSimilarity(model);
//			userSimilarity = new PearsonCorrelationSimilarity(model);
//			System.out.println("User Sim Done");
//			neighborhood = new NearestNUserNeighborhood(100000, userSimilarity, model);
////			System.out.println("Neighborhood Done");
//			recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
//			//itemRecommender = new GenericItemBasedRecommender(model, itemSimilarity);
//			System.out.println("Recommender Done");
//			cachingRecommender = new CachingRecommender(recommender);
//			System.out.println("Caching Recommender Done");

			input = new HashMap<>();
			readInData();

		} catch (Exception e) {
			System.out.println("Exception!");
			e.printStackTrace();
		}
		System.out.println("INITIALISED!");
	}

	public boolean itemBased(String inputData, String unrated, String rated){

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

		return true;
	}

	public boolean userBasedNeighborhood(String inputData, String unrated, String rated, int neighbourhood){

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

		return true;
	}

	public boolean userBasedThreshold(String inputData, String unrated, String rated, int threshold){

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

		return true;
	}



	private HashMap readInData(String file){
		System.out.println("Reading In Data...");

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
		System.out.println("Data Read In!");
	}


	public static void main(String[] args){

		Recommender rec = new Recommender();

		rec.init();
		rec.go();
	}
}