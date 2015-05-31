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
import com.squareup.picasso.Picasso;

/**
 * Created by pratap on 30/5/15.
 */
public class TestHistoryAdapter extends BaseAdapter {

    private TestHistorymodel[] mainList;

    private Context appContext;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public TestHistoryAdapter(Context applicationContext, TestHistorymodel[] itemList) {
        super();
        this.mainList = itemList;
        this.appContext = applicationContext;
    }

    public TestHistoryAdapter() {
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
            LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.testhistorycell, null);
        }

        TextView testname = (TextView)convertView.findViewById(R.id.testName);
        testname.setText(this.mainList[position].getName());

        TextView totalappear = (TextView)convertView.findViewById(R.id.totalAppear);
        totalappear.setText(this.mainList[position].getUserAttempt() + " people have given this test");

        TextView totalQuestion = (TextView)convertView.findViewById(R.id.totalQuestion);
        totalQuestion.setText(this.mainList[position].getTotalQuestion() + " Question");

        TextView score = (TextView)convertView.findViewById(R.id.scoreTextview);
        score.setText(this.mainList[position].getScore() + "%");


        try {
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(appContext, StartTestActivity.class);
                        intent.putExtra("test_id",mainList[position].getTestid());
                        appContext.startActivity(intent);
                    }



            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


}