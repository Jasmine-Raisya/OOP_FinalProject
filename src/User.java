
import java.io.Serializable; //Serializable interface

//Initialises the User Class and subclasses -- implements the Serializable Interface
//Serializable Serializes the User: username and passwords, it will be called in the Main file ToDoListApp -- with write
// write object to FileOutputStream


//This class is used to assign the private usernames and passcodes to access the to-do-list app
public class User implements Serializable {
    private String username;
    private String passcode;


    public User(String username, String passcode) {
        this.username = username;
        this.passcode = passcode;
    }

    public String getUsername() {
        return username;
    } //Why is this no usages?

    public String getPasscode() {
        return passcode;
    }

}
