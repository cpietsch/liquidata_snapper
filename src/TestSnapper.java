

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import processing.core.*;
import processing.opengl.*;
import javax.media.opengl.GL;
import codeanticode.glgraphics.*;

import TUIO.*;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;
import de.fhpotsdam.unfolding.utils.MapUtils;



public class TestSnapper extends PApplet implements TuioListener {
	boolean presentMode =true;
	Vector<Phone> phones= new Vector();
	//Place[] places = new Place[50];
	Vector <Place>places = new Vector<Place>();
	protected TuioClient tuioClient;
	protected TuioListener tuioListener;
	
	public void setup(){
		//size(1024,786,GLConstants.GLGRAPHICS);
		//size(1680,1050, GLConstants.GLGRAPHICS);
		size(1440,900, GLConstants.GLGRAPHICS);
		hint(DISABLE_OPENGL_2X_SMOOTH);
		hint(ENABLE_OPENGL_4X_SMOOTH);
		hint(ENABLE_NATIVE_FONTS);
		hint(ENABLE_ACCURATE_TEXTURES);
		hint(DISABLE_DEPTH_TEST);
		hint(DISABLE_OPENGL_ERROR_REPORT);
		smooth();	
		frameRate(200);
		tuioClient = new TuioClient();
		tuioClient.addTuioListener(this);
		tuioClient.connect();
		
		background(255);
		//make random Places
		for (int i = 0; i < 5; i++) {
			places.addElement(new Place(this, random(width),random(height/2), "Place "+String.valueOf(i)));
			}
		//when Places were updated:
		for (Enumeration i=places.elements();i.hasMoreElements();) {
			Place p1=(Place)i.nextElement();
			for (Enumeration j=places.elements();j.hasMoreElements();) {
				Place p2=(Place)j.nextElement();
				if (PVector.sub(p1.getPosition(),p2.getPosition()).mag() < 120) {
					if (!p1.equals(p2))p1.addNeighbor(p2);
	          }
			}
	      }
	}
	
	
	 public void draw() {
		 background(132);
		 println(frameRate);
		 //Places updaten & Zeichnen
			for (Enumeration e=places.elements();e.hasMoreElements();) {
				Place p = ((Place)e.nextElement());
				p.update();
				p.display();
			}
		//Phones updaten & Zeichnen
		Vector tuioObjectList = tuioClient.getTuioObjects();
	    for (int i=0;i<tuioObjectList.size();i++) {
			  if(tuioObjectList.size()>0){
			    TuioObject tobj = (TuioObject)tuioObjectList.elementAt(i);
			    for (Enumeration e=phones.elements();e.hasMoreElements();) {
					Phone p =(Phone) e.nextElement();
					if(tobj.getSymbolID()==p.getID()){
						
						p.update();
						p.display();
					  }
			      }	  
			  } 
		    }
	    
	    //TUIO Cursor Zeichnen
		 Vector tuioCursorList = tuioClient.getTuioCursors();
		   for (int i=0;i<tuioCursorList.size();i++) {
		      TuioCursor tcur = (TuioCursor)tuioCursorList.elementAt(i);
		      Vector pointList = tcur.getPath();
		      if (pointList.size()>0) {
		        fill(192);
		        ellipse(tcur.getScreenX(width), tcur.getScreenY(height),10,10);
		        fill(0);
		        text(""+ tcur.getCursorID(),  tcur.getScreenX(width)-5,  tcur.getScreenY(height)+5);
		      }
		   }		   
	 }
	 
	// these callback methods are called whenever a TUIO event occurs

	// called when an object is added to the scene
	 public void addTuioObject(TuioObject tobj) {
		 boolean b=true;
		 for (Enumeration e=phones.elements();e.hasMoreElements();) {
				if(tobj.getSymbolID()==((Phone)e.nextElement()).getID())b=false;
		     }
		if(b)phones.add(new Phone (this,tobj.getSymbolID(), tobj.getScreenX(width), tobj.getScreenY(height), tobj.getAngle()));
	}

	// called when an object is removed from the scene
	public void removeTuioObject(TuioObject tobj) {
	  println("remove object "+tobj.getSymbolID());
	}

	// called when an object is moved
	public void updateTuioObject (TuioObject tobj) {
		
		for (Enumeration e=phones.elements();e.hasMoreElements();) {
			Phone p =(Phone)e.nextElement();
			if(tobj.getSymbolID()==p.getID()){
				p.setLocation(tobj.getScreenX(width),tobj.getScreenY(height),tobj.getAngle());
				p.setPlace(places);
				p.update();
				}
			}
	}

	// called when a cursor is added to the scene
	public void addTuioCursor(TuioCursor tcur) {
		for (Enumeration e=phones.elements();e.hasMoreElements();) {
			((Phone)e.nextElement()).startTouch(tcur.getScreenX(width),tcur.getScreenY(height));
	      }
		}

	// called when a cursor is moved
	public void updateTuioCursor (TuioCursor tcur) {
		for (Enumeration e=phones.elements();e.hasMoreElements();) {
			((Phone)e.nextElement()).updateTouch(tcur.getScreenX(width),tcur.getScreenY(height));
	      }
	}

	// called when a cursor is removed from the scene
	public void removeTuioCursor(TuioCursor tcur) {
		for (Enumeration e=phones.elements();e.hasMoreElements();) {
			((Phone)e.nextElement()).stopTouch();
	      }
	}

	// called after each message bundle
	// representing the end of an image frame
	public void refresh(TuioTime bundleTime) { 
	  redraw();
	}	

	public static void main(String[] args) {

	    int primary_display = 0; //index into Graphic Devices array...  

	      int primary_width;
	      int primary_height;

	      GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	      GraphicsDevice devices[] = environment.getScreenDevices();
	       String location;
	       if(devices.length>1 ){ //we have a 2nd display/projector
	    	   println(devices[1].getType());
	          // primary_width = devices[0].getDisplayMode().getWidth();
	    	   //location = "--location="+primary_width+",0";
	    	   primary_height = devices[0].getDisplayMode().getHeight();
	    	   location = "--location=0,"+ primary_height;

	       }else{//leave on primary display
	           location = "--location=0,0";

	       }

	      String display = "--display="+primary_display+1;  //processing considers the first display to be # 1
	        PApplet.main(new String[] { location , "--hide-stop", display,"test_snapper" });  // Der letzte String muss der Pfad zu Deiner Klasse sein

	}

	public void init() {
	setWindowBorder();
	super.init();
	}
	public void setWindowBorder() {
	if (frame != null && presentMode) {
		frame.removeNotify();
		frame.setResizable(false);
		frame.setUndecorated(true);
		frame.setLocation(0, 0);
		frame.addNotify();
		}
	}
}
