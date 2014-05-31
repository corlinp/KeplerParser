package KeplerParser;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.geom.Line2D;

public class KeplerParser extends JFrame {
	private boolean scrolling = false;
	Star currentStar;
	GraphPanel gPanel;
	JLabel statusLabel = new JLabel("Select a File");
	ArrayList<DataPoint> data;
	ArrayList<DataView> views = new ArrayList<DataView>();
	protected int mouseX,mouseY;
	public KeplerParser(Star theStar){
		currentStar = theStar;
		data = currentStar.getData();
		display();

		showStarData();
		setVisible(true);
		findDev();
		fitX();
		repaint();
		statusLabel.setText("Opened the star with " + data.size() + " data points");

	}

	public void display(){
		setSize(1000,700);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		setTitle("Kepler Data Analysis");
		setLayout(new BorderLayout());

		///////////////Begin Status Label Stuff//////////////
		JPanel statusArea = new JPanel();
		statusArea.setLayout(new BorderLayout());
		//let's put some navigation buttons down here
		JPanel bottomButtons = new JPanel();
		bottomButtons.setLayout(new GridLayout(1,3));
		statusArea.add(bottomButtons,BorderLayout.EAST);
		JButton goLeft = new JButton(" < ");
		JButton goOut = new JButton(" - ");
		JButton goAll = new JButton(" [ ] ");
		JButton goIn = new JButton(" + ");
		JButton goRight = new JButton(" > ");

		goLeft.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setGraphWidth(leftGraph - (rightGraph - leftGraph)/3,rightGraph - (rightGraph - leftGraph)/3);
				repaint();
			}
		});
		goRight.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setGraphWidth(leftGraph + (rightGraph - leftGraph)/3,rightGraph + (rightGraph - leftGraph)/3);
				repaint();
			}
		});
		goOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setGraphWidth(leftGraph - (rightGraph - leftGraph)/2,rightGraph + (rightGraph - leftGraph)/2);
				repaint();
			}
		});
		goIn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setGraphWidth(leftGraph + (rightGraph - leftGraph)/4,rightGraph - (rightGraph - leftGraph)/4);
				repaint();
			}
		});
		goAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fitX();
				repaint();
			}
		});

		bottomButtons.add(goLeft);
		bottomButtons.add(goOut);
		bottomButtons.add(goAll);
		bottomButtons.add(goIn);
		bottomButtons.add(goRight);

		statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		statusArea.add(statusLabel, BorderLayout.WEST);
		//		JPanel statusPanel = new JPanel();
		//		statusPanel.setLayout(new BorderLayout());
		//		statusPanel.add(statusLabel, BorderLayout.CENTER);
		add(statusArea, BorderLayout.SOUTH);
		/////////////////end status label stuff////////////////
		///////////////Begin Central Graph////////////////////
		gPanel = new GraphPanel();
		add(gPanel, BorderLayout.CENTER);
		gPanel.getHeight();
		gPanel.setBackground(Color.black);
		gPanel.repaint();
		//////////////////begin top panel//////////////////
		JPanel topPanel = new JPanel();
		add(topPanel,BorderLayout.NORTH);

		JButton fitY = new JButton ("Fit Y Axis");
		topPanel.add(fitY);
		fitY.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fitY();
				repaint();
			}
		});

		/////////////////BEGIN CLICK LISTENER////////////////
		gPanel.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				int num = 0;
				for (DataPoint dp: data){
					if(Math.abs(dp.getX() - e.getX())<8 && Math.abs(dp.getY() - e.getY())<8){
						dp.setColor(Color.RED);
						dp.setSize(3);
						views.add(new DataView(data.indexOf(dp), ((dp.getPDCFlux()-average)/avgAvgDev), dp));
						num++;
						if(num > 3){
							statusLabel.setText("Don't click near so many points at once! Try Zooming in.");
							break;
						}

					}
					else{
						dp.setColor(Color.BLUE);
						dp.setSize(2);
					}
					repaint();
				}
			}

			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
				scrolling = true;
				mouseX=arg0.getX();
				mouseY=arg0.getY();
			}
			public void mouseReleased(MouseEvent arg0) {
				scrolling = false;
				repaint();
			}

		});
		//////////////////lets do some click and drag////////////////////
		gPanel.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {
				double inc = (leftGraph - rightGraph)/KeplerParser.this.getWidth();
				double change = ((mouseX - e.getX())*inc);
				setGraphWidth(leftGraph-change,rightGraph-change);
				mouseX = e.getX();

				int yCh = mouseY - e.getY();

				if(yCh > 30){
					setGraphWidth(leftGraph + (rightGraph - leftGraph)/20,rightGraph - (rightGraph - leftGraph)/20);
					mouseY = e.getY()+30;
				}
				if(yCh < -30){
					setGraphWidth(leftGraph - (rightGraph - leftGraph)/20,rightGraph + (rightGraph - leftGraph)/20);
					mouseY = e.getY()-30;
				}

				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {}

		});
		/////////////////begin graph width/////////////
		JButton refreshButton = new JButton("Refresh Graph");

		topPanel.add(refreshButton);
		JLabel xlLabel = new JLabel("Left Time");
		JLabel xrLabel = new JLabel("Right Time");
		xLeft = new JTextField("170");
		xRight = new JTextField("175");
		topPanel.add(xlLabel);
		topPanel.add(xLeft);
		topPanel.add(xrLabel);
		topPanel.add(xRight);
		xLeft.setPreferredSize(new Dimension(100,25));
		xRight.setPreferredSize(new Dimension(100,25));

		refreshButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});

		///////////y value things/////////////
		JLabel ylLabel = new JLabel("Max Flux");
		JLabel yrLabel = new JLabel("Min Flux");
		yBottom = new JTextField("122300");
		yTop = new JTextField("122400");
		topPanel.add(yrLabel);
		topPanel.add(yBottom);
		topPanel.add(ylLabel);
		topPanel.add(yTop);
		yBottom.setPreferredSize(new Dimension(100,25));
		yTop.setPreferredSize(new Dimension(100,25));
		//setVisible(true);
	}
	JTextField xLeft;
	JTextField xRight;
	JTextField yTop;
	JTextField yBottom;
	JFrame starInfoPanel;

	public void showStarData(){
		///////////////////begin left Panel////////////////

		int[] goodparts = new int[] {0,1,3,6,7,8,9,10,11,12,13,31,32,35,36,37,38,39,40};
		starInfoPanel = new JFrame("Star Information");
		starInfoPanel.setLayout(new GridLayout(goodparts.length+4,1));


		if(currentStar.getRadius()>=0.08 && currentStar.getRadius()<=0.14)
			starInfoPanel.add(new JLabel("Radius: T Star"));

		if(currentStar.getRadius()>=0.08 && currentStar.getRadius()<=0.15)
			starInfoPanel.add(new JLabel("Radius: L Star"));

		if(currentStar.getRadius()<=0.70 && currentStar.getRadius()>=0.17) 
			starInfoPanel.add(new JLabel("Radius: M Star"));

		if(currentStar.getRadius()>=0.70 && currentStar.getRadius()<=0.96)
			starInfoPanel.add(new JLabel("Radius: K Star"));

		if(currentStar.getRadius()>=0.96 && currentStar.getRadius()<=1.15)
			starInfoPanel.add(new JLabel("Radius: G Star"));

		if(currentStar.getRadius()>=1.15 && currentStar.getRadius()<=1.40)
			starInfoPanel.add(new JLabel("Radius: F Star"));

		if(currentStar.getRadius()>=1.40 && currentStar.getRadius()<=1.80)
			starInfoPanel.add(new JLabel("Radius: A Star"));

		if(currentStar.getRadius()>=1.80 && currentStar.getRadius()<=6.60)
			starInfoPanel.add(new JLabel("Radius: B Star"));

		if(currentStar.getRadius()>=6.60)
			starInfoPanel.add(new JLabel("Radius: O Star"));

		if(currentStar.getTemperature()>=500 && currentStar.getTemperature()<=1300)
			starInfoPanel.add(new JLabel("Temperature: T Star")); 

		if(currentStar.getTemperature()>=1300 && currentStar.getTemperature()<=2400)
			starInfoPanel.add(new JLabel("Temperature: L Star"));

		if(currentStar.getTemperature()>=3700 && currentStar.getTemperature()<=5200)
			starInfoPanel.add(new JLabel("Temperature: M Star"));

		if(currentStar.getTemperature()>=3700 && currentStar.getTemperature()<=5200)
			starInfoPanel.add(new JLabel("Temperature: K Star"));

		if(currentStar.getTemperature()>=5200 && currentStar.getTemperature()<=6000)
			starInfoPanel.add(new JLabel("Temperature: G Star"));

		if(currentStar.getTemperature()>=6000 && currentStar.getTemperature()<=7500)
			starInfoPanel.add(new JLabel("Temperature: F Star"));

		if(currentStar.getTemperature()>=7500 && currentStar.getTemperature()<=10000)
			starInfoPanel.add(new JLabel("Temperature: A Star"));

		if(currentStar.getTemperature()>=10000 && currentStar.getTemperature()<=30000)
			starInfoPanel.add(new JLabel("Temperature: B Star"));

		if(currentStar.getTemperature()>=30000)
			starInfoPanel.add(new JLabel("Temperature: O Star"));


		if(currentStar.getRadius()>= 17.0 && currentStar.getRadius()<=23.0 && currentStar.getTemperature()>= 30000 && currentStar.getTemperature()<=44500)
			starInfoPanel.add(new JLabel("O Supergiant"));


		if(currentStar.getRadius()>= 6.3 && currentStar.getRadius()<=13.0 || currentStar.getRadius()>=22.0 && currentStar.getRadius()<=45.0  && currentStar.getTemperature()>= 10000 && currentStar.getTemperature()<=30000)
			starInfoPanel.add(new JLabel("B Supergiant"));


		if(currentStar.getRadius()>= 3.0 && currentStar.getRadius()<=3.5 || currentStar.getRadius()>=65.0 && currentStar.getRadius()<= 90.0 && currentStar.getTemperature()>= 7450 && currentStar.getTemperature()<=10100)
			starInfoPanel.add(new JLabel("A Supergiant"));


		if(currentStar.getRadius()>= 2.9 && currentStar.getRadius()<=3.5 || currentStar.getRadius()>= 100.0 && currentStar.getRadius()<=130.0 && currentStar.getTemperature()>= 6150 && currentStar.getTemperature()<=7150)
			starInfoPanel.add(new JLabel("F Supergiant"));

		if(currentStar.getRadius()>= 5.5 && currentStar.getRadius()<=8.5 || currentStar.getRadius()>= 185.0 && currentStar.getRadius()<=250.0 && currentStar.getTemperature()>= 4900 && currentStar.getTemperature()<=5850)
			starInfoPanel.add(new JLabel("G Supergiant"));


		if(currentStar.getRadius()>= 10.0 && currentStar.getRadius()<=35.0 ||  currentStar.getRadius()>= 280.0 && currentStar.getRadius()<=450.0 && currentStar.getTemperature()>= 3850 && currentStar.getTemperature()<=4750)
			starInfoPanel.add(new JLabel("K Supergiant"));


		if(currentStar.getRadius()>= 40.0 && currentStar.getRadius()<=95.0 || currentStar.getRadius()>= 500.0 && currentStar.getRadius()<=2500.0 && currentStar.getTemperature()>= 3240 && currentStar.getTemperature()<=3800)
			starInfoPanel.add(new JLabel("M Supergiant"));



		for (int i = 0; i < goodparts.length; i++){
			JLabel st = new JLabel(currentStar.metadata[goodparts[i]]);
			starInfoPanel.add(st);
		}
		starInfoPanel.setBackground(new Color(230,230,230));
		starInfoPanel.setSize(300,600);
		starInfoPanel.setVisible(true);
		//add(starInfoPanel, BorderLayout.WEST);
	}

	public void setGraphWidth(double l, double r){
		leftGraph = (double)(Math.round(l*1000))/1000;
		rightGraph = (double)(Math.round(r*1000))/1000;
		xLeft.setText(String.valueOf(leftGraph));
		xRight.setText(String.valueOf(rightGraph));
	}

	public void setGraphHeight(double l, double r){
		topGraph = (double)(Math.round(l*1000))/1000;
		bottomGraph = (double)(Math.round(r*1000))/1000;
		yTop.setText(String.valueOf(leftGraph));
		yBottom.setText(String.valueOf(rightGraph));
	}

	public void fitY(){
		double max = 0;
		//TODO: make this binary find data at that time rather than displaypoints
		//that way we can get rid of displaypoints
		double min = 99999999;

		for(DataPoint dp : data){
			if(dp.getPDCFlux() > max){
				max = dp.getPDCFlux();
			}
			if(dp.getPDCFlux() < min){
				min = dp.getPDCFlux();
			}
		}
		max+=((max-min)/10);
		min-=((max-min)/10);
		setGraphHeight(max,min);
		statusLabel.setText("Fit Y axis to " + max + " , " + min);
	}

	public void fitX(){
		setGraphWidth(data.get(0).getTime(), data.get(data.size()-1).getTime());
	}

	/** this is going to be replaced by the new strategy
	double searchRad = 40;
	public double avgAvgDev;
	public double average;

	public void findDev(){
		average = currentStar.getAverage();
		avgAvgDev = currentStar.getAvgDev();

		//searchRad = (avgAvgDev * 3);

		//System.out.println("average: " + average + " average dev: " + avgDev + " avg avg dev" + avgAvgDev);
		final JFrame outlierFrame = new JFrame("Search for Outliers");
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5,1));
		final JPanel outPanel = new JPanel();
		outPanel.setLayout(new GridLayout(34,1));
		outlierFrame.setLayout(new BorderLayout());
		outlierFrame.setSize(200,580);
		outlierFrame.add(buttonPanel,BorderLayout.NORTH);
		outlierFrame.add(outPanel,BorderLayout.CENTER);
		outlierFrame.setVisible(true);
		buttonPanel.add(new JLabel("average: " + average));
		buttonPanel.add(new JLabel("Average Deviation: " + avgAvgDev));

		buttonPanel.add(new JLabel("Search Field"));
		final JTextField searchRadius = new JTextField(50);
		searchRadius.setText(String.valueOf(searchRad));
		buttonPanel.add(searchRadius);

		JButton searchJB = new JButton("Search!");
		buttonPanel.add(searchJB);
		searchJB.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){

		    	searchRad = doubleOf(searchRadius.getText());
		    	if (searchRad <= avgAvgDev){
		    		statusLabel.setText("There are too many points in that radius!");
		    	}
		    	outlierFrame.setVisible(false);
		    	findDev();
		    	statusLabel.setText("Searched for outlying data points in the range of " + searchRad);
		    	outlierFrame.dispose();
		    }
		});


		//System.out.println(searchRad);
		int numOutliers = 0;
		for(int i = 0; i < data.size(); i++){
			 if(Math.abs(data.get(i).getPDCFlux()-average) > searchRad){
				 numOutliers++;
				 if(numOutliers >= 34){
					 statusLabel.setText("Too many results! Try a larger search radius.");
					 break;
				 }
				 final JButton jb = new JButton(i + ": " + data.get(i).getTime());
				 jb.setBackground(Color.white);
				 outPanel.add(jb);
				 jb.addActionListener(new ActionListener(){
					    public void actionPerformed(ActionEvent e){
					    	DataPoint daytah = data.get((int)doubleOf(jb.getText().substring(0,jb.getText().indexOf(':'))));
					    	centerOn(daytah);
					    }
					});
			 }
		}
	}
	 **/
	int searchRad = 40;
	public double avgAvgDev;
	public double average;

	public void roundAxes(){
		topGraph = ((double)Math.round(doubleOf(yTop.getText())*1000))/1000;
		bottomGraph = ((double)Math.round(doubleOf(yBottom.getText())*1000))/1000;
		yTop.setText(String.valueOf(topGraph));
		yBottom.setText(String.valueOf(bottomGraph));
	}

	public void findDev(){
		average = currentStar.getAverage();
		avgAvgDev = currentStar.getAvgDev();

		//searchRad = (avgAvgDev * 3);

		//System.out.println("average: " + average + " average dev: " + avgDev + " avg avg dev" + avgAvgDev);
		final JFrame outlierFrame = new JFrame("Search for Outliers");
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5,1));
		final JPanel outPanel = new JPanel();
		outPanel.setLayout(new GridLayout(18,1));
		outlierFrame.setLayout(new BorderLayout());
		outlierFrame.setSize(200,580);
		outlierFrame.add(buttonPanel,BorderLayout.NORTH);
		outlierFrame.add(outPanel,BorderLayout.CENTER);
		outlierFrame.setVisible(true);
		buttonPanel.add(new JLabel("average: " + average));
		buttonPanel.add(new JLabel("Average Deviation: " + avgAvgDev));
		
		buttonPanel.add(new JLabel("Search Field"));
		final JTextField searchRadius = new JTextField(50);
		searchRadius.setText(String.valueOf(searchRad));
		buttonPanel.add(searchRadius);

		JButton searchJB = new JButton("Search!");
		buttonPanel.add(searchJB);
		searchJB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				searchRad = intOf(searchRadius.getText());
				if (searchRad <= avgAvgDev){
					statusLabel.setText("There are too many points in that radius!");
				}
				outlierFrame.setVisible(false);
				findDev();
				statusLabel.setText("Searched for outlying data points in the range of " + searchRad);
				outlierFrame.dispose();
			}
		});

		for(int i = 0; i < currentStar.maxPoints.length-1; i++){
			int index = data.indexOf(currentStar.maxPoints[i]);
			//			if ((data.get(i+1).difference < currentStar.avgDiff || data.get(i+1).difference < currentStar.avgDiff) && 
			//					currentStar.maxPoints[i].difference - currentStar.maxPoints[i+1].difference > 2 * currentStar.avgDiff &&
			//					avgAvgDev*1.5 < Math.abs(currentStar.maxPoints[i].getPDCFlux() - average)){
			if (currentStar.maxPoints[i].difference - currentStar.maxPoints[i+1].difference > 2 * currentStar.avgDiff &&
					avgAvgDev*1.5 < Math.abs(currentStar.maxPoints[i].getPDCFlux() - currentStar.getAverage())){
				final JButton jb = new JButton(index + ": " + currentStar.maxPoints[i].getTime());
				jb.setBackground(Color.white);
				outPanel.add(jb);
				jb.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						DataPoint daytah = data.get((int)doubleOf(jb.getText().substring(0,jb.getText().indexOf(':'))));
						centerOn(daytah);
					}
				});
			}
		}
	}

	public void centerOn(DataPoint dp){
		setGraphWidth(dp.getTime()-2,dp.getTime()+2);
		repaint();
		fitY();
		for (DataPoint daytah : data)
			daytah.setSize(2);
		dp.setSize(4);
		statusLabel.setText("Centered graph around data point " + data.indexOf(dp));
		repaint();
	}

	public double doubleOf(String s){
		double out=50;
		try{
			out = Double.parseDouble(s);
		}catch(Exception ee){
			statusLabel.setText("Invalid Input");
		}
		return out;
	}
	public int intOf(String s){
		int out=10;
		try{
			out = Integer.parseInt(s);
		}catch(Exception ee){
			statusLabel.setText("Invalid Input");
		}
		return out;
	}
	public int binaryFinder(double time){
		int low = 0;
		int high = data.size() - 1;
		int middle = 1;
		while(middle > low) {
			middle = (low + high) / 2;
			if(data.get(middle).getTime() < time) {
				low = middle + 1;
			}
			else if(data.get(middle).getTime() > time) {
				high = middle - 1;
			}
		}
		return middle;
	}

	//these define the scale of the graph, top and bottom numbers to use.
	//PDC_Flux usually ranges from ~10,000 to ~200,000
	double topGraph = 200000,
			bottomGraph = 0;
	double leftGraph = 160, rightGraph =173;

	LinkedList<Integer> displayPts = new LinkedList<Integer>();

	public class computor implements Runnable{
		int left;
		int skip;
		boolean stop=false;
		public void stop(){
			stop = true;
		}
		public void goPaint(){
			left = binaryFinder(leftGraph);
			skip = (int)(((data.size()*(rightGraph-leftGraph))/ (data.get(data.size()-1).getTime() - data.get(0).getTime()))/getWidth());
			if(skip <= 0)
				skip = 1;
			calculateXY();
			drawGraph(gPanel.getGraphics());
			if(skip == 1)
				return;
			skip = 1;
			calculateXY();
			drawGraph(gPanel.getGraphics());
		}
		public void calculateXY(){
			for(int i = left; i < data.size() && !stop; i+=skip){
				DataPoint dp = data.get(i);
				double incY = ((double)gPanel.getHeight() / (topGraph - bottomGraph));
				int pdcY = gPanel.getHeight() - (int)(incY * (dp.getPDCFlux() - bottomGraph));

				double incX = ((double)gPanel.getWidth() / (rightGraph - leftGraph));
				int pdcX = (int)(incX * (dp.getTime() - leftGraph));

				dp.setY(pdcY);
				dp.setX(pdcX);
				if(pdcX>gPanel.getWidth())
					break;
			}
		}
		public void drawGraph(Graphics g){
			//gPanel.pcG();
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.white);
			if(data != null && !stop){
				int lastY = 0;
				int lastX = 0;
				for(int i = left; i < data.size() && !stop; i+=skip){
					DataPoint dp = data.get(i);
					g.setColor(Color.black);
					g.fillRect(lastX+5, 0, dp.getX()-lastX, gPanel.getHeight());
					dp.paint(g2);
					g.setColor(Color.white);
					if(lastX !=0  && lastY != 0){
						g2.drawLine(lastX, lastY, dp.getX(), dp.getY());
					}
					lastY = dp.getY();
					lastX = dp.getX();
					if(dp.getX()>gPanel.getWidth())
						break;
				}
			}
			g2.drawString(String.valueOf(topGraph), gPanel.getWidth()/2, 10);
			g2.drawString(String.valueOf(bottomGraph), gPanel.getWidth()/2, gPanel.getHeight()-10);
			g2.drawString(String.valueOf(leftGraph), 10, 10);
			g2.drawString(String.valueOf(rightGraph), gPanel.getWidth()-55, 10);
			//g.fillOval(500, 400, 30, 20);
		}
		public void run() {
			goPaint();
		}
	}

	public class GraphPanel extends JPanel{
		Thread tt = null;
		int skip = 16;
		computor c=null;

		public void pcG(){
			super.paintComponent(getGraphics());
		}

		public void paintComponent(Graphics g){
			int scrollamt;
			if(!scrolling){
				scrollamt = 1;
//				if(c!=null)
//					c.stop();
//				//super.paintComponent(g);
//				c = new computor();
//				tt = new Thread(c);
//				tt.start();
			}
			else{
				scrollamt = (int)(((data.size()*(rightGraph-leftGraph))/ (data.get(data.size()-1).getTime() - data.get(0).getTime()))/getWidth());
			}
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				//roundAxes();
				super.paintComponent(g);
				g.setColor(Color.white);
				if(data != null){
					int lastY = 0;
					int lastX = 0;
					int sclog = (int)(Math.log(scrollamt) / Math.log(2));
					scrollamt = (int) Math.pow(2, sclog);
					if(scrollamt <=0)
						scrollamt=1;
					for(int i = binaryFinder(leftGraph); i < data.size(); i+=scrollamt){
						DataPoint dp = data.get(i);
						double incY = ((double)this.getHeight() / (topGraph - bottomGraph));
						int pdcY = this.getHeight() - (int)(incY * (dp.getPDCFlux() - bottomGraph));

						double incX = ((double)this.getWidth() / (rightGraph - leftGraph));
						int pdcX = (int)(incX * (dp.getTime() - leftGraph));
						//System.out.println(pdcX);
						dp.setY(pdcY);
						dp.setX(pdcX);
						if(!scrolling)
							dp.paint(g);
						g.setColor(Color.white);
						if(lastX !=0  && lastY != 0){
							g2.drawLine(lastX, lastY, pdcX, pdcY);
						}
						lastY = pdcY;
						lastX = pdcX;
						if(pdcX>this.getWidth())
							break;
				}
			}
		}

	}

}
