package zeitfaden.com.zeitfaden.authentication;

/**
 * Created by tobias on 15.08.15.
 */
public interface ServerAuthenticate {
    public String userSignUp(final String name, final String email, final String pass, String authType) throws Exception;
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
}
