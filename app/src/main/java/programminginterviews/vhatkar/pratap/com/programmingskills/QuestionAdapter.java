package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ScrollView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Created by pratap on 15/5/15.
 */
public class QuestionAdapter extends BaseAdapter {

    private QuestionModel[] mainList;

    private Context appContext;

    public AnswerModel[] ansList;

    private int storedposition = 999;

    private boolean isWebServiceCalled = false;

    private RadioButton r4,r1,r2,r3;

    private int[] correctAnswers;



    ArrayList<Integer> seriesOfAnsers = new ArrayList<Integer>();
    ArrayList<Integer> serverAns = new ArrayList<Integer>();
    ArrayList<Integer> userAns = new ArrayList<Integer>();
    ArrayList<Integer> reviewAns = new ArrayList<Integer>();
    ArrayList<Integer> reviewuserAns = new ArrayList<Integer>();
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;

    boolean isReviewTest = false;


    public QuestionAdapter(Context applicationContext, QuestionModel[] itemList, ArrayList<Integer> review, boolean isReview, ArrayList<Integer> userAns) {
        super();
        this.mainList = itemList;
        this.appContext = applicationContext;

        for(int i = 0 ; i < this.mainList.length ; i++){
            try {
                seriesOfAnsers.add(i, 99);
                this.userAns.add(i, 959);
                serverAns.add(i,854);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        for (int i=0;i<this.mainList.length ;i++)
        {
            AnswerModel[] aModel = this.mainList[i].getAnswerModel();

            for (int j = 0; j < 4 ; j++ )
            {
                if(aModel[j].isCorrectFlag())
                {
                    serverAns.set(i,j+1);
                }
            }

        }


            isReviewTest = isReview;
            reviewAns = review;
            reviewuserAns = userAns;

    }

//    public QuestionAdapter(QuestionActivity questionActivity, QuestionModel[] parse, ArrayList<Integer> reviewAns) {
//        super();
//        this.mainList = mainList;
//    }




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

        View rowView = convertView;
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater =  (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.newcustomcell, null);
        }



            TextView tv1 = (TextView) convertView
                    .findViewById(R.id.textQuestion);

            tv1.setText(this.mainList[position].

            getQuestionText()

            );


            final AnswerModel[] aModel = this.mainList[position].getAnswerModel();


            System.out.print("Inside call "+position);
            //option 1
            TextView option1 = (TextView) convertView
                    .findViewById(R.id.option1Text);

            option1.setText(aModel[0].

            getAnsText()

            );

            //option 2
            TextView option2 = (TextView) convertView
                    .findViewById(R.id.option2Text);

            option2.setText(aModel[1].

            getAnsText()

            );

            //option 1
            TextView option3 = (TextView) convertView
                    .findViewById(R.id.option3Text);

            option3.setText(aModel[2].

            getAnsText()

            );

            //option 1
            TextView option4 = (TextView) convertView
                    .findViewById(R.id.option4Text);

            option4.setText(aModel[3].

            getAnsText()

            );


//            ((QuestionActivity)appContext).
//
//            LastQuestionDetected(this.mainList.length, position);

            if(isReviewTest==true)

            {
                rl1 = (RelativeLayout) convertView.findViewById(R.id.relative1);
                rl2 = (RelativeLayout) convertView.findViewById(R.id.relative2);
                rl3 = (RelativeLayout) convertView.findViewById(R.id.relative3);
                rl4 = (RelativeLayout) convertView.findViewById(R.id.relative4);
                reviewTestManagemanet(reviewAns.get(position), position);
//            checkRadioBtnStatus(reviewuserAns.get(position),position);
            }

            else

            {

                r1 = (RadioButton) convertView.findViewById(R.id.option1RadioBox);
                r1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //check for ans

                        if (aModel[0].isCorrectFlag()) {
                            Toast.makeText(appContext, "correct Answer!!!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(appContext, "Wrong Answer!!!", Toast.LENGTH_SHORT).show();
                        }

                        checkRadioBtnStatus(1, position);
                    }
                });

                r2 = (RadioButton) convertView.findViewById(R.id.option2RadioBox);

                r2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (aModel[1].isCorrectFlag()) {
                            Toast.makeText(appContext, "correct Answer!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(appContext, "Wrong Answer!!!", Toast.LENGTH_SHORT).show();
                        }

                        checkRadioBtnStatus(2, position);
                    }
                });

                r3 = (RadioButton) convertView.findViewById(R.id.option3RadioBox);

                r3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (aModel[2].isCorrectFlag()) {
                            Toast.makeText(appContext, "correct Answer!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(appContext, "Wrong Answer!!!", Toast.LENGTH_SHORT).show();
                        }

                        checkRadioBtnStatus(3, position);
                    }
                });


                r4 = (RadioButton) convertView.findViewById(R.id.option4RadioBox);

                r4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (aModel[3].isCorrectFlag()) {
                            Toast.makeText(appContext, "correct Answer!!!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(appContext, "Wrong Answer!!!", Toast.LENGTH_SHORT).show();
                        }

                        checkRadioBtnStatus(4, position);
                    }
                });

                try {
                    if (seriesOfAnsers.get(position) != 99) {
                        reviewTest(seriesOfAnsers.get(position));
                    }
                } catch (IndexOutOfBoundsException e) {

                }


//                ((QuestionActivity) appContext).selectedAns(userAns, serverAns);

            }

            return convertView;
        }


    public void checkRadioBtnStatus(int pos, int index)
    {
        if(!isReviewTest)
        userAns.set(index,pos);


        if(pos==1){
            r1.setChecked(true);
            r2.setChecked(false);
            r3.setChecked(false);
            r4.setChecked(false);
            try {
                seriesOfAnsers.set(index, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(pos == 2)
        {
            r1.setChecked(false);
            r2.setChecked(true);
            r3.setChecked(false);
            r4.setChecked(false);
            try {
                seriesOfAnsers.set(index, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(pos == 3){
            r1.setChecked(false);
            r2.setChecked(false);
            r3.setChecked(true);
            r4.setChecked(false);
            try {
                seriesOfAnsers.set(index, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(pos==4)
        {   r1.setChecked(false);
            r2.setChecked(false);
            r3.setChecked(false);
            r4.setChecked(true);
            try {
                seriesOfAnsers.set(index, 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void reviewTestManagemanet(int pos, int index)
    {
        if(pos==1)
        {
            rl1.setBackgroundColor(appContext.getResources().getColor(R.color.highLight_yello));
        }
        else if(pos==2)
        {
            rl2.setBackgroundColor(appContext.getResources().getColor(R.color.highLight_yello));
        }
        else if(pos==3)
        {
            rl3.setBackgroundColor(appContext.getResources().getColor(R.color.highLight_yello));
        }
        else if(pos==4)
        {
            rl4.setBackgroundColor(appContext.getResources().getColor(R.color.highLight_yello));
        }
    }

    public void reviewTest(int position)
    {
        System.out.print("checkbox " + position);

//        position = p

        Toast.makeText(appContext, "checkbox -> " + position, Toast.LENGTH_SHORT).show();

        if(position == 1)
        {
            r1.setChecked(true);
            r1.setSelected(true);
        }
        else if(position == 2)
        {
            r2.setChecked(true);
            r2.setSelected(true);
        }
        else if(position == 3)
        {
            r3.setChecked(true);
            r3.setSelected(true);

        }else
        {
            r4.setChecked(true);
            r4.setSelected(true);
        }


    }



}


