package app.http;

import app.profile.Profile;
import app.profile.ProfileService;

import java.util.HashMap;

/**
 * Created by afilippo on 31.05.17.
 */
public class ShowUserAction extends Action {
    @Override
    public boolean toContainer(){
        return true;
    }

    public String execute(HashMap<String, String> params){
        long timeStart = System.currentTimeMillis();
        int userId = Integer.parseInt(params.get("user"));
        ProfileService profileService = ProfileService.getService();

        Profile profile = profileService.getProfile(userId);

        StringBuffer output = new StringBuffer("");

        long time = System.currentTimeMillis() - timeStart;

        if (profile == null){
            return "<b>Пользователя не существует</b>";
        }

        output.append("<table class=\"main-table\" data-id=\""+ profile.getId() +"\">");
        output.append("<tr><td>ID</td><td>").append(profile.getId()).append("</td></tr>");
        output.append("<tr><td>Имя</td><td>").append(profile.getFirstname()).append("</td></tr>");
        output.append("<tr><td>Фамилия</td><td>").append(profile.getLastname()).append("</td></tr>");
        output.append("<tr><td>Удаленный</td><td>").append(profile.getDeleted() ? "Да" : "Нет").append("</td></tr>");
        output.append("<tr><td>Статус</td><td class=\"user-status\">").append(profileService.formatStatus(profile.getStatus())).append("</td></tr>");
        output.append("<tr><td>Время формирования страницы</td><td>").append(time).append("мс</td></tr>");
        output.append("</table>");

        return output.toString();
    }
}
