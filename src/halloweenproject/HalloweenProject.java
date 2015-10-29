package halloweenproject;

import java.util.ArrayList;
import java.util.Random;

import org.opencv.core.Point;

import data.Agent;
import data.Entity;
import data.EyeEntity;
import data.Region;
import data.SpookyText;
import processing.core.PApplet;
import videoProcessing.DataPoint;


public class HalloweenProject extends PApplet {
	
	ArrayList<Region> regions;
	ArrayList<Entity> entities;
	ArrayList<Agent> agents;
	SpookyText text;
	Random random;
	
	public void settings() {
		fullScreen();
	}
	
	public void setup() {
		background(0);
		random = new Random();
		entities = new ArrayList<Entity>();
		regions = new ArrayList<Region>();
		agents = new ArrayList<Agent>();
		text = new SpookyText();
		
		float frameWidth = width / 6;
		float frameHeight = height / 4;
		for(int y=0; y<3; y++){
			for(int x=0; x<6; x++){
				EyeEntity testEye = new EyeEntity(x * frameWidth, y * frameHeight, frameWidth, frameHeight, 0.3f * frameHeight);
				testEye.trackWidth = width;
				entities.add(testEye);
			}
		}
		videoProcessing.MotionDetection.init();
		
		frameWidth = width / 6;
		frameHeight = height / 3;
		for(int y=0; y< 3; y++){
			for(int x=0; x<6; x++){
				regions.add(new Region(x * frameWidth, y * frameHeight, frameWidth, frameHeight));
			}
		}
		
	}
	
	public void draw() {
		clear();
		
		if(!videoProcessing.MotionDetection.read){
			int size = videoProcessing.MotionDetection.points.size();
			ArrayList<Point> points = videoProcessing.MotionDetection.points;
			
			ArrayList<Agent> newAgents = new ArrayList<Agent>();
			for(int i=0; i<size; i++){
				Point p = new Point();
				p.x = (videoProcessing.MotionDetection.WIDTH - points.get(i).x) / videoProcessing.MotionDetection.WIDTH * width;
				p.y = points.get(i).y / videoProcessing.MotionDetection.HEIGHT * height;
				
				int rIndex = 0;
				for(rIndex=0; rIndex < regions.size(); rIndex++){
					if(regions.get(rIndex).checkInRegion(p.x, p.y)){
						break;
					}
				}
//				
//				DataPoint point = new DataPoint(p.x, p.y, rIndex);
//				
//				boolean anyUpdate = false;
//				for(Agent a:agents){
//					if(a.checkAndUpdate(point)){
//						anyUpdate = true;
//						break;
//					}
//				}
//				
//				if(!anyUpdate){
//					agents.add(new Agent(point.point.x, point.point.y, point.regionIndex));
//				}
				
				DataPoint point = new DataPoint(p.x, p.y, rIndex);
				
				boolean anyUpdate = false;
				for(Agent a:newAgents){
					if(a.checkAndUpdate(point)){
						anyUpdate = true;
						break;
					}
				}
				
				if(!anyUpdate){
					newAgents.add(new Agent(point.point.x, point.point.y, point.regionIndex));
				}
			}
			
			for(Agent a:newAgents){
				a.udpate(this);
				for(Agent a2:agents){
					if(a.checkAgent(a2, this)){
						a.alive = false;
					}
				}
				
				agents.add(a);
			}
			
			ArrayList<Agent> requireDelete = new ArrayList<Agent>();
			for(int i=0; i<agents.size(); i++){
				agents.get(i).udpate(this);
				if(!agents.get(i).isAlive()){
					requireDelete.add(agents.get(i));
				}
			}
			
			for(Agent a:requireDelete){
				agents.remove(a);
			}
			
			text.setOpenEyesNumber(agents.size());;
			
			for(Entity e:entities){
				EyeEntity eye=(EyeEntity)e;
				eye.closeEye();
			}
			
			for(int i=0; i<agents.size(); i++){
				EyeEntity eye=(EyeEntity)entities.get(agents.get(i).getDataPoint().regionIndex);
				eye.openEye();
				eye.trackX = agents.get(i).getDataPoint().point.x;
			}
			videoProcessing.MotionDetection.read = true;
		}
		
//		for(Region r:regions){
//			fill(0);
//			stroke(255);
//			rect((float)r.x, (float)r.y, (float)r.width, (float)r.height);
//		}
//		
//		for(Agent a:agents){
//			fill(255);
//			ellipse((float)a.getDataPoint().point.x, (float)a.getDataPoint().point.y, 40, 40);
//			stroke(0);
//			fill(0);
//			textAlign(CENTER);
//			textSize(20);
//			text(a.getDataPoint().regionIndex, (float)a.getDataPoint().point.x, (float)a.getDataPoint().point.y);
//		}
		
		for(Entity e:entities){
			e.udpate(this);
			e.render(this);
		}
		
		text.udpate(this);
		text.render(this, width / 2, 7 * height / 8);
		
		System.out.println("---------------");
	}
}
