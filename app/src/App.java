import app.mysql.MySQLModel;
import app.profile.ProfileService;
import app.http.AsynHttpServer;

/**
 * Created by afilippo on 29.05.17.
 */
public class App {
    public static void main(String[] args) {
//        AsynHttpServer httpServer = new app.http.AsynHttpServer();
//        httpServer.start();



        MySQLModel model = new MySQLModel();
        ProfileService profileService = new ProfileService(model);
        AsynHttpServer httpServer = new AsynHttpServer();
        httpServer.start();

//        System.out.println(System.currentTimeMillis());
//        System.out.println(profileService.getProfile(1).toString());
//        System.out.println(System.currentTimeMillis());
//        System.out.println(profileService.getProfile(1).toString());
//        System.out.println(System.currentTimeMillis());
//        System.out.println(profileService.getProfile(2).toString());
//        System.out.println(System.currentTimeMillis());
//        System.out.println(profileService.getProfile(2).toString());
//        System.out.println(System.currentTimeMillis());
//        Profile profile = new Profile().set;
//        profileService.createProfile("Имя", "Фамилия");
    }
}
