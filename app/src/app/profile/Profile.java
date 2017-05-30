package app.profile;


public final class Profile {
    private int id = 0;
    private String firstname;
    private String lastname;
    private boolean deleted = false;

    Profile(int id, String firstname, String lastname){
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Profile(){

    }

    @Override
    public String toString(){
        return firstname + " " + lastname;
    }

    @Override
    public int hashCode(){
        long result = 17;
        result = (result * id) % Integer.MAX_VALUE;
        result = (result * firstname.hashCode()) % Integer.MAX_VALUE;
        result = (result * lastname.hashCode()) % Integer.MAX_VALUE;
        if (deleted){
            result = (result * 7) % Integer.MAX_VALUE;
        }
        return (int)result;
    }

    @Override
    public boolean equals(Object profile){
        if (this == profile) return true;
        if (profile == null) return false;
        if (this.getClass() != profile.getClass()) return false;
        Profile otherProfile = (Profile) profile;
        boolean equal = true;
        equal = equal && (this.firstname == otherProfile.firstname);
        equal = equal && (this.lastname == otherProfile.lastname);
        equal = equal && (this.deleted == otherProfile.deleted);
        equal = equal && (this.id == otherProfile.id);
        return equal;
    }

    public int getId(){
        return id;
    }

    void setId(int id){
        this.id = id;
    }

    public String getFirstname(){
        return firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public boolean getDeleted(){
        return deleted;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    public void setDeleted(boolean deleted){
        this.deleted = deleted;
    }
}
