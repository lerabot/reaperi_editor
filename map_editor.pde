static final int IDLE     = 0;
static final int MOVING   = 1;
static final int RELEASE  = 2;

XML xml;
XML objectData[];

int mapX, mapY;
int toolMode;

ArrayList<GameObject> objects = new ArrayList<GameObject>();

void loadSVG(String mapName, PGraphics g) {
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

void updateMap() {
  if(toolMode == MOVING) {
    selectedObject.pos.x = mouseX - mouseDiff.x;
    selectedObject.pos.y = mouseY - mouseDiff.y;
  }
}

void drawMap(PGraphics g) {
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

void selectObject() {
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

void releaseObject() {
  if(toolMode == MOVING)
    toolMode = RELEASE;
}

void mouseClicked() {
  if(toolMode != MOVING)
    selectObject();
  else
    releaseObject();
}

void map_keyPressed(char key) {
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
