package jalp.zeus;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
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
import android.widget.TableRow;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class DashBoardActivity extends AppCompatActivity {

    // side menu
    String[] zeusSections = new String[]{
            "Dashboard",
            "Control Panel",
            "Data Trends"
    };
    DrawerLayout drawerLayout;
    ListView drawerList;
    static Notifier notificationService;
    // side menu end

    public static String room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Notifier.created){
            System.out.println("##############################################################");
            System.out.println("##############################################################");
            System.out.println("##############################################################");
            System.out.println("<<<<<<<<<<<<<<<<<<<<<< CREATING NOTIFICATION SERVICE");
            System.out.println("##############################################################");
            System.out.println("##############################################################");
            System.out.println("##############################################################");
            notificationService = new Notifier(this);
        }


        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        System.out.println("##############################################################");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<" + ZeusMainActivity.ROOT.child("rooms").toString());

        final Context context = this;

        final RelativeLayout screen = (RelativeLayout) this.findViewById(R.id.mainScreen);
        final LinearLayout lL = new LinearLayout(context);
        lL.setOrientation(LinearLayout.VERTICAL);
        lL.setMinimumHeight(screen.getHeight());
        lL.setMinimumWidth(screen.getWidth());
        screen.addView(lL);

        ZeusMainActivity.ROOT.child("rooms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot snapshot, String previousChildKey) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + snapshot.getKey());
                CardView card = new CardView(context);
                card.setUseCompatPadding(true);
                card.setCardElevation(3);
                card.setContentPadding(10, 10, 10, 10);
                card.setMinimumWidth(screen.getWidth());
                card.setMinimumHeight(150);
                card.setClickable(true);
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().equals("name")) {
                                room = data.getValue(String.class);
                            }
                        }
                        startActivity(new Intent(DashBoardActivity.this, RoomActivity.class));
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                });
                lL.addView(card);


                //RelativeLayout container = new RelativeLayout(context);
                TableLayout container = new TableLayout(context);
                card.addView(container);

                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("name")) {
                        TextView roomDescription = new TextView(context);
                        roomDescription.setText(data.getValue(String.class));
                        roomDescription.setTextSize(30);


                        container.addView(roomDescription);


                    } else if (data.getKey().equals("description")) {
                        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>> Description: " + data.getValue(String.class));

                        TextView roomName = new TextView(context);
                        roomName.setText(data.getValue(String.class));
                        roomName.setTextSize(15);

                        container.addView(roomName);

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

        // side menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_dashboard);
        drawerList = (ListView) findViewById(R.id.left_drawer_dashboard);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, zeusSections));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZeusMainActivity.executeMenu(id, DashBoardActivity.this);
            }
        });
        // side menu end
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