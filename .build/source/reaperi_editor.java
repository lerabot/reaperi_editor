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
  //newMap();
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
  ellipse(mouse.x, mouse.y, 10, 10);
}

public void controlEvent(ControlEvent theEvent) {
  println("GUI Click: " + theEvent.getController().getName());
  int v;
  map_controlEvent(theEvent);
  /*
  switch(theEvent.getController().getName()) {
    case "npc":
      v = int(theEvent.getController().getValue());
      updateQuestBox(v);
    break;
    case "questName":
      v = int(theEvent.getController().getValue());
      println(v);
      updateDialogBox(v);
    break;
  }
  */
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

  updateMapGUI();
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
  loop();
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
  int     xFlip = 1;
  int     yFlip = 1;
  PImage  t;

  public GameObject(String path) {
    this.t = loadImage(path);
    this.path = path;
    this.imageName = "New item no name lol"; //MUST BE CHANGED
    this.pos = new PVector(mouseX, mouseY);
    this.size = new PVector(t.width, t.height);
  };

  public GameObject(XML raw) {
    this.raw  = raw;
    this.path = raw.getString("sodipodi:absref");
    this.imageName = raw.getString("xlink:href");
    this.pos = new PVector(raw.getFloat("x"), raw.getFloat("y"));
    this.size = new PVector(raw.getFloat("width"), raw.getFloat("height"));
    this.t = loadImage(this.path);
    if(raw.hasAttribute("transform")) {
      String transform = raw.getString("transform");
      if (transform.equals("scale(-1,1)")) {
        this.xFlip = -1;
        this.pos.x = -pos.x;

      }
    }

  }

  public void cleanXML() {
    XML xmlns = this.raw.getChild("xmlns");
    this.raw.removeChild(xmlns);
  }

  public void updateXML() {
    float _x = this.pos.x;
    float _y = this.pos.y;

    if(xFlip == -1) {
      _x = -this.pos.x;
      if(!this.raw.hasAttribute("transform")) {
        this.raw.addChild("transform");
        this.raw.setString("transform", "scale(-1,1)");
      }
    } else {
      if(this.raw.hasAttribute("transform")) {
        XML c = this.raw.getChild("transform");
        this.raw.removeChild(c);
      }
    }
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
static final int SELECTED       = 1;
static final int RELEASE        = 2;
static final int TRANSLATE_MAP  = 3;

static final int DONE       = 0;
static final int UPDATE     = 1;
static final int CLEAR      = 2;

XML xml;
XML objectData[];

int     bg          = 0xff422084;
int     textColor   = 0xff26968E;
int     hint        = 0xffF4BA3E;

PGraphics g;
PVector   mapTranslate  = new PVector(0, 0);
int       toolMode      = NOT_LOADED;
int       renderMode    = DONE;
boolean   renderGrid    = true;
boolean   mapLoaded     = false;
int       mapX, mapY;

String    mapFile;
String    objectFile;

ArrayList<GameObject> objects = new ArrayList<GameObject>();
GameObject            selectedObject;

public void newMap() {
  mapFile = "data/template.svg";
}

public void loadLastMap(){
  mapFile = "data/map_hideout.svg";
}

public void selectSVG() {
  selectInput("Select a .SVG file.", "fileSelected");
  noLoop();
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
  renderMode  = UPDATE;
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

//MAP BASED/////////////////////////////////////////////
public void setMapGUI(){
  int xSpacing = 2;
  int ySpacing = 2;
  int xSize = 75;
  int ySize = 20;

  int fontColor = color(255);

  mapGUI = new ControlP5(this);
  mapGUI.setColorBackground(color(100,100));
  mapGUI.setColorCaptionLabel(textColor);
  mapGUI.setColorForeground(hint);
  mapGUI.setColorActive(bg);

  mapGUI.addButton("Load Map")
  .setPosition((xSpacing + xSize) * 0, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);
  mapGUI.addButton("Add Object")
  .setPosition((xSpacing + xSize) * 1, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);
  mapGUI.addButton("Other")
  .setPosition((xSpacing + xSize) * 2, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);

  mapGUI.addButton("Save Map")
  .setPosition((xSpacing + xSize) * 0, (ySpacing + ySize) * 1)
  .setSize(xSize, ySize);
  mapGUI.addButton("GameObject ID")
  .setPosition((xSpacing + xSize) * 1, (ySpacing + ySize) * 1)
  .setSize(xSize, ySize);
  mapGUI.addButton("X")
  .setPosition((xSpacing + xSize) * 2, (ySpacing + ySize) * 1)
  .setSize(xSize, ySize);
  mapGUI.addButton("Y")
  .setPosition((xSpacing + xSize) * 3, (ySpacing + ySize) * 1)
  .setSize(xSize, ySize);

  mapGUI.addButton("FPS")
  .setPosition((xSpacing + xSize) * 0, (ySpacing + ySize) * 2)
  .setSize(xSize, ySize);
}

public void updateMapGUI(){
  if(selectedObject != null) {
    mapGUI.getController("X").setCaptionLabel("X:" + String.format("%.2f", selectedObject.pos.x));
    mapGUI.getController("Y").setCaptionLabel("Y:" +String.format("%.2f", selectedObject.pos.y));
  }
}

public void updateMap() {
  //Check if map is loaded -- KINDA BAD HERE
  if(mapFile != null && toolMode == NOT_LOADED) {
    loadSVG(mapFile);
  }

  if(toolMode == SELECTED) {
    selectedObject.pos.x = mouseX - mouseDiff.x;
    selectedObject.pos.y = mouseY - mouseDiff.y;
  }

  if(toolMode == TRANSLATE_MAP) {
    mapTranslate.x = mouseX - mouseDiff.x;
    mapTranslate.y = mouseY - mouseDiff.y;
  }

  renderMap();
}

public void renderMap() {
  if(mapLoaded && renderMode != DONE) {
    g.beginDraw();
    g.background(5);
    if(renderGrid)
      renderGrid();
    for(GameObject o: objects) {
      g.pushMatrix();
      g.translate(o.pos.x, o.pos.y);
      g.scale(o.xFlip, o.yFlip);
      g.image(o.t, 0, 0);
      g.noStroke();
      g.fill(100,0,0,100);
      if(o == selectedObject)
        g.fill(0,0,100,100);
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

public void renderGrid() {
  PVector angle     = new PVector(114, 49);
  int gridOpacity   = 35;

  angle.normalize();
  angle.setMag(mapX * 3);

  int     xSpacing   = 114 / 3;
  int     ySpacing   = 49 / 3;
  int i = 0;
  float b1 = 0;
  while(i < mapY || b1 < mapY) {
    float a   = 0;
    float b   = i;
    float a1  = a + PApplet.parseInt(angle.x);
    b1        = b - angle.y;
    g.pushMatrix();
    g.stroke(255, gridOpacity);
    g.line(a, b,  a1, b1);
    g.scale(-1, 1);
    g.translate(-mapX, 0);
    g.line(a, b,  a1, b1);
    g.popMatrix();
    /*
    if(i > 0) {
      b1 = -b + angle.y;

      g.line(a, -b, a1, b1);
    }
    */
    i += ySpacing;

  }
  /*
  for(int i = 0; i < mapX/xSpacing * 3; i++) {
    float a   = xSpacing * i;
    float b   = 0;
    float a1  = a - angle.x;
    float b1  = b + angle.y;
    g.stroke(255, gridOpacity);
    g.line(a, b, a1, b1);
    /*
    if(i > 0) {
      a1 = -a + angle.x;
      //g.line(-a, b, a1, b1);
    }

  }
  */
}

//OBJECT BASED//////////////////////////////////////////
public GameObject  selectObject() {
  PVector mSize = new PVector(1,1);
  for(int i = objects.size()-1; i > 0; i--) {
    GameObject o = objects.get(i);
      if(insideObject(mouse, o)){
        mapGUI.get(Button.class, "GameObject ID")
        .setCaptionLabel(o.imageName);
        mouseDiff = PVector.sub(mouse, o.pos);
        toolMode = SELECTED;
        renderMode = UPDATE;
        return o;
      }
  }
  return null;
}

public GameObject  getSelectedObject() {
  return selectedObject;
}

public void addObject() {
  selectInput("Select a .PNG file.", "loadObject");
  noLoop();
}

public void loadObject(File selection){
  GameObject o = new GameObject(selection.getAbsolutePath());
  objects.add(o);
  loop();
}

//fix xflipped collision detection.
public void changeDepth(int direction) {
  int selectedIndex = 0;

  for(int i = objects.size()-1; i > 0; i--) {
    GameObject o = objects.get(i);
    if(o == selectedObject)
      selectedIndex = i;
    if(insideObject(selectedObject, o)) {
      if(o != selectedObject) {
        println("Over " + o.imageName);
        if(direction == 1 && i > selectedIndex) {
            objects.add(i+1, selectedObject);
            objects.remove(selectedObject);
            renderMode = UPDATE;
            break;
        }
        if(direction == -1 && i < selectedIndex) {
            objects.add(i-1, selectedObject);
            objects.remove(selectedObject);
            renderMode = UPDATE;
            break;
        }
      }
    }
  }
}
//INTERACTION///////////////////////////////////////////
public void releaseObject() {
  if(toolMode == SELECTED)
    toolMode = IDLE;
}

public void mousePressed() {
  if(!mapGUI.isMouseOver()) {
    if(toolMode == IDLE) {
      selectedObject = selectObject();
    }
    //if(toolMode == TRANSLATE_MAP)
  }
}

public void mouseReleased() {
  switch(toolMode) {
    case SELECTED:
      toolMode    = IDLE;
      renderMode  = UPDATE;
    break;
    case TRANSLATE_MAP:
      toolMode    = IDLE;
    break;
  }
}

public void mouseDragged() {
  switch (toolMode) {
    case SELECTED:
      renderMode = UPDATE;
    break;

    case TRANSLATE_MAP:
      renderMode = UPDATE;
    break;
  }
}

public void map_keyPressed(char key) {
  renderMode = UPDATE;
  //toolMode = IDLE;
  if(key == ' ' && keyPressed) {
    mouseDiff = PVector.sub(mouse, mapTranslate);
    toolMode = TRANSLATE_MAP;
  }

  if(key == 's' && keyPressed)
    saveSVG();

  if(key == 'a' && keyPressed)
    addObject();

  if(key == CODED) {
    switch (keyCode) {
      case 33: //page up
        changeDepth(1);
      break;

      case 34: //page down
        changeDepth(-1);
      break;

      case LEFT:
      if(selectedObject != null)
        selectedObject.pos.x -= 1;
      break;
      case RIGHT:
      if(selectedObject != null)
        selectedObject.pos.x += 1;
      break;
      case UP:
      if(selectedObject != null)
        selectedObject.pos.y -= 1;
      break;
      case DOWN:
      if(selectedObject != null)
        selectedObject.pos.y += 1;
      break;
    }
  }
}

public void map_controlEvent(ControlEvent theEvent){
  renderMode = UPDATE;
  println("MAP GUI Click: " + theEvent.getController().getName());
  switch(theEvent.getController().getName()) {
    case "Load Map":
      clearSVG();
      selectSVG();
    break;
    case "Save Map":
      saveSVG();
    break;
    case "Add Object":
      addObject();
    break;
    case "X":
      if(selectedObject != null) {
        selectedObject.xFlip *= -1;
        selectedObject.pos.x -= selectedObject.size.x * selectedObject.xFlip;
      }
    break;
    }
}
//assumes A is the topLeft corner, and size is botRight
public boolean over(PVector a, PVector aSize, PVector b, PVector bSize) {
  if (abs(a.x) > abs(b.x) && abs(a.x) < (abs(b.x) + bSize.x) &&
      abs(a.y) > abs(b.y) && abs(a.y) < (abs(b.y) + bSize.y))
    return true;
  return false;
}

/*
//a mouse b object
boolean inside(PVector a, PVector aSize, PVector b, PVector bSize) {
  if(abs(a.x) > abs(b.x) && abs(a.x) < (abs(b.x) + size.x)){
    if(abs(a.y) > abs(b.y) && abs(a.y) < (abs(b.y) + size.y)){
      return true;
    }
  }
  return false;
}
*/
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

public boolean insideObject(GameObject a, GameObject o) {
  if(o.xFlip == -1){
    if(abs(a.pos.x) < abs(o.pos.x) && abs(a.pos.x) + (a.size.x * a.xFlip) > abs(o.pos.x) + (o.size.x * o.xFlip)){
      if(abs(a.pos.y) > abs(o.pos.y) && abs(a.pos.y) < abs(o.pos.y) + (o.size.y)){
        return true;
      }
    }
  }
  if(abs(a.pos.x) > abs(o.pos.x) && abs(a.pos.x) + (a.size.x * a.xFlip) < abs(o.pos.x) + (o.size.x * o.xFlip)){
    if(abs(a.pos.y) > abs(o.pos.y) && abs(a.pos.y) < abs(o.pos.y) + (o.size.y * o.yFlip)){
      return true;
    }
  }
  return false;
}
  public void settings() {  size(1000, 1000, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "reaperi_editor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
