package jalp.zeus;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

/**
 * Created by Josh Stennett on 24/02/16.
 */
public class Notifier {
    //Notification message ID
    private static int NOTIFY_ME_ID=1234;
    //Counter
    private int count=0;
    //Create NotificationManager  object
    private NotificationManager notifyMgr=null;
    Context context;
    public static boolean created = false;

    public Notifier(Context con){
        this.context = con;
        System.out.println("NOTIFIER #############################");
        notifyMgr=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        ZeusMainActivity.ROOT.child("notifications").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot snapshot, String previousChildKey) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + snapshot.getKey());
                NOTIFY_ME_ID++;

                String message = snapshot.child("msg").getValue(String.class);
                String title = snapshot.child("title").getValue(String.class);


                triggerNotification(title,message);
                ZeusMainActivity.ROOT.child("notifications").child(snapshot.getKey()).setValue(null);

            }
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.

            public void onChildRemoved(DataSnapshot snapshot) {}

            public void onChildChanged(DataSnapshot snapshot, String someString) {}

            public void onChildMoved(DataSnapshot snapshot, String someString) {}

            public void onCancelled(FirebaseError err) {}

        });

        created = true;
    }

    public void triggerNotification(String title, String text) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_info)
                        .setContentTitle(title)
                        .setContentText(text);

        // mId allows you to update the notification later on.
        notifyMgr.notify(NOTIFY_ME_ID, mBuilder.build());

    }

}
