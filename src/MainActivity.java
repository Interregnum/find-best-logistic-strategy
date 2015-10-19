import io.ReadNumbersFromExcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.poi.hslf.exceptions.InvalidRecordFormatException;

import util.FindClosetSum;

/**
 * @author Mengchao Zhong
 */
public class MainActivity {

	public static void main(String[] args) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(new File("config.properties"));
			prop.load(input);
			long maxRuntime = Long.parseLong(prop.getProperty("maxRuntime"));
			double maxDeviation = Double.parseDouble(prop.getProperty("maxDeviation"));
			double range = Double.parseDouble(prop.getProperty("range"));
			String dataFilePath = prop.getProperty("dataFilePath");
			String supportingFilePath = prop.getProperty("supportingFilePath");
			
			System.out.println("Data File: ");
			ReadNumbersFromExcel dataFile = new ReadNumbersFromExcel(dataFilePath);
			List<Double> nums = dataFile.readDoublesFromColumn(0);
			System.out.println("Supporting File: ");
			ReadNumbersFromExcel supportingFile = new ReadNumbersFromExcel(supportingFilePath);
			List<Integer> counts = supportingFile.readIntegersFromColumn(0);
			List<Double> targetSums = supportingFile.readDoublesFromColumn(1);
			
			if(counts.size() != targetSums.size()) {
				throw new InvalidRecordFormatException("Mismatched Array Sizes.");
			}
			
			File output = new File("result.csv");
			if (!output.exists()) {
				output.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(output.getAbsoluteFile());
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			
			for(int i = 0; i < counts.size(); ++i) {
				System.out.println();
				System.out.println("Target Numbers: " + counts.get(i));
				int count = counts.get(i);
				double targetSum = targetSums.get(i);
				double total = 0;
				FindClosetSum algorithm;
				List<Double> ans;
				double originDiff = Double.MAX_VALUE;
				double minusOneDiff = Double.MAX_VALUE;
				double plusOneDiff = Double.MAX_VALUE;
				
				algorithm = new FindClosetSum(maxDeviation, maxRuntime, range);
				List<Double> originAns = algorithm.findClosetSum(targetSum, nums, count);
				if(!algorithm.withinRange()) {
					originDiff = algorithm.getMinDiff();
					System.out.println("Current result is out of range, change target numbers to " + (count - 1) + ".");
					algorithm = new FindClosetSum(maxDeviation, maxRuntime, range);
					List<Double> minusOneAns = algorithm.findClosetSum(targetSum, nums, count - 1);
					if(!algorithm.withinRange()) {
						minusOneDiff = algorithm.getMinDiff();
						System.out.println("Current result is out of range, change target numbers to " + (count + 1) + ".");
						algorithm = new FindClosetSum(maxDeviation, maxRuntime, range);
						List<Double> plusOneAns = algorithm.findClosetSum(targetSum, nums, count + 1);
						if(!algorithm.withinRange()) {
							plusOneDiff = algorithm.getMinDiff();
							ans = originDiff <= minusOneDiff 
									? (originDiff <= plusOneDiff ? originAns : plusOneAns) 
									: (minusOneDiff <= plusOneDiff ? minusOneAns : plusOneAns);
						}
						else {
							ans = plusOneAns;
						}
					}
					else {
						ans = minusOneAns;
					}
				}
				else {
					ans = originAns;
				}
				
				if(ans == null) {
					break;
				}
				
				bufferWriter.write(Integer.toString(i + 1));
				for(Double num : ans) {
					total += num;
					nums.remove(num);
					bufferWriter.write(",");
					bufferWriter.write(num.toString());
				}
				bufferWriter.write("\n");
			
				System.out.println();
				System.out.println("Best Solution: " + ans);
				System.out.println("Total: " + total);
			}
			
			bufferWriter.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File Not Found.");
		}
		catch(IOException e) {
			System.out.println("IO Error.");
		}
		catch(InvalidRecordFormatException e) {
			System.out.println("Number of TARGET SUMS does not match number of NUMBER COUNTS. Please update supporting file.");
		}
	}
}