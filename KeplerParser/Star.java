package KeplerParser;
import java.io.*;
import java.util.*;

public class Star {
	Scanner scan;
	File starFile;
	public String[] metadata = new String[50];
	ArrayList<DataPoint> data = new ArrayList<DataPoint>();
	private Scanner s2;
	
	/**
	 * creates a new star from the given file 
	 * @param fileLoc the location of the file containing the Kepler data for a given star
	 */
	public Star(String fileLoc){
		starFile = new File(fileLoc);
		starInit();
	}
	
	/**
	 * creates a new star from the given file 
	 * @param fileLoc the file containing the Kepler data for a given star
	 */
	public Star(File fileLoc){
		starFile = fileLoc;
		starInit();
	}
	
	int temperature;
	double radius;
	
	double getRadius(){
		return radius;
	}
	int getTemperature(){
		return temperature;
	}
	
	/**
	 * creates a new set of star data
	 */
	public void starInit(){
		
		try{
			scan = new Scanner(starFile);
		}catch(IOException ioe){
			System.out.println("Error. File not found!" + ioe.getMessage());
		}
		
		String line;
		
		//loop for metadata
		for (int i=0; i<50; i++){
			line = scan.nextLine();
			if(line.charAt(0) == '#'){
				if(line.contains("T_eff")){
					temperature = Integer.parseInt(line.substring(line.indexOf('=') + 2, line.indexOf('K') - 1));
					//System.out.println(temperature);
				}
				if(line.contains("Radius")){
					radius = Double.parseDouble(line.substring(line.indexOf('=') + 2, line.indexOf("R_sun") - 1));
					//System.out.println(radius);
				}
			
				metadata[i] = line.substring(1);
			}
			else{
				break;
			}
		}
		
		//parsing data into a DataPoint class

		while(scan.hasNext()){
					line = scan.nextLine();
					s2 = new Scanner(line);
					
					double t = s2.nextDouble();
					double timc = toDouble(s2.next());
					int cad = s2.nextInt();
					double sf = toDouble(s2.next());
					double sfe = toDouble(s2.next());
					double pf= toDouble(s2.next());
					//System.out.println(pf);
					double pfe= toDouble(s2.next());
					double sq= s2.nextDouble();
					double mc1= s2.nextDouble();
					double mc1e= toDouble(s2.next());
					double mc2= s2.nextDouble();
					double mc2e= toDouble(s2.next());
					double pc1= s2.nextDouble();
					double pc2= s2.nextDouble(); 
					s2.close();
					
					data.add(new DataPoint(t, timc, cad, sf, sfe, pf, pfe, sq, mc1, mc1e, mc2, mc2e, pc1, pc2));
				}
		scan.close();

		
//		for(DataPoint dp : data){
			//System.out.println(dp.toString());
//			System.out.println(dp.getSapFlux());
//		}
		
		removeBadData();
		calculate();
		//numData = data.size();
		//loop to initialize DataPoints
	}
	
	double average = 0;
	double avgAvgDev = 0;
	
	double avgDiff = 0;
	public DataPoint[] maxPoints;
	
	public void calculate(){
		double total = 0;
		double avgDev = 0;

		for(int i = 0; i < data.size(); i++){
			 total += data.get(i).getPDCFlux();
		}

		average = total / data.size();

		for(int i=0; i< data.size(); i++){
			avgDev+= Math.abs(average - data.get(i).getPDCFlux());
		}

		avgAvgDev= avgDev/ data.size();
		DataPoint empty = new DataPoint(0,0,0,0,0,0,0,0,0,0,0,0,0,0);
		
		double totdiff=0;
		maxPoints = new DataPoint[34];
		for(int i = 0; i < maxPoints.length; i++)
			maxPoints[i] = empty;
		
		for(int i =1; i < data.size(); i++){
			double currDiff = Math.abs(data.get(i).getPDCFlux() - data.get(i-1).getPDCFlux());
			if(currDiff >= maxPoints[maxPoints.length-1].difference && data.get(i).getTime() - data.get(i-1).getTime() < .1){
				int spot=-1;
				for(int j = maxPoints.length-1; j >= 0; j--){
					if(currDiff > maxPoints[j].difference)
						spot = j;
				}
				for(int j = maxPoints.length-1; j < spot; j--){
					maxPoints[j+1] = maxPoints[j];
				}
				try{
					maxPoints[spot] = data.get(i);
				}catch(Exception e){}
				//System.out.println(currDiff);
			}
			
			data.get(i).difference = currDiff;
			totdiff+=currDiff;
		}
		avgDiff = totdiff / (data.size()-1);
		
		///////calculating increment;
		firstTime = data.get(0).getTime();
		increment = data.size() / (data.get(data.size()-1).getTime() - firstTime);
		System.out.println("Increment is " + increment + " with first time " + firstTime);
	}

	private double increment; //(time - firsttime) * increment= point number
	private double firstTime;
	
	public int indexOf(double time){
		System.out.println(((time - firstTime) * increment) + " might be the index of time " + time);
		int toReturn = (int)((time - firstTime) * increment);
		if(toReturn < 0)
			toReturn = 0;
		return toReturn;
	}
	
	public double getAverage() {
		return average;
	}

	public double getAvgDev() {
		return avgAvgDev;
	}

	public int removeBadData(){
		int num = 0;
		for (DataPoint dp : data){
			if(dp.getSapQuality()!=0){
				num++;
				data.remove(dp);
			}
		}
		return num;
	}
	
	/**
	 * converts a value such as 1.2277194e+04 to a double such as 1227.7194
	 * @param s a string containing a number in scientific notation denoted by e
	 * @return the double resulting from the scientific number
	 */
	public double toDouble(String s){
		int point = s.indexOf('e');
		double value, times;
		value = Double.parseDouble(s.substring(0, point));
		times = Double.parseDouble(s.substring(point+1, s.length()));
		
		value = value * Math.pow(10, times);
		
		//begin rounding. Disable this if we get weird graphs.
		/*String valString = String.valueOf(value);
		int i = valString.indexOf("000");
		if(i==-1)
			i = valString.indexOf("999");
		if(i != -1)
			value = Double.parseDouble(valString.substring(0, i));*/
		//end rounding
		
		return value;
	}
	
	public DataPoint dataAtTime(double t){
		return null;
	}
	
	public ArrayList<DataPoint> getData(){
		return data;
	}
	
//	public int size(){
//		return numData;
//	}
	
}
