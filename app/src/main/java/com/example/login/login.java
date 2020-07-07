package com.example.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class login extends AppCompatActivity{

    private ImageButton btRegister;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    EditText emails , passwords;
    String email = "",password = "";
    TextView warn;
    ProgressBar progressBa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        emails = findViewById(R.id.email_login);
        passwords = findViewById(R.id.password_login);
        mAuth = FirebaseAuth.getInstance();
        progressBa = findViewById(R.id.progress_login);
        warn = findViewById(R.id.login_warning);
    }
    @Override
    public void onStart() {

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        progressBa.setVisibility(View.INVISIBLE);

        if(currentUser != null){
            Intent i = new Intent(login.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void ss(View v) {
        Intent intent   = new Intent(login.this,Register.class);
        Pair[] pairs    = new Pair[1];
        pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(login.this,pairs);
        startActivity(intent,activityOptions.toBundle());


    }
    public void signIn(View v) {
        progressBa.setVisibility(View.VISIBLE);

        email = emails.getText().toString();
        password = passwords.getText().toString();
        if (!email.matches("") || !password.matches("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information


                                checkIfEmailVerified();
                            } else {
                                progressBa.setVisibility(View.INVISIBLE);

                                // If sign in fails, display a message to the user.
                                Toast.makeText(login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            warn.setText("Feilds can't be empty");
        }
    }
    private void checkIfEmailVerified()
    {

        progressBa.setVisibility(View.INVISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            if (user.isEmailVerified()) {
                // user is verified, so you can finish this activity or send user to activity which you want.
                finish();
                progressBa.setVisibility(View.VISIBLE);
                Toast.makeText(login.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(login.this, MainActivity.class);

                startActivity(i);

            } else {
                // email is not verified, so just prompt the message to the user and restart this activity.
                // NOTE: don't forget to log out the user.

                progressBa.setVisibility(View.INVISIBLE);
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(login.this, "Please verify your Email!",
                        Toast.LENGTH_SHORT).show();


                //restart this activity

            }
        }
    }

}
