package jalp.zeus;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.println("##############################################################");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<" + ZeusMainActivity.ROOT.child("rooms").toString());



        ZeusMainActivity.ROOT.child("rooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                for(DataSnapshot data : snapshot.getChildren())
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + data.getValue(String.class));
            }
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.

            public void onChildRemoved(DataSnapshot snapshot) {

            }

            public void onChildChanged(DataSnapshot snapshot, String someString) {

            }

            public void onChildMoved(DataSnapshot snapshot, String someString) {

            }

            public void onCancelled(FirebaseError err) {

            }


        });
    }
}