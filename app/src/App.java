import app.mysql.MySQLModel;
import app.profile.Profile;
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

        System.out.println(System.currentTimeMillis());
        System.out.println(profileService.getProfile(1).toString());
        System.out.println(System.currentTimeMillis());
        System.out.println(profileService.getProfile(1).toString());
        System.out.println(System.currentTimeMillis());
        System.out.println(profileService.getProfile(2).toString());
        System.out.println(System.currentTimeMillis());
        System.out.println(profileService.getProfile(2).toString());
        System.out.println(System.currentTimeMillis());
//        Profile profile = new Profile().set;
//        profileService.createProfile("Имя", "Фамилия");
    }
}
