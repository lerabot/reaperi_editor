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
  boolean selected = false;
  int     xFlip = 1;
  int     yFlip = 1;
  PImage  t;

  public GameObject(){};

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

  public GameObject(GameObject o) {
    if (o != null) {
      this.raw          = o.raw;
      this.path         = o.path;
      this.imageName    = o.imageName;
      this.name         = o.name;
      this.size         = o.size;
      this.xFlip        = o.xFlip;
      this.yFlip        = o.yFlip;
      this.pos          = new PVector(mouse.x - (size.x/2 * xFlip), mouse.y - (size.y/2 * yFlip));

      this.t            = o.t;
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
    g.pushMatrix();
    g.translate(this.pos.x, this.pos.y);
    g.scale(this.xFlip, this.yFlip);
    g.image(this.t, 0, 0);
    g.noStroke();
    if(renderBox) {
      if(this.selected)
        g.fill(0,0,100,100);
      else
        g.fill(100,0,0,100);
      g.rect(0, 0, this.size.x, this.size.y);
    }
    g.popMatrix();
  }

  public void flipX() {
  }

}
