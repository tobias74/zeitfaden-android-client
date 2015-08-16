package zeitfaden.com.zeitfaden.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * Created by tobias on 15.08.15.
 */
public class ZeitfadenAuthenticatorService extends Service{
    @Override
    public IBinder onBind(Intent intent) {

        ZeitfadenAuthenticator authenticator = new ZeitfadenAuthenticator(this);
        return authenticator.getIBinder();
    }
}
