package jalp.zeus;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ZeusMainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
        Firebase.setAndroidContext(this);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    Button buttonLogIn;
    EditText textFieldEmail;
    EditText textFieldPassword;
    TextView testUserFeedback;
    public static Firebase ROOT = null;

    @Override
    protected void onStart()
    {
        super.onStart();
        setContentView(R.layout.activity_test_main);

        buttonLogIn = (Button) findViewById(R.id.buttonLogIn);
        textFieldEmail = (EditText) findViewById(R.id.textFieldEmail);
        textFieldPassword = (EditText) findViewById(R.id.textFieldPassword);
        testUserFeedback = (TextView) findViewById(R.id.TextFieldOutput);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String email = textFieldEmail.getText().toString();
                final String password = textFieldPassword.getText().toString();

                Firebase userEmail = new Firebase("https://sunsspot.firebaseio.com/").child("usersRef").child(email.replaceAll("[.@]", " ").trim());
                System.out.println(userEmail.toString());

                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final Firebase firebaseUserRef = new Firebase("https://sunsspot.firebaseio.com/").child(snapshot.getValue(String.class));
                            firebaseUserRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                                public void onAuthenticated(AuthData authData) {
                                    testUserFeedback.setText("Login Successful");
                                    ROOT = firebaseUserRef;
                                    //startActivity(new Intent(ZeusMainActivity.this, MainMenuActivity.class));
                                }

                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    testUserFeedback.setText("Wrong Password");
                                }
                            });
                        } else
                            testUserFeedback.setText("Such email is not registered");
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The download failed: " + firebaseError.getMessage());
                    }
                };
                userEmail.addListenerForSingleValueEvent(listener);
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
