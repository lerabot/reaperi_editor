//assumes A is the topLeft corner, and size is botRight
boolean over(PVector a, PVector aSize, PVector b, PVector bSize) {
  if (abs(a.x) > abs(b.x) && abs(a.x) < (abs(b.x) + bSize.x) &&
      abs(a.y) > abs(b.y) && abs(a.y) < (abs(b.y) + bSize.y))
    return true;
  return false;
}

boolean minkowski(GameObject a, GameObject b) {
  PVector aCenter = new PVector(abs(a.pos.x) + (a.size.x/2 * a.xFlip), abs(a.pos.y) + (a.size.y/2 * a.yFlip));
  PVector bCenter = new PVector(abs(b.pos.x) + (b.size.x/2 * b.xFlip), abs(b.pos.y) + (b.size.y/2 * b.yFlip));
  PVector aMid    = new PVector(a.size.x/2, a.size.y/2);
  PVector bMid    = new PVector(b.size.x/2, b.size.y/2);;

  if(aCenter.x < bCenter.x - (aMid.x + bMid.x) || aCenter.x > bCenter.x + (aMid.x + bMid.x)) return false;
  if(aCenter.y < bCenter.y - (aMid.y + bMid.y) || aCenter.y > bCenter.y + (aMid.y + bMid.y)) return false;

  return true;
}

boolean touching(GameObject a, GameObject b) {
  boolean r = false;

  if(a == b)
    return r;

  r = minkowski(a,b);
  //if(r)
    //println(a.imageName + " -> " + b.imageName);

  return r;
}









boolean insideObject(PVector a, GameObject o) {
  if(o.xFlip == -1){
    if(abs(a.x) < abs(o.pos.x) && abs(a.x) > abs(o.pos.x) + (o.size.x * o.xFlip)){
      if(abs(a.y) > abs(o.pos.y) && abs(a.y) < abs(o.pos.y) + (o.size.y)){
        return true;
      }
    }
  }
  if(abs(a.x) > abs(o.pos.x) && abs(a.x) < abs(o.pos.x) + (o.size.x * o.xFlip)){
    if(abs(a.y) > abs(o.pos.y) && abs(a.y) < abs(o.pos.y) + (o.size.y * o.yFlip)){
      return true;
    }
  }
  return false;
}

boolean insideObject(GameObject a, GameObject o) {
  if(o.xFlip == -1){
    if(abs(a.pos.x) < abs(o.pos.x) && abs(a.pos.x) + (a.size.x * a.xFlip) > abs(o.pos.x) + (o.size.x * o.xFlip)){
      if(abs(a.pos.y) > abs(o.pos.y) && abs(a.pos.y) < abs(o.pos.y) + (o.size.y)){
        return true;
      }
    }
  }
  if(abs(a.pos.x) > abs(o.pos.x) && abs(a.pos.x) + (a.size.x * a.xFlip) < abs(o.pos.x) + (o.size.x * o.xFlip)){
    if(abs(a.pos.y) > abs(o.pos.y) && abs(a.pos.y) < abs(o.pos.y) + (o.size.y * o.yFlip)){
      return true;
    }
  }
  return false;
}
