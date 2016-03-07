package jalp.zeus;

import android.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TableLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GraphBatteryActivity extends AppCompatActivity {

    protected Firebase readings = ZeusMainActivity.ROOT.child("readings");

    protected String graphType = "battery";
    protected String graphTypeUpper = graphType.substring(0, 1).toUpperCase() + graphType.substring(1);
    protected LineChart chart;

    DrawerLayout drawerLayout;
    ListView drawerList;

    final ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_battery);
        chart = (LineChart) findViewById(R.id.graphBattery);

        ((TextView) findViewById(R.id.text_view_graph_battery_username)).setText(ZeusMainActivity.username);

        settings(chart);

        readings.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //For each spot
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //Spot name (8025, 7ABD, etc.)
                    String spotName = snapshot.getKey();

                    readings.child(spotName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            //Check if it has a child "light"
                            if (snapshot.hasChild(graphType)) {
                                plot(snapshot.getKey());//SpotName{8025, 7ABD, etc.)
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // side menu
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ((TextView) findViewById(R.id.text_view_graph_battery_username)).setText(ZeusMainActivity.username);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_graph_battery);
        drawerList = (ListView) findViewById(R.id.left_drawer_graph_battery);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ZeusMainActivity.zeusMenuSections));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZeusMainActivity.executeMenu(id, GraphBatteryActivity.this);
            }
        });
        // side menu end
    }

    //This method configure chart settings
    public void settings(LineChart chart) {

        //Interaction with the Chart : https://github.com/PhilJay/MPAndroidChart/wiki/Interaction-with-the-Chart
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setEnabled(true);
        chart.animateXY(2000,2000);
        chart.setDescription(graphTypeUpper + " Trend");

        //The Axis : https://github.com/PhilJay/MPAndroidChart/wiki/The-Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelsToSkip(3);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
    }

    public void plot(final String spotName) {
        final ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();

        readings.child(spotName).child(graphType).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                System.out.println("SPOTNAME: " + spotName);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Timestamp stamp = new Timestamp(Long.parseLong(snapshot.getKey()));
                    Date date = stamp;
                    SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");

                    float value = snapshot.getValue(Float.class);
                    entries.add(new Entry(value, i));
                    labels.add("" + ft.format(date));
                    i++;
                }

                LineDataSet dataSet = new LineDataSet(entries, spotName);
                dataSet.setColor(ColorTemplate.JOYFUL_COLORS[dataSets.size()]);
                dataSets.add(dataSet);

                LineData data = new LineData(labels, dataSets);
                chart.setData(data);
                chart.invalidate();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    // Menu Icon Start
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
    // Menu Icon End
}
