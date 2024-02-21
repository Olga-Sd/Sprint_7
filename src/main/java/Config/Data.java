package Config;
import io.restassured.RestAssured;
//import org.apache.http.Header;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class Data {

    public static final Header requestHeader = new Header( "Content-type", "application/json");
    public static String[] courierLogins = new String[]{"ninja1233", "super1122", "courier3210", "flash998"};
    public static String[] courierPasswords = new String[]{"12345", "pass1", "pass2", "54321"};
    public static String[] courierNames = new String[]{"saske", "alex", "zurab", "john"};
}
