package jalp.zeus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ControlPanelActivity extends AppCompatActivity {

    RadioGroup radioGroupTemperatures;
    RadioGroup radioGroupBases;
    Button buttonBoilOrStop;

    SeekBar seekBarEasybulbBrightness;
    SeekBar seekBarEasybulbColour;
    Button buttonEasybulbOn;
    Button buttonEasybulbOff;
    Button buttonEasybulbWhite;
    RadioGroup radioGroupEasybulbGroup;

    String[] zeusSections = new String[]{
            "Dashboard",
            "Control Panel",
            "Data Trends"
    };
    DrawerLayout drawerLayout;
    ListView drawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        Notifier notificationService = new Notifier(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        radioGroupTemperatures = (RadioGroup) findViewById(R.id.radioGroupTemperatures);
        radioGroupBases = (RadioGroup) findViewById(R.id.radioGroupBases);
        buttonBoilOrStop = (Button) findViewById(R.id.buttonBoilStopKettle);
        final boolean kettleBoiling[] = {false};

        ZeusMainActivity.ROOT.child("bases").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot baseEntry : dataSnapshot.getChildren()) {
                    RadioButton btn = new RadioButton(ControlPanelActivity.this);
                    radioGroupBases.addView(btn);
                    btn.setText(baseEntry.getKey());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                buttonBoilOrStop.setText("Error: Firebase Inaccesible");
            }
        });

        ZeusMainActivity.ROOT.child("kettleFeedback").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    if (value.endsWith("Set") || value.equals("Turned_On") || value.equals("Warm_Selected") || value.equals("Warm_5_Min") || value.equals("Warm_10_Min") || value.equals("Warm_20_Min")) {
                        kettleBoiling[0] = true;
                        buttonBoilOrStop.setText("STOP");
                    } else if (value.equals("Turned_Off") || value.equals("Problem") || value.equals("Kettle_Removed_While_On") || value.equals("Reached_Temp") || value.equals("Warm_Ended")) {
                        kettleBoiling[0] = false;
                        buttonBoilOrStop.setText("BOIL");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                buttonBoilOrStop.setText("Error: Firebase Inaccesible");
            }
        });

        buttonBoilOrStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String baseName = getSelectedBase();
                if(baseName != null) {
                    Button selectedTemp = (RadioButton) findViewById(radioGroupTemperatures.getCheckedRadioButtonId());
                    String temp = selectedTemp.getText().toString().split(" ")[0]; // removes the degrees C
                    ZeusMainActivity.ROOT.child("bases").child(baseName).setValue(baseName + " KETTLE " + (kettleBoiling[0] ? "OFF" : ("BOIL " + temp)));
                }
            }
        });


        // START EASYBULB HERE

        seekBarEasybulbBrightness = (SeekBar) findViewById(R.id.seekBarEasybulbBrightness);
        seekBarEasybulbColour = (SeekBar) findViewById(R.id.seekBarEasybulbColour);
        buttonEasybulbOn = (Button) findViewById(R.id.buttonEasybulbOn);
        buttonEasybulbOff = (Button) findViewById(R.id.buttonEasybulbOff);
        buttonEasybulbWhite = (Button) findViewById(R.id.buttonEasybulbWhite);
        radioGroupEasybulbGroup = (RadioGroup) findViewById(R.id.radioGroupEasybulbGroup);

        seekBarEasybulbColour.getProgressDrawable().setAlpha(0);
        seekBarEasybulbBrightness.getProgressDrawable().setAlpha(0);

        buttonEasybulbOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).setValue(baseName + " EASYBULB ON " + getSelectedEasybulbGroupCapitalised());
            }
        });

        buttonEasybulbOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).setValue(baseName + " EASYBULB OFF " + getSelectedEasybulbGroupCapitalised());
            }
        });

        buttonEasybulbWhite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).setValue(baseName + " EASYBULB WHITE " + getSelectedEasybulbGroupCapitalised());
            }
        });

        seekBarEasybulbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).setValue(baseName + " EASYBULB BRIGHTNESS " + getSelectedEasybulbGroupCapitalised() + " " + (progress - 128));
            }
        });

        seekBarEasybulbColour.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).setValue(baseName + " EASYBULB COLOUR " + getSelectedEasybulbGroupCapitalised() + " " + (progress - 128));
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_control_panel);
        drawerList = (ListView) findViewById(R.id.left_drawer_control_panel);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, zeusSections));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    case 0:
                        startActivity(new Intent(ControlPanelActivity.this, DashBoardActivity.class));
                        break;
                    case 1:
                        //startActivity(new Intent(ControlPanelActivity.this, ControlPanelActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(ControlPanelActivity.this, DataTrendsActivity.class));
                        break;
                }
            }
        });
    }

    private String getSelectedBase()
    {
        Button selectedBase = (RadioButton) findViewById(radioGroupBases.getCheckedRadioButtonId());
        String str =  selectedBase.getText().toString();
        return str.equals("N/A") ? null : str;
    }

    private String getSelectedEasybulbGroupCapitalised()
    {
        RadioButton selectedBase = (RadioButton) findViewById(radioGroupEasybulbGroup.getCheckedRadioButtonId());
        return selectedBase.getText().toString().toUpperCase();
    }
}


/*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/