package co.uk.mobilejug.kicc;

/**
 * Created by edit on 30/04/2014.
 */
public class Authorisation {

    private String error;
    private String name;
    private String email;
    private String apiKey;
    private String createdAt;
    private String message;




    public String getError() {
        return error;
    }

    public void setError(String Error) {
        error = Error;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        name = Name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        email = Email;
    }

    public String getApikey() {
        return apiKey;
    }

    public void setApikey(String Apikey) {
        apiKey = Apikey;
    }



    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String Message) {
        message = Message;
    }





}
