import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PFont;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Comparator;

import processing.opengl.*;
import javax.media.opengl.GL;

//import com.sun.tools.javac.util.List;

import megamu.shapetween.*;

public class Phone{
	
	PApplet parent; //parent von PApplet
	float x;
	float y;
	float dir;
	float view;
	Connection connection;
	/*
	LinkedList <PVector> places = new LinkedList<PVector>();
	LinkedList <Connection>connectionsCL = new LinkedList<Connection>();
	LinkedList <Connection>connectionsCCL = new LinkedList<Connection>();*/
	
	Vector <PVector> places = new Vector<PVector>();
	Vector <Connection>connectionsCL = new Vector<Connection>();
	Vector <Connection>connectionsCCL = new Vector<Connection>();
	float connectionDir;
	
	//private float pulseValue;
	 private Tween pulseTween;

	  //PVector location;
	  //PVector velocity;
	  //float rotation;
	//  PVector orientation;
	 // PVector intersection;
	//  float catchmentArea;
	//  PVector nearestNeighbor;
	  int TOUCH_AREA;
	  int ID;
	  int hue;
	
	Phone(PApplet p,int ID,float x,float y,float dir) {
		
		connectionDir=0;
	    this.parent = p;
	    this.ID=ID;
	    this.x=x;
	    this.y=y;
	    this.dir=dir;
	    TOUCH_AREA=0;
	    places.add(new PVector(600,500));
	 //   nearestNeighbor = new PVector(100,200);
	 //   intersection = new PVector(x,y);
	    connection = new Connection(this.parent, new PVector(x,y), places.get(0), dir, parent.color(240,164,12)); //#F0A40C
	    connection.setMainSnapper(true);
	   // connections.add(new Connection(this.parent, new PVector(x,y), places.get(0), dir, parent.color(240,164,12))); //#F0A40C

	    view=parent.PI/3;
	    //int []hues={202,81,267,56,359,23,181};
		//hue=hues[ID];
	    hue=(int)parent.random(360);
	    
	  	pulseTween = new Tween(this.parent, 1f, Tween.SECONDS, Shaper.COSINE);
	    pulseTween.setPlayMode( Tween.REVERSE_REPEAT );
	    pulseTween.start();
	  }
		int getID(){
			return ID;
		}
	/*  PVector getDistanceToPlace(PVector start,PVector dir, PVector place){
	    dir.normalize();
	    PVector sp = PVector.sub(start,place);
	    PVector d =  sp.cross(dir);
	    return d;
	  }
	float getDistance(Place p) {
		    float d = location.dist(p.getPosition());
		        return d;
		    }*/
	  
	void update() {

		
		
		connection.update(new PVector(x,y), places.get(0), dir);
		int ccl=0;
		int cl=0;
		for(int i=1;i<places.size();i++){
			//if(connectionsCCL.size()>0)connectionsCCL.get(0).update(new PVector(x,y), places.get(i), dir-(parent.PI/18)*i);
			
			//if(linePointPosition(new PVector(x,y),places.getFirst(),places.get(i))>=0){
			if(linePointPosition(new PVector(x,y),places.firstElement(),places.get(i))>=0){
				if(cl<connectionsCL.size()){
					connectionsCL.get(cl).update(new PVector(x,y), this.places.get(i), dir+(parent.PI/18)*(cl+1));
					cl++;
					}
			}
			else{
				if(ccl<connectionsCCL.size()){
					connectionsCCL.get(ccl).update(new PVector(x,y), this.places.get(i), dir-(parent.PI/18)*(ccl+1));
				 ccl++;
				 }
			}
		}
		//parent.println(ccl+" "+cl);
	}
	void display() {
		for(int i=0;i<connectionsCL.size();i++){
			connectionsCL.get(i).display();
			}
		for(int i=0;i<connectionsCCL.size();i++){
			connectionsCCL.get(i).display();
			}
	    	connection.display();
		    
	        int d =20;
	        float dimension = 214.71f;
	        

	//  draw Phone      
		 	parent.pushMatrix();
		 	parent.translate(x, y);
		 	parent.rotate(dir);
		 	//outer Circle
		    parent.noStroke();
			parent.fill(0,30);
			parent.rotate(connectionDir);
				PointerRing p = new PointerRing(parent,dimension+2*d,  40);
			parent.rotate(-connectionDir);
	        parent.noStroke();
	        parent.fill(255,30);
	        RingSlice r2 = new RingSlice(parent,  dimension+2*d,  40,  parent.HALF_PI+view, parent.PI*2.5f-view);
	        //inner Circle
	        parent.noStroke();
	        parent.fill(0,20);
	        parent.ellipse(0,0,dimension,dimension);
	        parent.noStroke();
	        parent.noFill();
	        parent.strokeWeight(1);
	        parent.colorMode(parent.HSB, 360, 100, 100);
	        //37,134,193
	        /*
	        float pulse = parent.lerp( 0, 8, pulseTween.position() );
	        for (int i = 1; i < dimension/2; i++) {
	          parent.stroke(hue,80,74,(28-i/3)-pulse);
	          parent.ellipse(0,0,parent.floor(dimension-i),parent.floor(dimension-i));
	        }
	        */
	        
	        parent.colorMode(parent.RGB,255);
	        parent.strokeWeight(3);
	        parent.stroke(251);
	        parent.fill(251);
	        parent.noStroke();
	        RingSlice r3 = new RingSlice(parent,dimension,5,parent.HALF_PI+view, parent.PI*2.5f-view);
	        parent.strokeWeight(3f);
	        parent.fill(255,30);
	        parent.noStroke();

	        parent.arc(0,0,dimension,dimension,parent.HALF_PI-view,parent.HALF_PI+view);
	        parent.noStroke();
	        parent.fill(76,200);
	        RingSlice r4 = new RingSlice(parent,  dimension+.5f,6,parent.HALF_PI-view,parent.HALF_PI+view);
	        float areaSize=400;
	        parent.noStroke();
	        parent.fill(0,5);
	        parent.strokeWeight(1);
	        parent.popMatrix();
	      }
	
	boolean setPlace(Vector places) {
		int index=0;
		boolean placeInArea=false;
		for (Enumeration e=places.elements();e.hasMoreElements();) {
			Place p =(Place)e.nextElement();
			p.displayName(false);
			float viewingAngle= PVector.angleBetween(new PVector(parent.cos(dir-parent.HALF_PI),parent.sin(dir-parent.HALF_PI)),PVector.sub(new PVector(this.x,this.y),p.getPosition()));
			p.setAngle(viewingAngle);
			p.setP(new PVector(this.x,this.y));
			if(p.getDist() >225f && p.getDist() <500f)placeInArea=true;
			}
		
		/*Collections.sort(places, new Comparator <Place>() {
			public int compare(Place p1, Place p2) {
			   float d1 = p1.getDist();
			   float d2 = p1.getDist();
			   return (d1>d2) ? -1 : (d1==d2) ? 0 : 1;
			 }
		});*/
		
		Collections.sort(places, new SortPlacesByAngle());
		boolean shift=true;
		while(shift && placeInArea){
			
			if( ((Place)places.firstElement()).getDist() <225f || ((Place)places.firstElement()).getDist() >500f){
				shift=true;
				Collections.rotate(places,-1);
			}
			else shift=false;
		}
		Place p = (Place)places.firstElement();		
		PVector [] n= p.getNeighbors();
		this.places.clear();
		this.places.add(p.getPosition());
		float ccl= 0;
		float cl= 0;
		//int sort=1;
		for(int i=0;i<n.length;i++){
		/*	for(int j=1;j<this.places.size();j++){
				float a1= PVector.angleBetween(PVector.sub(new PVector(x,y),this.places.get(0)),PVector.sub(new PVector(x,y),n[i]));
				float a2= PVector.angleBetween(PVector.sub(new PVector(x,y),this.places.get(j)),PVector.sub(new PVector(x,y),n[i]));
				if(a1<=a2){
					sort=j;
					break;

				}

				//parent.println(parent.degrees(	PVector.angleBetween(PVector.sub(new PVector(x,y),this.places.get(0)),PVector.sub(new PVector(x,y),n[i]))	));
			//parent.println(parent.degrees(	PVector.angleBetween(PVector.sub(new PVector(x,y),this.places.get(j)),PVector.sub(new PVector(x,y),n[i]))	));
				}*/
			
			this.places.add(n[i]);


		/*	for(int j=1;j<places.size();j++){
					break;
					}
				}*/
			

			if(linePointPosition(new PVector(x,y),p.getPosition(),n[i])>=0){
				cl++;
				connectionsCL.add(new Connection(this.parent, new PVector(x,y), n[i], dir+parent.PI/12, parent.color(255,50)));
				//connectionsCL.addLast(new Connection(this.parent, new PVector(x,y), n[i], dir+parent.PI/12, parent.color(255,50)));

				}
				else{
					ccl++;
					//connectionsCCL.addLast(new Connection(this.parent, new PVector(x,y), n[i], dir-parent.PI/12, parent.color(255,50)));
					connectionsCCL.add(new Connection(this.parent, new PVector(x,y), n[i], dir-parent.PI/12, parent.color(255,50)));

				}
		}
	/*	Collections.sort(places, new Comparator<PVector>() {
		    public int compare(PVector p1, PVector p2) {
		    	 float a1= PVector.angleBetween(PVector.sub(new PVector(x,y),places.get(0)),PVector.sub(new PVector(x,y),p1));
				 float a2= PVector.angleBetween(PVector.sub(new PVector(x,y),places.get(0)),PVector.sub(new PVector(x,y),p2));
				 return (a1<a2) ? -1 : (a1==a2) ?  0 : 1;

		    }
		});*/
		//parent.println("CCl= "+ccl+" CL= "+cl);
		for(int i=0;i<n.length;i++){
			
		}

		while(cl<this.connectionsCL.size()){
			//this.connectionsCL.removeFirst();
			this.connectionsCL.remove(0);

		}
		while(ccl<this.connectionsCCL.size()){
			//this.connectionsCCL.removeFirst();
			this.connectionsCCL.remove(0);
		}
		p.displayName(true);
		return !places.isEmpty();
	  }
	void setLocation(float x,float y,float dir){
		this.dir = dir;
	    this.x = x;
	    this.y = y;
	    }
	
	
	 void updateTouch(float x,float y){
		PVector c= new PVector(x,y);
		PVector l= new PVector(this.x,this.y);
		float d = PVector.angleBetween(new PVector(parent.cos(dir-parent.HALF_PI),parent.sin(dir-parent.HALF_PI)),PVector.sub(l,c));
		if(TOUCH_AREA==2)view=d;
		if(parent.degrees(PVector.angleBetween(new PVector(parent.cos(dir),parent.sin(dir)),PVector.sub(l,c)))>90)d*=-1;
		if(TOUCH_AREA==1)connectionDir=d;
	   }
	 
	 void startTouch(float x,float y){
		 
		 PVector c= new PVector(x,y);
		 PVector l= new PVector(this.x,this.y);
		 if(l.dist(c) >100f && l.dist(c) <130f){
			 float d = PVector.angleBetween(new PVector(parent.cos(dir-parent.HALF_PI),parent.sin(dir-parent.HALF_PI)),PVector.sub(l,c));
			 if(Math.abs(view-d) < parent.radians(5))TOUCH_AREA=2;
			 if(d < parent.radians(5))TOUCH_AREA=1;
			}

	 }
	 void stopTouch(){
	 		if(TOUCH_AREA!=0)TOUCH_AREA=0;
	 		connectionDir=(parent.round(18*connectionDir/parent.PI))/18f*parent.PI;
	 		dir+=connectionDir;
	 		connectionDir=0;
	 }
	 boolean isTouched(float x,float y){
		 return (new PVector(this.x,this.y).dist(new PVector(x,y))  < 130f);
	 }
	 
	 /*void addViewingAngle(float r){
	    if((view+r)< 2.4*parent.PI &&(view+r) > 0.05)view+=r;
	  }
	  
	  void addRotation(float r){
		  dir+=r;
	  }
	  
	  boolean isInCircle(PVector place, PVector root, float r){
	        //pow(x-m,2)+pow(y-n,2)=pow(r,2)
	        boolean inCircle = false;
	        PVector mp = PVector.sub(root,place);
	        float d =mp.mag();
	        if(d<=r)inCircle =true;
	        return inCircle;
	  }*/
	 void start() {
		 connection.start();
	 }
	 void sendData() {
		 connection.sendData();
	 }
	 PVector getMainPlaceXY() {
		 return this.places.firstElement();
	 }
	 float linePointPosition( PVector p1, PVector p2, PVector p3 ){
	     PVector diff = p2.get();
	     diff.sub( p1 );
	     PVector perp = new PVector( -diff.y, diff.x );
	     PVector pp = p3.get();
	     pp.sub( p1 );
	     return pp.dot( perp );
	 }

}
