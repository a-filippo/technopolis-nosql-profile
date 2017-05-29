import app.mysql.MySQLModel;
import app.profile.ProfileService;

/**
 * Created by afilippo on 29.05.17.
 */
public class App {
    public static void main(String[] args) {
//        HttpServer httpServer = new HttpServer();
//        httpServer.start();



        MySQLModel model = new MySQLModel();
        ProfileService profileService = new ProfileService(model);
        System.out.println(profileService.getProfile(1).toString());
//        Profile profile = new Profile();
    }
}
