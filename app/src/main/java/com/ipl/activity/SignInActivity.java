package com.ipl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ipl.R;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.model.UserDetailsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private final int RC_SIGN_IN = 101;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    final String TAG = "SignInActivity";
    private GoogleApiClient mGoogleApiClient;
    private String userName;
    private String userEmail;
    private String userNickName;
    private String userimgUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
//        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * This method is for the initialization of all the component
     */
    private void initView() {
        Firebase.setAndroidContext(getApplicationContext());
        final Button btnSignIn = (Button) findViewById(R.id.activity_login_btn_login_with_google);
        btnSignIn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_btn_login_with_google:
                signIn();
                break;
        }

    }

    /**
     * Firebase sign in methos with google
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * This method is for the response from the google login
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                final GoogleSignInAccount account = result.getSignInAccount();
                assert account != null;
                userName = account.getDisplayName();
                userEmail = account.getEmail();
                userNickName = account.getGivenName();
                userimgUrl = account.getPhotoUrl().toString();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * This method is for the firebase authentication with google
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final Firebase ref = new Firebase(FirebaseConstant.FIREBASE_URL);

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        ref.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                                        //do ur stuff
                                        final Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                        intent.putExtra("userName", userName);
                                        intent.putExtra("userEmail", userEmail);
                                        intent.putExtra("userNickName", userNickName);
                                        intent.putExtra("userImgUrl", userimgUrl);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        //do something
                                        storeUserDetails();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //store user details
    private void storeUserDetails() {
        final Firebase ref = new Firebase(FirebaseConstant.FIREBASE_URL);
        final UserDetailsModel userDetailsModel = new UserDetailsModel();
        userDetailsModel.setUserName(userName);
        userDetailsModel.setUserNickName(userNickName);
        userDetailsModel.setUserEmail(userEmail);
        userDetailsModel.setUserProfileImgUrl(userimgUrl);
        ref.child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userDetailsModel, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.e(TAG, firebaseError.getMessage());
                } else {
                    final Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("userNickName", userNickName);
                    intent.putExtra("userImgUrl", userimgUrl);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}
