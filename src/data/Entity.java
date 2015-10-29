package data;

import drawable.Drawable;
import processing.core.PApplet;

public class Entity implements Updatable{
	public float x;
	public float y;
	public Drawable renderObject;
	
	public Entity(){
		x = 0;
		y = 0;
		renderObject = null;
	}
	
	@Override
	public void udpate(PApplet app) {
		
	}
	
	public void render(PApplet app) {
		if(renderObject != null){
			renderObject.render(app, x, y);
		}
	}
}
