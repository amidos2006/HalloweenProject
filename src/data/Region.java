package data;

public class Region {
	public double x;
	public double y;
	public double width;
	public double height;
	
	public Region(double x, double y, double width, double height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean checkInRegion(double x, double y){
		return (x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height);
	}
}
