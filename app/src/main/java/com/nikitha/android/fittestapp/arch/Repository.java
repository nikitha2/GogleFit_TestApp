package com.nikitha.android.fittestapp.arch;

import android.content.Context;
import android.util.Log;
import com.nikitha.android.fittestapp.DatenStepsPojo;
import com.nikitha.android.fittestapp.OAuthAuthorization;
import java.util.List;
import static com.nikitha.android.fittestapp.AndroidPermissions.getAndroidPermission;

public class Repository  {
    String TAG= Repository.class.getSimpleName();
    Context context;
    OAuthAuthorization oAuthAuthorization;
    public Repository(Context context) {
        this.context=context;
        oAuthAuthorization=new OAuthAuthorization();
    }

    public void getAndroidPermissions() {
         int status= getAndroidPermission(context);
         if(status==0){
             getOAuthAuthorizationPermissions();
         }
    }

    public  void getOAuthAuthorizationPermissions() {
        oAuthAuthorization.getOAuthAuthorizationPermission(context);
    }



}
