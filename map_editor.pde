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

void loadLastMap(){
  mapFile = "data/map_hideout.svg";
}

void selectSVG() {
  selectInput("Select a .SVG file.", "fileSelected");
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
  renderMode  = UPDATED;
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

void setMapGUI(){
  int xSpacing = 2;
  int ySpacing = 2;
  int xSize = 75;
  int ySize = 20;

  color fontColor = color(255);

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

void updateMap() {
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

void renderMap() {
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

GameObject selectObject() {
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

void releaseObject() {
  if(toolMode == MOVING)
    toolMode = IDLE;
}

void mousePressed() {
  if(toolMode == IDLE) {
    selectedObject = selectObject();
  }
}

void mouseReleased() {
  if(toolMode == MOVING) {
    releaseObject();
    renderMode = UPDATED;
  }
}

void mouseDragged() {
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

void map_keyPressed(char key) {
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
