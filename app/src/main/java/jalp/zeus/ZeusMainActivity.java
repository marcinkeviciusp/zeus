package jalp.zeus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    Button buttonLogOut;
    EditText textFieldEmail;
    EditText textFieldPassword;
    TextView testUserFeedback;
    public static Firebase ROOT = null;
    public static String username = "User: ";
    public static String[] zeusMenuSections = new String[]{
        "Dashboard",
        "Control Panel",
        "Data Trends",
        "Log Out"
    };

    static Intent dashBoardActivity = null;
    static Intent controlPanelActivity = null;
    static Intent dataTrendsActivity = null;
    static ZeusMainActivity zeusMainActivity = null;

    static RelativeLayout loginLayout;
    static RelativeLayout logoutLayout;

    public static void executeMenu(long selection, AppCompatActivity parent)
    {
        switch ((int) selection) {
            case 0:
                if(!(parent instanceof DashBoardActivity))
                {
                    if(dashBoardActivity == null)
                        dashBoardActivity = new Intent(parent, DashBoardActivity.class);
                    parent.startActivity(dashBoardActivity);
                }
                break;
            case 1:
                if(!(parent instanceof ControlPanelActivity))
                {
                    if(controlPanelActivity == null)
                        controlPanelActivity = new Intent(parent, ControlPanelActivity.class);
                    parent.startActivity(controlPanelActivity);
                }
                break;
            case 2:
                if(!(parent instanceof DataTrendsActivity))
                {
                    if(dataTrendsActivity == null)
                        dataTrendsActivity = new Intent(parent, DataTrendsActivity.class);
                    parent.startActivity(dataTrendsActivity);
                }
                break;
            case 3:
                logout();
        }
    }

    private static void logout()
    {
        username = "User: ";
        ROOT = null;

        Intent intent = new Intent(zeusMainActivity, ZeusMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        zeusMainActivity.startActivity(intent);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setContentView(R.layout.activity_test_main);
        spinnerLoggingIn = (ProgressBar) findViewById(R.id.spinnerLoggingIn);
        buttonLogIn = (Button) findViewById(R.id.buttonLogIn);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        textFieldEmail = (EditText) findViewById(R.id.textFieldEmail);
        textFieldPassword = (EditText) findViewById(R.id.textFieldPassword);
        testUserFeedback = (TextView) findViewById(R.id.TextFieldOutput);
        spinnerLoggingIn.setVisibility(View.INVISIBLE);
        zeusMainActivity = this;

        loginLayout = (RelativeLayout) findViewById(R.id.login_layout_login);
        logoutLayout = (RelativeLayout) findViewById(R.id.login_layout_logout);

        if(!username.equals("User: "))
        {
            loginLayout.setVisibility(View.GONE);
            logoutLayout.setVisibility(View.VISIBLE);
        }

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { logout(); }
        });

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


                                    firebaseUserRef.child("firstName").addListenerForSingleValueEvent(new ValueEventListener() {
                                        public void onCancelled(FirebaseError firebaseError) {}
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            username += dataSnapshot.getValue(String.class);

                                            firebaseUserRef.child("lastName").addListenerForSingleValueEvent(new ValueEventListener() {
                                                public void onCancelled(FirebaseError firebaseError) {
                                                }

                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    username += " " + dataSnapshot.getValue(String.class);
                                                    loginLayout.setVisibility(View.GONE);
                                                    logoutLayout.setVisibility(View.VISIBLE);
                                                    Intent intent = new Intent(ZeusMainActivity.this, DashBoardActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
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
