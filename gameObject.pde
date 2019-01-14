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
  String  filename;
  String  name;
  String  luaScript;
  PVector pos;
  PVector size;
  boolean selected = false;
  int     xFlip = 1;
  int     yFlip = 1;
  int     depth = 0;
  PImage  t;

  public GameObject(){};

  public GameObject(String path) {
    this.t        = loadImage(path);
    this.path     = path;
    this.filename = "New item no name lol"; //MUST BE CHANGED
    this.pos      = new PVector(mouseX, mouseY);
    this.size     = new PVector(t.width, t.height);
    this.luaScript = null;
  };

  public GameObject(XML raw) {
    this.raw      = raw;
    this.path     = raw.getString("sodipodi:absref");
    this.filename = raw.getString("xlink:href");
    this.pos      = new PVector(raw.getFloat("x"), raw.getFloat("y"));
    this.size     = new PVector(raw.getFloat("width"), raw.getFloat("height"));
    this.t        = loadImage(this.path);

    /*
    if(raw.hasAttribute("luaScript")) {
      String lua = raw.getString("luaScript");
      this.luaScript = lua;
    } else {
      this.raw.addChild("luaScript");
      this.raw.setString("luaScript", "null");
      this.luaScript = null;
    }
    */
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
      this.filename     = o.filename;
      this.name         = o.name;
      this.size         = o.size;
      this.xFlip        = o.xFlip;
      this.yFlip        = o.yFlip;
      this.pos          = new PVector(mouse.x - (size.x/2 * xFlip), mouse.y - (size.y/2 * yFlip));

      this.t            = loadImage(this.path);
      }
  }

  public void cleanXML() {
    XML xmlns = this.raw.getChild("xmlns");
    this.raw.removeChild(xmlns);
  }

  public void fixPositionXML() {
    float _x = this.pos.x;
    float _y = this.pos.y;

    if(xFlip == -1) {
      _x = -this.pos.x;
      this.raw.setString("transform", "scale(-1,1)");
    } else {
      this.raw.setString("transform", "");
    }

    this.raw.setFloat("x", _x);
    this.raw.setFloat("y", _y);
  }

  public void showXML() {
    int attW = 50;
    int attH = 20;
    int spacerY = 15;
    //println("-- XML Attributes --");
    String att[] = getUsefulAttributes();
    //println(att);
    this.updateXML();

    for(int i = 0; i < att.length; i++) {
      String attribute = this.raw.getString(att[i]);
      if(attribute == null)
        attribute = "NA";
      xmlAtt.get(Textfield.class,att[i]).setValue(attribute);
    }

  }

  public void updateXML() {
    this.raw.setFloat("x", this.pos.x);
    this.raw.setFloat("y", this.pos.y);
    this.raw.setInt("depth" , this.depth);
    this.raw.setFloat("width", this.size.x);
    this.raw.setFloat("height", this.size.y);
    this.raw.setString("xlink:href", this.filename);
    this.raw.setString("sodipodi:absref", this.path);
    this.raw.setString("luaScript", this.luaScript);

    if(this.xFlip == 1)
      this.raw.setString("transform", "scale(-1,1)");

    this.fixPositionXML();
  }

  public void copyPosition(GameObject o) {
    this.pos.x = o.pos.x;
    this.pos.y = o.pos.y;
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
