package jalp.zeus;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DataTrendsActivity extends AppCompatActivity {

    Button mBtnLight;
    Button mBtnTemp;
    Button mBtnAccel;
    Button mBtnBattery;

    DrawerLayout drawerLayout;
    ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_trends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((TextView) findViewById(R.id.text_view_data_trends_username)).setText(ZeusMainActivity.username);
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


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_data_trends);
        drawerList = (ListView) findViewById(R.id.left_drawer_data_trends);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ZeusMainActivity.zeusMenuSections));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZeusMainActivity.executeMenu(id, DataTrendsActivity.this);
            }
        });
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
