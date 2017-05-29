package app.profile;

/**
 * Created by afilippo on 29.05.17.
 */
public class Profile {
    private int id;
    private String firstname;
    private String lastname;

    public Profile(int id, String firstname, String lastname){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String toString(){
        return firstname + " " + lastname;
    }

    public int getId(){
        return id;
    }

    public String getFirstname(){
        return firstname;
    }

    public String getLastname(){
        return lastname;
    }
}
