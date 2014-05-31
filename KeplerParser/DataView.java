package KeplerParser;

import java.awt.*;
import javax.swing.*;

public class DataView extends JFrame {
	
	public DataView(int num, double sigmas, DataPoint dp){
		setSize(350,540);
		setTitle("Info for Data Point " + num);
		setLayout(new GridLayout(14,1));
		
		//JLabel titleLabel = new JLabel("Displaying Data Point from Star " + star.metadata[0]);
		//add(titleLabel);
		add(new JLabel("Deviations from avg: "));
		add(new JTextField(String.valueOf(sigmas)));
		
		JLabel timeL = new JLabel("Time: ");
		add(timeL);
		
		JTextField timeTF = new JTextField(String.valueOf(dp.getTime()));
		add(timeTF);
		
		JLabel timecorrL= new JLabel("Time Corrected");
		add(timecorrL);

		JTextField timecorrTF= new JTextField(String.valueOf(dp.getTimcorr()));
		add(timecorrTF);
		
		JLabel sapfluxL= new JLabel("Sap Flux");
		add(sapfluxL);
	
		JTextField sapfluxTF= new JTextField(String.valueOf(dp.getSapFlux()));
		add(sapfluxTF);

		JLabel sapfluxerrL= new JLabel("Sap Flux Error");
		add(sapfluxerrL);

		JTextField sapfluxerrTF= new JTextField(String.valueOf(dp.getSapFluxErr()));
		add(sapfluxerrTF);

		JLabel pdcfluxL= new JLabel("PDC Flux");
		add(pdcfluxL);
	
		JTextField pdcfluxTF= new JTextField(String.valueOf(dp.getPDCFlux()));
		add(pdcfluxTF);

		JLabel pdcfluxerrL= new JLabel("PDC Flux Error");
		add(pdcfluxerrL);
		JTextField pdcfluxerrTF= new JTextField(String.valueOf(dp.getPDCFluxErr()));
		add(pdcfluxerrTF);

		JLabel sapqualityL= new JLabel("Sap Quality");
		add(sapqualityL);

		JTextField sapqualityTF= new JTextField(String.valueOf(dp.getSapQuality()));
		add(sapqualityTF);

		JLabel momcentr1L= new JLabel("MOM Centr 1");
		add(momcentr1L);

		JTextField momcentr1TF= new JTextField(String.valueOf(dp.getMomCentr1()));
		add(momcentr1TF);

		JLabel momcentr1errL= new JLabel("MOM Centr 1 Error");
		add(momcentr1errL);

		JTextField momcentr1errTF= new JTextField(String.valueOf(dp.getMomCentr1Err()));
		add(momcentr1errTF);

		JLabel momcentr2L=new JLabel("MOM Centr 2");
		add(momcentr2L);

		JTextField momcentr2TF= new JTextField(String.valueOf(dp.getMomCentr2Err()));
		add(momcentr2TF);
		
		JLabel momcentr2errL= new JLabel("MOM Centr 2 Error");
		add(momcentr2errL);
		
		JTextField momcentr2errTF= new JTextField(String.valueOf(dp.getMomCentr2Err()));
		add(momcentr2errTF);
		
		JLabel poscorr1L= new JLabel("POS Correction 1");
		add(poscorr1L);

		JTextField poscorr1TF=new JTextField(String.valueOf(dp.getPosCorr1()));
		add(poscorr1TF);

		JLabel poscorr2L= new JLabel("POS Correction 2");
		add(poscorr2L);

		JTextField poscorr2TF= new JTextField(String.valueOf(dp.getPosCorr2()));
		add(poscorr2TF);

		
		
		
		setVisible(true);
	}

}
