import processing.core.PApplet;
import processing.core.PVector;

import processing.opengl.*;
import javax.media.opengl.GL;

import megamu.shapetween.*;


public class Connection {
	 PApplet parent; //parent von PApplet
	 PVector loc1;
	 PVector loc2;
	 PVector handler1;
	 PVector handler2;
	 PVector offset;
	 PVector o,o2; 
	 int c;
	// float pulseValue;
	 boolean isMainSnapper;
	 float startDiameter;
	 
	 Tween pulseTween,startTween,dataTween;

	  Connection(PApplet parent,PVector start, PVector end,  float dir, int strokeColor) {
		this.parent = parent;
	    loc1 = start.get();
	    loc2 = end.get();
	    o = new PVector(-parent.cos(dir),-parent.sin(dir));
	    handler1 = PVector.sub(loc1,o);
	  	handler2 = PVector.add(loc2,o);
	  	c = strokeColor;
	  	isMainSnapper=false;
	  	
	  	pulseTween = new Tween(parent, .5f, Tween.SECONDS, Shaper.COSINE);
	    pulseTween.setPlayMode( Tween.REVERSE_REPEAT );
	    pulseTween.start();
	    
		startTween = new Tween(parent, 1.5f, Tween.SECONDS, Shaper.OUT);
		startTween.setPlayMode( Tween.ONCE );
		startTween.start();
		
		dataTween = new Tween(parent, 2.5f, Tween.SECONDS, Shaper.OUT);
		dataTween.setPlayMode( Tween.ONCE );
		dataTween.end();
		startDiameter=137.355f-10f;
	  }

	  void update(PVector start, PVector end, float dir) {
		
	     loc1 = start.get();
	     loc2 = end.get();
	     dir-=parent.HALF_PI;
	     o = new PVector(parent.cos(dir),parent.sin(dir));
		
	    
	     //Curve-Shape, when rotated
	     o.normalize();
	     float view = PVector.angleBetween(PVector.sub(loc1,loc2),o);
	     
	     if(view>parent.PI/4){
	    	 if(view>parent.HALF_PI)view=parent.PI;
	    	 else view*=2;
	    	 if(PVector.angleBetween(PVector.sub(loc1,loc2),new PVector(parent.cos(dir+parent.HALF_PI),parent.sin(dir+parent.HALF_PI)) )<parent.HALF_PI)view*=-1;
	     }
	     else view=0;
	     
		 PVector oOffset = new PVector(parent.cos(dir-view),parent.sin(dir-view));
		 oOffset.normalize();
	     loc1.sub(PVector.mult(o,startDiameter));
	     o.mult( parent.floor(loc1.dist(loc2))/2);
	     oOffset.mult( parent.floor(loc1.dist(loc2))/2);
	     
	     handler1 = PVector.sub(loc1,o);
	     handler2 = PVector.add(loc2,oOffset);
	    // handler2 = PVector.add(loc2,o);
	     
	     o.normalize();
	     
	     float pulse = parent.lerp( -parent.PI/8, parent.PI/8, pulseTween.position() );
	     
	     o2 = new PVector(parent.cos(dir-pulse+parent.PI),parent.sin(dir-pulse+parent.PI));
	     o2.normalize();

	  }
	  float getStrokeWeight(){
	     float w =1;
	    return w;
	  }
	  
	  void setMainSnapper(boolean isMainSnapper){
		  this.isMainSnapper=isMainSnapper;
		  
		  if(isMainSnapper)startDiameter=137.355f;
		  else startDiameter=137.355f-10f;
		  }
	  float[] getBezierPoints(){
		  float bezierPoints[]={
				  loc1.x,
				  loc1.y,
				  loc1.y,
				  loc2.y,
				  handler1.x,
				  handler1.y,
				  handler1.x,
				  handler2.y
		  };
		  return bezierPoints;
	  }
	
	  
	  void display() {

		parent.noFill();
		parent.bezierDetail(20);
		parent.stroke(c);
		parent.strokeWeight(2);
		
		if(startTween.isTweening()&& isMainSnapper){
			Shaper s = new LinearShaper(Shaper.OUT);
			parent.beginShape();
			for( float i=0; i<=startTween.position(); i+= 0.015 ){
				parent.vertex( parent.bezierPoint(loc1.x ,handler1.x, handler2.x, loc2.x, s.shape( i )), parent.bezierPoint(loc1.y ,handler1.y, handler2.y, loc2.y, s.shape( i ) ) );
				if(startTween.position()==1)startTween.end();
			}
			parent.endShape();
			}
		else{
			parent.bezier(loc1.x , loc1.y ,handler1.x, handler1.y,handler2.x, handler2.y, loc2.x, loc2.y);
		}
		if(isMainSnapper){
  		if(dataTween.isTweening()){
  			 float size = parent.lerp( 8, 12, pulseTween.position() );
			 parent.noStroke();
			 parent.fill(c);
			 parent.ellipse(  parent.bezierPoint(loc1.x ,handler1.x, handler2.x, loc2.x, dataTween.position()), parent.bezierPoint(loc1.y ,handler1.y, handler2.y, loc2.y, dataTween.position()),size,size);
			 parent.noFill();
		}

		//zeichne Innere Linie
		
			PVector offset = PVector.add(PVector.mult(o,startDiameter),loc1);
			o2.mult( parent.floor(loc1.dist(offset))*1/3/1.2f);
			o.mult( parent.floor(loc1.dist(offset))*1/3*1.2f);
			PVector h1 = PVector.add(loc1,o);
			PVector h2 = PVector.add(offset,o2);
		
			parent.stroke(c);
			parent.strokeWeight(2);
			parent.noFill();
			parent.bezier(loc1.x, loc1.y,h1.x, h1.y,h2.x, h2.y,offset.x, offset.y);
			o.normalize();
		}
		
		parent.bezierDetail(24);
	    parent.strokeWeight(1);
		parent.noStroke();
	  }
	  
	  void sendData(){
			dataTween.start();

	  }
	  void start(){
		  startTween.start();
		  parent.println("start");
	  }
	}