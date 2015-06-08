package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;

/**
 * Created by pratap on 4/6/15.
 */
public class videoAdapter extends BaseAdapter {

    private videoModel[] mainList;

    private Context appContext;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public videoAdapter(Context applicationContext,videoModel[] itemList) {
        super();
        this.mainList = itemList;
        this.appContext = applicationContext;
    }

    public videoAdapter() {
        super();
        this.mainList = mainList;
    }




    @Override
    public int getCount() {
        return mainList.length;
    }

    @Override
    public Object getItem(int position) {
        return mainList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =  (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.videocell, null);
        }

        TextView videoName = (TextView)convertView.findViewById(R.id.videoName);
        videoName.setText(this.mainList[position].getName());

        //Initialize ImageView
        ImageView imageView = (ImageView) convertView.findViewById(R.id.videoimage);

        Picasso.with(appContext)
                .load(this.mainList[position].getThumbnailurl())
                .placeholder(R.drawable.oldtv)   // optional
                .error(R.drawable.oldtv)      // optional
                        // optional
                .rotate(0)                             // optional
                .into(imageView);

        final String videourl = this.mainList[position].getUrl();

        try {
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(appContext, YouTubePlayerActivity.class);


//                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, this.);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_URL, videourl);


                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);


                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);

                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);

                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    appContext.startActivity(intent);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


}