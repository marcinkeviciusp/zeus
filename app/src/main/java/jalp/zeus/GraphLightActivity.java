package jalp.zeus;

import android.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class GraphLightActivity extends AppCompatActivity {

    Firebase mRef;
    protected String graphType = "light";
    protected int seriesNumber = 0;
    Firebase readings = ZeusMainActivity.ROOT.child("readings");
    LineChart chart;
    final ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_light);
        chart = (LineChart) findViewById(R.id.graphLight);

        final ArrayList<String> spots = new ArrayList<>();
        settings(chart);

        readings.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //For each spot
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Spot name = snapshot.getKey() -> 8025, 7ABD
                    spots.add(snapshot.getKey());

                }

                System.out.println("Spot Size: " + spots.size());

                for (int i = 0; i < spots.size(); i++) {
                    final String spotName = spots.get(i);
                    final int finalI = i;

                    readings.child(spotName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild("light")) {
                                final String spotName = snapshot.getKey();
                                plot(spotName);
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
    }

    //This method configure chart settings
    public void settings(LineChart chart) {

        //Interaction with the Chart : https://github.com/PhilJay/MPAndroidChart/wiki/Interaction-with-the-Chart
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        //The Axis : https://github.com/PhilJay/MPAndroidChart/wiki/The-Axis
        chart.setEnabled(true);
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelsToSkip(3);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
    }

    public void plot(final String spotName) {
        final ArrayList<Entry> entries = new ArrayList<Entry>();
        final ArrayList<String> labels = new ArrayList<String>();

        readings.child(spotName).child("light").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                System.out.println("SPOTNAME: " + spotName);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                            System.out.println("Timestamp " + snapshot.getKey());
//                                            System.out.println("Value " + snapshot.getValue());
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
}
