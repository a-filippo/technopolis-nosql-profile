package app.http;

import java.util.HashMap;

/**
 * Created by afilippo on 31.05.17.
 */
abstract public class Action {
    abstract public String execute(HashMap<String, String> params);

    public boolean toContainer(){
        return false;
    }
}
