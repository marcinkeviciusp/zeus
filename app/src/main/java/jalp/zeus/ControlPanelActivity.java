package jalp.zeus;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ControlPanelActivity extends AppCompatActivity {

    RadioGroup radioGroupTemperatures;
    RadioGroup radioGroupBases;
    Button buttonBoilOrStop;
/**/
    SeekBar seekBarEasybulbBrightness;
    SeekBar seekBarEasybulbColour;
    Button buttonEasybulbOn;
    Button buttonEasybulbOff;
    Button buttonEasybulbWhite;
    RadioGroup radioGroupEasybulbGroup;

    Button tooltipButton;
    TextView tooltipText;
    boolean toolTipOut = false;
    DrawerLayout drawerLayout;
    ListView drawerList;

    // garying out by Pov
    ImageView brightnessGradient;
    ImageView colourGradient;

    TextView textEasybulb;
    TextView textColour;
    TextView textBrightness;
    TextView textGroup;
    TextView textKettle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((TextView) findViewById(R.id.text_view_control_panel_username)).setText(ZeusMainActivity.username);
        tooltipText = (TextView) findViewById(R.id.text_contro_panel_tooltip_info);
        tooltipButton = (Button) findViewById(R.id.button_tooltip_base);
        radioGroupTemperatures = (RadioGroup) findViewById(R.id.radioGroupTemperatures);
        radioGroupBases = (RadioGroup) findViewById(R.id.radioGroupBases);
        buttonBoilOrStop = (Button) findViewById(R.id.buttonBoilStopKettle);
        final boolean kettleBoiling[] = {false};

        brightnessGradient = (ImageView) findViewById(R.id.imageViewBrightnessGradient);
        colourGradient = (ImageView) findViewById(R.id.imageViewColourGradient);

        textEasybulb = (TextView) findViewById(R.id.textEasybulbHeader);
        textColour = (TextView) findViewById(R.id.textViewEasybulbColour);
        textBrightness = (TextView) findViewById(R.id.textViewEasybulbBrightness);
        textGroup = (TextView) findViewById(R.id.textFieldEasybulbGroup);
        textKettle = (TextView) findViewById(R.id.textKettleTitle);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_control_panel);
        drawerList = (ListView) findViewById(R.id.left_drawer_control_panel);
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, ZeusMainActivity.zeusMenuSections));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZeusMainActivity.executeMenu(id, ControlPanelActivity.this);
            }
        });

        for(View v : radioGroupTemperatures.getTouchables())
        { v.setEnabled(false); }

        tooltipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tooltipText.setVisibility(toolTipOut ? View.GONE : View.VISIBLE);
                toolTipOut = !toolTipOut;
            }
        });

        ZeusMainActivity.ROOT.child("bases").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot baseEntry : dataSnapshot.getChildren()) {
                    RadioButton btn = new RadioButton(ControlPanelActivity.this);
                    radioGroupBases.addView(btn);
                    btn.setText(baseEntry.getKey());

                    ZeusMainActivity.ROOT.child("bases").child(baseEntry.getKey()).child("kettle").addValueEventListener(new ValueEventListener() {
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
                    ZeusMainActivity.ROOT.child("bases").child(baseName).child("script").setValue(baseName + " KETTLE " + (kettleBoiling[0] ? "OFF" : ("BOIL " + temp)));
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

        for(View v : radioGroupEasybulbGroup.getTouchables())
            v.setEnabled(false);
        seekBarEasybulbColour.getProgressDrawable().setAlpha(0);
        seekBarEasybulbBrightness.getProgressDrawable().setAlpha(0);

        seekBarEasybulbBrightness.setEnabled(false);
        seekBarEasybulbColour.setEnabled(false);

        radioGroupBases.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checked) {
            if(checked == R.id.radioButtonBaseNA)
            {
                seekBarEasybulbBrightness.setEnabled(false);
                seekBarEasybulbColour.setEnabled(false);

                brightnessGradient.setImageResource(R.drawable.easybulbgray);
                colourGradient.setImageResource(R.drawable.easybulbgray);

                int gray = Color.parseColor("#bdbdbd");
                textEasybulb.setTextColor(gray);
                textColour.setTextColor(gray);
                textBrightness.setTextColor(gray);
                textGroup.setTextColor(gray);
                textKettle.setTextColor(gray);

                for(int i = 0; i < radioGroupEasybulbGroup.getChildCount(); i++)
                { ((RadioButton)radioGroupEasybulbGroup.getChildAt(i)).setEnabled(false); }

                for(int i = 0; i < radioGroupTemperatures.getChildCount(); i++)
                { ((RadioButton)radioGroupTemperatures.getChildAt(i)).setEnabled(false); }

                buttonBoilOrStop.setEnabled(false);
                buttonEasybulbOff.setEnabled(false);
                buttonEasybulbOn.setEnabled(false);
                buttonEasybulbWhite.setEnabled(false);

                buttonBoilOrStop.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
                buttonEasybulbOff.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
                buttonEasybulbOn.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
                buttonEasybulbWhite.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));


            }
            else
            {
                seekBarEasybulbBrightness.setEnabled(true);
                seekBarEasybulbColour.setEnabled(true);

                brightnessGradient.setImageResource(R.drawable.easybulbbrightness);
                colourGradient.setImageResource(R.drawable.easybulbgradient);

                int balck = Color.parseColor("#000000");
                textEasybulb.setTextColor(balck);
                textColour.setTextColor(balck);
                textBrightness.setTextColor(balck);
                textGroup.setTextColor(balck);
                textKettle.setTextColor(balck);

                for(int i = 0; i < radioGroupEasybulbGroup.getChildCount(); i++){
                    ((RadioButton)radioGroupEasybulbGroup.getChildAt(i)).setEnabled(true);
                }

                for(int i = 0; i < radioGroupTemperatures.getChildCount(); i++){
                    ((RadioButton)radioGroupTemperatures.getChildAt(i)).setEnabled(true);
                }

                buttonBoilOrStop.setEnabled(true);
                buttonEasybulbOff.setEnabled(true);
                buttonEasybulbOn.setEnabled(true);
                buttonEasybulbWhite.setEnabled(true);

                buttonBoilOrStop.setBackgroundResource(android.R.drawable.btn_default);
                buttonEasybulbOff.setBackgroundResource(android.R.drawable.btn_default);
                buttonEasybulbOn.setBackgroundResource(android.R.drawable.btn_default);
                buttonEasybulbWhite.setBackgroundResource(android.R.drawable.btn_default);
            }
            }

        });

        buttonEasybulbOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).child("script").setValue(baseName + " EASYBULB ON " + getSelectedEasybulbGroupCapitalised());
            }
        });

        buttonEasybulbOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).child("script").setValue(baseName + " EASYBULB OFF " + getSelectedEasybulbGroupCapitalised());
            }
        });

        buttonEasybulbWhite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).child("script").setValue(baseName + " EASYBULB WHITE " + getSelectedEasybulbGroupCapitalised());
            }
        });

        seekBarEasybulbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).child("script").setValue(baseName + " EASYBULB BRIGHTNESS " + getSelectedEasybulbGroupCapitalised() + " " + (progress - 128));
            }
        });

        seekBarEasybulbColour.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String baseName = getSelectedBase();
                if (baseName != null)
                    ZeusMainActivity.ROOT.child("bases").child(baseName).child("script").setValue(baseName + " EASYBULB COLOUR " + getSelectedEasybulbGroupCapitalised() + " " + (progress - 128));
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
