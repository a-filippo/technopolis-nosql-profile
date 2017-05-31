package app.profile;

import app.cache.LFUCache;
import app.mysql.MySQLModel;

public class ProfileService {
    private static ProfileService profileService;

    MySQLModel mysqlModel;
    ProfileModel profileModel;
    ProfileStatus profileStatus;

    LFUCache<Integer, Profile> cache;
    public ProfileService(MySQLModel mysqlModel){
        this.mysqlModel = mysqlModel;
        profileModel = new ProfileModel(mysqlModel);
        profileStatus = new ProfileStatus();
        cache = new LFUCache<>(3);

        profileService = this;
    }

    public static ProfileService getService(){
        return profileService;
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
            cache.set(id, profile);
        }
        System.out.println(cache.toString());
        return profile;
    }

    public void setStatusOnline(int id){
        int timestamp = profileStatus.updateStatus(id);
        Profile profile = cache.get(id);
        if (profile != null){
            profile.setStatus(timestamp);
            cache.set(id, profile);
        }
        System.out.println(cache.toString());
    }

//    public boolean isOnline(int timestamp){
//        return profileStatus.getStatus(timestamp);
//    }
//
//    public String formatDate(int timestamp){
//        return profileStatus.formatDate(timestamp);
//    }

    public int getUserStatus(int id){
        int timestamp = profileStatus.getTimestamp(id);
        if (timestamp == 0){
            Profile profile = cache.get(id);
            if (profile == null){
                timestamp = profileModel.getProfile(id).getStatus();
            } else {
                timestamp = profile.getStatus();
            }
        }
        System.out.println(cache.toString());
        return timestamp;
    }

    public String formatStatus(int time){
        String out;
        if (profileStatus.getStatus(time)){
            out = "Онлайн";
        } else {
            out = "Был в сети " + profileStatus.formatDate(time);
        }
        return out;
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
        cache.delete(id);
        System.out.println(cache.toString());
    }

    public void removeProfile(Profile profile){
        removeProfile(profile.getId());
    }

    public void updateProfile(Profile profile){
        profileModel.updateProfile(profile);
        cache.delete(profile.getId());
        System.out.println(cache.toString());
    }
}
