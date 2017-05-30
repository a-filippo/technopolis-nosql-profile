package app.profile;

import app.cache.LFUCache;
import app.mysql.MySQLModel;

import java.util.*;

public class ProfileService {
    MySQLModel mysqlModel;
    ProfileModel profileModel;
    LFUCache<Integer, Profile> cache;
    public ProfileService(MySQLModel mysqlModel){
        this.mysqlModel = mysqlModel;
        profileModel = new ProfileModel(mysqlModel);
        cache = new LFUCache<>(3, (float)0.5);
    }

    public Profile[] getProfiles(int[] ids){
        // TODO IT
//        return profileModel.getProfiles(ids);
//        List<Integer> lookInDb = new ArrayList<>();
//        List<Integer> inCache = new ArrayList<>();
//
//        for (int i = 0; i < ids.length; i++) {
//            Profile profile = cache.get(ids[i]);
//            if (profile == null){
//                lookInDb.add(ids[i]);
//            } else {
//                inCache.add(ids[i]);
//            }
//        }
//        int[] newIds = new int[lookInDb.size()];
//        for (int i = 0; i < lookInDb.size(); i++){
//            newIds[i] = lookInDb.get(i);
//        }
//
        return profileModel.getProfiles(ids);
    }

    public Profile getProfile(int id){
        Profile profile = cache.get(id);
        if (profile == null){
            profile = profileModel.getProfile(id);
            cache.put(id, profile);
        }
        return profile;
    }

    public void setStatusOnline(int id){

    }

    public boolean getUserStatus(int id){
        return false;
    }

    public Profile createProfile(String firstname, String lastname){
        int id = profileModel.createProfile(firstname, lastname);
        return new Profile(id, firstname, lastname);
    }

    public boolean createProfile(Profile profile){
        if (profile.getId() > 0) return false;

        updateProfile(profile);
        return true;
    }

    public void removeProfile(int id){
        profileModel.removeProfile(id);
        cache.remove(id);
    }

    public void removeProfile(Profile profile){
        removeProfile(profile.getId());
    }

    public void updateProfile(Profile profile){
        profileModel.updateProfile(profile);
        cache.remove(profile);
    }
}
