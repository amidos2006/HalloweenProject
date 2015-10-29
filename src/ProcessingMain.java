import org.opencv.core.Core;

import processing.core.PApplet;

public class ProcessingMain {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	    PApplet.main(new String[] { "--present", "halloweenproject.HalloweenProject" });
	}
}
