package jalp.zeus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

public class RoomActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ListView drawerList;

    public static String spot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        //Notifier notificationService = new Notifier(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.RoomName);
        textView.setText(DashBoardActivity.room);
        setSupportActionBar(toolbar);
        final TextView noOfSpots = (TextView) findViewById(R.id.amountOfSpots);

        final Context context = this;

        final RelativeLayout screen = (RelativeLayout) this.findViewById(R.id.mainScreen);

        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        /*TextView title = new TextView(context);
        title.setText("Rooms");
        title.setTextSize(30);
        screen.addView(title);*/

       // final ScrollView screen = (ScrollView) this.findViewById(R.id.scrollView);
        final LinearLayout lL = new LinearLayout(context);
        lL.setOrientation(LinearLayout.VERTICAL);
  //      lL.setMinimumHeight(screen.getHeight());
        lL.setMinimumWidth(screen.getWidth());
        lL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        lL.setPadding(0,200,0,0);

        screen.addView(lL, params);
        System.out.println("Liam's stuff>>>>> In Room Activity " + DashBoardActivity.room);

        Firebase ref = ZeusMainActivity.ROOT.child("spots");
        Query queryRef =  ref.orderByChild("room").equalTo(DashBoardActivity.room);

        queryRef.addChildEventListener(new ChildEventListener(){

            public void onChildAdded(final DataSnapshot snapshot, String previousChildKey) {
            


                System.out.println("Liam's stuff>>>>>" + snapshot.getKey());
                CardView card = new CardView(context);
                card.setUseCompatPadding(true);
                card.setCardElevation(3);
                card.setContentPadding(10, 10, 10, 10);
                card.setMinimumWidth(screen.getWidth());
                card.setMinimumHeight(150);
                card.setClickable(false);
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
                        spot = snapshot.getKey();
                        startActivity(new Intent(RoomActivity.this, SpotActivity.class));
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> spot = " + spot);
                    }
                });
                card.setId(nameToID(snapshot.getKey()));
                lL.addView(card);



                //RelativeLayout container = new RelativeLayout(context);
                TableLayout container = new TableLayout(context);
                container.setId(nameToID("container"));
                card.addView(container);

                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.getKey().equals("name")){
                        TextView spotName = new TextView(context);
                        spotName.setText(data.getValue(String.class));
                        spotName.setTextSize(30);
                        spotName.setId(nameToID(data.getKey()));
                        container.addView(spotName);

                    }else if(data.getKey().equals("alive")){
                        TextView spotStatus = new TextView(context);
                        if(data.getValue(String.class).equals("true")) {
                            spotStatus.setText("Alive");
                        }else if(data.getValue(String.class).equals("false")){
                            spotStatus.setText("Off");
                        }
                        spotStatus.setTextSize(15);
                        spotStatus.setId(nameToID(data.getKey()));
                        container.addView(spotStatus);

                        View divider = new View(context);
                        divider.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                3
                        ));

                        divider.setBackgroundColor(Color.LTGRAY);
                        container.addView(divider);
                    }else if(data.getKey().equals("battery")){
                        TextView spotBattery = new TextView(context);
                        spotBattery.setText("Battery Level: " + data.getValue(String.class));
                        if(data.getValue(Double.class)>25) {
                            spotBattery.setTextColor(Color.parseColor("#006400"));
                        }else if(data.getValue(Double.class)>10){
                            spotBattery.setTextColor(Color.YELLOW);
                        }else{
                            spotBattery.setTextColor(Color.RED);
                        }
                        spotBattery.setTextSize(15);
                        spotBattery.setId(nameToID(data.getKey()));
                        container.addView(spotBattery);

                        View divider = new View(context);
                        divider.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                3
                        ));

                        divider.setBackgroundColor(Color.LTGRAY);
                        container.addView(divider);
                    }else if(data.getKey().equals("liveData")){
                        TextView spotType = new TextView(context);
                        spotType.setText(typeReader(data.getValue(String.class)));
                        spotType.setTextSize(15);
                        spotType.setId(nameToID(data.getKey()));
                        container.addView(spotType);

                        View divider = new View(context);
                        divider.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                3
                        ));

                        divider.setBackgroundColor(Color.LTGRAY);
                        container.addView(divider);
                    }
                }

                String noSpotText = (String)noOfSpots.getText();
                int numberOfSpots = Integer.parseInt(noSpotText.replaceAll("\\D", ""));
                numberOfSpots++;
                noSpotText = "Number of Spots = " + numberOfSpots;
                noOfSpots.setText(noSpotText);
            }

            public void onChildRemoved(DataSnapshot snapshot) {
                CardView card = (CardView)lL.findViewById(nameToID(snapshot.getKey()));
                lL.removeView(card);

                String noSpotText = (String)noOfSpots.getText();
                int numberOfSpots = Integer.parseInt(noSpotText.replaceAll("\\D", ""));
                numberOfSpots--;
                noSpotText = "Number of Spots = " + numberOfSpots;
                noOfSpots.setText(noSpotText);
            }

            public void onChildChanged(DataSnapshot snapshot, String someString) {
                int id = nameToID(snapshot.getKey());
                CardView card = (CardView)lL.findViewById(id);

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("room")) {
                        if(!data.getValue(String.class).equals(DashBoardActivity.room)){
                            lL.removeView(card);

                            String noSpotText = (String)noOfSpots.getText();
                            int numberOfSpots = Integer.parseInt(noSpotText.replaceAll("\\D", ""));
                            numberOfSpots--;
                            noSpotText = "Number of Spots = " + numberOfSpots;
                            noOfSpots.setText(noSpotText);
                        }
                    }else if (data.getKey().equals("name")){
                        TableLayout container = (TableLayout)card.findViewById(nameToID("container"));
                        TextView text = (TextView)container.findViewById(nameToID(data.getKey()));
                        text.setText(data.getValue(String.class));
                    }else if (data.getKey().equals("alive")){
                        TableLayout container = (TableLayout)card.findViewById(nameToID("container"));
                        TextView text = (TextView)container.findViewById(nameToID(data.getKey()));
                        if(data.getValue(String.class).equals("true")) {
                            text.setText("Alive");
                        }else if(data.getValue(String.class).equals("false")){
                            text.setText("Off");
                        }
                    }else if (data.getKey().equals("battery")){
                        TableLayout container = (TableLayout)card.findViewById(nameToID("container"));
                        TextView text = (TextView)container.findViewById(nameToID(data.getKey()));
                        text.setText("Battery Level: " + data.getValue(String.class));
                        if(data.getValue(Double.class)>25) {
                            text.setTextColor(Color.parseColor("#006400"));
                        }else if(data.getValue(Double.class)>10){
                            text.setTextColor(Color.YELLOW);
                        }else{
                            text.setTextColor(Color.RED);
                        }
                    }else if (data.getKey().equals("liveData")){
                        TableLayout container = (TableLayout)card.findViewById(nameToID("container"));
                        TextView text = (TextView)container.findViewById(nameToID(data.getKey()));
                        text.setText(typeReader(data.getValue(String.class)));
                    }
                }
            }

            public void onChildMoved(DataSnapshot snapshot, String someString) {

            }

            public void onCancelled(FirebaseError err) {

            }


        });


        System.out.println("###############################");
        System.out.println("Selected room ID is: " + DashBoardActivity.room);
        System.out.println("###############################");

        // side menu
        ((TextView) findViewById(R.id.text_view_room_username)).setText(ZeusMainActivity.username);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_room);
        drawerList = (ListView) findViewById(R.id.left_drawer_room);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ZeusMainActivity.zeusMenuSections));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZeusMainActivity.executeMenu(id, RoomActivity.this);
            }
        });
        // side menu end
    }

    private static String typeReader(String dataString){
        String returner = "Types: ";
        for(int i=0;i<dataString.length();i++){
            if(i==(dataString.length()-1)){
                returner += typeSwitch(dataString.charAt(i))+".";
            }else {
                returner += typeSwitch(dataString.charAt(i))+", ";
            }
        }
        return returner;
    }

    private static String typeSwitch(char character){
        switch(character){
            case 'c':
                return "Compass";
            case 't':
                return "Temperature";
            case 'l':
                return "Light";
            case 'a':
                return "Acceleration";
            case 'b':
                return "Left Button";
            case 'r':
                return "Right Button";
            case 's':
                return "Sound";
            case 'e':
                return "Battery";
            case 'i':
                return "Infrared";
            case 'w':
                return "A2";
            case 'x':
                return "A3";
            case 'y':
                return "D2";
            case 'z':
                return "D3";
        }
        return null;
    }

    private static int nameToID(String name){
        String idString = "";
        String str = name.toUpperCase();
        if(name.length() > 4){
            for(int i = 0; i<4; i++){
                idString += (int) str.charAt(i);
            }
        }else {
            for (int i = 0; i < name.length(); i++) {
                idString += (int) str.charAt(i);
            }
        }

        return Integer.parseInt(idString,10);
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
