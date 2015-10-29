package data;

import java.util.ArrayList;

import processing.core.PApplet;
import videoProcessing.DataPoint;

public class Agent implements Updatable{
	private int MAX_ALIVE = 30;
	private double MIN_DISTANCE = 0.2;
	private double DESTROY_DISTANCE = 100;
	private int MAX_DESTROY_TIMER = 30;
	
	private DataPoint currentPoint;
	private int aliveTimer;
	private int destroyTimer;
	private boolean positionUpdated;
	private ArrayList<DataPoint> updatePoints;
	
	public boolean alive;
	
	public Agent(double x, double y, int index){
		currentPoint = new DataPoint(x, y, index);
		updatePoints = new ArrayList<DataPoint>();
		aliveTimer = 0;
		destroyTimer = 0;
		alive = true;
		positionUpdated = false;
	}
	
	public boolean checkAndUpdate(DataPoint p){
		if(currentPoint.regionIndex == p.regionIndex){
			positionUpdated = true;
			aliveTimer = 0;
			destroyTimer = 0;
			updatePoints.add(p);
			
			return true;
		}
		
		return false;
	}
	
	public boolean checkAgent(Agent a, PApplet app){
		return a.currentPoint.getDistance(this.currentPoint) <= MIN_DISTANCE * app.width;
	}
	
	public DataPoint getDataPoint(){
		return currentPoint;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	@Override
	public void udpate(PApplet app) {
		if(!positionUpdated){
			aliveTimer += 1;
			if(currentPoint.point.x < DESTROY_DISTANCE || currentPoint.point.y < DESTROY_DISTANCE || 
					currentPoint.point.x > app.width - DESTROY_DISTANCE || currentPoint.point.y > app.displayHeight){
				destroyTimer += 1;
			}
		}
		else{
			double avgX = 0;
			double avgY = 0;
			for(DataPoint p:updatePoints){
				avgX += p.point.x;
				avgY += p.point.y;
			}
			
			currentPoint.point.x = avgX / updatePoints.size();
			currentPoint.point.y = avgY / updatePoints.size();
			updatePoints.clear();
		}
		
		if(destroyTimer > MAX_DESTROY_TIMER){
			alive = false;
		}
		
		if(aliveTimer > MAX_ALIVE){
			alive = false;
		}
		
		positionUpdated = false;
	}
}
