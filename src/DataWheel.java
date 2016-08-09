import java.util.Collections;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import megamu.shapetween.*;

public class DataWheel {
	
	PApplet parent;
	float x;
	float y;
	float dir;
	
	float diameter;
	float sliderDir;
	float sliderSize;

	int ID;
	int TOUCH_AREA;
	int dataSize;
	Vector <DataElement> elements = new Vector<DataElement>();
	float scaleElements;
	Tween startTween;
	float scale;
	
    DataWheel(PApplet parent,int ID,float x,float y,float dir){
    	this.parent = parent;
    	this.ID=ID;
        this.x=x;
        this.y=y;
        this.dir=dir;
        TOUCH_AREA=0;
        diameter=340;
    	sliderDir=-parent.PI/4;
    	sliderSize=parent.PI/3;
    	
    	for (int i = 1; i < 10; i++) {
    		elements.add(new DataElement(this.parent,(parent.random(3)+2)*200,(parent.random(3)+2)*200));
    	}
    	scaleElements=0;
    	startTween = new Tween(parent, .8f, Tween.SECONDS, Shaper.COSINE);
 	    startTween.setPlayMode(Tween.ONCE);
 	    startTween.end();
 	    scale=0;
    }
	void update() {
		
	}
	void display() {
		
		if(startTween.isTweening())scale=startTween.position();
		else{
			if(scale>.5)scale=1;
			else scale=0;
		}
		parent.pushMatrix();
		parent.translate(x,y);

		parent.scale(scale);
		parent.rotate(dir);

		parent.noStroke();
		parent.fill(240,30);
		RingSlice r = new RingSlice(parent,  diameter,  30,  0, parent.PI*2);
		parent.noFill();
		parent.strokeWeight(1);
		parent.stroke(252);
		parent.ellipse(0,0,diameter-15,diameter-15);
		parent.strokeWeight(1);
		parent.noStroke();
		parent.fill(240,164,12);
		RingSlice slider = new RingSlice(parent,  diameter,  30,  sliderDir-sliderSize/2, sliderDir+sliderSize/2);
		
		parent.pushMatrix();
		parent.translate(-diameter/2f+20,-diameter/2f+20);
			float size=7.5f;
			parent.noStroke();
			parent.fill(240,30);
			parent.ellipse(0,0,30,30);
			parent.noFill();
			parent.stroke(252);
			parent.strokeWeight(1.2f);
			parent.line(-size,-size,size,size);
			parent.line(size,-size,-size,size);
			parent.strokeWeight(1);
			parent.noStroke();
		parent.popMatrix();
		for(int i=0;i<elements.size();i++){
			DataElement e=elements.get(i);

			e.setScale(parent.norm(i, -elements.size()/2, elements.size())+scaleElements);
			e.display();
			}
		parent.popMatrix();

	}
	void setSize(float diameter) {
		this.diameter = diameter;
	}
	void setLocation(float x,float y,float dir){
		this.dir = dir;
	    this.x = x;
	    this.y = y;
	    }
	
	
	void updateTouch(float x,float y){
		float d =  parent.abs(parent.atan2(x-this.x,y-this.y)-parent.PI)-parent.HALF_PI-dir;
		
		if(TOUCH_AREA==1){
			int w=1;
			if(sliderDir-d>0){
				scaleElements+=0.01f;
			}
			else {
				scaleElements-=0.01f;
				w*=-1;
			}
			//parent.println(elements.lastElement().getScale());
			if(elements.lastElement().getScale()>=1 ||elements.lastElement().getScale()<=0.875f){
				scaleElements=0;
				Collections.rotate(elements,w);
				}
			sliderDir=d;
			}
	
		if(TOUCH_AREA==3){
			DataElement e= elements.lastElement();
			e.setPosition(x,y);
		}
	   }
	
	
	 void startTouch(float x,float y){
		 parent.println("start");
		 PVector c= new PVector(x,y);
		 PVector l= new PVector(this.x,this.y);
		 float d =  parent.abs(parent.atan2(x-this.x,y-this.y)-parent.PI)-parent.HALF_PI-dir;
		 
		 if(l.dist(c) >diameter/2-30f && l.dist(c) < diameter/2+10){
			 if(Math.abs(d-sliderDir) < sliderSize/2){
				 TOUCH_AREA=1;
				 parent.println("slider");
				 //Collections.rotate(elements,-1);
			 }
		 }
		 if(l.dist(c) >diameter/2f+25f && l.dist(c) < diameter/2f+55){
			 if(Math.abs(d-1.25f*parent.PI)<parent.PI/36){
				 TOUCH_AREA=2;
				 parent.println("close");
				 startTween.end();
				 startTween.setPlayMode( Tween.REVERSE_ONCE);
				 startTween.start();
				 startTween.seek(1);
				// scale=0;
				 
			 }
		 }
		 if(l.dist(c) < diameter/2f-20f){
				 TOUCH_AREA=3;
				 parent.println("image");
			 
		 }

	 }
	 void stopTouch(){
	 		if(TOUCH_AREA!=0)TOUCH_AREA=0;
	 		sliderDir=(parent.round(18*sliderDir/parent.PI))/18f*parent.PI;
			DataElement e= elements.lastElement();
			e.setPosition(0,0);
	 }
	 
	 void push(PImage img){
		 DataElement e= new DataElement(parent,PImage.A,PImage.B);
		 e.setImage(img);
		 elements.add(e);
		}
	 void push(String txt){
		 DataElement e=new DataElement(parent,400,200);
		 e.setText(txt);
		 elements.add(e);
		}
	 DataElement peek(){
		 return elements.lastElement();
		}
	 void start() {
		 scale=1;
		 startTween.setPlayMode(Tween.ONCE);
		 startTween.start();

		}
	 boolean isVisible() {
		 return scale>0;
		}
	 
	 boolean isTouched(float x, float y) {
			PVector c= new PVector(x,y);
			PVector l= new PVector(this.x,this.y);
			//float d =  parent.abs(parent.atan2(x-this.x,y-this.y)-parent.PI)-parent.HALF_PI-dir;
			return(l.dist(c)<(diameter/2+10f));
		}
}
