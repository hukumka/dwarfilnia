package nktl.dwarf.graphics.controls;

import java.util.HashMap;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 26.11.2015.
 */
public class ZActionMap {

    private HashMap<String, ZAction> actionMap = new HashMap<>();

    public static class ZAction {
        protected int keyCode;
        public ZAction(int keyCode){
            this.keyCode = keyCode;
        }

        public int getKeyCode(){
            return keyCode;
        }
    }

    public void addAction(String action, int keyCode){
        if (!actionMap.containsKey(action)){
            actionMap.put(action, new ZAction(keyCode));
        }
    }

    public void removeAction(String action){
        if (actionMap.containsKey(action)){
            actionMap.remove(action);
        }
    }

    public void changeKey(String action, int keyCode){
        if (actionMap.containsKey(action)){
            actionMap.get(action).keyCode = keyCode;
        }
    }

    public int getKeyCode(String action){
        return actionMap.containsKey(action)? actionMap.get(action).keyCode : 0;
    }

    public boolean containsAction(String action){
        return actionMap.containsKey(action);
    }
}
