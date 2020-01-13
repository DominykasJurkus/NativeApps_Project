package com.dominykas.jurkus.WordQuiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    int RC_SIGN_IN=0;


    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp, textView3;
    FirebaseAuth mFirebaseAuth;
    LoginButton loginButton;
    CallbackManager callbackManager;
    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;


    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public String fbUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        btnSignIn = findViewById(R.id.button2);
        tvSignUp = findViewById(R.id.textView);
        loginButton = (LoginButton)findViewById(R.id.fb_login_bn);
        textView3 = (TextView)findViewById(R.id.textView3);
        callbackManager = CallbackManager.Factory.create();
        signin = findViewById(R.id.sign_in_button);


        ArrayList<String> list = new ArrayList<String>();
        list.add("email");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {



                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        // Application code
                        if (response.getError() != null) {
                            System.out.println("ERROR");
                        } else {
                            System.out.println("Success");
                            String jsonresult = String.valueOf(json);
                            System.out.println("JSON Result" + jsonresult);

                            fbUserEmail = json.optString("email");

                           textView3.setText(fbUserEmail);


                        }
                        Log.v("FaceBook Response :", response.toString());
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();



                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }

            @Override
            public void onCancel() {
                textView3.setText("Login Canceled");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //Google sign in

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

       signin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

                   switch (view.getId()) {
                       case R.id.sign_in_button:
                           signIn();
                           break;
                       // ...
                   }

           }
       });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this, "Signin successful!",Toast.LENGTH_SHORT);
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email");
                    emailId.requestFocus();
                }
                else if (pwd.isEmpty()) {
                    password.setError("Please enter password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are empty!",Toast.LENGTH_SHORT);
                }
                else if (!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error, Please try again!",Toast.LENGTH_SHORT);
                            }
                            else{
                                Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);

                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Error Occurred!",Toast.LENGTH_SHORT);
                }



            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intSignUp);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }



}
