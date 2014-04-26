package co.uk.mobilejug.kicc;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edit on 26/04/2014.
 */
public class VideoView_fragment extends ListFragment {

    private ListAdapter mAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static Integer sectionNum = 0;
    private List VideoList;


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

        VideoList = new ArrayList();
        VideoItem V1 = new VideoItem();
        V1.setID(1);
        V1.setDescription("Pastors Conference");
        V1.setTitle("This is a title");
        V1.setImageLocation("http://temp/file/book/images/1325310017.jpg");
        VideoList.add(V1);
        VideoList.add(V1);
        VideoList.add(V1);
        mAdapter = new VideoListAdapter(getActivity(), VideoList);
        setListAdapter(mAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
