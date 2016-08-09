import java.util.Comparator;
import processing.core.PVector;

public class SortPlacesByAngle implements Comparator <Place>{

		 public int compare(Place p1, Place p2) {
		   float a1 = p1.getAngle();
		   float a2 = p2.getAngle();
		   float d1 = p1.getDist();
		   float d2 = p2.getDist();
		   return (a1<a2) ? -1 : (a1==a2) ? (d1<d2) ? -1 : (d1==d2) ? 0 : 1: 1;
		   //return (d1<d2) ? -1 : (d1==d2) ? 0 : 1;
		 }
}
