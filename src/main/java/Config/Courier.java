package Config;

import java.util.Random;

public class Courier {
    private String login;
    private String password;
    private String firstName;
    boolean isInApp;
    public Courier() {
        this.login =  "nintiago"; //Data.courierLogins[new Random().nextInt(Data.courierLogins.length)];
        this.password =  "123499"; //Data.courierPasswords[new Random().nextInt(Data.courierPasswords.length)];
        this.firstName = Data.courierNames[new Random().nextInt(Data.courierNames.length)];
        this.isInApp = false;
    }

    public String getNewCourierRequestBody(){
        return "{\"login\": \""+this.login+"\", \"password\": \""+this.password+"\", \"firstName\": \""+
                this.firstName+"\"}";
    }
    public String getLoginCourierRequestBody(){
        return "{\"login\": \""+this.login+"\", \"password\": \""+this.password+"\"}";
    }

    public String getNewCourierAPIPath(){
        return "/api/v1/courier";
    }
    public String getLoginCourierAPIPath(){
        return "/api/v1/courier/login";
    }

    public String getDeleteCourierAPIPath(int courierId){
        return "/api/v1/courier/"+courierId;    // +id !!!
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean isInApp() {
        return isInApp;
    }

    public void setInApp(boolean inApp) {
        isInApp = inApp;
    }
}
