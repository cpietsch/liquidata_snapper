import java.util.Collections;

import processing.core.PApplet;
import processing.core.PVector;
import megamu.shapetween.*;

public class Metaball {
	
	
	float x;
	float y;
	PVector m[]= new PVector[3];
	float 	r[]= new float[3];
	float arc[]= new float[3];
	PApplet parent;
	Tween pulseTween,startTween;
	Tween rTween[]=new Tween[3];
	float w;
	float scale;
	private boolean zoom;
	private int TOUCH_AREA = 0;
	//DataWheel dw[] = new DataWheel[3];
	
	Metaball(PApplet parent,float x,float y,float r1,float r2,float r3){
		 this.parent = parent;

		this.x = x;
		this.y = y;
		
		r[0]=r1;
		r[1]=r2;
		r[2]=r3;
		
		arc[0]=(r[0]+r[1])/2;
		arc[1]=(r[1]+r[2])/2;
		arc[2]=(r[2]+r[0])/2;	
		
		pulseTween = new Tween(parent, .5f, Tween.SECONDS, Shaper.COSINE);
	    pulseTween.setPlayMode( Tween.REVERSE_REPEAT );
	    pulseTween.start();
	    startTween = new Tween(parent, .8f, Tween.SECONDS, Shaper.COSINE);
	    startTween.setPlayMode( Tween.ONCE);
	    startTween.seek(1);
		//update();
		//display();
	    

	    
	    if((r1+r2+r3)>130){
	    	zoom=true;
	    	w=2;
	    }
	    else {
	    	zoom=false;
	    	w=1;
	    }
	    scale=1;
	    for (int i = 0; i < 3; i++) {
			  float slice = 2*parent.PI / 3;
			  m[i]=new PVector(parent.sin(i*slice)* (r[i]/2), parent.cos(i*slice)* (r[i]/2));
			    rTween[i] = new Tween(parent, parent.random(.4f)+.6f, Tween.SECONDS, Shaper.COSINE);
			    rTween[i].setPlayMode( Tween.REVERSE_REPEAT );
			    rTween[i].start();
			//    dw[i] = new DataWheel(parent, 0, m[i].x, m[i].y,0);
			 }
	}
	void update() {
		for (int i = 0; i < 3; i++) {
		  float slice = 2*parent.PI / 3;
		  m[i]=new PVector(parent.sin(i*slice)* (r[i]/2), parent.cos(i*slice)* (r[i]/2));
		  arc[i] = parent.lerp( r[i]/1.4f,  r[i]*2f, pulseTween.position())+.5f;
		  r[i] = parent.lerp( r[i]-0.15f, r[i]+0.15f, rTween[i].position());
		 }
	}
	
	void display() {
	  parent.colorMode(parent.RGB);
	  parent.pushMatrix();
	  parent.translate(x, y);
	  if(startTween.isTweening())scale=startTween.position();
		else{
			if(scale>.5)scale=1;
			else scale=0;
		}
	  
	if(startTween.isTweening())scale=startTween.position();
		else{
			if(scale>.5)scale=1;
			else scale=0;
		}
	  parent.scale(scale);
	  parent.fill(255);
	 // parent.fill(255,170,0);
	  for (int i = 0; i < 3; i++) {
	    parent.ellipse(m[i].x, m[i].y, r[i], r[i]);
	  }
	  	
	  parent.beginShape();
	      drawMeta(m[0], m[1], r[0]/2, r[1]/2, arc[0]);
	      drawMeta(m[1], m[2], r[1]/2, r[2]/2, arc[1]);
	      drawMeta(m[2], m[0], r[2]/2, r[0]/2, arc[2]);
	   parent.endShape(parent.CLOSE);
	    parent.fill(255,170,0);
	   parent.beginShape();
	      drawMeta(m[0], m[1], r[0]/2-w, r[1]/2-w,  arc[0]+w);
	      drawMeta(m[1], m[2], r[1]/2-w, r[2]/2-w,  arc[1]+w);
	      drawMeta(m[2], m[0], r[2]/2-w, r[0]/2-w,  arc[2]+w);
	   parent.endShape(parent.CLOSE);
	   
	   
	  for (int i = 0; i < 3; i++) {
	    parent.ellipse(m[i].x, m[i].y, r[i]-2*w, r[i]-2*w);
	   }
	  if(zoom){
		  parent.fill(0,100);
		  parent.ellipse(m[0].x, m[0].y, 20,20);
		  parent.ellipse(m[1].x, m[1].y, 20,20);
		  parent.ellipse(m[2].x, m[2].y, 20,20);
	  }
	/*  for (int i = 0; i < 3; i++) {
		  	dw[i].update();
		  	dw[i].display();
		   }*/
	   parent.popMatrix();
	}


	void drawMeta(PVector m1, PVector m2, float r1, float r2, float arc) {
	  float a=r2+arc/2;
	  float b=r1+arc/2;
	  float c=PVector.dist(m1, m2);

	  float theta=parent.acos(  (parent.pow(a, 2)+parent.pow(b, 2)-parent.pow(c, 2)  )/(2*a*b));
	  float alpha=parent.acos(  (parent.pow(b, 2)+parent.pow(c, 2)-parent.pow(a, 2)  )/(2*b*c));
	  float  beta=parent.acos(  (parent.pow(a, 2)+parent.pow(c, 2)-parent.pow(b, 2)  )/(2*a*c));
	  float delta = PVector.angleBetween(PVector.sub(m1, m2), new PVector(0, 1));
	  if (delta>parent.PI/4 && delta<3*parent.PI/4)delta*=-1;  
	  drawVertexArc(m1.x-parent.sin(-alpha-delta)* b, m1.y-parent.cos(-alpha-delta)*b, arc, delta+alpha-1.5f*parent.PI,theta );
	}

	void drawVertexArc(float x,float y,float r,float s,float theta){
	  r/=2;
	  int details=20;//20
	  theta/=details;
	  for (int i = 0; i < details; i++){
		  parent.vertex(x+parent.cos(s+theta*i)* r, y+parent.sin(s+theta*i)* r);
	  }  
	}
	
	public void start() {
		 scale=1;
		 startTween.setPlayMode(Tween.ONCE);
		 startTween.start();
	}
	public void get() {

	}/*
	void updateTouch(float x,float y){
		dw[0].updateTouch(x,y);

		//float d =  parent.abs(parent.atan2(x-this.x,y-this.y)-parent.PI)-parent.HALF_PI;
		if(TOUCH_AREA==0){
			
			}
		else{
			dw[0].updateTouch(x,y);
		}
	   }
	
	
	 void startTouch(float x,float y){
		 PVector c= new PVector(x,y);
		 PVector l[]=new PVector[3];
		 if(TOUCH_AREA==0){
		 for (int i = 0; i < 3; i++) {
			 l[i]= PVector.add(new PVector(this.x,this.y), m[i]);
			 if(l[i].dist(c) < 20){
				 parent.println(i);
				 TOUCH_AREA=i+1;	
				 if(!dw[i].isVisible())dw[i].start();
				 else dw[i].startTouch(x,y);
			 }

		 }
		 }
		 

	 }
	 void stopTouch(){
			if(TOUCH_AREA!=0)TOUCH_AREA=0;
		    for (int i = 0; i < 3; i++) {
		    	dw[i].stopTouch();
		    	}
	 }*/
	void updateTouch(float x,float y){
		
	   }
	
	 void startTouch(float x,float y){
		 
	 }
	 void stopTouch(){
		 
	 }
	public boolean isTouched(float x, float y) {
		 PVector c= new PVector(x,y);
		 PVector xy=new PVector(this.x,this.y);
		 boolean in=false;
		 if(zoom){
			 PVector l[]=new PVector[3];
			 for (int i = 0; i < 3; i++) {
				// in=(dw[i].isTouched(x,y));
				 l[i]= PVector.add(xy, m[i]);
				 if(l[i].dist(c) < r[i]/2){
					 in=true;
					 TOUCH_AREA=i;
					 }
				 }
			}
			else in=(xy.dist(c)  < 20);
		 return in;
	}
	PVector getTouched(){
		return PVector.add(new PVector(this.x,this.y), m[TOUCH_AREA]);
	}
}