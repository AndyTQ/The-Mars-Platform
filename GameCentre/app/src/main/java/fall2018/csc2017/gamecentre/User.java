package fall2018.csc2017.gamecentre;

import java.io.Serializable;

/**
 * A User class that allows FireBase to store username and email into FireBase Database.
 * Adapted from https://www.youtube.com/watch?v=7Yc3Pt37coM&t=68s
 */
public class User implements Serializable  {
    /**
     * User's name. The name is unique. (Constraint is set by Firebase Database.)
     */
    private String name;
    /**
     * User's email. The email is unique. (Constraint is set by Firebase Database.)
     */
    private String email;
    /**
     * User's bio. (Self-introduction).
     */
    private String bio;
    /**
     * User's url for profile image.
     */
    private String imageUrl;
    /**
     * User's location.
     */
    private String location;
    /**
     * User's hashed unique-identifier.
     */
    private String uid;

    /**
     * The required empty constructor for firebase database.
     */
    public User() {
    }

    /**
     * The constructor.
     *
     * @param name  the name of user.
     * @param email the email of user
     */
    public User(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.location = "";
        this.bio = "Introduce yourself...";
        this.imageUrl = "https://firebasestorage.googleapis.com/v0/b/gamecentre-fa151.appspot.com/o/default_profile_pic.png?alt=media&token=bdbb942c-a7ae-49a9-90f1-a5bfa1c0fc94";
        this.uid = uid;
    }

    /**
     * Getter for Name.
     *
     * @return return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for image url.
     *
     * @return the user's image url.
     */
    public String getImageUrl() {
        return imageUrl;
    }



    /**
     * Getter for personal bio.
     *
     * @return return the personal bio.
     */
    public String getBio() {
        return bio;
    }

    /**
     * Getter for the user's location
     *
     * @return return the user's location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for the user's email.
     *
     * @return return the user's email.
     */
    public String getEmail(){
        return email;
    }

    /**
     * Setter for the user's location.
     */
    public void setLocation(String location) {this.location = location;}

    /**
     * Return the string representation of the user
     *
     * @return String representation of the user.
     */
    public String toString() {
        return name + email.replaceAll(",", "_");
    }
}
