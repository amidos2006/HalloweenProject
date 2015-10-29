package videoProcessing;

import org.opencv.core.Point;

public class DataPoint {
	public Point point;
	public int regionIndex;
	
	public DataPoint(double x, double y, int index){
		point = new Point(x, y);
		regionIndex = index;
	}
	
	public double getDistance(DataPoint p){
		return Math.sqrt(Math.pow(p.point.x - this.point.x, 2) + Math.pow(p.point.y - this.point.y, 2));
	}
}
