package edu.ucsb.cs184.moments.moments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

/**
 * CustomAdapter login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String VALID_EMAIL = "Please enter a valid email address!";
    public static final String VALID_PASSWORD = "Please enter a valid password!";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String TAG = "LoginActivity";
    public static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth mAuth;

    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextView _forgotPassword;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseHelper.init();
        mAuth = FirebaseAuth.getInstance();
        FirebaseHelper.init();
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
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                if (!_emailText.getText().toString().isEmpty()){
                    intent.putExtra(EMAIL, _emailText.getText().toString());
                    if (!_passwordText.getText().toString().isEmpty())
                        intent.putExtra(PASSWORD, _passwordText.getText().toString());
                }
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        _forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = _emailText.getText().toString();
                if (!validateEmail()) {
                    _emailText.setError("Please enter a valid email address!");
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(LoginActivity.this, "CustomAdapter password reset link has been sent to your email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "This email address is not registered!", Toast.LENGTH_SHORT).show();
                    }
                    // something bad happened
                });
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                _loginButton.setEnabled(true);
            }
        });
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "SignIn:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                onLoginSuccess();
            }
        }
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        User.firebaseUser = firebaseUser;
        while (!FirebaseHelper.initFinished()) {}
        if (User.user == null){
            String id = firebaseUser.getUid();
            User.user = FirebaseHelper.findUser(id);
            if (User.user == null){
                User user = new User(id, "New User"+id);
                FirebaseHelper.insertUser(user);
                Toast.makeText(getApplicationContext(), "Your account was deleted.\nCustomAdapter new one is generated.", Toast.LENGTH_SHORT).show();
            }
        }
//        FirebaseHelper.addDataReceivedListener(new FirebaseHelper.OnDataReceivedListener() {
//            @Override
//            public void onUDBReceived() {
//                if (User.user == null){
//                    String id = firebaseUser.getUid();
//                    User.user = FirebaseHelper.findUser(id);
//                    if (User.user == null){
//                        User user = new User(id, "New User"+id);
//                        FirebaseHelper.insertUser(user);
//                        Toast.makeText(getApplicationContext(), "Your account was deleted.\nA new one is generated.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onGDBReceived() {
//
//            }
//        });
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
//        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(VALID_EMAIL);
            valid = false;
        }

        if (password.isEmpty()) {
            _passwordText.setError(VALID_PASSWORD);
            valid = false;
        }

        return valid;
    }

    public boolean validateEmail() {
        boolean valid = true;

        String email = _emailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(VALID_EMAIL);
            valid = false;
        }

        return valid;
    }
}

