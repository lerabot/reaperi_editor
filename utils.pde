//a mouse b object
boolean inside(PVector a, PVector b, PVector size) {
  if(abs(a.x) > abs(b.x) && abs(a.x) < abs(b.x) + abs(size.x)){
    if(abs(a.y) > abs(b.y) && abs(a.y) < abs(b.y) + abs(size.y)){
      return true;
    }
  }
  return false;
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
