package data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import drawable.Color;
import drawable.Drawable;
import processing.core.PApplet;
import processing.core.PConstants;

public class SpookyText implements Drawable, Updatable{
	private static int MAX_LIVE = 100;
	private static int APPEARING_SPEED = 50;
	private static int CHANGE_TIME = 20000;
	private static int TIME_PERIOD = 3;
	private static int TEXT_SIZE = 42;
	private static Color currentColor = new Color(255, 0, 0);
	
	private ArrayList<String>[] timedText;
	private int currentText;
	private int numberOfOpenEyes;
	private double colorIntense;
	private int changeTimer;
	private int currentTime;
	private Random random;
	
	public SpookyText() {
		numberOfOpenEyes = 0;
		colorIntense = 0;
		changeTimer = 0;
		currentTime = LocalDateTime.now().getHour() / TIME_PERIOD;
		random = new Random();
		timedText = new ArrayList[8];
		for(int i=0; i<24/TIME_PERIOD; i++){
			String filename = "SpookyText/" + i + ".txt";
			timedText[i] = new ArrayList<String>();
			timedText[i].addAll(data.SpookyIO.readFile(filename));
		}
		currentText = random.nextInt(timedText[currentTime].size());
		
	}
	
	public void setOpenEyesNumber(int number){
		numberOfOpenEyes = number;
	}
	
	@Override
	public void udpate(PApplet app) {
		currentTime = LocalDateTime.now().getHour() / TIME_PERIOD;
		
		if(numberOfOpenEyes <= 0){
			colorIntense -= 1.0/MAX_LIVE;
			if(colorIntense < 0){
				colorIntense = 0;
			}
		}
		else{
			colorIntense += 1.0 / APPEARING_SPEED;
			if(colorIntense > 1){
				colorIntense = 1;
			}
		}
		
		changeTimer += 1;
		if(changeTimer > CHANGE_TIME){
			changeTimer = 0;
			currentText = (currentText + random.nextInt(timedText[currentTime].size() - 1) + 1) % timedText[currentTime].size();
		}
	}

	@Override
	public void render(PApplet app, float x, float y) {
		app.fill((int)(colorIntense * currentColor.r),(int)(colorIntense * currentColor.g),(int)(colorIntense * currentColor.b));
		app.textAlign(PConstants.CENTER);
		app.textSize(TEXT_SIZE);
		app.text(timedText[currentTime].get(currentText), x, y);
	}

}
