import processing.core.PApplet;

class PointerRing{
   PApplet parent; 
   float radius, w, s1, e1, x, y, theta1,s2, e2, theta2;
   int slices, fill, s;
   float[] bezierPointsCL,bezierPointsCCL;
   
   PointerRing(PApplet parent,float radius,float w){
	   

	 this.parent = parent;
     this.radius=radius;
     this.w=w;
     s2=parent.HALF_PI;
     theta2=2*parent.PI;
     e2=s2+theta2;
     s1=s2+parent.PI/26;
     e1=e2-parent.PI/26;
     theta1 = e1 - s1;
     slices = 4;
     
     theta1/=slices;
     theta2/=slices;
     bezierPointsCL= new float[0];
     bezierPointsCCL= new float[0];
     this.update();
     this.display();
     fill=parent.g.fillColor;
	// s=parent.g.strokeColor;
	 s=parent.color(255);
   }
   void update(){
	   for(int i=0;i<slices;i++){
	         int offset=2;if(i==0)offset=0;
	         bezierPointsCL = parent.concat(bezierPointsCL,parent.subset(getArcBezierPoints(radius, s1+theta1*(i+1),s1+theta1*i), offset, 8-offset));
	        }
	        //Counter-Clockwise
	        for(int i=0;i<slices;i++){
	       	 int offset=2;if(i==0)offset=0;
	       	 bezierPointsCCL = parent.concat(bezierPointsCCL,parent.subset(getArcBezierPoints(radius-w, e2-theta2*(i+1),e2-theta2*i), offset, 8-offset));
	        }
     
   }
   void display(){


	   parent.noStroke();
	   
	   parent.beginShape();
	   //Clockwise
	   parent.vertex(bezierPointsCL[bezierPointsCL.length-2],bezierPointsCL[bezierPointsCL.length-1]);
	   parent.vertex(0,  radius/2+radius/22);

	   parent.vertex(bezierPointsCL[0],bezierPointsCL[1]);
	   for(int j=0;j<bezierPointsCL.length/6;j++){
		  parent.bezierVertex(  bezierPointsCL[j*6+2+0],bezierPointsCL[j*6+2+1],
                   bezierPointsCL[j*6+2+2],bezierPointsCL[j*6+2+3],
                   bezierPointsCL[j*6+2+4],bezierPointsCL[j*6+2+5]);
                 }
	   
	   //Counter-Clockwise
	   parent.vertex(bezierPointsCCL[0],bezierPointsCCL[1]);
	   for(int j=0;j<bezierPointsCCL.length/6;j++){
		   parent.bezierVertex(  bezierPointsCCL[j*6+2+0],bezierPointsCCL[j*6+2+1],
                   bezierPointsCCL[j*6+2+2],bezierPointsCCL[j*6+2+3],
                   bezierPointsCCL[j*6+2+4],bezierPointsCCL[j*6+2+5]);
                 }
	  
	   parent.endShape(parent.CLOSE);
	   parent.noFill();
	   parent.strokeWeight(1.2f);
	   parent.stroke(255,95);
	   parent.beginShape();    
	   parent.vertex(0,radius/2+radius/22);

	   parent.vertex(bezierPointsCL[0],bezierPointsCL[1]);
	   for(int j=0;j<bezierPointsCL.length/6;j++){
		  parent.bezierVertex(  bezierPointsCL[j*6+2+0],bezierPointsCL[j*6+2+1],
                bezierPointsCL[j*6+2+2],bezierPointsCL[j*6+2+3],
                bezierPointsCL[j*6+2+4],bezierPointsCL[j*6+2+5]);
              }
	   parent.endShape(parent.CLOSE);
	  /* parent.beginShape();    

	   //Counter-Clockwise
	   parent.vertex(bezierPointsCCL[0],bezierPointsCCL[1]);
	   for(int j=0;j<bezierPointsCCL.length/6;j++){
		   parent.bezierVertex(  bezierPointsCCL[j*6+2+0],bezierPointsCCL[j*6+2+1],
                bezierPointsCCL[j*6+2+2],bezierPointsCCL[j*6+2+3],
                bezierPointsCCL[j*6+2+4],bezierPointsCCL[j*6+2+5]);
              }
	   parent.endShape(parent.CLOSE);*/
  }
   
   //get BezierPoints of each Slice
   float[] getArcBezierPoints(float radius, float startAngle, float endAngle){
    float px0,py0;
    float px1,py1;
    float px2,py2;
    float px3,py3;
    float theta = endAngle - startAngle;
    radius/=2;
    // Compute raw Bezier coordinates.
    float x0 = parent.cos(theta/2f);
    float y0 = parent.sin(theta/2f);
    float x3 = x0;
    float y3 = 0-y0;
    float x1 = (4f-x0)/3f;
    float y1 = ((1f-x0)*(3f-x0))/(3f*y0); // y0 != 0...
    float x2 = x1;
    float y2 = 0-y1;
    
    // Compute rotationally-offset Bezier coordinates, using:
    // x' = cos(angle) * x - sin(angle) * y;
    // y' = sin(angle) * x + cos(angle) * y;
    float bezAng = startAngle + theta/2f;
    float cBezAng = parent.cos(bezAng);
    float sBezAng = parent.sin(bezAng);

    px0 = x + radius*(cBezAng * x0 - sBezAng * y0);
    py0 = y + radius*(sBezAng * x0 + cBezAng * y0);
    px1 = x + radius*(cBezAng * x1 - sBezAng * y1);
    py1 = y + radius*(sBezAng * x1 + cBezAng * y1);
    px2 = x + radius*(cBezAng * x2 - sBezAng * y2);
    py2 = y + radius*(sBezAng * x2 + cBezAng * y2);
    px3 = x + radius*(cBezAng * x3 - sBezAng * y3);
    py3 = y + radius*(sBezAng * x3 + cBezAng * y3);
    
    float[] bezierPoints = {px0,py0,px1,py1,px2,py2,px3,py3};
    return bezierPoints;
  } 
}
/*import processing.core.PApplet;
 


public class PointerRing extends RingSlice{
	PApplet parent;
	float[] CL,CCL;
	
	PointerRing(PApplet parent, float radius, float w) {
		
		super(parent, radius, w, 0, parent.PI*2);
		CL=super.getClockwise(theta,4);
	    CCL=super.getCounterClockwise(theta-parent.PI/18,4);
		// TODO Auto-generated constructor stub
	}
	void update(){
		
	}
	void display(){
		   parent.beginShape();
		   //Clockwise
		   parent.vertex(CL[0],CL[1]);
		   parent.vertex(CL[0],CL[1]);
		   for(int j=0;j<CL.length/6;j++){
			  parent.bezierVertex(  CL[j*6+2+0],CL[j*6+2+1],
	                   CL[j*6+2+2],CL[j*6+2+3],
	                   CL[j*6+2+4],CL[j*6+2+5]);
	                 }
		   //Counter-Clockwise
		   parent.vertex(CCL[0],CCL[1]);
		   for(int j=0;j<CCL.length/6;j++){
			   parent.bezierVertex(  CCL[j*6+2+0],CCL[j*6+2+1],
	                   CCL[j*6+2+2],CCL[j*6+2+3],
	                   CCL[j*6+2+4],CCL[j*6+2+5]);
	                 }
	    parent.endShape(parent.CLOSE);
	  }

}*/
