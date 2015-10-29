package data;

import java.util.Random;

import drawable.Color;
import drawable.Eye;
import processing.core.PApplet;

public class EyeEntity extends Entity{
	private Eye eye;
	private float speed;
	private float threshold;
	private float timer;
	private float timerThreshold;
	private Random random;
	public double trackX;
	public double trackWidth;
	
	public EyeEntity(float x, float y, float width, float height, float pupilRadius){
		this.x = x + width/2;
		this.y = y + height/2;
		eye = new Eye(0.7f * width / 2, 0.5f * height / 2, pupilRadius);
		eye.pupilColor = new Color(0, 0, 0);
		renderObject = eye;
		threshold = 5;
		speed = 0;
		random = new Random();
		
		timer = 0;
		timerThreshold = 200;
	}
	
	public void openEye(){
		speed = 0.08f;
	}
	
	public void closeEye(){
		speed = -0.08f;
	}
	
	@Override
	public void udpate(PApplet app) {
		float mouseInfluence = (float) ((trackX  - x) / trackWidth);
		if(mouseInfluence > 0.5){
			mouseInfluence = 0.5f;
		}
		if(mouseInfluence < -0.5){
			mouseInfluence = -0.5f;
		}
		float difference = Math.abs(mouseInfluence * eye.width - eye.pupilXPosition);
		if(difference < threshold && eye.openRatio >= 1){
			timer += 1;
			if(timer > timerThreshold){
				timer = timerThreshold;
			}
		}
		else{
			timer -= 50;
			if(timer < 0){
				timer = 0;
			}
		}
		eye.shakeX = (float)(random.nextFloat() - 0.5) * (timer / timerThreshold);
		eye.shakeY = (float)(random.nextFloat() - 0.5) * (timer / timerThreshold);
		eye.pupilColor.r = (int)(255 * (timer / timerThreshold));
		
		if(eye.openRatio < 1){
			eye.pupilColor. r = 0;
			eye.shakeX = 0;
			eye.shakeY = 0;
		}
		eye.pupilXPosition = mouseInfluence * eye.width;
		eye.openRatio += speed;
	}
}
