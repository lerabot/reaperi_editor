import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import java.util.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class reaperi_editor extends PApplet {




ControlP5 cp5;
ControlP5 mapGUI;
PGraphics map_graphic;
PVector   mouse;
PVector   mouseDiff = new PVector();


GameObject selectedObject;

public void setup(){
  
  mouse = new PVector(mouseX, mouseY);
  setMapGUI();
  map_graphic = createGraphics(0,0);
  loadSVG("map_hideout.svg", map_graphic);
  //loadJSON("hideout_dialog.json");
}

public void update() {
  mouse.x = mouseX;
  mouse.y = mouseY;

  updateMap();
}

public void draw(){
  update();
  background(230);
  //image(map_graphic, 0, 0);
  drawMap(map_graphic);
}

public void controlEvent(ControlEvent theEvent) {
  println(theEvent.getController().getName());
  int v;
  switch(theEvent.getController().getName()) {
    case "npc":
      v = PApplet.parseInt(theEvent.getController().getValue());
      updateQuestBox(v);
    break;
    case "questName":
      v = PApplet.parseInt(theEvent.getController().getValue());
      println(v);
      updateDialogBox(v);
    break;
  }
}

public void keyPressed() {
  map_keyPressed(key);
}

public void setMapGUI(){
  int xSpacing = 5;
  int ySpacing = 5;
  int xSize = 20;
  int ySize = 50;

  int fontColor = color(255);

  mapGUI = new ControlP5(this);
  mapGUI.addButton("GameObject ID")
  .setPosition(xSpacing, ySpacing);
}


public void setDialogGUI(){
  PFont pfont = createFont("Arimo",12,true); // use true/false for smooth/no-smooth
  ControlFont font = new ControlFont(pfont,12);

  int spacing = 2;

  cp5 = new ControlP5(this);
  cp5.addScrollableList("npc")
  .setVisible(false)
  .setFont(font)
  .setBarHeight(22)
  .setWidth(100)
  .setType(ControlP5.DROPDOWN);

  cp5.addScrollableList("questName")
  .setPosition(100,0)
  .setWidth(100)
  .setVisible(false)
  .setFont(font)
  .setType(ControlP5.DROPDOWN);

  cp5.addTextfield("dialog")
  .setVisible(false)
  .setPosition(0,200)
  .setFont(font)
  .setSize(width, 200);
}

public void updateGUI() {
}
public class GameObject {
  String  path;
  String  imageName;
  String  name;
  PVector pos;
  PVector size;
  int xFlip = 1;
  int yFlip = 1;
  PImage  t;

  public GameObject(XML raw) {

    this.path = raw.getString("sodipodi:absref");
    this.imageName = raw.getString("xlink:href");
    this.pos = new PVector(raw.getFloat("x"), raw.getFloat("y"));
    this.size = new PVector(raw.getFloat("width"), raw.getFloat("height"));
    if(raw.hasAttribute("transform")) {
      String transform = raw.getString("transform");
      if (transform.equals("scale(-1,1)")) {
        xFlip = -1;
        pos.x = -pos.x;
      }
    }



    t = loadImage(this.path);
  }

}


JSONObject  json;
StringList  names;
String      active_npc;

public void loadJSON(String path) {
  json = loadJSONObject(path);
  names = new StringList();

  JSONArray map = json.getJSONArray("hideout");
  for(int i = 0; i < map.size(); i++){
    JSONObject npc = map.getJSONObject(i);
    if(!names.hasValue(npc.getString("npc"))) {
      names.append(npc.getString("npc"));
      cp5.get(ScrollableList.class, "npc").addItem(npc.getString("npc"), 1);
      }
    }
}

public void updateQuestBox(int index) {
  JSONArray list = json.getJSONArray("hideout");
  cp5.get(ScrollableList.class, "questName").clear();
  for(int i = 0; i < list.size(); i++) {
    JSONObject npc = list.getJSONObject(i);
    String npc_name = npc.getString("npc");
    active_npc = names.get(index);
    if(npc_name.equals(active_npc) == true) {
      cp5.get(ScrollableList.class, "questName").setVisible(true);
      cp5.get(ScrollableList.class, "questName").addItem(npc.getString("questName") + " -> " + npc.getString("textID"), 1);
    }
  }
  //JSONObject o = list.getJSONObject(index);
  //JSONArray text_array = o.getJSONArray("text");
}

public void updateDialogBox(int index) {
  JSONArray list = json.getJSONArray("hideout");
  //cp5.get(ScrollableList.class, "dialog").clear();
  for(int i = 0; i < list.size(); i++) {
    JSONObject npc = list.getJSONObject(i);
    String npc_name = npc.getString("npc");
    if(npc_name.equals(active_npc)) {
      if(index > 0)
        index--;

      if(index == 0){
        JSONArray text_array = npc.getJSONArray("text");
        String s = text_array.getString(0);
        cp5.get(Textfield.class, "dialog").setText(s);
      }
    }
  }
}
static final int IDLE     = 0;
static final int MOVING   = 1;
static final int RELEASE  = 2;

XML xml;
XML objectData[];

int mapX, mapY;
int toolMode;

ArrayList<GameObject> objects = new ArrayList<GameObject>();

public void loadSVG(String mapName, PGraphics g) {
  xml = loadXML(mapName);
  //println(xml.listChildren());
  mapX = xml.getInt("width");
  mapY = xml.getInt("height");
  g.setSize(mapX, mapY);

  XML layer1 = xml.getChild("g");
  objectData = layer1.getChildren("image");
  int imageNum = objectData.length;
  for(int i = 0; i < imageNum; i++) {
    objects.add(i, new GameObject(objectData[i]));
    GameObject o = objects.get(i);
  }

  toolMode = IDLE;
}

public void updateMap() {
  if(toolMode == MOVING) {
    selectedObject.pos.x = mouseX - mouseDiff.x;
    selectedObject.pos.y = mouseY - mouseDiff.y;
  }
}

public void drawMap(PGraphics g) {
    g.beginDraw();
    g.background(0);
    //g.imageMode(CENTER);
    for(GameObject o: objects) {
      g.pushMatrix();
      g.translate(o.pos.x, o.pos.y);
      g.scale(o.xFlip, o.yFlip);
      g.image(o.t, 0, 0);
      g.noStroke();
      g.fill(100,0,0,100);
      g.rect(0, 0, o.size.x, o.size.y);
      g.popMatrix();
    }
    g.endDraw();
    image(g, 0, 0);
}

public void selectObject() {
  for(int i = objects.size()-1; i > 0; i--) {
    GameObject o = objects.get(i);
      if(insideObject(mouse, o)){
        selectedObject = o;
        mapGUI.get(Button.class, "GameObject ID")
        .setCaptionLabel("ID " + o.imageName);
        mouseDiff = PVector.sub(mouse, o.pos);
        toolMode = MOVING;
        break;
      }
  }
}

public void releaseObject() {
  if(toolMode == MOVING)
    toolMode = RELEASE;
}

public void mouseClicked() {
  if(toolMode != MOVING)
    selectObject();
  else
    releaseObject();
}

public void map_keyPressed(char key) {
  if(key == CODED) {
    switch (keyCode) {
      case 33: //page up
      break;

      case 34: //page down
      break;
    }
  } else {
    switch (key) {
      case 'A':
    }
  }
}
//a mouse b object
public boolean inside(PVector a, PVector b, PVector size) {
  if(abs(a.x) > abs(b.x) && abs(a.x) < abs(b.x) + abs(size.x)){
    if(abs(a.y) > abs(b.y) && abs(a.y) < abs(b.y) + abs(size.y)){
      return true;
    }
  }
  return false;
}

public boolean insideObject(PVector a, GameObject o) {
  if(o.xFlip == -1){
    if(abs(a.x) < abs(o.pos.x) && abs(a.x) > abs(o.pos.x) + (o.size.x * o.xFlip)){
      if(abs(a.y) > abs(o.pos.y) && abs(a.y) < abs(o.pos.y) + (o.size.y)){
        return true;
      }
    }
  }
  if(abs(a.x) > abs(o.pos.x) && abs(a.x) < abs(o.pos.x) + (o.size.x * o.xFlip)){
    if(abs(a.y) > abs(o.pos.y) && abs(a.y) < abs(o.pos.y) + (o.size.y * o.yFlip)){
      return true;
    }
  }
  return false;
}
  public void settings() {  size(1000, 1000, JAVA2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "reaperi_editor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
