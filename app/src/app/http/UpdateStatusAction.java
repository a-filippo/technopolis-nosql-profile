package app.http;

import app.profile.Profile;
import app.profile.ProfileService;

import java.util.HashMap;

/**
 * Created by afilippo on 31.05.17.
 */
public class UpdateStatusAction extends Action {
    public String execute(HashMap<String, String> params) {
        long timeStart = System.currentTimeMillis();
        int userId = Integer.parseInt(params.get("user"));
        ProfileService profileService = ProfileService.getService();

        profileService.setStatusOnline(userId);

        StringBuffer output = new StringBuffer("");

        long time = System.currentTimeMillis() - timeStart;

        return "{\"text\": \"Активность обновлена за " + time + "мс\"}";
    }
}
