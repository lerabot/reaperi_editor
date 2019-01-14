import controlP5.*;
import java.util.*;

ControlP5 cp5;
ControlP5 mapGUI;
ControlP5 xmlAtt;
PVector   mouse;
PVector   pmouse;
PVector   mouseDiff = new PVector();


public void setup(){
  size(1000, 600, P2D);
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

void keyPressed() {
  println("Key:" + key + " KeyCode:" + keyCode);
  map_keyPressed(key);
  xml_keyPressed(key);
  library_keyPressed(key);
}

void mousePressed() {
  xml_mousePressed();
  map_mousePressed();
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
  float _f = frameRate;

  updateMapGUI();
  mapGUI.getController("FPS").setCaptionLabel(String.format("%.2f", frameRate) + "FPS");
}

void fileSelected(File selection) {
  if (selection == null) {
    println("Window was closed or the user hit cancel.");
    mapFile = null;
  } else {
    println("User selected " + selection.getAbsolutePath());
    mapFile = selection.getAbsolutePath();
  }
  loop();
}
