
import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PFont;
import java.util.*;

import processing.opengl.*;
import javax.media.opengl.GL;



public class Place /*implements Comparable <Place>*/{
	PApplet parent;
	PVector location;
	String locationName;
	boolean nameIsDisplayed;
	PFont font; 
	int size;
	int color;
	Vector <Place> neighbors = new Vector<Place>();
	private float angle;
	private PVector p;
	Metaball mb;

	Place(PApplet p,float x,float y, String name ) {
	    parent = p;
	    location = new PVector(x,y);
	    locationName = name;
	    nameIsDisplayed = false;
	    size = 5;
	    color = parent.color(100);
	    mb = new Metaball(parent, location.x,location.y,parent.random(2)+8,parent.random(5)+8, parent.random(5)+8);
	  }
	
	
	void display() {
			parent.noStroke();
	        parent.fill(color);
	        parent.fill(255,170,0);
		//	parent.ellipse(location.x,location.y,size,size);
	        //mb.update();
			mb.display();
	        parent.noFill();
	       /* parent.stroke(color);
	        parent.ellipse(location.x,location.y,size+4,size+4);
	        parent.noStroke();*/
	        
			if(nameIsDisplayed){
				/*parent.strokeWeight(4);
				parent.stroke(0,50);
				for (Enumeration e=neighbors.elements();e.hasMoreElements();) {
					Place n =((Place)e.nextElement());
					parent.line(location.x, location.y, n.getPosition().x, n.getPosition().y);	
				}*/
				parent.pushMatrix();
		        parent.noStroke();
				parent.translate(location.x+3,location.y+2); 
				parent.rectMode(parent.CORNER);
				parent.fill(0,160);
				parent.rect(0, 0, parent.textWidth(locationName)+8, 14);
				parent.fill(160);
		        parent.noStroke();
				parent.text(locationName,4,10);
				parent.popMatrix(); 
				
				
			}
	}
	void update() {
	/*	neighbors.isEmpty();
		if(PVector.sub(location, new PVector(parent.mouseX,parent.mouseY)).mag() < 5){
			parent.println(neighbors.isEmpty());
			parent.println(locationName+" hat Nachbarn"+neighbors.size());
		}*/
	//	mb.update();
	}
	
	String getName() {
		return locationName;
	}
	PVector getPosition() {
		return location;
	}
	void highlight(boolean isActive) {
		if(isActive==true){
			size=8;
			color = parent.color(0);
			}
		else{
			size=5;
			color = parent.color(100);
		}
		
	}
	void displayName(boolean isDisplayed){
		
		nameIsDisplayed=isDisplayed;
	}
	void addNeighbor(Place p){
		neighbors.add(p);
	}
	
	PVector []getNeighbors(){
		//LinkedList <PVector>n = new LinkedList<PVector>();
		PVector[] n = new PVector[neighbors.size()];
		for(int i=0;i<n.length;i++){
			n[i]=neighbors.get(i).getPosition();
		}
		return  n;
	}
	
	void setAngle(float angle){
		 angle=(parent.round(12*angle/parent.PI))/12f*parent.PI;
		 this.angle=angle;
	}
	float getAngle(){		   
		return this.angle;
	}
	
	float getDist(){
		return (location.dist(p));
	}
	void setP(PVector p){
		this.p=p;
	}
/*
	@Override
	public int compareTo(Place p) {
		float angle =p.getAngle();
		//float dist =p.getDist();
		//if(dist<50 || dist>350)dist*=1000;
		return (angle>this.angle) ? -1 : (angle==this.angle) ? 0 : 1;
		//return (dist>location.dist(this.p)) ? -1 : (dist==location.dist(this.p)) ? 0 : 1;
	}*/
}
