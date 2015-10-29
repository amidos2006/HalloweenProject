package drawable;

import processing.core.PApplet;

public class Eye implements Drawable{
	public float width;
	public float height;
	public float pupilRadius;
	public float pupilXPosition;
	public Color pupilColor;
	public float openRatio;
	public float shakeX;
	public float shakeY;
	
	public Eye(float width, float height, float pupilRadius){
		this.width = width;
		this.height = height;
		this.pupilRadius = pupilRadius;
		this.pupilXPosition = 0;
		
		this.openRatio = 0;
	}
	
	@Override
	public void render(PApplet app, float x, float y){
		if(openRatio > 1){
			openRatio = 1;
		}
		if(openRatio < 0){
			openRatio = 0;
		}
		
		app.stroke(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b);
		app.fill(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b);
		app.ellipse(x, y, 2 * width, 2 * height * openRatio);
		
		float pX = pupilXPosition;
		float direction = -0.2f;
		if(y < app.height / 2){
			direction = 1;
		}
		float pY = direction * (float)(((height - pupilRadius / 2)) * Math.sqrt(1 - Math.pow(pX / (width - pupilRadius / 2), 2)));
		app.stroke(pupilColor.r, pupilColor.g, pupilColor.b);
		app.fill(pupilColor.r, pupilColor.g, pupilColor.b);
		app.ellipse(x + pX + shakeX * 10, y + pY + shakeY * 10, pupilRadius, pupilRadius);
	}
}
