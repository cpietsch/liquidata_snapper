import megamu.shapetween.*;
import processing.core.*;
import processing.opengl.*;
import javax.media.opengl.GL;
import codeanticode.glgraphics.*;
import processing.core.PVector;


public class TestDataWheel extends PApplet{

	Tween t1, pulseTween,t2,t3;
	Connection connection;
	DataWheel dw;
	Metaball mb;
	//Place place;
	public void setup(){
		size( 800, 800,GLConstants.GLGRAPHICS);
		hint(DISABLE_OPENGL_2X_SMOOTH);
		hint(ENABLE_OPENGL_4X_SMOOTH);
		hint(ENABLE_NATIVE_FONTS);
		hint(ENABLE_ACCURATE_TEXTURES);
		hint(DISABLE_DEPTH_TEST);
		hint(DISABLE_OPENGL_ERROR_REPORT);
		smooth();	
		colorMode( HSB );
		smooth();
		frameRate(200);
	  
		t1 = new Tween(this, 5, Tween.SECONDS, Shaper.COSINE);
		t1.setPlayMode( Tween.REVERSE_REPEAT );
		t1.start();
		
		int c = color(240,164,12);
		connection = new Connection(this, new PVector(50,50), new PVector(350,350), 0, c);
		pulseTween = new Tween(this, .5f, Tween.SECONDS, Shaper.COSINE);
	    pulseTween.setPlayMode( Tween.REVERSE_REPEAT );
	    pulseTween.start();
	    t2 = new Tween(this, 4, Tween.SECONDS, Shaper.OUT);
	    
		t2.start();
		
		 t3 = new Tween(this, 3, Tween.SECONDS, Shaper.OUT);
		 t3.setPlayMode( Tween.REPEAT );
		 t3.start();
		
		 mb = new Metaball(this, width/2, height/2,random(8)+50,random(80)+50, random(80)+50);
		 dw = new DataWheel(this, 0, width/2, height/2,0);
		
		 dw.push( loadImage("test.jpg"));
		 dw.push("Wirklich sehr schön hier. Da muss man unbedingt mal hin! Die grünen Wiesen und der Park laden zum sonnen ein.");
		 dw.start();
	}

	public void draw(){
		//println(frameRate);
	  background( 180 );
	  noStroke();
  
	  	mb.update();
	  	mb.display();

	  dw.update();
	  dw.display();

	}

	 public void mousePressed() {
		if(!dw.isVisible() && mb.isTouched(mouseX,mouseY) ){
				PVector m = mb.getTouched();
				dw.setLocation(m.x, m.y, 0);
				dw.start();
		}
		else dw.startTouch(mouseX,mouseY);
		
		// mb.startTouch(mouseX,mouseY);
		// println(dw.scale);
		 
		// if(!dw.isVisible())dw.start();
		// else dw.startTouch(mouseX,mouseY);
		}
	public void mouseDragged() {
		fill(0);
		ellipse(mouseX,mouseY,10,10);
		dw.updateTouch(mouseX,mouseY);
		noFill();
		// mb.updateTouch(mouseX,mouseY);
		}
	public void mouseReleased() {
		 //mb.stopTouch();
		 dw.stopTouch();


		//dw.stopTouch();
		}
}
