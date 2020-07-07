package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    private FirebaseAuth mAuth;
    TextView t;

    ProgressBar progressBar;
    EditText emails,passwords,username,con_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        emails = findViewById(R.id.email_reg);
        passwords = findViewById(R.id.password_reg);
        username = findViewById(R.id.user_name);
        progressBar = findViewById(R.id.progress_reg);
        t = findViewById(R.id.warning);
        con_pass = findViewById(R.id.con_password_reg);

    }

    public void createAccount(View v){
        //User name and email and password
        final String user_name = username.getText().toString();
        final String email = emails.getText().toString();
        String password = passwords.getText().toString();
        String con_pas = con_pass.getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        if(!user_name.matches("") || !email.matches("")  || !password.matches("") || !con_pas.matches("") ) {
            if(con_pas.equals(password)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    sendVerificationEmail();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.setVisibility(View.INVISIBLE);

                                    Toast.makeText(Register.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });            }
            else{
                //warning password
                t.setText("Passwords do not match!");
            }
        }
        else{
            //text warning
            t.setText("Feilds can't be empty!!");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // email sent
                                Toast.makeText(Register.this, "Verification email sent.", Toast.LENGTH_SHORT).show();

                                // after email is sent just logout the user and finish this activity
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(Register.this, login.class));
                                finish();
                            } else {

                                // email not sent, so display message and restart the activity or do whatever you wish to do
                                Toast.makeText(Register.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                                //restart this activity
                                overridePendingTransition(0, 0);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());

                            }
                        }
                    });
        }
    }
}

