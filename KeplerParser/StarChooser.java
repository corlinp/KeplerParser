package KeplerParser;

import java.io.File;
import java.util.ArrayList;

public class StarChooser {
	static boolean skipGoods = false;
	static boolean deleteBads = false;
	static String fileLoc = "C:/Q16_1ascii/129";

	public static void main(String[] args){
		System.out.println("Star Scanner v0.8 by Corlin Palmer...\n");

		File[] files = new File(fileLoc).listFiles();


		try {
			if(files.length == 0){
				System.out.println("Error. No files found in folder.");
				return;
			}
		} catch (Exception e) {
			System.out.println("Error. No folder of that name found.");
			return;
		}
		System.out.println("Scanning " + (files.length+1) + " Files. Estimated Time: "
				+ ((int)((files.length+1)*3.166)/100.0) + " minutes\n\n");
		int numGoods = 0, numFails = 0, numSkips = 0;

		long benchtime = System.currentTimeMillis();

//		try {
//			Thread.sleep(2600);
//		} catch (InterruptedException e) {		}

		for (int f = 0; f < files.length; f++) {
			try{

				if(skipGoods == true && files[f].getName().substring(0, 4).equals("GOOD")){
					numSkips++;
					System.out.println(files[f].getName().substring(4) + 
							" was previously found as good, so I'm skipping it. Skipped " + numSkips + " Files");
				}
				else if(files[f].getName().substring(0, 4).equals("SK0IP")){
					numSkips++;
					System.out.println(files[f].getName().substring(4) + 
							" was marked as bad, failed, or corrupted, so I'm skipping it. Skipped " + numSkips + " Files");
				}
				else{

					Star currentStar = new Star(files[f]);
					//ArrayList<DataPoint> data = currentStar.getData();
					double avgAvgDev = currentStar.getAvgDev();

					int goodCount=0;

					for(int i = 1; i < currentStar.maxPoints.length-1; i++){
						//int index = data.indexOf(currentStar.maxPoints[i]);
						if (currentStar.maxPoints[i].difference - currentStar.maxPoints[i+1].difference > 2 * currentStar.avgDiff &&
								avgAvgDev*1.5 < Math.abs(currentStar.maxPoints[i].getPDCFlux() - currentStar.getAverage())){
							goodCount++;
						}
					}
					if(goodCount == 0){
						System.out.println(files[f].getName() + " has no interesting points. Deleting. Completion: "
								+ ((100*f)/(files.length-1)) + "%");
						if(deleteBads == true){
							files[f].delete();}
						else{
							File fn = new File(fileLoc + "/SKIP"+files[f].getName());
							files[f].renameTo(fn);
						}
					}
					else{
						System.out.println(files[f].getName() + ":  " + goodCount + "  points.                         Completion: "
								+ ((100*f)/(files.length-1)) + "%");
						if(skipGoods == true){
							File fn = new File(fileLoc + "/GOOD"+files[f].getName());
							files[f].renameTo(fn);
						}
						numGoods++;
					}
				}
			}
			catch(Exception ee){
				System.out.println("Failed to read " + files[f].getName());
				//				File fn = new File(fileLoc + "/FAIL"+files[f].getName());
				//				files[f].renameTo(fn);
				numSkips++;
				numFails++;
			}
			if(f%100 == 99 && numSkips != (f+1)){
				System.out.println("\nStatus report:\nScanned " + ((f+1)-numSkips) + " files so far (" + (f+1) 
						+ " total files)\nFound " 
						+ numGoods + " Interesting Stars\nFound " + numFails + " Corrupted or Failed Stars");
				long temp=System.currentTimeMillis()-benchtime;
				System.out.println("Average time: " + (temp/((f+1)-numSkips)) + "ms per file\n");

			}
		}
		benchtime=System.currentTimeMillis()-benchtime;
		System.out.println("\nData scanned successfully! Average time: " + (benchtime/(files.length-numSkips)) + "ms per file");
	}
}