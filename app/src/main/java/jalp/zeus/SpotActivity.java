package jalp.zeus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class SpotActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.SpotName);
        textView.setText(RoomActivity.spot);
        setSupportActionBar(toolbar);

        ((TextView) findViewById(R.id.text_view_spot_username)).setText(ZeusMainActivity.username);

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
                        text.setText("Off");
                }else if(dataSnapshot.getKey().equals("compass")){
                    TextView text = (TextView) findViewById(R.id.compassText);
                    text.setText("Compass: " + dataSnapshot.getValue(double.class).toString());
                }else if(dataSnapshot.getKey().equals("accel")){
                    TextView text = (TextView) findViewById(R.id.accelText);
                    text.setText("Acceleration: " + dataSnapshot.getValue(double.class).toString());
                }else if(dataSnapshot.getKey().equals("btn_l")){
                    TextView text = (TextView) findViewById(R.id.lbtnText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Left Pressed");
                    else
                        text.setText("Left Not Pressed");
                }else if(dataSnapshot.getKey().equals("btn_r")){
                    TextView text = (TextView) findViewById(R.id.rbtnText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Right Pressed");
                    else
                        text.setText("Right Not Pressed");
                }else if(dataSnapshot.getKey().equals("infrared")){
                    TextView text = (TextView) findViewById(R.id.infraredText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Infrared Active");
                    else
                        text.setText("Infrared Not Active");
                }else if(dataSnapshot.getKey().equals("light")){
                    TextView text = (TextView) findViewById(R.id.lightText);
                    text.setText("Light: " + dataSnapshot.getValue(double.class).toString());
                }else if(dataSnapshot.getKey().equals("sound")){
                    TextView text = (TextView) findViewById(R.id.soundText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Sound Active");
                    else
                        text.setText("Sound Not Active");
                }else if(dataSnapshot.getKey().equals("temp")){
                    TextView text = (TextView) findViewById(R.id.tempText);
                    text.setText("Temperature: " + dataSnapshot.getValue(double.class).toString());
                }else if(dataSnapshot.getKey().equals("liveData")){
                    removeUnusedTypes(dataSnapshot.getValue(String.class));
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
                        text.setText("Off");
                }else if(dataSnapshot.getKey().equals("compass")){
                    TextView text = (TextView) findViewById(R.id.compassText);
                    text.setText("Compass: " + dataSnapshot.getValue(int.class).toString());
                }else if(dataSnapshot.getKey().equals("accel")){
                    TextView text = (TextView) findViewById(R.id.accelText);
                    text.setText("Acceleration: " + dataSnapshot.getValue(int.class).toString());
                }else if(dataSnapshot.getKey().equals("btn_l")){
                    TextView text = (TextView) findViewById(R.id.lbtnText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Left Pressed");
                    else
                        text.setText("Left Not Pressed");
                }else if(dataSnapshot.getKey().equals("btn_r")){
                    TextView text = (TextView) findViewById(R.id.rbtnText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Left Pressed");
                    else
                        text.setText("Left Not Pressed");
                }else if(dataSnapshot.getKey().equals("infrared")){
                    TextView text = (TextView) findViewById(R.id.infraredText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Infrared Active");
                    else
                        text.setText("Infrared Not Active");
                }else if(dataSnapshot.getKey().equals("light")){
                    TextView text = (TextView) findViewById(R.id.lightText);
                    text.setText("Light: " + dataSnapshot.getValue(int.class).toString());
                }else if(dataSnapshot.getKey().equals("sound")){
                    TextView text = (TextView) findViewById(R.id.soundText);
                    if(dataSnapshot.getValue(boolean.class))
                        text.setText("Sound Active");
                    else
                        text.setText("Sound Not Active");
                }else if(dataSnapshot.getKey().equals("temp")){
                    TextView text = (TextView) findViewById(R.id.tempText);
                    text.setText("Temperature: " + dataSnapshot.getValue(int.class).toString());
                }else if(dataSnapshot.getKey().equals("liveData")){
                    removeUnusedTypes(dataSnapshot.getValue(String.class));
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



        // side menu
        ((TextView) findViewById(R.id.text_view_spot_username)).setText(ZeusMainActivity.username);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_spot);
        drawerList = (ListView) findViewById(R.id.left_drawer_spot);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ZeusMainActivity.zeusMenuSections));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZeusMainActivity.executeMenu(id, SpotActivity.this);
            }
        });
        // side menu end
    }

    private  void removeUnusedTypes(String dataString){
        String allTypes = "ctlabrseiwxyz";
        char[] allTypesArray = allTypes.toCharArray();
        for(int i=0;i<dataString.length();i++){
            for(int j=0;j<allTypesArray.length;j++) {
                if(dataString.charAt(i) == allTypesArray[j]){
                    addType(dataString.charAt(i));
                    allTypesArray[j] = 'u';
                }
            }
        }
        for(int i=0;i<allTypesArray.length;i++){
            removeType(allTypesArray[i]);
        }
    }

    private void addType(char character){
        if(character == 'c'){
            TextView text = (TextView) findViewById(R.id.compassText);
            text.setVisibility(View.VISIBLE);
        }else if(character == 't'){
            TextView text = (TextView) findViewById(R.id.tempText);
            text.setVisibility(View.VISIBLE);
        }else if(character == 'l'){
            TextView text = (TextView) findViewById(R.id.lightText);
            text.setVisibility(View.VISIBLE);
        }else if(character == 'a'){
            TextView text = (TextView) findViewById(R.id.accelText);
            text.setVisibility(View.VISIBLE);
        }else if(character == 'b'){
            TextView text = (TextView) findViewById(R.id.lbtnText);
            text.setVisibility(View.VISIBLE);
        }else if(character == 'r'){
            TextView text = (TextView) findViewById(R.id.rbtnText);
            text.setVisibility(View.VISIBLE);
        }else if(character == 's'){
            TextView text = (TextView) findViewById(R.id.soundText);
            text.setVisibility(View.VISIBLE);
        }else if(character == 'i'){
            TextView text = (TextView) findViewById(R.id.infraredText);
            text.setVisibility(View.VISIBLE);
        }else if(character == 'e'){
            ProgressBar mProgress = (ProgressBar) findViewById(R.id.batteryProgress);
            mProgress.setVisibility(View.VISIBLE);
            TextView text = (TextView) findViewById(R.id.infraredText);
            text.setVisibility(View.VISIBLE);
        }
    }

    private void removeType(char character){
        if(character == 'c'){
            System.out.println("In Compass");
            TextView text = (TextView) findViewById(R.id.compassText);
            text.setVisibility(View.GONE);
        }else if(character == 't'){
            TextView text = (TextView) findViewById(R.id.tempText);
            text.setVisibility(View.GONE);
        }else if(character == 'l'){
            TextView text = (TextView) findViewById(R.id.lightText);
            text.setVisibility(View.GONE);
        }else if(character == 'a'){
            TextView text = (TextView) findViewById(R.id.accelText);
            text.setVisibility(View.GONE);
        }else if(character == 'b'){
            TextView text = (TextView) findViewById(R.id.lbtnText);
            text.setVisibility(View.GONE);
        }else if(character == 'r'){
            TextView text = (TextView) findViewById(R.id.rbtnText);
            text.setVisibility(View.GONE);
        }else if(character == 's'){
            TextView text = (TextView) findViewById(R.id.soundText);
            text.setVisibility(View.GONE);
        }else if(character == 'i'){
            TextView text = (TextView) findViewById(R.id.infraredText);
            text.setVisibility(View.GONE);
        }else if(character == 'e'){
            ProgressBar mProgress = (ProgressBar) findViewById(R.id.batteryProgress);
            mProgress.setVisibility(View.GONE);
            TextView text = (TextView) findViewById(R.id.infraredText);
            text.setVisibility(View.GONE);
        }
    }

    private boolean typeUsed(char character){
        String allTypes = "ctlabrseiwxyz";
        boolean unused = true;
        for(int i=0;i<allTypes.length();i++){
            if(character == allTypes.charAt(i)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_test_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
        return true;
    }
}
