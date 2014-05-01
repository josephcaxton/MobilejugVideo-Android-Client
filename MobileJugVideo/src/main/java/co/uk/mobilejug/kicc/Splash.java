package co.uk.mobilejug.kicc;

import android.app.Activity;
import android.content.Intent;
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

        // Comment this out it is only used for testing
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("APIKEY","");
        editor.commit();*/


        Intent mainIntent = new Intent(Splash.this, MainActivity.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

}
