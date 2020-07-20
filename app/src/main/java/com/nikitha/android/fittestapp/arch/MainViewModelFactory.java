package com.nikitha.android.fittestapp.arch;

import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

// COMPLETED (1) Make this class extend ViewModel ViewModelProvider.NewInstanceFactory
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
        Context context;
        public MainViewModelFactory(Context context) {
           this.context=context;
        }

        @Override
        public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ViewModelMain(context);
        }
}

