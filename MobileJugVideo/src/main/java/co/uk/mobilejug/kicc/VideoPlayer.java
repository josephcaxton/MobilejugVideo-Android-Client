package co.uk.mobilejug.kicc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by edit on 05/05/2014.
 */
public class VideoPlayer extends Activity {

    private static final String RECORDHISTORY = "http://mobilejug.co.uk/services/v1/index.php/history";
    private VideoView vv;
    private static String URL;
    private static String APIKey;
    private static String VideoID;
    private static int Startfrom;
    private static int NumberTo;
    private ArrayList videolist;

    private static ProgressDialog progressDialog;
    public VideoPlayer() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.videoplayer);

        progressDialog = ProgressDialog.show(this, "", "Loading video...", true);
        progressDialog.setCancelable(true);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            // If this is playlist then play each video in turn a starting from the selected video
            if(extras.getParcelableArrayList("VideoList") != null){
                videolist = extras.getParcelableArrayList("VideoList");
                NumberTo = videolist.size();

                Startfrom = extras.getInt("StartPlayingFrom");
                VideoItem vi = (VideoItem)videolist.get(Startfrom);
                //Log.e("Joseph", vi.getDescription());
                URL = vi.getURN();
                VideoID = String.valueOf(vi.getID());

                APIKey = extras.getString("APIKEY");

                Startfrom ++;

            }
            else {
            /*URL = extras.getString("VIDEOURN");
            APIKey =extras.getString("APIKEY");
            VideoID = String.valueOf(extras.getInt("VIDEOID"));*/
                // this is coming from parcelable
                VideoItem vi = extras.getParcelable("VideoItem");
                URL = vi.getURN();
                VideoID = String.valueOf(vi.getID());

                APIKey = extras.getString("APIKEY");
            }
        }

        vv = (VideoView)findViewById(R.id.videoView);
        // GEt the Name part of the video URL
        int index = URL.indexOf("/", 2);
        String VideoName = URL.substring(index);

        vv.setVideoPath("http://mobilejug.co.uk" + URL + "/" +"30" + VideoName + "_30.mp4" );

        recordToHistory();

        MediaController mc  = new MediaController(this);
        mc.setAnchorView(vv);
        vv.setMediaController(mc);

        vv.requestFocus();

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        progressDialog.dismiss();
                                                        vv.start();
                                                    }
                                                });

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp) {

                if(Startfrom > 0 && Startfrom < NumberTo) {

                    VideoItem vi = (VideoItem) videolist.get(Startfrom);
                    URL = vi.getURN();
                    VideoID = String.valueOf(vi.getID());
                    int index = URL.indexOf("/", 2);
                    String VideoName = URL.substring(index);
                    vv.setVideoPath("http://mobilejug.co.uk" + URL + "/" + "30" + VideoName + "_30.mp4");
                    recordToHistory();
                    Startfrom ++;
                    vv.start();
                }
            }
            });


    }

    private void recordToHistory(){

        new RecordHistoryNow().execute(APIKey, VideoID);
    }



    private class RecordHistoryNow extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            OutputStreamWriter out = null;
            InputStream in = null;

            try {

                String data = URLEncoder.encode("ApiKey", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                data += "&" + URLEncoder.encode("videoTransactionid", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                OkHttpClient client = new OkHttpClient();
                // Ignore invalid SSL endpoints.
                client.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
                HttpURLConnection conn;

                conn = client.open(new java.net.URL(RECORDHISTORY));
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

        }


        @Override
        protected void onPreExecute () {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate (String...values){

        }


    }

}
