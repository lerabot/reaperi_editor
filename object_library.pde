ArrayList<GameObject> library = new ArrayList<GameObject>();
ArrayList<PImage> thumbnails = new ArrayList<PImage>();

int row     = 5;
int column  = 2;
int spacing  = 3;
int iconSize = 50;

void        addToLibrary(GameObject o) {
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

boolean     isAddedToLib(String filename) {
  for(GameObject o: library){
    if (o.filename.equals(filename))
      return true;
  }
  return false;
}

GameObject  getObjectFromLib(String filename) {
  for(GameObject o: library) {
      if (o.filename.equals(filename))
       return o;
  }
  return null;
}

void        createLibrary(ControlP5 c) {
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

void        renderLibrary(){
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

void        library_keyPressed(int key) {

}

void        library_controlEvent(ControlEvent theEvent) {
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
