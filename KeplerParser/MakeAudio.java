//
//
package KeplerParser;
//
//import java.util.*;
//
public class MakeAudio {
//
//	Star currStar = new Star("C:/Users/Corlin/Downloads/Kepler Data/Q3/kplr000757076-2009350155506_llc.fits.asc");
//	ArrayList<DataPoint> data;
//	int[] values;
//	Sound sound;
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		MakeAudio ma = new MakeAudio();
//		ma.make();
//	}
//	
//	public void make(){
//		data = currStar.getData();
//		values = new int[data.size()];
//		
//		int i = 0;
//		for (DataPoint dp : data){
//			values[i] = (int)(((dp.getPDCFlux()/currStar.getAverage())-1)*16000000);
//			System.out.println(values[i]);
//			i++;
//		}
//		sound = new Sound(values.length);
//		for(int q = 0; q < values.length; q++){
//			sound.setSampleValueAt(q,values[q]);
//		}
//		
//		Sound nSound = sound.slowTween(10, 0);
//		nSound.blockingPlay();
//		
//		try{
//		sound.writeToFile("C:/Users/Corlin/Downloads/Kepler Data/Q32Audio.wav");
//		nSound.writeToFile("C:/Users/Corlin/Downloads/Kepler Data/Q32AudioSLOWER.wav");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		nSound.explore();
//	}
//
}
//
//
//
//
//
//
//
//
