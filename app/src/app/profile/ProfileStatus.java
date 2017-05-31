package app.profile;

import app.cache.StatusCache;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by afilippo on 30.05.17.
 */
public class ProfileStatus {
    private StatusCache cache;
    private SimpleDateFormat dateFormat;

    ProfileStatus(){
        cache = new StatusCache();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }

    public static int ONLINE_TIME = 5;

    public int updateStatus(int id){
        int timestamp = (int)(System.currentTimeMillis() / 1000);
        cache.put(id, timestamp);
        return timestamp;
    }

    public int getTimestamp(int id){
        return cache.get(id);
    }

    public boolean getStatus(int time){
        return time >= (System.currentTimeMillis() / 1000) - 5;
    }

    public String formatDate(int time){
        return dateFormat.format(new Date((long)time * 1000));
    }
}
