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
        Notifier notificationService = new Notifier(this);
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
                    }
                }
            }

            public void onChildRemoved(DataSnapshot snapshot) {

            }

            public void onChildChanged(DataSnapshot snapshot, String someString) {

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

}
