package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.Button;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by pratap on 15/5/15.
 */
public class QuestionAdapter extends BaseAdapter {

    private QuestionModel[] mainList;

    private Context appContext;

    public AnswerModel[] ansList;

    private int storedposition = 999;

    private boolean isWebServiceCalled = false;

    public QuestionAdapter(Context applicationContext,QuestionModel[] itemList) {
        super();
        this.mainList = itemList;
        this.appContext = applicationContext;

    }

    public QuestionAdapter() {
        super();
        this.mainList = mainList;
    }




    @Override
    public int getCount() {
        return this.mainList.length;
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
            convertView = inflater.inflate(R.layout.questioncell, null);
        }


            TextView tv1 = (TextView) convertView
                    .findViewById(R.id.textQuestion);

            tv1.setText(this.mainList[position].getQuestionText());


        AnswerModel[] aModel = this.mainList[position].getAnswerModel();


        System.out.print("Inside call " + position);
            //option 1
            TextView option1 = (TextView) convertView
                    .findViewById(R.id.option1Text);

            option1.setText(aModel[0].getAnsText());

            //option 2
            TextView option2 = (TextView) convertView
                    .findViewById(R.id.option2Text);

            option2.setText(aModel[1].getAnsText());

            //option 1
            TextView option3 = (TextView) convertView
                    .findViewById(R.id.option3Text);

            option3.setText(aModel[2].getAnsText());

            //option 1
            TextView option4 = (TextView) convertView
                    .findViewById(R.id.option4Text);

            option4.setText(aModel[3].getAnsText());




        return convertView;
    }

}


