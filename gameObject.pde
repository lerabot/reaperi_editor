public class GameObject {
  String  path;
  String  imageName;
  String  name;
  PVector pos;
  PVector size;
  int xFlip = 1;
  int yFlip = 1;
  PImage  t;

  public GameObject(XML raw) {

    this.path = raw.getString("sodipodi:absref");
    this.imageName = raw.getString("xlink:href");
    this.pos = new PVector(raw.getFloat("x"), raw.getFloat("y"));
    this.size = new PVector(raw.getFloat("width"), raw.getFloat("height"));
    if(raw.hasAttribute("transform")) {
      String transform = raw.getString("transform");
      if (transform.equals("scale(-1,1)")) {
        xFlip = -1;
        pos.x = -pos.x;
      }
    }



    t = loadImage(this.path);
  }

}
