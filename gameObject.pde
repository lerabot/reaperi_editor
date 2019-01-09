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
  int     xFlip = 1;
  int     yFlip = 1;
  PImage  t;

  public GameObject(String path) {
    this.t = loadImage(path);
    this.path = path;
    this.imageName = "New item no name lol"; //MUST BE CHANGED
    this.pos = new PVector(mouseX, mouseY);
    this.size = new PVector(t.width, t.height);
  };

  public GameObject(XML raw) {
    this.raw  = raw;
    this.path = raw.getString("sodipodi:absref");
    this.imageName = raw.getString("xlink:href");
    this.pos = new PVector(raw.getFloat("x"), raw.getFloat("y"));
    this.size = new PVector(raw.getFloat("width"), raw.getFloat("height"));
    this.t = loadImage(this.path);
    if(raw.hasAttribute("transform")) {
      String transform = raw.getString("transform");
      if (transform.equals("scale(-1,1)")) {
        this.xFlip = -1;
        this.pos.x = -pos.x;

      }
    }

  }

  public void cleanXML() {
    XML xmlns = this.raw.getChild("xmlns");
    this.raw.removeChild(xmlns);
  }

  public void updateXML() {
    float _x = this.pos.x;
    float _y = this.pos.y;

    if(xFlip == -1) {
      _x = -this.pos.x;
      if(!this.raw.hasAttribute("transform")) {
        this.raw.addChild("transform");
        this.raw.setString("transform", "scale(-1,1)");
      }
    } else {
      if(this.raw.hasAttribute("transform")) {
        XML c = this.raw.getChild("transform");
        this.raw.removeChild(c);
      }
    }
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
