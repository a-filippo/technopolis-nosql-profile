package app.profile;

import app.mysql.MySQLModel;

import java.sql.Connection;
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

    private MySQLModel model;

    public ProfileModel(MySQLModel model){
        this.model = model;
    }

    public Profile getProfile(int id){
        Profile[] profile = new Profile[1];
        model.query((Connection con) -> {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select " + FIRSTNAME + ", " + LASTNAME + " from " + TABLE_USER + " where " + USER_ID+ "=" + id);
            if (rs.next()) {
                String firstname = rs.getString(FIRSTNAME);
                String lastname = rs.getString(LASTNAME);
                profile[0] = new Profile(id, firstname, lastname);
            } else {
                profile[0] = null;
            }

            con.close();
            stmt.close();
            rs.close();
        });

        return profile[0];
    }
}
