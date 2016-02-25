package jalp.zeus;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GraphBatteryActivity extends AppCompatActivity {

    Firebase mRef;
    protected String graphType = "battery";
    protected int seriesNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Notifier notificationService = new Notifier(this);
        setContentView(R.layout.activity_graph_battery);

        final ArrayList<String> spots = new ArrayList<>();
        final ArrayList<DataPoint>[] points = new ArrayList[10];
        final GraphView graph = (GraphView) findViewById(R.id.graphBattery);
        final TextView spotTitle = (TextView) findViewById(R.id.textViewSpot);
        final TextView spotTitle2 = (TextView) findViewById(R.id.textViewSpot2);
        final TextView spotTitle3 = (TextView) findViewById(R.id.textViewSpot3);
        final TextView spotTitle4 = (TextView) findViewById(R.id.textViewSpot4);
        final TextView spotTitle5 = (TextView) findViewById(R.id.textViewSpot5);

        ZeusMainActivity.ROOT.child("readings").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("name " + snapshot.getKey());
                    spots.add(snapshot.getKey());
                }

                System.out.println("Spot size: " + spots.size());

                for (int i = 0; i < spots.size(); i++) {
                    final int finalI = i;
                    ZeusMainActivity.ROOT.child("readings").child(spots.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(graphType)) {
                                if (finalI == 0) {
                                    spotTitle.setText(spots.get(finalI));
                                    spotTitle.setTextColor(Color.RED);
                                } else if (finalI == 1) {
                                    spotTitle2.setText(spots.get(finalI));
                                    spotTitle2.setTextColor(Color.BLUE);
                                } else if (finalI == 2) {
                                    spotTitle3.setText(spots.get(finalI));
                                    spotTitle3.setTextColor(Color.GREEN);
                                } else if (finalI == 3) {
                                    spotTitle4.setText(spots.get(finalI));
                                    spotTitle4.setTextColor(Color.YELLOW);
                                } else if (finalI == 4) {
                                    spotTitle5.setText(spots.get(finalI));
                                    spotTitle5.setTextColor(Color.BLACK);
                                }
                                points[finalI] = new ArrayList<>();
                                plot(points[finalI], graph, spots.get(finalI));
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

    protected void plot(final ArrayList<DataPoint> points, final GraphView graph, String spotName) {
        ZeusMainActivity.ROOT.child("readings").child(spotName).child(graphType).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot entry : dataSnapshot.getChildren()) {
                    Timestamp stamp = new Timestamp(Long.parseLong(entry.getKey()));
                    Date date = stamp;
                    points.add(new DataPoint(date, entry.getValue(Double.class)));
                }

                DataPoint[] dbPoint = points.toArray(new DataPoint[points.size()]);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dbPoint);
                graph.addSeries(series);

                if (seriesNumber == 0) {
                    series.setColor(Color.BLUE);
                } else if (seriesNumber == 1) {
                    series.setColor(Color.RED);
                } else if (seriesNumber == 2) {
                    series.setColor(Color.GREEN);
                } else {
                    series.setColor(Color.YELLOW);
                }
                seriesNumber++;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
