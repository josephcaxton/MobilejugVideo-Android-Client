package co.uk.mobilejug.kicc;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, Login_fragment.OnFragmentInteractionListener, VideoView_fragment.OnFragmentInteractionListener, Register_fragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private String ENDPOINT = "http://mobilejug.co.uk/services/v1/index.php/freevideos";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public static String UserName;
    public static String UserEmail;
    public static String UserApikey;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        if(position == 5){
            // If user is logged in then he is asking to be logout here
            if(UserApikey != null && !UserApikey.isEmpty()){
                UserName = "";
                UserEmail = "";
                UserApikey = "";
                // Show logout on the navigation Drawer
                NavigationDrawerFragment nf = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
                nf.RefreshDrawer(UserApikey);
                // Clean out Preference
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("APIKEY","");
                editor.commit();
                // Take us back to the login screen
                Login_fragment f = Login_fragment.newInstance(position + 1);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, f)
                        .commit();
            }
            else {
                Login_fragment f = Login_fragment.newInstance(position + 1);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, f)
                        .commit();
            }
        }
        else {
               // if postion is not 5 / then is the user logged in already.. check preferences

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String apikey = pref.getString("APIKEY","");

            if (apikey.equalsIgnoreCase("")) {
                //We need to force login.
                Login_fragment f = Login_fragment.newInstance(position + 1);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, f)
                        .commit();
            }
            else {
                // User is already logged in
                UserApikey = apikey;

                VideoView_fragment vv = VideoView_fragment.newInstance(position + 1);
                //Bundle args = new Bundle();
                //Bundle args = vv.getArguments();
                //args.putString("APIKEY", UserApikey);
                //vv.setArguments(args);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, vv)
                        .commit();
            }
        }
    }

    public void onFragmentInteraction(String[] authDetails){

        UserName = authDetails[0];
        UserEmail =authDetails[1];
        UserApikey = authDetails[2];
        //Toast toast = Toast.makeText(this, UserName + " " + UserEmail+ " " + UserApikey , Toast.LENGTH_SHORT); toast.show();
        // Update List on Drawer Fragment
        NavigationDrawerFragment nf = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        nf.RefreshDrawer(UserApikey);
        // Store ApiKey into Preference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("APIKEY",UserApikey);
        editor.commit();

    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //TODO
            /*getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar(); */
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
