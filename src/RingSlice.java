import processing.core.PApplet;

class RingSlice{
   PApplet parent; 
   float radius, w, s, e, x, y, theta;
   int slices;
   float[] bezierPointsCL,bezierPointsCCL;
   
   RingSlice(PApplet parent,float radius,float w, float s,float e){
	 this.parent = parent;
     this.radius=radius;
     this.w=w;
     this.s=s;
     this.e=e;
     theta = e - s;
     if (theta>=2*parent.PI)theta=2*parent.PI;
     slices = parent.ceil(theta / (parent.PI/2));
     
     theta/=slices;
     bezierPointsCL= new float[0];
     bezierPointsCCL= new float[0];
     this.update();
     this.display();
   }
   void update(){

	   //parent.arraycopy(getClockwise(theta, slices),bezierPointsCL);
	   //parent.arraycopy(getCounterClockwise(theta, slices),bezierPointsCCL);
	  // parent.print(bezierPointsCL);
	  // parent.println("new:");
    //Clockwise 
     for(int i=0;i<slices;i++){
      int offset=2;if(i==0)offset=0;
      bezierPointsCL = parent.concat(bezierPointsCL,parent.subset(getArcBezierPoints(radius, s+theta*(i+1),s+theta*i), offset, 8-offset));
     }
     //Counter-Clockwise
     for(int i=0;i<slices;i++){
    	 int offset=2;if(i==0)offset=0;
    	 bezierPointsCCL = parent.concat(bezierPointsCCL,parent.subset(getArcBezierPoints(radius-w, e-theta*(i+1),e-theta*i), offset, 8-offset));
     }
   }
   void display(){
	   
	   parent.beginShape();
	   //Clockwise
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
  }
   float[] getClockwise( float theta, int slices){
	   float[] cl = new float[0];
	   for(int i=0;i<slices;i++){
		      int offset=2;if(i==0)offset=0;
		      cl = parent.concat(bezierPointsCL,parent.subset(getArcBezierPoints(radius, s+theta*(i+1),s+theta*i), offset, 8-offset));
	   }
	   return cl;
   }
   float[] getCounterClockwise(float theta, int slices){
	   float[] ccl = new float[0];
	   for(int i=0;i<slices;i++){
	    	 int offset=2;if(i==0)offset=0;
	    	 ccl = parent.concat(bezierPointsCCL,parent.subset(getArcBezierPoints(radius-w, e-theta*(i+1),e-theta*i), offset, 8-offset));
	     }
	   return ccl;
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