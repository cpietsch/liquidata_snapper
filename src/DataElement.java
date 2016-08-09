import processing.core.PApplet;
import processing.core.PImage;


public class DataElement {
	PApplet parent;
	float w;
	float h;
	float scale;
	float transp;
	float d;	//MaxSize
	float x;
	float y;
	private PImage img;
	private String txt;

	DataElement(PApplet parent, float w,float h) {
		this.parent=parent;
		//this.w=w;
		//this.h=h;
		scale=1;
		transp=255;
		d=280;
		
		float resize=(d/	parent.sqrt(parent.pow(w,2)+parent.pow(h,2))	);
		this.w=w*resize;
		this.h=h*resize;
		 x=0;
		 y=0;
	}
	void display() {
		parent.pushMatrix();
		if(x+y!=0)parent.translate(x-parent.width/2,y-parent.height/2);
		
		parent.strokeWeight(2);
		parent.stroke(255,100);
		parent.fill(252,transp*.75f);
		//if(txt!=null)parent.ellipse(0, 0, 305, 305);
		parent.scale(scale);
		parent.rectMode(parent.CENTER);
		parent.imageMode(parent.CENTER);
		
		if(img!=null){
			parent.tint(255, transp); 
			parent.image(img, 0, 0);
			}
		else {
			parent.rect(0, 0, w, h);
			parent.fill(20,transp);
			if(txt!=null)parent.text(txt, 0, 0, w-30, h-50);
		}
		parent.rectMode(parent.CORNER);
		parent.imageMode(parent.CORNER);
		parent.textAlign(parent.CORNER, parent.CORNER);
		parent.strokeWeight(1);
		parent.popMatrix();
	}

	void update() {
		
	}
	void setPosition(float x,float y){
		this.x=x;
		this.y=y;
	}
	void setScale(float s) {
		this.scale=s;
		transp=parent.map(s,0,1,0,255);
	//	w=w*s;
		//h=h*s;
	}
	float getScale() {
		return this.scale;
	}
	public void setImage(PImage img) {
		this.img=img;
		img.resize((int)w, (int)h);
		
	}
	public void setText(String txt) {
		parent.textAlign(parent.CENTER, parent.CENTER);
		this.txt=txt;
		
	}
}