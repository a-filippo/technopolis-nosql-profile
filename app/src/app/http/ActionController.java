package app.http;

import java.util.HashMap;

/**
 * Created by afilippo on 31.05.17.
 */
public class ActionController {
    public String call(String url, HashMap<String, String> params, String container){
        Action action = null;
        String result = "";
        switch (url){
            case "/show-user":
                action = new ShowUserAction();
                break;
            case "/update-status":
                action = new UpdateStatusAction();
                break;
            case "/get-status":
                action = new GetStatusAction();
                break;
        }
        if (action != null){
            result = action.execute(params);
        }

        if (action.toContainer()){
            return putIntoContainer(container, result);
        } else {
            return result;
        }
    }

    private String putIntoContainer(String container, String data){
        String find = "{{{content}}}";
        int index = container.indexOf(find);
        return container.substring(0, index) + data + container.substring(index + find.length());
    }
}
