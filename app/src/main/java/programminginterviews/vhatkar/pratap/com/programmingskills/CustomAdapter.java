package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

/**
 * Created by pratap on 14/5/15.
 */
public class CustomAdapter extends BaseAdapter {

    private TechnologiesModel[] mainList;

    private Context appContext;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public CustomAdapter(Context applicationContext,TechnologiesModel[] itemList) {
        super();
        this.mainList = itemList;
        this.appContext = applicationContext;
    }

    public CustomAdapter() {
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
            convertView = inflater.inflate(R.layout.customcell, null);
        }


        TextView tv1 = (TextView) convertView
                .findViewById(R.id.row_textView1);
        TextView tv2 = (TextView) convertView
                .findViewById(R.id.detailText);

        System.out.print( "Asset path" + appContext.getAssets());

//        Typeface type = Typeface.createFromAsset(appContext.getAssets(), "monaco.ttf");
//        tv1.setTypeface(type);
//        tv2.setTypeface(type);

        tv1.setText(this.mainList[position].getTechnologyName());
        tv2.setText(this.mainList[position].getGetTechnologyDescription());


//        mRequestQueue = Volley.newRequestQueue(appContext);
//        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
//            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
//            public void putBitmap(String url, Bitmap bitmap) {
//                mCache.put(url, bitmap);
//            }
//            public Bitmap getBitmap(String url) {
//                return mCache.get(url);
//            }
//        });
//
//        NetworkImageView avatar = (NetworkImageView)convertView.findViewById(R.id.tech_image);
//        avatar.setImageUrl(this.mainList[position].getIconurl(),mImageLoader);

        //Initialize ImageView
        ImageView imageView = (ImageView) convertView.findViewById(R.id.tech_image);

        Picasso.with(appContext)
                .load(this.mainList[position].getIconurl())
                .placeholder(R.mipmap.ic_launcher)   // optional
                .error(R.mipmap.ic_launcher)      // optional
                .resize(90, 90)                        // optional
                .rotate(0)                             // optional
                .into(imageView);



        try {

            tv1.setText(this.mainList[position].getTechnologyName());
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    TechnologiesModel model = mainList[position];
                    Intent intent = new Intent(appContext, TestListActivity.class);
                    intent.putExtra("techid",model.getTechnologyID());
                    appContext.startActivity(intent);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


}