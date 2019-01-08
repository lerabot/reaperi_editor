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
  String  imageName;
  String  name;
  PVector pos;
  PVector size;
  int xFlip = 1;
  int yFlip = 1;
  PImage  t;

  public GameObject(XML raw) {
    this.raw  = raw;
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

  public void cleanXML() {
    XML xmlns = this.raw.getChild("xmlns");
    this.raw.removeChild(xmlns);
  }

  public void updateXML() {
    float _x = this.pos.x;
    float _y = this.pos.y;

    if(xFlip == -1)
      _x = -this.pos.x;

    this.raw.setFloat("x", _x);
    this.raw.setFloat("y", _y);
  }

  public void drawObject() {
    /*
    if (transform.equals("scale(-1,1)")) {
      xFlip = -1;
      pos.x = -pos.x;
    }
    */
  }

  public void flipX() {
  }

}
