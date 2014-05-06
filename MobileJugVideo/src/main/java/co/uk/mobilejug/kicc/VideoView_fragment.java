package co.uk.mobilejug.kicc;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by edit on 26/04/2014.
 */
public class VideoView_fragment extends ListFragment {

    private static final String  ALLRECENTVIDEOS = "http://mobilejug.co.uk/services/v1/index.php/recentfreevideos";
    private static final String ALLFREEVIDEOS = "http://mobilejug.co.uk/services/v1/index.php/freevideos";
    private static final String ALLPREMIUMVIDEOS = "http://mobilejug.co.uk/services/v1/index.php/getPremiumVideos";
    private static final String ALLHISTORYVIDEOS = "http://mobilejug.co.uk/services/v1/index.php/getHistoryVideos";
    private ArrayAdapter<VideoItem> mAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static Integer sectionNum = 0;
    private List VideoList;
    private String APIKey;
    private OnFragmentInteractionListener mListener;


    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    public static VideoView_fragment newInstance(int sectionNumber) {
        VideoView_fragment fragment = new VideoView_fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        sectionNum = sectionNumber;
        return fragment;
    }
    public VideoView_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        APIKey = pref.getString("APIKEY","");


        // You need to have been logged in successfully to be in this fragment so update Drawer
        String[] authDetails = {"","", APIKey};
        mListener.onFragmentInteraction(authDetails);

        // Async call to server
        new GetVideosNow().execute(APIKey);

        VideoList = new ArrayList();
        /*VideoItem V1 = new VideoItem();
        V1.setID(1);
        V1.setDescription("Pastors Conference");
        V1.setTitle("This is a title");
        V1.setImageLocation("http://temp/file/book/images/1325310017.jpg");
        VideoList.add(V1);
        VideoList.add(V1);
        VideoList.add(V1);*/
        mAdapter = new VideoListAdapter(getActivity(), VideoList);
        setListAdapter(mAdapter);


            return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

       VideoItem vi =(VideoItem) getListView().getItemAtPosition(position);

        //VideoPlayer vp = VideoPlayer.newInstance(1, vi.getURN());

        Intent videoIntent = new Intent(getActivity(),VideoPlayer.class);
        videoIntent.putExtra("VIDEOURN", vi.getURN());
        videoIntent.putExtra("APIKEY",APIKey);
        videoIntent.putExtra("VIDEOID",vi.getID());
        startActivity(videoIntent);
        /*Toast.makeText(getActivity(),
                vi.getURN(),
                Toast.LENGTH_LONG).show(); */
        /*Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        mListener = (OnFragmentInteractionListener) activity;
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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


    protected void CreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage("Loading ...");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

            default:
                //mProgressDialog.setProgress(id);


        }
    }

    private class GetVideosNow extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            OutputStreamWriter out = null;
            InputStream in = null;

            try {


                OkHttpClient client = new OkHttpClient();
                // Ignore invalid SSL endpoints.
                client.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
                HttpURLConnection conn;
                if(sectionNum == 1){
                    conn = client.open(new URL(ALLRECENTVIDEOS));
                }
                else if(sectionNum == 2) {
                    conn = client.open(new URL(ALLFREEVIDEOS));
                }
                else if(sectionNum == 3) {

                    conn = client.open(new URL(ALLPREMIUMVIDEOS));
                }
                else if (sectionNum == 5) {
                    conn = client.open(new URL(ALLHISTORYVIDEOS));
                }
                else{

                    conn = client.open(new URL(ALLRECENTVIDEOS));
                }


                publishProgress("10");
                // Write the request.
                //conn.setReadTimeout(10000);
                //conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                // Api key must be added to the header
                conn.addRequestProperty("Authorization", params[0]);
                //int respCode = conn.getResponseCode();

                //out = new OutputStreamWriter(conn.getOutputStream());
                //out.close();


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
                publishProgress("50");
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
            publishProgress("70");
             Makedata(result);
            publishProgress("100");
            mProgressDialog.dismiss();

        }


        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            CreateDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected void onProgressUpdate (String...values){
            mProgressDialog.setProgress(Integer.parseInt(values[0]));
        }

        private void Makedata(String result) {

            try{
                //Log.e("Joseph", result);
                JSONObject js = new JSONObject(result);
                String err = js.getString("error");


                 if (err.equals("false")) {

                     JSONArray arr = js.getJSONArray("videos");
                     for (int i = 0; i < arr.length(); i++) {

                         JSONObject obj = arr.getJSONObject(i);
                         VideoItem vi = new VideoItem(obj);
                         VideoList.add(vi);
                         mAdapter.notifyDataSetChanged();

                     }
                      if (VideoList.size() < 1){
                          Toast.makeText(getActivity(),"No videos to show",Toast.LENGTH_SHORT).show();
                      }

                 }
                else
                 {
                     Toast.makeText(getActivity(),"Failed to connect to server", Toast.LENGTH_SHORT).show();
                 }

            }

            catch (JSONException e){

                e.printStackTrace();

            }
        }

    }
}
