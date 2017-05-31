package app.http;

import app.profile.Profile;
import app.profile.ProfileService;

import java.util.HashMap;

/**
 * Created by afilippo on 31.05.17.
 */
public class GetStatusAction extends Action {
    public String execute(HashMap<String, String> params) {
        long timeStart = System.currentTimeMillis();
        int userId = Integer.parseInt(params.get("user"));
        ProfileService profileService = ProfileService.getService();

        int timestamp = profileService.getUserStatus(userId);

        long time = System.currentTimeMillis() - timeStart;

        return "{\"text\": \"Статус получен за " + time + "мс\", \"status\": \""+profileService.formatStatus(timestamp)+"\"}";
    }
}
