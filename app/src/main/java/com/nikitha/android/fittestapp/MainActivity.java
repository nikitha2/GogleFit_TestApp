package com.nikitha.android.fittestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nikitha.android.fittestapp.arch.MainViewModelFactory;
import com.nikitha.android.fittestapp.arch.ViewModelMain;

import java.util.List;

import static com.nikitha.android.fittestapp.Commons.PERMISSION_REQUEST_ACTIVITY_RECOGNITION;
import static com.nikitha.android.fittestapp.Commons.RC_SIGN_IN;
import static com.nikitha.android.fittestapp.Commons.datenStepsPojo;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener, OAuthAuthorization.FitnessGetHistoryClientOnSuccessCallback, StepsByDateAdapter.DateClickListener {
    String TAG=MainActivity.class.getSimpleName();
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    RecyclerView tableLayout;
    TextView hiCustomer;
    final Context context=this;
    GoogleSignInAccount account;
    MainViewModelFactory factory;
    ViewModelMain viewModel;
    OAuthAuthorization oAuthAuthorization;
    StepsByDateAdapter stepsByDateAdapter;
    List<DatenStepsPojo> datenStepsPojoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Commons.CONTEXT_MainActivity=this;
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        init();
    }

    private void init() {
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Sign in with Google");
        signInButton.setOnClickListener(this);
        tableLayout=(RecyclerView) findViewById(R.id.tableLayout);
        tableLayout.setLayoutManager(new GridLayoutManager(this,1));
        tableLayout.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        hiCustomer = findViewById(R.id.hiCustomer);
        factory = new MainViewModelFactory(this);
        viewModel = ViewModelProviders.of(this,factory).get(ViewModelMain.class);
        oAuthAuthorization=new OAuthAuthorization(this);

    }

    /*
    * check if signed in with google account, else signin and getPermissions
    * */
    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account==null){
            setSignBtnNtableVisibility(View.VISIBLE, View.GONE);
        }
        else {
            getPermissions(account);
            setSignBtnNtableVisibility(View.GONE, View.VISIBLE);
        }
    }

    /*get android permission required to access the history client*/
    private void getPermissions(GoogleSignInAccount account) {
        if(account!=null) {
            viewModel.getAndroidPermissions();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            default:
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*After Permission is denied/ accepted this is a call back method called  to comeback on the main thread*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
                viewModel.getOAuthAuthorizationPermissions();
                Log.i(TAG, String.valueOf(datenStepsPojo.size()));

            }
        }
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /*After Permission is denied/ accepted this is a call back method called  to comeback on the main thread*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        Log.i(TAG,"onRequestPermissionsResult");
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Toast.makeText(this,"android permission granted: ACTIVITY_RECOGNITION",Toast.LENGTH_SHORT).show();
                viewModel.getOAuthAuthorizationPermissions();
                Log.i(TAG, String.valueOf(datenStepsPojo.size()));
            } else {
                Toast.makeText(this,"android permission denied: ACTIVITY_RECOGNITION",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            getPermissions(account);
            setSignBtnNtableVisibility(View.GONE, View.VISIBLE);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            getPermissions(null);
            setSignBtnNtableVisibility(View.VISIBLE, View.GONE);
        }
    }

    private void setSignBtnNtableVisibility(int signInButtonVisibility, int tableVisibility) {
        signInButton.setVisibility(signInButtonVisibility);
        tableLayout.setVisibility(tableVisibility);
        hiCustomer.setVisibility(tableVisibility);
        String hi= context.getResources().getString(R.string.welcome);
        String exclamations= context.getResources().getString(R.string.esclamation);
        hiCustomer.setText(hi+" "+account.getEmail().split("@")[0]+" "+exclamations);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.signout:
                signOut();break;
        }
        return true;
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                                        setSignBtnNtableVisibility(View.VISIBLE, View.GONE);
                                        Fitness.getConfigClient(context, account).disableFit();
            }
                });
    }

    @Override
    public void onFitnessGetHistoryClientOnSuccess(List<DatenStepsPojo> resultList) {
        Log.i(TAG,"onFitnessGetHistoryClientOnSuccess");
        if(resultList!=null){
            Log.i(TAG, String.valueOf(resultList.size()));
            addHeader(resultList);
            stepsByDateAdapter = new StepsByDateAdapter(this,resultList,this);
            tableLayout.setAdapter(stepsByDateAdapter);
        }
    }

    private void addHeader(List<DatenStepsPojo> resultList) {
        resultList.add(0, new DatenStepsPojo(getString(R.string.date),getString(R.string.steps)));
        resultList.get(0).setType(0);
    }

    @Override
    public void onDateClick(List<DatenStepsPojo> data) {
        data.remove(0);
        datenStepsPojoResult = stepsByDateAdapter.SortAscOrDes(data);
        addHeader(datenStepsPojoResult);
        stepsByDateAdapter.setData(datenStepsPojoResult);
    }
}