package app.profile;

import app.mysql.MySQLModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by afilippo on 29.05.17.
 */
public class ProfileModel {
    private final String TABLE_USER = "user";
    private final String USER_ID = "user_id";
    private final String FIRSTNAME = "firstname";
    private final String LASTNAME = "lastname";
    private final String DELETED = "deleted";
    private final String STATUS = "status";

    private MySQLModel model;

    public ProfileModel(MySQLModel model){
        this.model = model;
    }

    public Profile[] getProfiles(int[] ids){
        if (ids.length == 0) return new Profile[0];

        Profile[][] profile = new Profile[1][0];
        model.query((Connection con) -> {
            Statement stmt = con.createStatement();
            String queryString = "select " +
                    USER_ID + ", " +
                    FIRSTNAME + ", " +
                    STATUS + ", " +
                    LASTNAME + ", " +
                    DELETED + " from " +
                    TABLE_USER + " where " +
                    USER_ID + " in " + model.createFormatIn(ids.length);
            PreparedStatement query = con.prepareStatement(queryString);
            for (int i = 0; i < ids.length; i++){
                query.setInt(i + 1, ids[i]);
            }
            ResultSet rs = query.executeQuery();

            rs.last();
            int size = rs.getRow();
            if (size == 0) return;
            rs.beforeFirst();

            profile[0] = new Profile[size];

            int i = 0;
            if (rs.next()) {
                int id = rs.getInt(USER_ID);
                String firstname = rs.getString(FIRSTNAME);
                String lastname = rs.getString(LASTNAME);
                boolean deleted = rs.getBoolean(DELETED);
                int status = rs.getInt(STATUS);
                profile[0][i] = new Profile(id, firstname, lastname);
                profile[0][i].setDeleted(deleted);
                profile[0][i].setStatus(status);
                i++;
            }

            con.close();
            stmt.close();
            rs.close();
            query.close();
        });
        return profile[0];
    }



    public Profile getProfile(int id){
        Profile[] profile = new Profile[1];
        model.query((Connection con) -> {
            Statement stmt = con.createStatement();
            String queryString = "select " +
                    FIRSTNAME + ", " +
                    LASTNAME + ", " +
                    STATUS + ", " +
                    DELETED + " from " +
                    TABLE_USER + " where " +
                    USER_ID + " = ?";
            PreparedStatement query = con.prepareStatement(queryString);
            query.setInt(1, id);
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                String firstname = rs.getString(FIRSTNAME);
                String lastname = rs.getString(LASTNAME);
                boolean deleted = rs.getBoolean(DELETED);
                int status = rs.getInt(STATUS);
                profile[0] = new Profile(id, firstname, lastname);
                profile[0].setDeleted(deleted);
                profile[0].setStatus(status);
            } else {
                profile[0] = null;
            }

            con.close();
            stmt.close();
            rs.close();
            query.close();
        });
        return profile[0];
    }

    public int createProfile(String firstname, String lastname){
        Profile profile = new Profile(0, firstname, lastname);
        return updateProfile(profile);
    }

//    public int getStatus(int id){
//        int[] status = new int[1];
//        model.query((Connection con) -> {
//            Statement stmt = con.createStatement();
//            String queryString = "select " +
//                    STATUS + " from " +
//                    TABLE_USER + " where " +
//                    USER_ID + " = ?";
//            PreparedStatement query = con.prepareStatement(queryString);
//            query.setInt(1, id);
//            ResultSet rs = query.executeQuery();
//            rs.next();
//            status[0] = rs.getInt(STATUS);
//
//            con.close();
//            stmt.close();
//            rs.close();
//            query.close();
//        });
//        return status[0];
//    }

    public void removeProfile(int id){
        model.query((Connection con) -> {
            String queryString = "update " + TABLE_USER + " set " + DELETED + " = ?";
            PreparedStatement insertQuery = con.prepareStatement(queryString);
            insertQuery.setInt(1, 1);
            insertQuery.executeUpdate();

            con.close();
            insertQuery.close();
        });
    }

    public int updateProfile(Profile profile){
        int[] id = new int[1];
        model.query((Connection con) -> {
            String queryString;
            boolean updating = profile.getId() > 0;
            if (updating){
                queryString = "update " + TABLE_USER + " set " +
                        FIRSTNAME + " = ?, " +
                        LASTNAME + " = ?, " +
                        STATUS + " = ?, " +
                        DELETED + " = ? where " +
                        USER_ID + " = ?";
            } else {
                queryString = "insert into " + TABLE_USER + " set " +
                        FIRSTNAME + " = ?, " +
                        LASTNAME + " = ?, " +
                        STATUS + " = ?, " +
                        DELETED + " = ?";
            }
            PreparedStatement insertQuery = con.prepareStatement(queryString, PreparedStatement.RETURN_GENERATED_KEYS);
            insertQuery.setString(1, profile.getFirstname());
            insertQuery.setString(2, profile.getLastname());
            insertQuery.setInt(3, profile.getStatus());
            insertQuery.setBoolean(4, profile.getDeleted());
            if (updating){
                insertQuery.setInt(5, profile.getId());
            }
            insertQuery.executeUpdate();

            if (updating) {
                id[0] = profile.getId();
            } else {
                ResultSet rs = insertQuery.getGeneratedKeys();
                rs.next();
                id[0] = rs.getInt(1);
                profile.setId(id[0]);
                rs.close();
            }
            con.close();
            insertQuery.close();
        });

        return id[0];
    }
}