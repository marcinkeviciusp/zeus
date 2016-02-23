package jalp.zeus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class DataTrendsActivity extends AppCompatActivity {

    Button mBtnLight;
    Button mBtnTemp;
    Button mBtnAccel;
    Button mBtnBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_trends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBtnLight = (Button) findViewById(R.id.btnLight);
        mBtnTemp = (Button) findViewById(R.id.btnTemp);
        mBtnAccel = (Button) findViewById(R.id.btnAccel);
        mBtnBattery = (Button) findViewById(R.id.btnBattery);

        mBtnLight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DataTrendsActivity.this, GraphLightActivity.class));
            }
        });
        mBtnTemp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DataTrendsActivity.this, GraphTempActivity.class));
            }
        });
        mBtnAccel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DataTrendsActivity.this, GraphAccelActivity.class));
            }
        });
        mBtnBattery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DataTrendsActivity.this, GraphBatteryActivity.class));
            }
        });
    }

}
