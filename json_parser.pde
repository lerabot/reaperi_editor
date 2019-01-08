import java.util.*;

JSONObject  json;
StringList  names;
String      active_npc;

void loadJSON(String path) {
  json = loadJSONObject(path);
  names = new StringList();

  JSONArray map = json.getJSONArray("hideout");
  for(int i = 0; i < map.size(); i++){
    JSONObject npc = map.getJSONObject(i);
    if(!names.hasValue(npc.getString("npc"))) {
      names.append(npc.getString("npc"));
      cp5.get(ScrollableList.class, "npc").addItem(npc.getString("npc"), 1);
      }
    }
}

void updateQuestBox(int index) {
  JSONArray list = json.getJSONArray("hideout");
  cp5.get(ScrollableList.class, "questName").clear();
  for(int i = 0; i < list.size(); i++) {
    JSONObject npc = list.getJSONObject(i);
    String npc_name = npc.getString("npc");
    active_npc = names.get(index);
    if(npc_name.equals(active_npc) == true) {
      cp5.get(ScrollableList.class, "questName").setVisible(true);
      cp5.get(ScrollableList.class, "questName").addItem(npc.getString("questName") + " -> " + npc.getString("textID"), 1);
    }
  }
  //JSONObject o = list.getJSONObject(index);
  //JSONArray text_array = o.getJSONArray("text");
}

void updateDialogBox(int index) {
  JSONArray list = json.getJSONArray("hideout");
  //cp5.get(ScrollableList.class, "dialog").clear();
  for(int i = 0; i < list.size(); i++) {
    JSONObject npc = list.getJSONObject(i);
    String npc_name = npc.getString("npc");
    if(npc_name.equals(active_npc)) {
      if(index > 0)
        index--;

      if(index == 0){
        JSONArray text_array = npc.getJSONArray("text");
        String s = text_array.getString(0);
        cp5.get(Textfield.class, "dialog").setText(s);
      }
    }
  }
}
