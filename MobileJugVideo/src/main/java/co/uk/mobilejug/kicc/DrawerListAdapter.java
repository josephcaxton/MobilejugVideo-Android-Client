package co.uk.mobilejug.kicc;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by joseph on 25/04/2014.
 */
public class DrawerListAdapter  extends ArrayAdapter<DrawerListItem> {

    private final Context context;
    private ArrayList<DrawerListItem> values;
    int resource;

    public DrawerListAdapter(Context context, int resource, ArrayList<DrawerListItem> values) {
        super(context, resource, values);
        this.context = context;
        this.values = values;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, parent, false);
        }
        DrawerListItem di = values.get(position);

        if (di!= null) {

            ImageView imageView = (ImageView) view.findViewById(R.id.listImage1);
            TextView txtLabel= (TextView) view.findViewById(R.id.textView1);
            imageView.setImageResource(di.getIcon());
            txtLabel.setText(di.getTitle());

        }
        return view;
    }
}
