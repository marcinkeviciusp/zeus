package jalp.zeus;

import com.firebase.client.Firebase;

/**
 * Created by Chicken on 22/02/2016.
 */
public class ZeusLogIn extends android.app.Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
