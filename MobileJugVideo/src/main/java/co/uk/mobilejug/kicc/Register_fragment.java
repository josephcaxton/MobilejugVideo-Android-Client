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
import android.widget.Toast;

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
 * Created by edit on 05/05/2014.
 */
public class Register_fragment extends Fragment implements View.OnClickListener {

    private String ENDPOINT = "http://mobilejug.co.uk/services/v1/index.php/register";
    private OnFragmentInteractionListener mListener;
    private EditText Name;
    private EditText Email;
    private EditText Password;
    private EditText ConfirmPassword;
    private Button Register;
    private Button Clear;
    private Button btnBack;
    private TextView registerFailed;
    private String APIKEY;

    public static Register_fragment newInstance(int sectionNumber) {
        Register_fragment fragment = new Register_fragment();

        return fragment;
    }
    public Register_fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Name = (EditText)view.findViewById(R.id.nName);
        Email = (EditText)view.findViewById(R.id.nEmail);
        Password = (EditText)view.findViewById(R.id.nPassword);
        ConfirmPassword = (EditText)view.findViewById(R.id.nConfiPass);
        Register = (Button)view.findViewById(R.id.nRegister);
        Clear = (Button)view.findViewById(R.id.nClear);
        registerFailed =(TextView)view.findViewById(R.id.registerFailed);
        btnBack = (Button)view.findViewById(R.id.nBack);
        Register.setOnClickListener(this);
        Clear.setOnClickListener(this);
        Email.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View view) {
        if (view.equals(Register)){
            try {

                if(Email.getText().length() < 1 || Password.getText().length() < 1 || Name.getText().length() < 1  ){
                    // Did not enter email address or password
                    registerFailed.setText("Enter your name, email address and password");
                    registerFailed.setTextColor(Color.BLUE);
                    Email.requestFocus();

                }
                else if( !Password.getText().toString().equals(ConfirmPassword.getText().toString())){
                    registerFailed.setText("The password entered does not match ");
                    registerFailed.setTextColor(Color.BLUE);
                    Password.requestFocus();

                }
                else {
                    // Now go and login async
                    new RegisterNow().execute(Name.getText().toString(), Email.getText().toString(), Password.getText().toString());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(view.equals(Clear)){
            registerFailed.setText("");
            Name.setText("");
                Email.setText("");
                Password.setText("");
                ConfirmPassword.setText("");


        }

        else if(view.equals(Name)){

            Name.setText("");

        }
        else if(view.equals(btnBack)){

            CloseFragment();
        }
        // Do something
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
    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(String[] apiKey);
    }
    private void CloseFragment() {
        Login_fragment vv = Login_fragment.newInstance(1);

        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.container, vv)
                .commit();
    }
    private class RegisterNow extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            OutputStreamWriter out = null;
            InputStream in = null;

            try {



                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");

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
                if ((conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) && (conn.getResponseCode() != HttpURLConnection.HTTP_OK)) {

                    //Log.e("Joseph",conn.getResponseCode() + " code/message " + conn.getResponseMessage());

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
                APIKEY = auth.getApikey();
                if (mListener != null) {
                    String[] authDetails = {auth.getName(),auth.getEmail(),auth.getApikey()};
                    mListener.onFragmentInteraction(authDetails);
                    Toast.makeText(getActivity(),auth.getMessage(),Toast.LENGTH_SHORT).show();
                    // Update the list in the Drawer so that it can show logout

                    CloseFragment();
                }
            }
            else{
                // Login failed because error was returned from service
                registerFailed.setText(auth.getMessage());
                registerFailed.setTextColor(Color.RED);
                Name.setText("");
                Email.setText("");
                Password.setText("");
                ConfirmPassword.setText("");
                Name.requestFocus();
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
