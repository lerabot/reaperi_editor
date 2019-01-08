import controlP5.*;
import java.util.*;

ControlP5 cp5;
ControlP5 mapGUI;
PGraphics map_graphic;
PVector   mouse;
PVector   mouseDiff = new PVector();


GameObject selectedObject;

public void setup(){
  size(1000, 1000, JAVA2D);
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
      v = int(theEvent.getController().getValue());
      updateQuestBox(v);
    break;
    case "questName":
      v = int(theEvent.getController().getValue());
      println(v);
      updateDialogBox(v);
    break;
  }
}

void keyPressed() {
  map_keyPressed(key);
}

void setMapGUI(){
  int xSpacing = 5;
  int ySpacing = 5;
  int xSize = 20;
  int ySize = 50;

  color fontColor = color(255);

  mapGUI = new ControlP5(this);
  mapGUI.addButton("GameObject ID")
  .setPosition(xSpacing, ySpacing);
}


void setDialogGUI(){
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

void updateGUI() {
}
