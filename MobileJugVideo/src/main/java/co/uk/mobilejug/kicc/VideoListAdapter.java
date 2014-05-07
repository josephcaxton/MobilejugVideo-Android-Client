package co.uk.mobilejug.kicc;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by edit on 26/04/2014.
 */
public class VideoListAdapter extends ArrayAdapter {

    private Context context;

    // We can use list or Grid.. List is the default
    private boolean useList = true;
    public VideoListAdapter(Context context, ArrayList items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;

    }

    /** * Holder for the list items. */
    private class ViewHolder{
        ImageView image;
        TextView titleText;
    }


    /**
     * *
     * * @param position
     * * @param convertView
     * * @param parent * @return */

     public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        VideoItem item = (VideoItem)getItem(position);
        View viewToUse = null;

        LayoutInflater mInflater = (LayoutInflater) context .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if(useList){
                viewToUse = mInflater.inflate(R.layout.fragment_video_list, null);
                }
            else
                {
                 viewToUse = mInflater.inflate(R.layout.fragment_video_grid, null);
                }
            holder = new ViewHolder();
            holder.image =(ImageView)viewToUse.findViewById(R.id.VideolistImage1);
            holder.titleText = (TextView)viewToUse.findViewById(R.id.VideotextView1);

            viewToUse.setTag(holder);
        }
        else
        {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

         holder.image.setImageResource(R.drawable.ic_launcher);
         String Website = "http://mobilejug.co.uk";
         Picasso.with(context).load(Website + item.getImageLocation()).noFade().into(holder.image);
         //holder.image.setImageURI(Uri.parse(item.getImageLocation()));
         holder.titleText.setText(item.getDescription());
        return viewToUse;
    }



}
