package com.flymr92gmail.sejonghangugeo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.flymr92gmail.sejonghangugeo.MainActivity;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefManager prefManager = new PrefManager(this);
        if (!prefManager.getIsFirstAppActivation()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            prefManager.setIsFirstAppActivation(false);
            startActivity(new Intent(this, PreviewActivity.class));
            finish();
        }

    }
}
