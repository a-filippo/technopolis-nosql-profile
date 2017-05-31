package app.cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;


public class StatusCache {

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StatusCache cache = new StatusCache();
        try {
            while (true) {
                String s = in.readLine();
                String[] ss = s.split("\\s+");
                switch (ss[0]){
                    case "put":
                        cache.put(Integer.parseInt(ss[1]), Integer.parseInt(ss[2]));
                        System.out.println(cache.out());
                        break;
                    case "get":
                        System.out.println(cache.get(Integer.parseInt(ss[1])));
                        break;
                }

            }
        } catch (IOException e){
            // TODO
        }


    }

    private int maxSize = 3;

    private HashMap<Integer, Status> statusStorage;
    private TreeSet<Status> sorted;

    public StatusCache(){
        statusStorage = new HashMap<>(5);
        sorted = new TreeSet<>((o1, o2) -> o1.compareTo(o2));

    }

    public void put(int id, int timestamp){
        Status statusOld = statusStorage.get(id);
        if (statusOld != null){
            sorted.remove(statusOld);
        } else if (statusStorage.size() == maxSize){
            deleteOld();
        }

        Status statusNew = new Status(id, timestamp);
        statusStorage.put(id, statusNew);
        sorted.add(statusNew);
    }

    public int get(int id){
        Status status = statusStorage.get(id);
        if (status == null){
            return 0;
        } else {
            return status.timestamp;
        }
    }

    private void deleteOld(){
        Status status = sorted.first();
        statusStorage.remove(status.id);
        sorted.remove(status);
    }

    public String out(){
        return statusStorage.toString() + "\r\n" + sorted.toString();
    }

    private class Status{
        int id;
        int timestamp;
        Status(int id, int timestamp){
            this.id = id;
            this.timestamp = timestamp;
        }

        @Override
        public String toString(){
            return id + " " + timestamp;
        }

        public int compareTo(Status s){
            if (this.timestamp == s.timestamp){
                return (this.id == s.id) ? 0 : Integer.compare(this.id, s.id);
            }
            return Integer.compare(this.timestamp, s.timestamp);
        }
    }
}
