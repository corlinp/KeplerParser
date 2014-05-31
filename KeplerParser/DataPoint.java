package KeplerParser;

import java.awt.Color;
import java.awt.Graphics;

public class DataPoint{
	double time, timcorr, sap_flux, sap_flux_err, pdc_flux, pdc_flux_err, sap_quality
,mom_centr1, mom_centr1_err, mom_centr2, mom_centr2_err, pos_corr1, pos_corr2;
	int cadence_number;
	int graphX, graphY;
	Color color = Color.blue;
	double difference = 0; //this is the difference from the last point in the set
	
	public DataPoint(double t, double tc, int cad, double sf, double sfe, double pf, 
			double pfe, double sq, double mc1, double mc1e, double mc2, double mc2e, double pc1, double pc2){
		time = t;
		timcorr = tc;
		cadence_number = cad;
		sap_flux = sf;
		sap_flux_err = sfe;
		pdc_flux= pf;
		pdc_flux_err= pfe;
		sap_quality= sq;
		mom_centr1= mc1;
		mom_centr1_err= mc1e;
		mom_centr2= mc2;
		mom_centr2_err=mc2e;
		pos_corr1= pc1;
		pos_corr2= pc2;
		
	}
	public double getTime(){
		return time;
	}
	public double getTimcorr(){
		return timcorr;
	}
	public double getSapFlux(){
		return sap_flux;
	}
	public double getSapFluxErr(){
		return sap_flux_err; 
	}
	public double getPDCFlux(){
		return pdc_flux; 
	}
	public double getPDCFluxErr(){
		return pdc_flux_err;
	}
	public double getSapQuality(){
		return sap_quality;
	}
	public double getMomCentr1(){
		return mom_centr1;
}
public double getMomCentr1Err(){
return mom_centr1_err;
}
public double getMomCentr2(){
return mom_centr2;
}
public double getMomCentr2Err(){
return mom_centr2_err;
}
public double getPosCorr1(){
return pos_corr1;
}
public double getPosCorr2(){
return pos_corr2;
}   


	public String toString(){
		String output = "time: " + time + ", timcorr: " + timcorr + ", Cadence Number: " + cadence_number + ", sap_flux: " + sap_flux
				+ ", sap_flux_err:" + sap_flux_err;
		return output;
	}
	
	
	int size=2;
	public void paint(Graphics g){
		g.setColor(color);
		g.fillOval(graphX-size, graphY-size, size*2, size*2);
	}
	public void setY(int lastY) {
		// TODO Auto-generated method stub
		graphY = lastY;
	}
	public void setX(int x) {
		// TODO Auto-generated method stub
		graphX = x;
	}
	public int getY() {
		return graphY;
	}
	public int getX() {
		// TODO Auto-generated method stub
		return graphX;
	}
	public void setColor(Color c) {
		// TODO Auto-generated method stub
		color = c;
	}
	public void setSize(int s){
		size = s;
	}
	
}









