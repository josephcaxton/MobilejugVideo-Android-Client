package co.uk.mobilejug.kicc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by edit on 24/04/2014.
 */
public class Splash extends Activity {

    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    private static boolean SPLASH_DISPLAY_SHOWN = false;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);

        if (!SPLASH_DISPLAY_SHOWN) {
			/*
			 * New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.
			 */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SPLASH_DISPLAY_SHOWN = true;
                    launchActivity();
                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            launchActivity();
        }
    }

    protected void launchActivity() {
        SharedPreferences pref = getSharedPreferences("default", MODE_PRIVATE);
        Intent mainIntent;
        if (pref.contains("loginUser")) {
            mainIntent = new Intent(Splash.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            //mainIntent = new Intent(Splash.this, Login.class);
            mainIntent = new Intent(Splash.this, MainActivity.class);
        }
        Intent intent = getIntent();

        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

}
