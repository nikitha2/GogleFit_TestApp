package com.nikitha.android.fittestapp.arch;

import android.content.Context;

import com.nikitha.android.fittestapp.DatenStepsPojo;

import java.util.List;

public class ViewModelMain extends androidx.lifecycle.ViewModel {
    Repository repository;

    public ViewModelMain(Context context) {
        repository = new Repository(context);
    }

    public void getAndroidPermissions(){
        repository.getAndroidPermissions();
    }

    public  void getOAuthAuthorizationPermissions(){
         repository.getOAuthAuthorizationPermissions();
    }

   /* public List<DatenStepsPojo> accessGoogleFit(){
        return repository.accessGoogleFit();
    }*/

}
