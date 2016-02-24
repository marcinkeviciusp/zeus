package jalp.zeus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

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

public class GraphAccelActivity extends AppCompatActivity {

    Firebase mRef;
    protected String graphType = "accel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Notifier notificationService = new Notifier(this);
        setContentView(R.layout.activity_graph_accel);

        final ArrayList<String> spots = new ArrayList<>();
        final ArrayList<DataPoint>[] points = new ArrayList[10];
        final GraphView graph = (GraphView) findViewById(R.id.graphAccel);

        ZeusMainActivity.ROOT.child("readings").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    spots.add(snapshot.getKey());
                }

                System.out.println("Spot size: " + spots.size());

                for (int i = 0; i < spots.size(); i++) {
                    final int finalI = i;
                    ZeusMainActivity.ROOT.child("readings").child(spots.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(graphType)) {
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

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
