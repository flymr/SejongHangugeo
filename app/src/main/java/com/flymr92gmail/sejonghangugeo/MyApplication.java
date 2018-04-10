package com.flymr92gmail.sejonghangugeo;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by hp on 4/3/2018.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        // Required initialization logic here!
    }


}
