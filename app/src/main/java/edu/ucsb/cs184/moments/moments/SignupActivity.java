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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.jude.swipbackhelper.SwipeBackHelper;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;

    private EditText _nameText;
    private EditText _emailText;
    private EditText _passwordText;
    private EditText _cpasswordText;
    private Button _signupButton;
    private TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        setContentView(R.layout.activity_signup);
        Intent signInIntent = getIntent();

        _nameText = findViewById(R.id.input_name);
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _cpasswordText = findViewById(R.id.input_confirmPassword);
        _signupButton = findViewById(R.id.btn_signup);
        _loginLink = findViewById(R.id.link_login);
        mAuth = FirebaseAuth.getInstance();

        String email = signInIntent.getStringExtra(LoginActivity.EMAIL);
        if (signInIntent.getStringExtra(LoginActivity.EMAIL) != null){
            _emailText.setText(email);
            String password = signInIntent.getStringExtra(LoginActivity.PASSWORD);
            if (signInIntent.getStringExtra(LoginActivity.PASSWORD) != null)
                _passwordText.setText(password);
        }

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed("Sign up failed");
            return;
        }

        _signupButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                _signupButton.setEnabled(true);
            }
        });
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest updateName = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.updateProfile(updateName);
                    User.user = new User(user.getUid(), name);
                    FirebaseHelper.setAfterUserInsertionListener(new FirebaseHelper.AfterUserInsertedListener() {
                        @Override
                        public void afterUserInserted(User user) {
                            progressDialog.dismiss();
                            onSignupSuccess();
                        }
                    });
                    FirebaseHelper.insertUser(User.user);
                } else {
                    progressDialog.dismiss();
                    onSignupFailed(task.getException().toString());
                }
            }
        });
    }


    public void onSignupSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _signupButton.setEnabled(true);
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
            }
        });
    }

    public void onSignupFailed(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String cpassword = _cpasswordText.getText().toString();

        valid = checkName(_nameText, name);
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(LoginActivity.INVALID_EMAIL);
            valid = false;
        }
        if (password.length() < 8) {
            _passwordText.setError("You password should have at least 8 characters.");
            valid = false;
        }else if (password.length() > 30) {
            _passwordText.setError("You password cannot have more than 30 characters.");
            valid = false;
        }
        if (!cpassword.equals(password)) {
            _cpasswordText.setError("Passwords NOT match!");
            valid = false;
        }
        return valid;
    }

    public static boolean checkName(EditText editText, String name){
        boolean valid = true;
        if (name.trim().length() < 2) {
            editText.setError("Username should have at least 2 characters");
            valid = false;
        }else if (name.trim().length() > 40) {
            editText.setError("Username cannot have more than 40 characters");
            valid = false;
        }else if (name.contains(" ")){
            editText.setError("You cannot have space in your name.");
            valid = false;
        }else if (FirebaseHelper.findUserWithName(name) != null){
            editText.setError("This name has been used.");
            valid = false;
        }
        return valid;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}