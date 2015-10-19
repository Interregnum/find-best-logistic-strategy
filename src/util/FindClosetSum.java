package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Mengchao Zhong
 */
public class FindClosetSum {

	private Map<Integer, Double> minMap = new HashMap<Integer, Double>();
	private Map<Integer, Double> maxMap = new HashMap<Integer, Double>();
	private List<Double> currCloset = new ArrayList<Double>();
	private List<Double> allCloset = new ArrayList<Double>();
	private double minDiff = Double.MAX_VALUE;
	private boolean isTimedOut = false;
	private final double diffThreshold;
	private final long runtimeThreshold;
	private final double range;
	
	/**
	 * @param diffThreshold - Satisfactory difference.
	 * @param runtimeThreshold - Maximum running time allowed.
	 * @param range - Acceptance range.
	 */
	public FindClosetSum(double diffThreshold, long runtimeThreshold, double range) {
		super();
		this.diffThreshold = diffThreshold;
		this.runtimeThreshold = runtimeThreshold;
		this.range = range;
	}

	/**
	 * @return If current result is within acceptance range.
	 */
	public boolean withinRange() {
		return minDiff < range;
	}

	/**
	 * @return Difference to target sum.
	 */
	public double getMinDiff() {
		return minDiff;
	}
	
	/**
	 * @param target - Target sum.
	 * @param nums - List of numbers.
	 * @param targetCount - Number of numbers expected to fulfill.
	 * @return Numbers of the closet sum solution. 
	 */
	public List<Double> findClosetSum(double target, List<Double> nums, int targetCount) {
		if(nums.size() < targetCount || targetCount <= 0) {
			return null;
		}
		else {
	        System.out.print("Sorting...");
	        Collections.sort(nums);
	        System.out.println("Done");
			System.out.print("Optimizing...");
			optimize(target, nums, targetCount);
			System.out.println("Done");
			
			System.out.print("Searching...");
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isTimedOut = true;
				}
			}, runtimeThreshold);
			recursion(target, 0, nums, targetCount, 0);
			System.out.println("Done");
		}
		return allCloset;
	}
	
	/**
	 * @param target - Target sum.
	 * @param index - Index of current recursion pointer.
	 * @param nums - List of numbers.
	 * @param targetCount - Number of numbers expected to fulfill.
	 * @param count - Current count of numbers.
	 */
	private void recursion(double target, int index, List<Double> nums, int targetCount, int count) {
		if(minDiff <= diffThreshold || isTimedOut) {
			return;
		}
		
		if(count == targetCount) {
			if(Math.abs(target) < minDiff) {
				minDiff = Math.abs(target);
				allCloset.clear();
				allCloset.addAll(currCloset);
				//System.out.println("New combination found: +/- " + minDiff);
			}
		}
		else {
			for(int i = index; i < nums.size(); ++i) {
				if(target >= minDiff + maxMap.get(count) || target <= minMap.get(count) - minDiff) {
					break;
				}
				currCloset.add(nums.get(i));
				recursion(target - nums.get(i), i + 1, nums, targetCount, count + 1);
				currCloset.remove(currCloset.size() - 1);
			}
		}
	}
	
	/**
	 * @param target - Target sum.
	 * @param nums - List of numbers.
	 * @param targetCount - Number of numbers expected to fulfill.
	 */
	private void optimize(double target, List<Double> nums, int targetCount) {
		double minNumbers = 0;
		double maxNumbers = 0;
		
		for(int i = 0; i < targetCount; ++i) {
			minNumbers += nums.get(i);
			maxNumbers += nums.get(nums.size() - i - 1);
			
			minMap.put(targetCount - i - 1, Math.max(0, minNumbers));
			maxMap.put(targetCount - i - 1, Math.min(target, maxNumbers));
		}
	}
}
