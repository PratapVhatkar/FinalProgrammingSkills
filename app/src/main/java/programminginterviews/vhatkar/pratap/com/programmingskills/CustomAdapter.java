package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pratap on 14/5/15.
 */
public class CustomAdapter extends BaseAdapter {

    private TechnologiesModel[] mainList;

    private Context appContext;

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

        tv1.setText(this.mainList[position].getTechnologyName());
        tv2.setText(this.mainList[position].getGetTechnologyDescription());

        ImageView imageIcon = (ImageView) convertView
                .findViewById(R.id.row_imageView1);
        ImageView imageClick = (ImageView) convertView
                .findViewById(R.id.row_click_imageView1);

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