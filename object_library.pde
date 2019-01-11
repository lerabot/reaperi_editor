ArrayList<GameObject> library = new ArrayList<GameObject>();

int row     = 5;
int column  = 2;

void addToLibrary(GameObject o) {
  if(!library.contains(o)){
    library.add(o);
  }
}

void createLibrary(ControlP5 c) {

  int size    = 50;
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

      c.addButton(iconName)
      .setPosition(pos.x + (j * (size + spacing)), pos.y + (i * (size + spacing)))
      .setSize(size, size);

      //rect(pos.x + (i * (size + spacing)), pos.y + (j * (size + spacing)), size, size);
    }
  }
}

void renderLibrary(){
  int size    = 50;
  int spacing = 3;
  PVector pos = new PVector(0, 100);
  PImage  t   = null;

  fill(100, 100);
  noStroke();
  for(int i = 0; i < row; i++) {
    for(int j = 0; j < column; j++) {
      int iconNum = (i * column) + j;
      //image(library.get(iconNum).t, pos.x + (j * (size + spacing)), pos.y + (i * (size + spacing)));
    }
  }
}
