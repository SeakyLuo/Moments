package edu.ucsb.cs184.moments.moments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    public static final String INVALID_EMAIL = "Please enter a valid email address!";
    public static final String INVALID_PASSWORD = "Please enter a valid password!";
    public static final String AUTHENTICATING = "Authenticating...";
    public static final String FETCH_DATA = "Fetching Data...";
    public static final String LOGIN_FAILED = "Login Failed";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final int REQUEST_SIGNUP = 0;
    private static FirebaseAuth mAuth;

    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextView _forgotPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        if (!FirebaseHelper.initFinished())
            FirebaseHelper.init();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                _loginButton.setEnabled(true);
            }
        });
        if (mAuth.getCurrentUser() != null) {
            onLoginSuccess();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);
        _forgotPassword = findViewById(R.id.link_forgot_password);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra(EMAIL, _emailText.getText().toString());
                intent.putExtra(PASSWORD, _passwordText.getText().toString());
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        _forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailText.getText().toString();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    _emailText.setError(INVALID_EMAIL);
                }else{
                    mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Password reset link has been sent to your email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "This email address is not registered!", Toast.LENGTH_SHORT).show();
                        }
                        // something bad happened
                    });
                }
            }
        });
    }

    private void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        progressDialog.setMessage(AUTHENTICATING);
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) onLoginSuccess();
                        else onLoginFailed();
                    }
                });
    }

    public static void logout(){
        mAuth.signOut();
        User.firebaseUser = null;
        User.user = null;
    }

    private void onLoginSuccess() {
        _loginButton.setEnabled(true);
        User.firebaseUser = mAuth.getCurrentUser();
        if (_emailText != null) _emailText.setText(User.firebaseUser.getEmail());
        if (_passwordText != null) _passwordText.setText("Password");
        if (FirebaseHelper.initFinished()){
            onDataReceived();
        }else {
            progressDialog.setMessage(FETCH_DATA);
            progressDialog.show();
            FirebaseHelper.setOnUDBReceivedListener(new FirebaseHelper.OnUDBReceivedListener() {
                @Override
                public void onUDBReceived() {
                    onDataReceived();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void onLoginFailed() {
        Toast.makeText(getApplicationContext(), LOGIN_FAILED, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
        progressDialog.dismiss();
    }

    private boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(INVALID_EMAIL);
            valid = false;
        }
        if (password.isEmpty()) {
            _passwordText.setError(INVALID_PASSWORD);
            valid = false;
        }
        return valid;
    }

    private void onDataReceived(){
        if (User.firebaseUser == null) return;
        User.user = FirebaseHelper.findUser(User.firebaseUser.getUid());
        if (User.user == null){
            Toast.makeText(getApplicationContext(), LOGIN_FAILED, Toast.LENGTH_SHORT).show();
            User.firebaseUser = null;
            mAuth.signOut();
            _passwordText.setText("");
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                onLoginSuccess();
            }
        }
    }
}

