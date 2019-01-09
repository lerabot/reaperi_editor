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

color     bg          = #422084;
color     textColor   = #26968E;
color     hint        = #F4BA3E;

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

void newMap() {
  mapFile = "data/template.svg";
}

void loadLastMap(){
  mapFile = "data/map_hideout.svg";
}

void selectSVG() {
  selectInput("Select a .SVG file.", "fileSelected");
  noLoop();
}

void clearSVG() {
  if(objects != null)
    objects.clear();

  xml           = null;
  mapFile       = null;
  mapLoaded     = false;
  toolMode      = NOT_LOADED;
  renderMode    = CLEAR;
}

void loadSVG(String mapName) {
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

void saveSVG() {
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
void setMapGUI(){
  int xSpacing = 2;
  int ySpacing = 2;
  int xSize = 75;
  int ySize = 20;

  color fontColor = color(255);

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

void updateMapGUI(){
  if(selectedObject != null) {
    mapGUI.getController("X").setCaptionLabel("X:" + String.format("%.2f", selectedObject.pos.x));
    mapGUI.getController("Y").setCaptionLabel("Y:" +String.format("%.2f", selectedObject.pos.y));
  }
}

void updateMap() {
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

void renderMap() {
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

void renderGrid() {
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
    float a1  = a + int(angle.x);
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
GameObject  selectObject() {
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

GameObject  getSelectedObject() {
  return selectedObject;
}

void addObject() {
  selectInput("Select a .PNG file.", "loadObject");
  noLoop();
}

void loadObject(File selection){
  GameObject o = new GameObject(selection.getAbsolutePath());
  objects.add(o);
  loop();
}

//fix xflipped collision detection.
void changeDepth(int direction) {
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
void releaseObject() {
  if(toolMode == SELECTED)
    toolMode = IDLE;
}

void mousePressed() {
  if(!mapGUI.isMouseOver()) {
    if(toolMode == IDLE) {
      selectedObject = selectObject();
    }
    //if(toolMode == TRANSLATE_MAP)
  }
}

void mouseReleased() {
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

void mouseDragged() {
  switch (toolMode) {
    case SELECTED:
      renderMode = UPDATE;
    break;

    case TRANSLATE_MAP:
      renderMode = UPDATE;
    break;
  }
}

void map_keyPressed(char key) {
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

void map_controlEvent(ControlEvent theEvent){
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
