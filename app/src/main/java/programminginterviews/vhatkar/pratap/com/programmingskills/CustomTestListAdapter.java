package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFlat;

import java.util.ArrayList;

/**
 * Created by pratap on 15/5/15.
 */
public class CustomTestListAdapter extends BaseAdapter {

    private TestListModel[]  mainList;
    private Context appContext;
    public String para;
    public String className;
    public CustomTestListAdapter(Context applicationContext,TestListModel[] itemList) {
        super();
        this.mainList = itemList;
        this.appContext = applicationContext;
    }


    @Override
    public int getCount()
    {
        return mainList.length;
    }

    @Override
    public Object getItem(int position)
    {
        return mainList[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.testlistcell, null);
        }

        TextView tv1 = (TextView) convertView
                .findViewById(R.id.testName);



        tv1.setText(this.mainList[position].getName());
        ButtonFlat buyTest = (ButtonFlat) convertView.findViewById(R.id.byeBtn);

        if(this.mainList[position].isPaid())
        {
            if(this.mainList[position].Is_user_purchased())
            {
                buyTest.setText("Purchased");
            }
            else
            {
                buyTest.setText("Buy");
            }
        }
        else
        {
            buyTest.setText("Free");
        }



        TextView tv2 = (TextView)convertView.findViewById(R.id.totalQuestion);
        tv2.setText("Total " + this.mainList[position].getQuestionAttept() +  " question");


        TextView tv3 = (TextView)convertView.findViewById(R.id.totalAppear);
        tv3.setText(this.mainList[position].getUserAttempt() +  " users have given this test");


        buyTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((TestListActivity) appContext).onBuyPressed(mainList[position].getProductString(), mainList[position].getTestid(),mainList[position].getName());
            }
        });

        try {
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(mainList[position].isPaid()){

                        if(!mainList[position].Is_user_purchased())
                        {
                            ((TestListActivity) appContext).onBuyPressed(mainList[position].getProductString(), mainList[position].getTestid(), mainList[position].getName() );;
                        }
                        else {
                            TestListModel model = mainList[position];
                            Intent intent = new Intent(appContext, StartTestActivity.class);
                            intent.putExtra("test_id",mainList[position].getTestid());
                            intent.putExtra("test_name",mainList[position].getName());
                            appContext.startActivity(intent);
                        }
                        //show paypal
                    }
                    else {

                        TestListModel model = mainList[position];
                        Intent intent = new Intent(appContext, StartTestActivity.class);
                        intent.putExtra("test_id",mainList[position].getTestid());
                        intent.putExtra("test_name",mainList[position].getName());
                        appContext.startActivity(intent);
                    }

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;

    }


}