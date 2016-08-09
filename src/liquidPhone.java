import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import processing.core.*;
import processing.opengl.*;
import processing.xml.XMLElement;

import javax.media.opengl.GL;
import codeanticode.glgraphics.*;

import TUIO.*;

import de.fhpotsdam.unfolding.Map;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class liquidPhone extends PApplet implements TuioListener {
	boolean presentMode = false;

	Map map;
	TuioCursorHandler tuioCursorHandler;
	TuioClient tuioClient;
	int placeNumber = 20;
	Integer hitTuioCursorId;

	// Snapper, Places
	Vector<Phone> phones = new Vector();
	Vector<Place> places = new Vector<Place>();
	List<Location> placeLoc = new ArrayList<Location>();

	Metaball mb;
	DataWheel dw[] = new DataWheel[1];

	public void setup() {
		size(1024, 786, GLConstants.GLGRAPHICS);
		// size(1680,1050, GLConstants.GLGRAPHICS);
		// size(1440,900, GLConstants.GLGRAPHICS);

		hint(DISABLE_OPENGL_2X_SMOOTH);
		hint(ENABLE_OPENGL_4X_SMOOTH);
		hint(ENABLE_NATIVE_FONTS);
		hint(ENABLE_ACCURATE_TEXTURES);
		hint(DISABLE_DEPTH_TEST);
		hint(DISABLE_OPENGL_ERROR_REPORT);
		smooth();

		map = new Map(this);
		map.zoomAndPanTo(new Location(52.52317f, 13.4116f), 14);

		EventDispatcher eventDispatcher = MapUtils
				.createDefaultEventDispatcher(this, map);

		tuioCursorHandler = new TuioCursorHandler(this, false, map);
		eventDispatcher.addBroadcaster(tuioCursorHandler);

		tuioClient = tuioCursorHandler.getTuioClient();
		tuioClient.addTuioListener(this);

		loadRSSPlaces("place2.xml");
		for (Location location : placeLoc) {
			float xy[] = map.mapDisplay.getScreenPositionFromLocation(location);

			if (xy[0] <= 0 || xy[1] <= 0 || xy[0] >= width || xy[1] >= height) {

			} else {
				places.addElement(new Place(this, xy[0], xy[1], "Place "));
			}
		}
		if (places.size() == 0) {
			places.addElement(new Place(this, 100, 100, "MATHAFACKA "));
		}

		// find Neigborhood
		for (Enumeration i = places.elements(); i.hasMoreElements();) {
			Place p1 = (Place) i.nextElement();
			for (Enumeration j = places.elements(); j.hasMoreElements();) {
				Place p2 = (Place) j.nextElement();
				if (PVector.sub(p1.getPosition(), p2.getPosition()).mag() < 120) {
					if (!p1.equals(p2))
						p1.addNeighbor(p2);
				}
			}
		}
		for (int i = 0; i < dw.length; i++) {
			dw[i] = new DataWheel(this, 0, width / 2, height / 2, 0);
		}
		for (int i = 0; i < 5; i++) {
			dw[0].push(loadImage("test.jpg"));
		}
		for (int i = 0; i < 5; i++) {
			dw[0].push("Wirklich sehr schön hier. Da muss man unbedingt mal hin! Die grünen Wiesen und der Park laden zum sonnen ein.");
		}
	}

	private void loadRSSPlaces(String url) {

		XMLElement rss = new XMLElement(this, url);
		XMLElement[] itemXMLElements = rss.getChildren("location");

		for (int i = 0; i < itemXMLElements.length; i++) {

			float lat = itemXMLElements[i].getFloat("lat");
			float lng = itemXMLElements[i].getFloat("lng");

			placeLoc.add(new Location(lat, lng));
		}

		println("Locations:" + itemXMLElements.length);
	}

	public void draw() {

		map.draw();
		// fill(132,200);
		// rect(0,0,width,height);
		// Places updaten & Zeichnen
		for (Enumeration e = places.elements(); e.hasMoreElements();) {
			Place p = ((Place) e.nextElement());
			p.update();
			p.display();
		}

		// Phones updaten & Zeichnen
		Vector tuioObjectList = tuioClient.getTuioObjects();
		for (int i = 0; i < tuioObjectList.size(); i++) {
			if (tuioObjectList.size() > 0) {
				TuioObject tobj = (TuioObject) tuioObjectList.elementAt(i);
				for (Enumeration e = phones.elements(); e.hasMoreElements();) {
					Phone p = (Phone) e.nextElement();
					if (tobj.getSymbolID() == p.getID()) {

						p.update();
						p.display();
					}
				}
			}
		}
		if (mb != null) {
			mb.display();
			mb.update();
		}
		for (int i = 0; i < dw.length; i++) {
			dw[i].update();
			dw[i].display();
		}
		// TUIO Cursor Zeichnen
		Vector tuioCursorList = tuioClient.getTuioCursors();
		for (int i = 0; i < tuioCursorList.size(); i++) {
			TuioCursor tcur = (TuioCursor) tuioCursorList.elementAt(i);
			Vector pointList = tcur.getPath();
			if (pointList.size() > 0) {
				fill(192);
				ellipse(tcur.getScreenX(width), tcur.getScreenY(height), 10, 10);
				fill(0);
				text("" + tcur.getCursorID(), tcur.getScreenX(width) - 5,
						tcur.getScreenY(height) + 5);
			}
		}

		if (frameCount % 10 == 0) {
			println(frameRate);
		}
	}

	boolean isHit(float checkX, float checkY) {
		boolean isHited = false;
		for (Enumeration e = phones.elements(); e.hasMoreElements();) {
			isHited = (((Phone) e.nextElement()).isTouched(checkX, checkY));
		}
		if (mb != null)
			isHited = (mb.isTouched(checkX, checkY));
		return isHited;
	}

	@Override
	public void addTuioCursor(TuioCursor tuioCursor) {
		float x = tuioCursor.getScreenX(width);
		float y = tuioCursor.getScreenY(height);
		
		// wenn es oben links hinzugefügt wird dann isses ein phone
		
		if (mb != null && !this.isDataWheelVisible()) {
			for (int i = 0; i < dw.length; i++)
				if (!dw[i].isVisible() && mb.isTouched(x, y)) {
					PVector m = mb.getTouched();
					dw[i].setLocation(m.x, m.y, 0);
					dw[i].start();
				}
		} else {
			for (int i = 0; i < dw.length; i++)
				dw[i].startTouch(x, y);
			if (isHit(x, y)) {
				hitTuioCursorId = tuioCursor.getCursorID();
				for (Enumeration e = phones.elements(); e.hasMoreElements();) {
					((Phone) e.nextElement()).startTouch(
							tuioCursor.getScreenX(width),
							tuioCursor.getScreenY(height));
				}
			} else {
				tuioCursorHandler.addTuioCursor(tuioCursor);

			}
		}
	}

	@Override
	public void removeTuioCursor(TuioCursor tuioCursor) {
		hitTuioCursorId = null;
		tuioCursorHandler.removeTuioCursor(tuioCursor);

		for (int i = 0; i < dw.length; i++)
			dw[i].stopTouch();

		for (Enumeration e = phones.elements(); e.hasMoreElements();) {
			((Phone) e.nextElement()).stopTouch();
		}

	}

	@Override
	public void updateTuioCursor(TuioCursor tuioCursor) {

		if (this.isDataWheelVisible()) {
			for (int i = 0; i < dw.length; i++)
				if (dw[i].isVisible())
					dw[i].updateTouch(tuioCursor.getScreenX(width),
							tuioCursor.getScreenY(height));
		} else {
			if (hitTuioCursorId != null
					&& hitTuioCursorId.equals(tuioCursor.getCursorID())) {
				for (Enumeration e = phones.elements(); e.hasMoreElements();) {
					((Phone) e.nextElement()).updateTouch(
							tuioCursor.getScreenX(width),
							tuioCursor.getScreenY(height));
				}
			} else {
				tuioCursorHandler.updateTuioCursor(tuioCursor);
				places.clear();

				for (Location location : placeLoc) {
					float xy[] = map.mapDisplay
							.getScreenPositionFromLocation(location);

					if (xy[0] <= 0 || xy[1] <= 0 || xy[0] >= width
							|| xy[1] >= height) {

					} else {
						places.addElement(new Place(this, xy[0], xy[1],
								"Place "));
					}
				}
				if (places.size() == 0) {
					places.addElement(new Place(this, 100, 100, "MATHAFACKA "));
				}

				// find Neigborhood
				for (Enumeration i = places.elements(); i.hasMoreElements();) {
					Place p1 = (Place) i.nextElement();
					for (Enumeration j = places.elements(); j.hasMoreElements();) {
						Place p2 = (Place) j.nextElement();
						if (PVector.sub(p1.getPosition(), p2.getPosition())
								.mag() < 120) {
							if (!p1.equals(p2))
								p1.addNeighbor(p2);
						}
					}
				}
				// Verbindung des Telefons zu neuem PLaces setzen
				for (Enumeration e = phones.elements(); e.hasMoreElements();) {
					Phone p = (Phone) e.nextElement();
					if (places != null)
						p.setPlace(places);
					// p.update();

				}
			}
		}

	}

	@Override
	public void addTuioObject(TuioObject tobj) {
		boolean b = true;
		for (Enumeration e = phones.elements(); e.hasMoreElements();) {
			Phone p = (Phone) e.nextElement();
			if (tobj.getSymbolID() == p.getID()) {
				p.start();
				b = false;
			}
		}
		if (b)
			phones.add(new Phone(this, tobj.getSymbolID(), tobj
					.getScreenX(width), tobj.getScreenY(height), tobj
					.getAngle()));

	}

	@Override
	public void refresh(TuioTime arg0) {
		// redraw();
	}

	@Override
	public void removeTuioObject(TuioObject tobj) {
		println("remove object " + tobj.getSymbolID());

	}

	@Override
	public void updateTuioObject(TuioObject tobj) {
		for (Enumeration e = phones.elements(); e.hasMoreElements();) {
			Phone p = (Phone) e.nextElement();
			if (tobj.getSymbolID() == p.getID()) {
				p.setLocation(tobj.getScreenX(width), tobj.getScreenY(height),
						tobj.getAngle());
				if (places != null)
					p.setPlace(places);
				p.update();
			}
		}
	}

	// called when Key Pressed
	public void keyPressed() {
		println(keyCode);
		switch (keyCode) {
		case 32:
			for (Enumeration e = phones.elements(); e.hasMoreElements();) {
				((Phone) e.nextElement()).sendData();
			}
			break;
		case 157:
			for (Enumeration e = phones.elements(); e.hasMoreElements();) {
				PVector p = ((Phone) e.nextElement()).getMainPlaceXY();
				if (mb == null) {
					mb = new Metaball(this, p.x, p.y, random(8) + 50,
							random(80) + 50, random(80) + 50);
					mb.start();
				} else
					mb = null;
			}
			break;
		}
	}

	boolean isDataWheelVisible() {
		boolean isVisible = false;
		for (int i = 0; i < dw.length; i++)
			if (dw[0].isVisible())
				isVisible = true;
		return isVisible;
	}

	public static void main(String[] args) {

		int primary_display = 0; // index into Graphic Devices array...

		int primary_width;
		int primary_height;

		GraphicsEnvironment environment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice devices[] = environment.getScreenDevices();
		String location;
		if (devices.length > 1) { // we have a 2nd display/projector
			println(devices[1].getType());
			// primary_width = devices[0].getDisplayMode().getWidth();
			// location = "--location="+primary_width+",0";
			primary_height = devices[0].getDisplayMode().getHeight();
			location = "--location=0," + primary_height;

		} else {// leave on primary display
			location = "--location=0,0";

		}

		String display = "--display=" + primary_display + 1; // processing
																// considers the
																// first display
																// to be # 1
		PApplet.main(new String[] { location, "--hide-stop", display,
				"liquidPhone" }); // Der letzte String muss der Pfad zu Deiner
									// Klasse sein

	}

	public void init() {
		setWindowBorder();
		super.init();
	}

	public void setWindowBorder() {
		if (frame != null && presentMode) {
			frame.removeNotify();
			frame.setResizable(false);
			frame.setUndecorated(true);
			frame.setLocation(0, 0);
			frame.addNotify();
		}
	}

}
