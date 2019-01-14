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
ControlP5 xmlAtt;
PVector   mouse;
PVector   pmouse;
PVector   mouseDiff = new PVector();


public void setup(){
  
  surface.setResizable(true);
  mouse = new PVector(mouseX, mouseY);
  pmouse = new PVector(mouseX, mouseY);
  setMapGUI();
  setXMLGUI();

  //newMap();
  loadLastMap();
}

public void update() {
  updateGUI();
  updateMap();

  pmouse.x = mouse.x;
  pmouse.y = mouse.y;
  mouse.x = mouseX - mapTranslate.x;
  mouse.y = mouseY - mapTranslate.y;
}

public void draw(){
  background(30);
  update();
  renderMap();
  renderLibrary();
}

public void controlEvent(ControlEvent theEvent) {
  println("Event Click: " + theEvent.getController().getName());

  map_controlEvent(theEvent);
  library_controlEvent(theEvent);
  xml_controlEvent(theEvent);
}

public void keyPressed() {
  println("Key:" + key + " KeyCode:" + keyCode);
  map_keyPressed(key);
  xml_keyPressed(key);
  library_keyPressed(key);
}

public void mousePressed() {
  xml_mousePressed();
  map_mousePressed();
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
  String  filename;
  String  name;
  String  luaScript;
  PVector pos;
  PVector size;
  boolean selected = false;
  int     xFlip = 1;
  int     yFlip = 1;
  int     depth = 0;
  PImage  t;

  public GameObject(){};

  public GameObject(String path) {
    this.t        = loadImage(path);
    this.path     = path;
    this.filename = "New item no name lol"; //MUST BE CHANGED
    this.pos      = new PVector(mouseX, mouseY);
    this.size     = new PVector(t.width, t.height);
    this.luaScript = null;
  };

  public GameObject(XML raw) {
    this.raw      = raw;
    this.path     = raw.getString("sodipodi:absref");
    this.filename = raw.getString("xlink:href");
    this.pos      = new PVector(raw.getFloat("x"), raw.getFloat("y"));
    this.size     = new PVector(raw.getFloat("width"), raw.getFloat("height"));
    this.t        = loadImage(this.path);

    /*
    if(raw.hasAttribute("luaScript")) {
      String lua = raw.getString("luaScript");
      this.luaScript = lua;
    } else {
      this.raw.addChild("luaScript");
      this.raw.setString("luaScript", "null");
      this.luaScript = null;
    }
    */
    if(raw.hasAttribute("transform")) {
      String transform = raw.getString("transform");
      if (transform.equals("scale(-1,1)")) {
        this.xFlip = -1;
        this.pos.x = -pos.x;

      }
    }
  }

  public GameObject(GameObject o) {
    if (o != null) {
      this.raw          = o.raw;
      this.path         = o.path;
      this.filename     = o.filename;
      this.name         = o.name;
      this.size         = o.size;
      this.xFlip        = o.xFlip;
      this.yFlip        = o.yFlip;
      this.pos          = new PVector(mouse.x - (size.x/2 * xFlip), mouse.y - (size.y/2 * yFlip));

      this.t            = loadImage(this.path);
      }
  }

  public void cleanXML() {
    XML xmlns = this.raw.getChild("xmlns");
    this.raw.removeChild(xmlns);
  }

  public void fixPositionXML() {
    float _x = this.pos.x;
    float _y = this.pos.y;

    if(xFlip == -1) {
      _x = -this.pos.x;
      this.raw.setString("transform", "scale(-1,1)");
    } else {
      this.raw.setString("transform", "");
    }

    this.raw.setFloat("x", _x);
    this.raw.setFloat("y", _y);
  }

  public void showXML() {
    int attW = 50;
    int attH = 20;
    int spacerY = 15;
    //println("-- XML Attributes --");
    String att[] = getUsefulAttributes();
    //println(att);
    this.updateXML();

    for(int i = 0; i < att.length; i++) {
      String attribute = this.raw.getString(att[i]);
      if(attribute == null)
        attribute = "NA";
      xmlAtt.get(Textfield.class,att[i]).setValue(attribute);
    }

  }

  public void updateXML() {
    this.raw.setFloat("x", this.pos.x);
    this.raw.setFloat("y", this.pos.y);
    this.raw.setInt("depth" , this.depth);
    this.raw.setFloat("width", this.size.x);
    this.raw.setFloat("height", this.size.y);
    this.raw.setString("xlink:href", this.filename);
    this.raw.setString("sodipodi:absref", this.path);
    this.raw.setString("luaScript", this.luaScript);

    if(this.xFlip == 1)
      this.raw.setString("transform", "scale(-1,1)");

    this.fixPositionXML();
  }

  public void copyPosition(GameObject o) {
    this.pos.x = o.pos.x;
    this.pos.y = o.pos.y;
  }

  public void drawObject() {
    g.pushMatrix();
    g.translate(this.pos.x, this.pos.y);
    g.scale(this.xFlip, this.yFlip);
    g.image(this.t, 0, 0);
    g.noStroke();
    if(renderBox) {
      if(this.selected)
        g.fill(0,0,100,100);
      else
        g.fill(100,0,0,100);
      g.rect(0, 0, this.size.x, this.size.y);
    }
    g.popMatrix();
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
static final int MULTI_SELECT   = 2;
static final int TRANSLATE_MAP  = 3;
static final int TRANSLATE_OBJ  = 4;
static final int XML_INPUT      = 5;

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
PVector   pMapTranslate = new PVector(0, 0);
PVector   mouseDelta    = new PVector(0, 0);
int       toolMode      = NOT_LOADED;
int       renderMode    = DONE;
boolean   renderGrid    = true;
boolean   renderBox     = false;
boolean   mapLoaded     = false;
int       mapX, mapY;
int       selectedIndex = 0;

String    mapFile;
String    objectFile;

ArrayList<GameObject> objects = new ArrayList<GameObject>();
ArrayList<GameObject> selectedObjects = new ArrayList<GameObject>();
GameObject            selectedObject;

public void newMap() {
  mapFile = "data/template.svg";
}

public void loadLastMap(){
  //mapFile = "data/template.svg";
  mapFile = "data/map_temple.svg";
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
  g = createGraphics(mapX, mapY, P2D);

  XML layer1 = xml.getChild("g");
  objectData = layer1.getChildren("image");
  int imageNum = objectData.length;
  for(int i = 0; i < imageNum; i++) {
    objects.add(i, new GameObject(objectData[i]));
    GameObject o = objects.get(i);
    o.depth = i;
    addToLibrary(o);
  }
  createLibrary(mapGUI);
  //if(mapX < 1920 && mapY < 1080)
    //surface.setSize(mapX, mapY);
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

  //createLibrary(mapGUI);

  mapGUI.addButton("Load Map")
  .setPosition((xSpacing + xSize) * 0, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);
  mapGUI.addButton("Add Object")
  .setPosition((xSpacing + xSize) * 1, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);
  mapGUI.addButton("Toggle B-Box")
  .setPosition((xSpacing + xSize) * 2, (ySpacing + ySize) * 0)
  .setSize(xSize, ySize);
  mapGUI.addButton("Toggle Grid")
  .setPosition((xSpacing + xSize) * 3, (ySpacing + ySize) * 0)
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
  if(selectedObjects.size() > 0) {
    GameObject o = getActiveObject();
    mapGUI.getController("X").setCaptionLabel("X:" + String.format("%.2f", o.pos.x));
    mapGUI.getController("Y").setCaptionLabel("Y:" + String.format("%.2f", o.pos.y));
  }
}

public void updateMap() {
  //Check if map is loaded -- KINDA BAD HERE
  if(mapFile != null && toolMode == NOT_LOADED) {
    loadSVG(mapFile);
  }

  if(toolMode == SELECTED) {
    if(selectedObjects.size() > 0) {
      for(GameObject o: selectedObjects) {
        o.pos.x += mouse.x - pmouse.x;
        o.pos.y += mouse.y - pmouse.y;
      }
    }
  }
  if(toolMode == TRANSLATE_MAP) {
    pMapTranslate.x = pmouse.x;
    pMapTranslate.y = pmouse.y;
    mapTranslate.x += mouseX - pmouseX;
    mapTranslate.y += mouseY - pmouseY;
  }
}

public void renderMap() {
  if(mapLoaded && renderMode != DONE) {
    g.beginDraw();
    g.background(5);
    if(renderGrid)
      renderGrid();
    for(GameObject o: objects) {
      o.drawObject();
    }
    if(selectedObject != null) {
      g.pushMatrix();
      g.translate(selectedObject.pos.x, selectedObject.pos.y);
      g.scale(selectedObject.xFlip, selectedObject.yFlip);
      g.stroke(hint);
      g.noFill();
      g.rect(0, 0, selectedObject.size.x, selectedObject.size.y);
      g.popMatrix();
    }
    if(selectedObjects.size() > 0) {
      for(GameObject o : selectedObjects) {
        g.pushMatrix();
        g.translate(o.pos.x, o.pos.y);
        g.scale(o.xFlip, o.yFlip);
        g.stroke(hint);
        g.noFill();
        g.rect(0, 0, o.size.x, o.size.y);
        g.popMatrix();
      }
    }
    g.endDraw();
    /*
    if(!mousePressed)
      renderMode = DONE;
      */
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
  for(int i = objects.size()-1; i > 0; i--) {
    GameObject o = objects.get(i);
      if(insideObject(mouse, o)){
        if(o.selected) {
          selectedObjects.remove(o);
          o.selected    = false;
          toolMode      = SELECTED;
          renderMode    = UPDATE;
          return null;
        }

        mapGUI.get(Button.class, "GameObject ID")
        .setCaptionLabel(o.filename);
        o.updateXML();
        o.showXML();
        selectedIndex   = i;
        o.selected      = true;
        toolMode        = SELECTED;
        renderMode      = UPDATE;
        return o;
      }
  }
  clearSelected();
  renderMode  = UPDATE;
  toolMode    = IDLE;
  return null;
}

public void printSelObjects() {
  if(selectedObjects.size() > 0) {
    println("-- Selected Objects --");
    for(GameObject o: selectedObjects) {
      println(o.filename);
    }
    //println("===================");
  }
}

public void clearSelected() {
  if(selectedObjects.size() > 0) {
    for(GameObject o: selectedObjects){
      o.selected = false;
    }
  }
  selectedObjects.clear();
}

public void addObject() {
  selectInput("Select a .PNG file.", "loadObject");
  noLoop();
}

public void loadObject(File selection){
  GameObject o = new GameObject(selection.getAbsolutePath());
  objects.add(o);
  o.depth = objects.size()-1;
  o.updateXML();
  o.showXML();
  addToLibrary(o);
  loop();
}

//fix xflipped collision detection.
public int changeDepth(int direction) {
  int target = -1;
  int depth = -1;
  if(selectedObjects.size() == 0)
    return -1;

  GameObject lastSelected = selectedObjects.get(selectedObjects.size()-1);

  //DE CEUX PROCHE DE NOUS, AUX PLUS RECUL\u00c9S
  for(int i = objects.size()-1; i > 0; i--) {
    GameObject o = objects.get(i);

    if((direction == -1 && selectedIndex > i) || (direction == 1 && selectedIndex < i)) {
      if(touching(lastSelected, o)) {
        depth = i + direction;
        println("Selected  " + o.filename + " at depth " + selectedIndex);
        println("Touching " + o.filename + " at depth " + i);
        println("Adding "   + lastSelected.filename + " at depth " + depth);
        GameObject newCopy = new GameObject(lastSelected);
        newCopy.copyPosition(lastSelected);
        newCopy.depth = depth;
        newCopy.updateXML();
        newCopy.showXML();
        objects.add(depth, newCopy);
        objects.remove(lastSelected);
        selectedObjects.remove(lastSelected);
        selectedObjects.add(newCopy);
        selectedIndex = depth;
        return depth;
      }
    }
  }
  return depth;
}

public void flipObject(char axis) {
  for(GameObject o: selectedObjects) {
    if(axis == 'x') {
      o.xFlip *= -1;
      o.pos.x -= o.size.x * o.xFlip;
    }
  }
}

public GameObject getActiveObject() {
  return objects.get(selectedIndex);
}

public void updateScreen() {
  renderMode = UPDATE;
}


public void testTouch() {

  for(int i = objects.size()-1; i > 0; i--) {
    GameObject o = objects.get(i);
    //if(touching(selectedObject, o) && o != selectedObject)

  }
}
//INTERACTION///////////////////////////////////////////
public void releaseObject() {
  if(toolMode == SELECTED)
    toolMode = IDLE;
}

public void map_mousePressed() {



  if(!mapGUI.isMouseOver()) {
    if(toolMode == IDLE) {
      if(!(keyCode == SHIFT && keyPressed))
        clearSelected();
      GameObject o = selectObject();
      if(o != null) {
        selectedObjects.add(o);
        //selectedObject = o;
      }


      printSelObjects();
    }

    if(toolMode == TRANSLATE_MAP) {
      //mouseDiff = PVector.sub(mouse, mapTranslate);
    }
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
      renderMode  = UPDATE;
    break;
  }
}

public void mouseDragged() {
  if(key == ' ' && keyPressed)
    toolMode = TRANSLATE_MAP;
  switch (toolMode) {
    case SELECTED:
      renderMode = UPDATE;
    break;
    case MULTI_SELECT:
    break;
    case TRANSLATE_MAP:
      //renderMode = UPDATE;
    break;
  }
}

public void map_keyPressed(char key) {
  renderMode = UPDATE;
  if(toolMode != XML_INPUT) {
    if(key == 'x' && keyPressed)
      flipObject('x');
    if(key == 'd' && keyPressed) {
      if(selectedObjects.size() > 0)
        for(GameObject o: selectedObjects)
          objects.add(new GameObject(o));
    }
    if(key == 't' && keyPressed) {
      testTouch();
    }

    if(key == 's' && keyPressed)
      saveSVG();

    if(key == 'a' && keyPressed)
      addObject();

    if((key == DELETE || key == BACKSPACE) && keyPressed) {
      for(GameObject o: selectedObjects)
        objects.remove(o);
      selectedObjects.clear();
    }

    if(key == 'i' && keyPressed)
      changeDepth(1);
    if(key == 'o' && keyPressed)
      changeDepth(-1);
  }
}

public void map_controlEvent(ControlEvent theEvent){
  renderMode = UPDATE;
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
    case "Toggle B-Box":
      renderBox = !renderBox;
    break;
    case "Toggle Grid":
      renderGrid = !renderGrid;
    break;
    case "X":
      for(GameObject o: selectedObjects) {
        o.xFlip *= -1;
        o.pos.x -= o.size.x * o.xFlip;
      }
      /*
      if(selectedObject != null) {
        selectedObject.xFlip *= -1;
        selectedObject.pos.x -= selectedObject.size.x * selectedObject.xFlip;
      }
      */
    break;
    }
}
ArrayList<GameObject> library = new ArrayList<GameObject>();
ArrayList<PImage> thumbnails = new ArrayList<PImage>();

int row     = 5;
int column  = 2;
int spacing  = 3;
int iconSize = 50;

public void        addToLibrary(GameObject o) {
  PVector pos = new PVector(0, 100);

  if(!isAddedToLib(o.filename)){
    GameObject temp = new GameObject(o);
    PImage     thumbnail = loadImage(temp.path);
    library.add(temp);
    thumbnails.add(thumbnail);

    if(thumbnail.width > thumbnail.height)
      thumbnail.resize(iconSize, 0);
    else
      thumbnail.resize(0, iconSize);

    int i = (library.size()-1) / column;
    int j = (library.size()-1) % column;

    String iconName = "Object " + String.valueOf(library.size()-1);
    iconName = temp.filename;
    mapGUI.addButton(iconName)
    .setPosition(pos.x + (j * (iconSize + spacing)), pos.y + (i * (iconSize + spacing)))
    .setImage(thumbnail)
    .setSize(iconSize, iconSize);

    //println("Library : Added " + temp.filename + " #" + (library.size()-1));
    /*
    if(library.size() < (row * column)) {
      //String iconName = "Object " + String.valueOf(library.size()-1);
      mapGUI.getController(iconName).setImage(temp.t);
    }
    */
  }
}

public boolean     isAddedToLib(String filename) {
  for(GameObject o: library){
    if (o.filename.equals(filename))
      return true;
  }
  return false;
}

public GameObject  getObjectFromLib(String filename) {
  for(GameObject o: library) {
      if (o.filename.equals(filename))
       return o;
  }
  return null;
}

public void        createLibrary(ControlP5 c) {
  int spacing = 3;
  PVector pos = new PVector(0, 100);

  fill(100, 100);
  noStroke();
  for(int i = 0; i < row; i++) {
    for(int j = 0; j < column; j++) {
      int iconNum = (i * column) + j;
      String iconName = "Object " + String.valueOf(iconNum);

      if(library.size() > 0) {
        //if(library.get(iconNum) != null)
          //t = library.get(iconNum).t;
          //t.resize(size, size);
      }
      /*
      c.addButton(iconName)
      .setPosition(pos.x + (j * (iconSize + spacing)), pos.y + (i * (iconSize + spacing)))
      .setSize(iconSize, iconSize);
      */
      //rect(pos.x + (i * (size + spacing)), pos.y + (j * (size + spacing)), size, size);
    }
  }
}

public void        renderLibrary(){
  int size    = 50;
  int spacing = 3;
  PVector pos = new PVector(0, 100);
  PImage  t   = null;

  fill(100, 100);
  noStroke();
  for(int i = 0; i < row; i++) {
    for(int j = 0; j < column; j++) {
      int iconNum = (i * column) + j;
      /*
      rect(pos.x + (j * (size + spacing)), pos.y + (i * (size + spacing)), size-5, size-5);
      if(iconNum < library.size())
        image(library.get(iconNum).t, pos.x + (j * (size + spacing)), pos.y + (i * (size + spacing)));
      */
    }
  }
}

public void        library_keyPressed(int key) {

}

public void        library_controlEvent(ControlEvent theEvent) {
  String event = theEvent.getController().getName();
  GameObject o = getObjectFromLib(event);
  if(o != null) {
    println("Found " + o.filename + " in library");
    GameObject temp = new GameObject(o);
    temp.pos.x = width/2 + abs(mapTranslate.x);
    temp.pos.y = height/2 + abs(mapTranslate.y);
    temp.depth = objects.size();
    temp.showXML();
    objects.add(temp);
    selectedObjects.add(temp);
  }
}
//assumes A is the topLeft corner, and size is botRight
public boolean over(PVector a, PVector aSize, PVector b, PVector bSize) {
  if (abs(a.x) > abs(b.x) && abs(a.x) < (abs(b.x) + bSize.x) &&
      abs(a.y) > abs(b.y) && abs(a.y) < (abs(b.y) + bSize.y))
    return true;
  return false;
}

public boolean minkowski(GameObject a, GameObject b) {
  PVector aCenter = new PVector(abs(a.pos.x) + (a.size.x/2 * a.xFlip), abs(a.pos.y) + (a.size.y/2 * a.yFlip));
  PVector bCenter = new PVector(abs(b.pos.x) + (b.size.x/2 * b.xFlip), abs(b.pos.y) + (b.size.y/2 * b.yFlip));
  PVector aMid    = new PVector(a.size.x/2, a.size.y/2);
  PVector bMid    = new PVector(b.size.x/2, b.size.y/2);;

  if(aCenter.x < bCenter.x - (aMid.x + bMid.x) || aCenter.x > bCenter.x + (aMid.x + bMid.x)) return false;
  if(aCenter.y < bCenter.y - (aMid.y + bMid.y) || aCenter.y > bCenter.y + (aMid.y + bMid.y)) return false;

  return true;
}

public boolean touching(GameObject a, GameObject b) {
  boolean r = false;

  if(a == b)
    return r;

  r = minkowski(a,b);
  //if(r)
    //println(a.imageName + " -> " + b.imageName);

  return r;
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
String usefulAttributes[] = {"x", "y", "depth", "height", "width", "xlink:href", "luaScript"};

public void setXMLGUI() {
  int attW = 50;
  int attH = 20;
  int spacerY = 15;

  xmlAtt = new ControlP5 (this);

  for(int i = 0; i < usefulAttributes.length; i++) {
    xmlAtt.addTextfield(usefulAttributes[i])
    .setValue("EMPTY")
    .setAutoClear(true)
    .setLabelVisible(true)
    .setPosition(width - (attW + 10), 100 + i * (attH + spacerY))
    .setSize(attW, attH);
  }
}

public void xml_controlEvent(ControlEvent theEvent){
  if(toolMode == XML_INPUT) {
    String event = theEvent.getController().getName();
    String value = theEvent.getController().getStringValue();
    GameObject o = getActiveObject();
    switch(event) {
      case "x" :
        o.pos.x = Float.valueOf(value);
      break;
      case "y" :
        o.pos.y = Float.valueOf(value);
      break;
      case "width" :
      break;
      case "height" :
      break;
      case "xlink:href" :
      break;
      case "luaScript" :
        o.luaScript = value;
      break;

    }
    o.showXML();
    updateScreen();
    toolMode = IDLE;
  }
}

public void xml_keyPressed(int key){
}

public void xml_mousePressed() {

  if(xmlAtt.isMouseOver()) {
    toolMode = XML_INPUT;
  }
}

public String[] getUsefulAttributes() {
  return usefulAttributes;
}
  public void settings() {  size(1000, 600, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "reaperi_editor" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
