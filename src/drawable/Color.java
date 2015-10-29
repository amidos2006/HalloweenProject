package drawable;

public class Color {
	public static Color BLACK = new Color(0);
	public static Color WHITE = new Color(255);
	
	public int r;
	public int g;
	public int b;
	
	public Color(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color(int i){
		this.r = i;
		this.g = i;
		this.b = i;
	}
}
