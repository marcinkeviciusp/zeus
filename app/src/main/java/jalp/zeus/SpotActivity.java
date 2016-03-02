package jalp.zeus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class SpotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.SpotName);
        textView.setText(RoomActivity.spot);
        setSupportActionBar(toolbar);

        Firebase ref = ZeusMainActivity.ROOT.child("spots").child(RoomActivity.spot);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("battery")){
                    ProgressBar mProgress = (ProgressBar) findViewById(R.id.batteryProgress);
                    mProgress.setProgress(dataSnapshot.getValue(int.class));
                    TextView text = (TextView) findViewById(R.id.batteryText);
                    text.setText("Battery Level: " + dataSnapshot.getValue(double.class).toString());
                }else if(dataSnapshot.getKey().equals("alive")){
                    TextView text = (TextView) findViewById(R.id.aliveText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Alive");
                    else
                        text.setText("Alive");
                }else if(dataSnapshot.getKey().equals("compass")){
                    TextView text = (TextView) findViewById(R.id.compassText);
                    text.setText(dataSnapshot.getValue(double.class).toString());
                }else if(dataSnapshot.getKey().equals("accel")){
                    TextView text = (TextView) findViewById(R.id.accelText);
                    text.setText(dataSnapshot.getValue(double.class).toString());
                }else if(dataSnapshot.getKey().equals("btn_l")){
                    TextView text = (TextView) findViewById(R.id.lbtnText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Pressed");
                    else
                        text.setText("Not Press");
                }else if(dataSnapshot.getKey().equals("btn_r")){
                    TextView text = (TextView) findViewById(R.id.rbtnText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Pressed");
                    else
                        text.setText("Not Press");
                }else if(dataSnapshot.getKey().equals("infrared")){
                    TextView text = (TextView) findViewById(R.id.infraredText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Active");
                    else
                        text.setText("Not Active");
                }else if(dataSnapshot.getKey().equals("light")){
                    TextView text = (TextView) findViewById(R.id.lightText);
                    text.setText(dataSnapshot.getValue(double.class).toString());
                }else if(dataSnapshot.getKey().equals("sound")){
                    TextView text = (TextView) findViewById(R.id.soundText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Active");
                    else
                        text.setText("Not Active");
                }else if(dataSnapshot.getKey().equals("temp")){
                    TextView text = (TextView) findViewById(R.id.tempText);
                    text.setText(dataSnapshot.getValue(double.class).toString());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("battery")){
                    ProgressBar mProgress = (ProgressBar) findViewById(R.id.batteryProgress);
                    mProgress.setProgress(dataSnapshot.getValue(int.class));
                    TextView text = (TextView) findViewById(R.id.batteryText);
                    text.setText("Battery Level: " + dataSnapshot.getValue(int.class).toString());
                }else if(dataSnapshot.getKey().equals("alive")){
                    TextView text = (TextView) findViewById(R.id.aliveText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Alive");
                    else
                        text.setText("Alive");
                }else if(dataSnapshot.getKey().equals("compass")){
                    TextView text = (TextView) findViewById(R.id.compassText);
                    text.setText(dataSnapshot.getValue(int.class).toString());
                }else if(dataSnapshot.getKey().equals("accel")){
                    TextView text = (TextView) findViewById(R.id.accelText);
                    text.setText(dataSnapshot.getValue(int.class).toString());
                }else if(dataSnapshot.getKey().equals("btn_l")){
                    TextView text = (TextView) findViewById(R.id.lbtnText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Pressed");
                    else
                        text.setText("Not Press");
                }else if(dataSnapshot.getKey().equals("btn_r")){
                    TextView text = (TextView) findViewById(R.id.rbtnText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Pressed");
                    else
                        text.setText("Not Press");
                }else if(dataSnapshot.getKey().equals("infrared")){
                    TextView text = (TextView) findViewById(R.id.infraredText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Active");
                    else
                        text.setText("Not Active");
                }else if(dataSnapshot.getKey().equals("light")){
                    TextView text = (TextView) findViewById(R.id.lightText);
                    text.setText(dataSnapshot.getValue(int.class).toString());
                }else if(dataSnapshot.getKey().equals("sound")){
                    TextView text = (TextView) findViewById(R.id.soundText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Active");
                    else
                        text.setText("Not Active");
                }else if(dataSnapshot.getKey().equals("temp")){
                    TextView text = (TextView) findViewById(R.id.tempText);
                    text.setText(dataSnapshot.getValue(int.class).toString());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
