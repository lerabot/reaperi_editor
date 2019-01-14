String usefulAttributes[] = {"x", "y", "depth", "height", "width", "xlink:href", "luaScript"};

void setXMLGUI() {
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

void xml_controlEvent(ControlEvent theEvent){
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

void xml_keyPressed(int key){
}

void xml_mousePressed() {

  if(xmlAtt.isMouseOver()) {
    toolMode = XML_INPUT;
  }
}

String[] getUsefulAttributes() {
  return usefulAttributes;
}
