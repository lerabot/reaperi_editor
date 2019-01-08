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
PVector   mouse;
PVector   mouseDiff = new PVector();


public void setup(){
  
  mouse = new PVector(mouseX, mouseY);
  setMapGUI();
  loadLastMap();
}

public void update() {
  mouse.x = mouseX;
  mouse.y = mouseY;
  updateGUI();
  updateMap();
}

public void draw(){
  background(30);
  update();
}

public void controlEvent(ControlEvent theEvent) {
  println(theEvent.getController().getName());
  int v;
  switch(theEvent.getController().getName()) {
    case "Load Map":
      clearSVG();
      selectSVG();
    break;
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
  float _f = frameRate;
  mapGUI.getController("FPS").setCaptionLabel(String.format("%.2f", frameRate) + "FPS");
}

public void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
    mapFile = null;
  } else {
    println("User selected " + selection.getAbsolutePath());
    mapFile = selection.getAbsolutePath();
  }
}
/*////////////////
<image
   sodipodi:absref="/home/magnes-tower/Projets/DC Reaperi Cycle/assets/map_hideout/soussol_floor.png"
   xlink:href="soussol_floor.png"
   width="64"
   height="64"
   preserveAspectRatio="none"
   style="image-rendering:optimizeSpeed"
   id="image1761"
   x="-367.71426"
   y="245.14285" />

/*///////////////

public class GameObject {
  XML     raw;
  String  path;
  String  imageName;
  String  name;
  PVector pos;
  PVector size;
  int xFlip = 1;
  int yFlip = 1;
  PImage  t;

  public GameObject(XML raw) {
    this.raw  = raw;
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

  public void cleanXML() {
    XML xmlns = this.raw.getChild("xmlns");
    this.raw.removeChild(xmlns);
  }

  public void updateXML() {
    float _x = this.pos.x;
    float _y = this.pos.y;

    if(xFlip == -1)
      _x = -this.pos.x;

    this.raw.setFloat("x", _x);
    this.raw.setFloat("y", _y);
  }

  public void drawObject() {
    /*
    if (transform.equals("scale(-1,1)")) {
      xFlip = -1;
      pos.x = -pos.x;
    }
    */
  }

  public void flipX() {
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
static final int NOT_LOADED     = -1;
static final int IDLE           = 0;
static final int MOVING         = 1;
static final int RELEASE        = 2;
static final int TRANSLATE_MAP  = 3;

static final int DONE       = 0;
static final int UPDATED    = 1;
static final int CLEAR      = 2;

XML xml;
XML objectData[];

PGraphics g;
PVector   mapTranslate  = new PVector();
int       toolMode      = NOT_LOADED;
int       renderMode    = DONE;
boolean   mapLoaded     = false;
int       mapX, mapY;

String    mapFile;

ArrayList<GameObject> objects = new ArrayList<GameObject>();
GameObject selectedObject;

public void loadLastMap(){
  mapFile = "data/map_hideout.svg";
}

public void selectSVG() {
  selectInput("Select a .SVG file.", "fileSelected");
}

public void clearSVG() {
  if(objects != null)
    objects.clear();

  xml           = null;
  mapFile       = null;
  mapLoaded     = false;
  toolMode      = NOT_LOADED;
  renderMode    = CLEAR;
}

public void loadSVG(String mapName) {
  xml = loadXML(mapName);
  //println(xml.listChildren());
  mapX = xml.getInt("width");
  mapY = xml.getInt("height");
  g = createGraphics(mapX, mapY);

  XML layer1 = xml.getChild("g");
  objectData = layer1.getChildren("image");
  int imageNum = objectData.length;
  for(int i = 0; i < imageNum; i++) {
    objects.add(i, new GameObject(objectData[i]));
    GameObject o = objects.get(i);
  }
  mapLoaded   = true;
  toolMode    = IDLE;
  renderMode  = UPDATED;
  println("Map Loaded");
}

public void saveSVG() {
  XML newSVG = loadXML("data/template.svg");
  String att[] = xml.listAttributes();
  //SVG Attributes//////////////////////
  for(int i = 0; i < xml.getAttributeCount(); i++)
    newSVG.setString(att[i], xml.getString(att[i]));

  newSVG.addChild(xml.getChild("defs"));
  newSVG.addChild(xml.getChild("sodipodi:namedview"));
  newSVG.addChild(xml.getChild("metadata"));

  newSVG.addChild("g");
  XML g = newSVG.getChild("g");
  for(GameObject o: objects) {
    o.updateXML();
    g.addChild(o.raw);
    /*
    for(int i = 0; i < o.raw.getAttributeCount(); i++) {
    }
    */
  }
  saveXML(newSVG, "data/new_map.svg");
  println("Map Saved!");
}

public void setMapGUI(){
  int xSpacing = 2;
  int ySpacing = 2;
  int xSize = 75;
  int ySize = 20;

  int fontColor = color(255);

  mapGUI = new ControlP5(this);
  mapGUI.addButton("Load Map")
  .setPosition((xSpacing + xSize) * 0, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);
  mapGUI.addButton("Add Object")
  .setPosition((xSpacing + xSize) * 1, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);
  mapGUI.addButton("Other")
  .setPosition((xSpacing + xSize) * 2, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);



  mapGUI.addButton("FPS")
  .setPosition((xSpacing + xSize) * 0, (ySpacing + ySize) * 1)
  .setSize(xSize, ySize);
  mapGUI.addButton("GameObject ID")
  .setPosition((xSpacing + xSize) * 1, (ySpacing + ySize) * 1)
  .setSize(xSize, ySize);
}

public void updateMap() {
  //Check if map is loaded -- KINDA BAD HERE
  if(mapFile != null && toolMode == NOT_LOADED) {
    loadSVG(mapFile);
  }

  if(toolMode == MOVING) {
    selectedObject.pos.x = mouseX - mouseDiff.x;
    selectedObject.pos.y = mouseY - mouseDiff.y;
  }

  renderMap();
}

public void renderMap() {
  if(mapLoaded && renderMode != DONE) {
    g.beginDraw();
    g.background(5);
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
    if(!mousePressed)
      renderMode = DONE;
  }

  if(mapLoaded) {
    pushMatrix();
    translate(mapTranslate.x, mapTranslate.y);
    image(g, 0, 0);
    popMatrix();

  }
}

public GameObject selectObject() {
  for(int i = objects.size()-1; i > 0; i--) {
    GameObject o = objects.get(i);
      if(insideObject(mouse, o)){
        mapGUI.get(Button.class, "GameObject ID")
        .setCaptionLabel(o.imageName);
        mouseDiff = PVector.sub(mouse, o.pos);
        toolMode = MOVING;
        return o;
      }
  }
  return null;
}

public void releaseObject() {
  if(toolMode == MOVING)
    toolMode = IDLE;
}

public void mousePressed() {
  if(toolMode == IDLE) {
    selectedObject = selectObject();
  }
}

public void mouseReleased() {
  if(toolMode == MOVING) {
    releaseObject();
    renderMode = UPDATED;
  }
}

public void mouseDragged() {
  if(selectedObject != null)
    toolMode = MOVING;

  switch (toolMode) {
    case MOVING:
    renderMode = UPDATED;
    break;
    /*
    case TRANSLATE_MAP:
    mapTranslate = mouse;
    break;
    */
  }
}

public void map_keyPressed(char key) {
  renderMode = UPDATED;
  //toolMode = IDLE;
  if(key == ' ' && keyPressed)
    toolMode = TRANSLATE_MAP;

  if(key == 's' && keyPressed)
    saveSVG();

  if(key == CODED) {
    switch (keyCode) {
      case 33: //page up
      break;

      case 34: //page down
      break;
    }
  } else {
    switch (key) {
      case ' ':
      break;
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
