package jalp.zeus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        //Notifier notificationService = new Notifier(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = this;

        final RelativeLayout screen = (RelativeLayout) this.findViewById(R.id.mainScreen);
        final LinearLayout lL = new LinearLayout(context);
        lL.setOrientation(LinearLayout.VERTICAL);
        lL.setMinimumHeight(screen.getHeight());
        lL.setMinimumWidth(screen.getWidth());
        screen.addView(lL);
        System.out.println("Liam's stuff>>>>> In Room Activity " + DashBoardActivity.room);

        Firebase ref = ZeusMainActivity.ROOT.child("spots");
        Query queryRef =  ref.orderByChild("room").equalTo(DashBoardActivity.room);

        queryRef.addChildEventListener(new ChildEventListener(){

            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                System.out.println("Liam's stuff>>>>>" + snapshot.getKey());
                CardView card = new CardView(context);
                card.setUseCompatPadding(true);
                card.setCardElevation(3);
                card.setContentPadding(10, 10, 10, 10);
                card.setMinimumWidth(screen.getWidth());
                card.setMinimumHeight(150);
                card.setClickable(false);
                card.setId(nameToID(snapshot.getKey()));
                lL.addView(card);



                //RelativeLayout container = new RelativeLayout(context);
                TableLayout container = new TableLayout(context);
                card.addView(container);

                for (DataSnapshot data : snapshot.getChildren()) {
                    if(data.getKey().equals("name")){
                        TextView spotName = new TextView(context);
                        spotName.setText(data.getValue(String.class));
                        spotName.setTextSize(30);

                        container.addView(spotName);

                    }else if(data.getKey().equals("alive")){
                        TextView spotStatus = new TextView(context);
                        if(data.getValue(String.class).equals("true")) {
                            spotStatus.setText("Alive");
                        }else if(data.getValue(String.class).equals("false")){
                            spotStatus.setText("Off");
                        }
                        spotStatus.setTextSize(15);

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
                            spotBattery.setTextColor(Color.GREEN);
                        }else if(data.getValue(Double.class)>10){
                            spotBattery.setTextColor(Color.YELLOW);
                        }else{
                            spotBattery.setTextColor(Color.RED);
                        }
                        spotBattery.setTextSize(15);

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
            }

            public void onChildRemoved(DataSnapshot snapshot) {
                CardView card = (CardView)lL.findViewById(nameToID(snapshot.getKey()));
                lL.removeView(card);
            }

            public void onChildChanged(DataSnapshot snapshot, String someString) {
                boolean roomChanged = false;
                int id = nameToID(snapshot.getKey());
                CardView card = (CardView)lL.findViewById(id);

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("room")) {
                        if(!data.getValue(String.class).equals(DashBoardActivity.room)){
                            roomChanged = true;
                        }
                    }
                }

                if(roomChanged){
                    lL.removeView(card);
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

        for(int i=0;i<name.length();i++){
            idString += (int)name.charAt(i);
        }

        return Integer.parseInt(idString,10);
    }
}
