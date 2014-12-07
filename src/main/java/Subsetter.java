import java.io.*;
import java.util.ArrayList;

/**
 * Created by George on 06/12/2014.
 */
public class Subsetter {

	private int rowNumber = 10;
	private String input = "C:\\Users\\George\\Uni\\COMP3208\\TrainingSetData\\rs-trainingset.csv";
	private String outputDataMain = "C:\\Users\\George\\Uni\\COMP3208\\TrainingSetData\\subset-data-input.csv";
	private String outputRated = "C:\\Users\\George\\Uni\\COMP3208\\TrainingSetData\\subset-rated.csv";
	private String outputUnrated = "C:\\Users\\George\\Uni\\COMP3208\\TrainingSetData\\subset-unrated.csv";


	public void makeSubset(){

		System.out.println("Starting...");


		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int count = 1;


		try {

			PrintWriter ratedWriter = new PrintWriter(new File(outputRated));
			PrintWriter unratedWriter = new PrintWriter(new File(outputUnrated));
			PrintWriter dataMainWriter = new PrintWriter(new File(outputDataMain));

			br = new BufferedReader(new FileReader(input));
			while ((line = br.readLine()) != null) {
				String[] entryData = line.split(cvsSplitBy);
				if(count % rowNumber == 0){

					ratedWriter.println(entryData[0] + "," + entryData[1] + "," + entryData[2]);
					unratedWriter.println(entryData[0] + "," + entryData[1]);
				}else{
					dataMainWriter.println(entryData[0] + "," + entryData[1] + "," + entryData[2]);
				}

//                Read in data



				if(count % 10000 == 0){
					System.out.println("Input Count :: " + count);
				}
				count++;



			}

			ratedWriter.close();
			unratedWriter.close();
			dataMainWriter.close();

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

		System.out.println("Finished!");

	}

	public static void main(String[] args){
		Subsetter subsetter = new Subsetter();
		subsetter.makeSubset();
	}
}
