//assumes A is the topLeft corner, and size is botRight
boolean over(PVector a, PVector aSize, PVector b, PVector bSize) {
  if (abs(a.x) > abs(b.x) && abs(a.x) < (abs(b.x) + bSize.x) &&
      abs(a.y) > abs(b.y) && abs(a.y) < (abs(b.y) + bSize.y))
    return true;
  return false;
}

/*
//a mouse b object
boolean inside(PVector a, PVector aSize, PVector b, PVector bSize) {
  if(abs(a.x) > abs(b.x) && abs(a.x) < (abs(b.x) + size.x)){
    if(abs(a.y) > abs(b.y) && abs(a.y) < (abs(b.y) + size.y)){
      return true;
    }
  }
  return false;
}
*/
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
