package co.uk.mobilejug.kicc;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;



/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Login_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Login_fragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Login_fragment extends Fragment implements View.OnClickListener{


    private String ENDPOINT = "http://mobilejug.co.uk/services/v1/index.php/login";
    private OnFragmentInteractionListener mListener;
    private EditText Email;
    private EditText Password;
    private Button btnLogin;
    private Button btnCancel;
    private TextView txtLoginFailed;

    public static Login_fragment newInstance(int sectionNumber) {
        Login_fragment fragment = new Login_fragment();

        return fragment;
    }
    public Login_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Email = (EditText)view.findViewById(R.id.txtEmail);
        Password = (EditText)view.findViewById(R.id.txtpassword);
        btnLogin = (Button)view.findViewById(R.id.btnlogin);
        btnCancel = (Button)view.findViewById(R.id.btncancel);
        txtLoginFailed = (TextView)view.findViewById(R.id.loginFailed);

        btnLogin.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        Email.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnLogin)){
    try {

        if(Email.getText().length() < 1 || Password.getText().length() <1 ){
            // Did not enter email address or password
            txtLoginFailed.setText("Enter email address and password");
            txtLoginFailed.setTextColor(Color.BLUE);
            Email.requestFocus();

        }
        else {
            // Now go and login async
            new LoginNow().execute(Email.getText().toString(), Password.getText().toString());
        }
        }
        catch (Exception e) {
            e.printStackTrace();
         }
        }
        else if(view.equals(btnCancel)){


            CloseFragment();

        }

        else if(view.equals(Email)){

            txtLoginFailed.setText("");

        }
        // Do something
    }

    private void CloseFragment() {
        getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.container, VideoView_fragment.newInstance(1))
                    .commit();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            ((MainActivity) activity).onSectionAttached(6);

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(String[] apiKey);
    }

    private class LoginNow extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            OutputStreamWriter out = null;
            InputStream in = null;

            try {



                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                OkHttpClient client = new OkHttpClient();
                // Ignore invalid SSL endpoints.
                client.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
                HttpURLConnection conn;
                conn = client.open(new URL(ENDPOINT));


                // Write the request.
                //conn.setReadTimeout(10000);
                //conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                out = new OutputStreamWriter(conn.getOutputStream());
                out.write(data);
                out.close();


                // Read the response.
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException("Unexpected HTTP response: "
                            + conn.getResponseCode() + " " + conn.getResponseMessage());
                }
                in = conn.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();

            }
            catch (Exception e) {
                e.printStackTrace();

            }
            finally {
                // Clean up.
                if (out != null){
                        try {
                            out.close();
                        }
                        catch (Exception e) {

                    }
                    }


                }
                if (in != null){
                    try {
                        in.close();
                    }
                    catch (Exception e) {

                    }
                }
            return "";
            }


        @Override
        protected void onPostExecute (String result){

            //Log.e("Joseph", result);
            Gson gson = new Gson();
            Authorisation auth = gson.fromJson(result, Authorisation.class);

            String error = auth.getError();
            if (error.equals("false")){
                // User has logged in successfully and api key has been issued
                // Send data to FragmentActivity by hooking into UI event
                if (mListener != null) {
                    String[] authDetails = {auth.getName(),auth.getEmail(),auth.getApikey()};
                    mListener.onFragmentInteraction(authDetails);

                    // Update the list in the Drawer so that it can show logout

                    CloseFragment();
                }
            }
            else{
                // Login failed because error was returned from service
                txtLoginFailed.setText(auth.getMessage());
                txtLoginFailed.setTextColor(Color.RED);
                Email.setText("");
                Password.setText("");
                Email.requestFocus();
            }

        }

        @Override
        protected void onPreExecute () {
        }

        @Override
        protected void onProgressUpdate (Void...values){

        }

    }
}



