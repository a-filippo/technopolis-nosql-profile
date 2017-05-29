package app.profile;

import app.mysql.MySQLModel;

public class ProfileService {
    MySQLModel mysqlModel;
    ProfileModel profileModel;
    public ProfileService(MySQLModel mysqlModel){
        this.mysqlModel = mysqlModel;
        profileModel = new ProfileModel(mysqlModel);
    }

    public Profile[] getProfiles(int[] ids){
        return new Profile[0];
    }

    public Profile getProfile(int id){
        return profileModel.getProfile(id);
    }

    public boolean getUserStatus(int id){
        return false;
    }

    public void saveProfile(String firstname, String lastname){
         
    }
}
