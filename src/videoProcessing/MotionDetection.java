package videoProcessing;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;

import org.opencv.imgproc.Imgproc;

public class MotionDetection {
	
	public static ArrayList<Point> points = new ArrayList<Point>();
	public static boolean read = true;
	public static int WIDTH = 640;
	public static int HEIGHT = 480;
	private static ArrayList<Point> bufferedPoints = new ArrayList<Point>();
	
	static int SENSITIVITY_VALUE = 20;
	static int SMALLEST_AREA = 2500;
	static int UPDATE_INTERVAL = 20;
	static int BLUR_SIZE = 15;
	static VideoCapture camera;
	static Timer timer;
	static Mat previousFrame = new Mat();
	static Mat currentFrame = new Mat();
	static Mat grayImage1 = new Mat();
	static Mat grayImage2 = new Mat();
	static Mat differenceImage = new Mat();
	static Mat thresholdImage = new Mat();

	public static void init()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		camera = new VideoCapture();
		camera.open(0);
		timer = new Timer();
		
		TimerTask frameGrabber = new TimerTask() {
			@Override
	        public void run()
	        {
				readFromCamera();
	        }
	    };
	    timer = new Timer();
	    timer.schedule(frameGrabber, 0, UPDATE_INTERVAL);
	}
	
	public static void readFromCamera()
	{
		if(grayImage1.empty()){
			camera.read(previousFrame);
			
			Imgproc.cvtColor(previousFrame, grayImage1,
					Imgproc.COLOR_BGR2GRAY);
			return;
		}
		
		camera.read(currentFrame);
		Imgproc.cvtColor(currentFrame, grayImage2,
			Imgproc.COLOR_BGR2GRAY);
		
		Core.subtract(grayImage1, grayImage2, differenceImage);
		Imgproc.threshold(differenceImage, thresholdImage,
			SENSITIVITY_VALUE, 255, Imgproc.THRESH_BINARY);
		Imgproc.blur(thresholdImage, thresholdImage, new Size(
			BLUR_SIZE, BLUR_SIZE));
		Imgproc.threshold(thresholdImage, thresholdImage,
			SENSITIVITY_VALUE, 255, Imgproc.THRESH_BINARY);
					
		searchForMovement(thresholdImage, currentFrame);
		grayImage2.copyTo(grayImage1);
		
		if(read){
			read = false;
			points = bufferedPoints;
		}
	}
	
	static void searchForMovement(Mat thresholdImage, Mat frame)
	{
		bufferedPoints = new ArrayList<Point>();
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(thresholdImage, contours, hierarchy,
			Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		Rect objectBoundingRectangle = new Rect(0, 0, 0, 0);
		for (int i = 0; i < contours.size(); i++)
		{
			objectBoundingRectangle = Imgproc.boundingRect(contours.get(i));
			if(objectBoundingRectangle.area()>SMALLEST_AREA){
				Point p = new Point();
				p.x = (objectBoundingRectangle.tl().x + objectBoundingRectangle.br().x) / 2;
				p.y = (objectBoundingRectangle.tl().y + objectBoundingRectangle.br().y) / 2;
				bufferedPoints.add(new Point(p.x, p.y));
			}
		}
	}

}
