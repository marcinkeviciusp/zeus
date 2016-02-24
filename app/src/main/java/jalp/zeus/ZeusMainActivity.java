package jalp.zeus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    }

    ProgressBar spinnerLoggingIn;
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

        spinnerLoggingIn = (ProgressBar) findViewById(R.id.spinnerLoggingIn);
        buttonLogIn = (Button) findViewById(R.id.buttonLogIn);
        textFieldEmail = (EditText) findViewById(R.id.textFieldEmail);
        textFieldPassword = (EditText) findViewById(R.id.textFieldPassword);
        testUserFeedback = (TextView) findViewById(R.id.TextFieldOutput);
        spinnerLoggingIn.setVisibility(View.INVISIBLE);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String email = textFieldEmail.getText().toString();
                final String password = textFieldPassword.getText().toString();

                if(email.length() == 0 || password.length() == 0)
                {
                    testUserFeedback.setText("Please enter both email and password");
                    return;
                }

                spinnerLoggingIn.setVisibility(View.VISIBLE);
                buttonLogIn.setVisibility(View.INVISIBLE);
                Firebase userEmail = new Firebase("https://sunsspot.firebaseio.com/").child("usersRef").child(email.replaceAll("[.@]", " ").trim());
                System.out.println(userEmail.toString());

                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final Firebase firebaseUserRef = new Firebase("https://sunsspot.firebaseio.com/users/").child(snapshot.getValue(String.class));
                            firebaseUserRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                                public void onAuthenticated(AuthData authData) {
                                    spinnerLoggingIn.setVisibility(View.INVISIBLE);
                                    buttonLogIn.setVisibility(View.VISIBLE);
                                    testUserFeedback.setText("Login Successful");
                                    ROOT = firebaseUserRef.child("data");
                                    Intent intent = new Intent(ZeusMainActivity.this, DashBoardActivity.class);
                                    startActivity(intent);
                                }

                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    spinnerLoggingIn.setVisibility(View.INVISIBLE);
                                    buttonLogIn.setVisibility(View.VISIBLE);
                                    testUserFeedback.setText("Wrong Password");
                                }
                            });
                        } else
                        {
                            spinnerLoggingIn.setVisibility(View.INVISIBLE);
                            buttonLogIn.setVisibility(View.VISIBLE);
                            testUserFeedback.setText("Such email is not registered");
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        spinnerLoggingIn.setVisibility(View.INVISIBLE);
                        buttonLogIn.setVisibility(View.VISIBLE);
                        System.out.println("The download failed: " + firebaseError.getMessage());
                    }
                };
                userEmail.addListenerForSingleValueEvent(listener);
            }
        });
    }
}
