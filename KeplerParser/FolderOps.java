package KeplerParser;

import java.io.File;
import java.util.ArrayList;

public class FolderOps {

	static boolean skipGoods = false;
	static boolean deleteBads = false;
	static String fileLoc = "C:/goods/12done";

	public static void main(String[] args){
		System.out.println("Folder Operations v0.1 by Corlin Palmer...\n");

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
		System.out.println("Scanning " + (files.length+1) + " Files.");
		int numGoods = 0, numFails = 0, numSkips = 0;

		long benchtime = System.currentTimeMillis();

		for (int f = 0; f < files.length; f++) {
			try{

				if(skipGoods == true && files[f].getName().substring(0, 4).equals("GOOD")){
					numSkips++;
					System.out.println(files[f].getName().substring(4) + 
							" was previously found as good, so I'm skipping it. Skipped " + numSkips + " Files");
				}
				else if(files[f].getName().substring(0, 4).equals("SKIP")){
					numSkips++;
					System.out.println(files[f].getName().substring(4) + 
							" was marked to skip, so I'm skipping it. Skipped " + numSkips + " Files");
				}
				else{
					operations(files[f]);
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
	
	
	public static void operations(File f){
		if(f.getName().substring(0, 4).equals("GOOD")){
			File fn = new File(fileLoc + "/"+f.getName().substring(4));
			f.renameTo(fn);
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
