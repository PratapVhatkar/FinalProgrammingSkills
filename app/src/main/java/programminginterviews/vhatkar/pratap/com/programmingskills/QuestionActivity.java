package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.util.ArrayList;


public class QuestionActivity extends ActionBarActivity {

    public  HorizontialListView listview;

    private QuestionModel[] modelArray;

    private  QuestionAdapter mAdapter;

    private int totalListCount = 0;

    private boolean isFinalQuestion = false;

    private ButtonFlat backBtn;
    private ButtonFlat nextBtn;

    private ButtonFlat endTest;
    private ButtonFlat submitButton;
    private QuestionModel[] resultsScreenAns;
    public AnswerModel[] userAnsList;
    ArrayList<Integer> serverAns = new ArrayList<Integer>();
    ArrayList<Integer> reviewAns = new ArrayList<Integer>();
    ArrayList<Integer> userAns = new ArrayList<Integer>();

    ArrayList<Integer> seriesOfAnsers = new ArrayList<Integer>();
    ArrayList<Integer> serverCorrectans = new ArrayList<Integer>();
    private int para;


    //New

    private TextView questiontext;

    private RadioButton radiobtn1;
    private TextView option1Text;

    private RadioButton radiobtn2;
    private TextView option2Text;

    private RadioButton radiobtn3;
    private TextView option3Text;

    private RadioButton radiobtn4;
    private TextView option4Text;

    QuestionModel[] qResposne;

    public int currentQuestion = 0;

    private ButtonFlat QuestionStatus;

    InterstitialAd mInterstitialAd;

    private boolean isReview =false;
    private LinearLayout rl1;
    private LinearLayout rl2;
    private LinearLayout rl3;
    private LinearLayout rl4;

    boolean isPracticeMode ;

    private String responseStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(String.valueOf(R.string.full_screen_ads));
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        //New

        questiontext = (TextView)findViewById(R.id.textQuestion);

        radiobtn1 = (RadioButton)findViewById(R.id.option1RadioBox);
        option1Text = (TextView)findViewById(R.id.option1Text);

        radiobtn2 = (RadioButton)findViewById(R.id.option2RadioBox);
        option2Text = (TextView)findViewById(R.id.option2Text);

        radiobtn3 = (RadioButton)findViewById(R.id.option3RadioBox);
        option3Text = (TextView)findViewById(R.id.option3Text);

        radiobtn4 = (RadioButton)findViewById(R.id.option4RadioBox);
        option4Text = (TextView)findViewById(R.id.option4Text);

        QuestionStatus = (ButtonFlat)findViewById(R.id.btnQuestionStatus);

        rl1 = (LinearLayout) findViewById(R.id.relative1);
        rl2 = (LinearLayout) findViewById(R.id.relative2);
        rl3 = (LinearLayout) findViewById(R.id.relative3);
        rl4 = (LinearLayout) findViewById(R.id.relative4);

        //Backbtn
        endTest = (ButtonFlat) findViewById(R.id.btnEndtest);

        backBtn = (ButtonFlat) findViewById(R.id.backBtn);
        nextBtn = (ButtonFlat) findViewById(R.id.nextBtn);
        resultsScreenAns = null;
        userAnsList = null;

//        nextBtn.setBackgroundResource(R.mipmap.next_arrow);
//        backBtn.setBackgroundResource(R.mipmap.back_arrow);

//        listview = (HorizontialListView) findViewById(R.id.listview);
//        listview.setScrollContainer(false);

        RequestQueue queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        para = intent.getIntExtra("test_id", 0);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restoredAuth = prefs.getString("auth", null);
        String restoredEmail = prefs.getString("email", null);

        String url = "http://52.24.180.90/api/v1/questions.json?test_id=" + para +"&auth_token=" + restoredAuth+"&"+ "email="+restoredEmail;


        reviewAns = intent.getIntegerArrayListExtra("serverAns");
        userAns = intent.getIntegerArrayListExtra("userans");
        isReview = getIntent().getExtras().getBoolean("isReview");
        isPracticeMode = getIntent().getExtras().getBoolean("isPractice");
        String reponseString  = getIntent().getExtras().getString("savedResponse");

        nextBtn.setVisibility(View.GONE);
        endTest.setVisibility(View.GONE);

        if(isReview==false) {

            endTest.setText("END TEST");
            nextBtn.setVisibility(View.VISIBLE);
            endTest.setVisibility(View.VISIBLE);


            radiobtn1.setEnabled(true);
            radiobtn2.setEnabled(true);
            radiobtn3.setEnabled(true);
            radiobtn4.setEnabled(true);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                           responseStr =  response;
                            System.out.println(response);
                            qResposne = parse(response);
                            currentQuestion = 0;
                            fillnext(0);

                            for (int i = 0; i < qResposne.length; i++) {
                                seriesOfAnsers.add(i, 998);
                                AnswerModel[] model = qResposne[i].getAnswerModel();

                                for (int j = 0; j < 4; j++) {
                                    if (model[j].isCorrectFlag())
                                        serverCorrectans.add(i, j + 1);
                                }

                            }

                            if (isReview == true) {
                                reviewTestManagemanet(reviewAns.get(currentQuestion), currentQuestion);
                                reviewTest(userAns.get(currentQuestion));
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    System.out.print("Errors--->" + error.toString());
                }
            });

            queue.add(stringRequest);
        }
        else
        {
            endTest.setText("END REVIEW");

            radiobtn1.setEnabled(false);
            radiobtn2.setEnabled(false);
            radiobtn3.setEnabled(false);
            radiobtn4.setEnabled(false);


            nextBtn.setVisibility(View.VISIBLE);
            endTest.setVisibility(View.VISIBLE);

            qResposne = parse(reponseString);
            currentQuestion = 0;
            fillnext(0);

            for (int i = 0; i < qResposne.length; i++) {
                seriesOfAnsers.add(i, 998);
                AnswerModel[] model = qResposne[i].getAnswerModel();

                for (int j = 0; j < 4; j++) {
                    if (model[j].isCorrectFlag())
                        serverCorrectans.add(i, j + 1);
                }

            }

            if (isReview == true) {
                reviewTestManagemanet(reviewAns.get(currentQuestion), currentQuestion);
                reviewTest(userAns.get(currentQuestion));
            }
        }
        //Nextbtn

        nextBtn = (ButtonFlat) findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


//                listview.post(new Runnable() {
//                    public void run() {
//
////                        if (listview.getFirstVisiblePosition() + 1 == totalListCount) {
//                            //Last position
//                            nextBtn.setVisibility(Button.GONE);
//                            backBtn.setVisibility(Button.VISIBLE);
////                            submitButton.setVisibility(Button.VISIBLE);
//
//                        } else {
//                            nextBtn.setVisibility(Button.VISIBLE);
//                            backBtn.setVisibility(Button.VISIBLE);
//                            int position = listview.getFirstVisiblePosition();
//                            position = position + 1;
//                            listview.setSelection(position);
//                            mAdapter.notifyDataSetChanged();
//
//                        }
//
//
//
//                    }
//                });


                        currentQuestion = currentQuestion + 1;
                        fillnext(currentQuestion);



            }

        });


        //End Test


        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentQuestion = currentQuestion - 1;

                fillnext(currentQuestion);


//                listview.post(new Runnable() {
//                    public void run() {
////
////                        if (listview.getFirstVisiblePosition() == 0) {
////                            //Last position
////                            backBtn.setVisibility(Button.GONE);
////
////                        } else {
////
////                            nextBtn.setVisibility(Button.VISIBLE);
////                            int position = listview.getFirstVisiblePosition();
////                            position = position - 1;
////
////                            if (position == 0) {
////                                backBtn.setVisibility(Button.GONE);
////                            }
////
////                            listview.setSelection(position);
////                            mAdapter.notifyDataSetChanged();
////                        }
//
//
////                        Toast.makeText(QuestionActivity.this, "back -> " + position  , Toast.LENGTH_SHORT).show();
////                    }
//                });


            }

        });


        endTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                int totalQuestions = serverCorrectans.size();

                int totalCorrect = 0;

                for(int i = 0 ;i < serverCorrectans.size() ; i++)
                {
                    if(serverCorrectans.get(i) == seriesOfAnsers.get(i))
                    {
                        totalCorrect = totalCorrect + 1;
                    }
                }


                final float percentage =( totalCorrect / totalQuestions) * 100;
                String s = "";
                String ss = "";
                if(isReview ==false) {
                     s = "End this test ?";
                     ss = "all the progress will be losed";
                }else
                {
                    s = "End Review";
                    ss = "";
                }

                AlertDialog.Builder builder1 = new AlertDialog.Builder(QuestionActivity.this);
                builder1.setMessage("End this test ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                if(isReview==false) {
                                    Intent intent = new Intent(QuestionActivity.this, Result.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("userans", seriesOfAnsers);
                                    intent.putExtra("serverAns", serverCorrectans);
                                    intent.putExtra("test_id", para);
                                    intent.putExtra("percentage", percentage);
                                    intent.putExtra("savedResponse", responseStr);

                                    if(isPracticeMode==true)
                                    intent.putExtra("isInstant",true);
                                    else
                                    intent.putExtra("isInstant",false);

                                    getApplicationContext().startActivity(intent);
                                }else
                                {
                                    Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(intent);
                                }
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

        });



//        listview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                listview.requestDisallowInterceptTouchEvent(true);
//
//                return false;
//
//            }
//        });




if(isReview == false) {
    radiobtn1 = (RadioButton) findViewById(R.id.option1RadioBox);
    radiobtn1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AnswerModel[] aModel = qResposne[currentQuestion].getAnswerModel();



            if (isPracticeMode==true)
            if (aModel[0].isCorrectFlag()) {
                //Toast.makeText(QuestionActivity.this, "correct Answer!!!", Toast.LENGTH_SHORT).show();
                showAlert(true,"");

            } else {
                //Toast.makeText(QuestionActivity.this, "Wrong Answer!!!", Toast.LENGTH_SHORT).show();
                showAlert(false,getCorrectAns());
            }

            checkRadioBtnStatus(1, currentQuestion);

        }
    });

    radiobtn2 = (RadioButton) findViewById(R.id.option2RadioBox);
    radiobtn2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AnswerModel[] aModel = qResposne[currentQuestion].getAnswerModel();

            if (isPracticeMode==true)
            if (aModel[1].isCorrectFlag()) {
                //Toast.makeText(QuestionActivity.this, "correct Answer!!!", Toast.LENGTH_SHORT).show();
                showAlert(true,"");

            } else {
                //Toast.makeText(QuestionActivity.this, "Wrong Answer!!!", Toast.LENGTH_SHORT).show();
                showAlert(false,getCorrectAns());
            }

            checkRadioBtnStatus(2, currentQuestion);
        }
    });

    radiobtn3 = (RadioButton) findViewById(R.id.option3RadioBox);
    radiobtn3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AnswerModel[] aModel = qResposne[currentQuestion].getAnswerModel();

            if (isPracticeMode==true)
            if (aModel[2].isCorrectFlag()) {
                //Toast.makeText(QuestionActivity.this, "correct Answer!!!", Toast.LENGTH_SHORT).show();
                showAlert(true, "");

            } else {
                //Toast.makeText(QuestionActivity.this, "Wrong Answer!!!", Toast.LENGTH_SHORT).show();
                showAlert(false, getCorrectAns());
            }

            checkRadioBtnStatus(3, currentQuestion);
        }
    });


    radiobtn4 = (RadioButton) findViewById(R.id.option4RadioBox);
    radiobtn4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AnswerModel[] aModel = qResposne[currentQuestion].getAnswerModel();

            if (isPracticeMode==true)
            if (aModel[3].isCorrectFlag()) {
                //Toast.makeText(QuestionActivity.this, "correct Answer!!!", Toast.LENGTH_SHORT).show();
                showAlert(true, "");

            } else {
               // Toast.makeText(QuestionActivity.this, "Wrong Answer!!!", Toast.LENGTH_SHORT).show();
                showAlert(false, getCorrectAns());
            }
            checkRadioBtnStatus(4, currentQuestion);

        }
    });
}


    }

//    public void LastQuestionDetected(int count,int position)
//    {
//        System.out.print("This is the Last question");
//
//        if(position+1==count){
//            isFinalQuestion = true;
//
//            endTest.setText("SUBMIT TEST");
//
//        }else {
//            isFinalQuestion = false;
//        }
//
//        ButtonFlat status= (ButtonFlat)findViewById(R.id.btnQuestionStatus);
//        status.setText(" " + (position+1) + "/" + count + " ");
//        totalListCount = count;
//    }


//    public void selectedAns(ArrayList<Integer> mainList, ArrayList<Integer> ansList)
//    {
//        userAns =mainList;
//        serverAns = ansList;
//    }


    public String getCorrectAns(){
        String correct_Ans = null;
        for(int i = 0 ; i<qResposne[currentQuestion].getAnswerModel().length ; i++)
        {
            AnswerModel[] aModel = qResposne[currentQuestion].getAnswerModel();
            if(aModel[i].isCorrectFlag()==true)
            {
                correct_Ans = aModel[i].getAnsPrefix();
            }
        }

        return correct_Ans;
    }

    public QuestionModel[] parse(String jsonLine)
    {
        System.out.println("\n\n\nIncoming response----->\n" + jsonLine);
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("questions");
        QuestionModel[] array = new QuestionModel[jarray.size()];


        for(int i=0 ; i < jarray.size();i++){
            JsonObject temp = jarray.get(i).getAsJsonObject();
            String testName = temp.get("name").getAsString();
            int QuestionID = temp.get("id").getAsInt();
            int testID = temp.get("test_id").getAsInt();

            JsonArray ansArray = temp.getAsJsonArray("answers");
            AnswerModel[] ansModel = new AnswerModel[ansArray.size()];

             for(int j = 0 ;  j < ansArray.size() ; j++)
             {
                JsonObject ansTemp = ansArray.get(j).getAsJsonObject();
                String name = ansTemp.get("name").getAsString();
                int id = ansTemp.get("id").getAsInt();
                boolean flag = ansTemp.get("is_correct").getAsBoolean();
                String prefix = ansTemp.get("answer_prefix").getAsString();
                int questionID = ansTemp.get("question_id").getAsInt();
                AnswerModel aModel = new AnswerModel(id,name,flag,prefix,questionID);
                ansModel[j] = aModel;
             }

            QuestionModel model = new QuestionModel(QuestionID,testName,testID,ansModel);
            array[i] = model;
            ansModel = null;

        }
        modelArray = array;
        return array;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void fillnext(int position)
    {

        if(position%3==0)
        {
            mInterstitialAd.show();
            requestNewInterstitial();
        }

        radiobtn1.setChecked(false);
        radiobtn2.setChecked(false);
        radiobtn3.setChecked(false);
        radiobtn4.setChecked(false);
        if(isReview == true)
        {
            reviewTestManagemanet(reviewAns.get(position), position);
            reviewTest(userAns.get(position));
        }

       if(position < qResposne.length && position >= 0 )
        if(qResposne !=null) {

            QuestionStatus.setText(" " + (position + 1) + "/" + qResposne.length);

            questiontext.setText(qResposne[position].getQuestionText());
            AnswerModel[] answerModel = qResposne[position].getAnswerModel();
            option1Text.setText(answerModel[0].getAnsText());
            option2Text.setText(answerModel[1].getAnsText());
            option3Text.setText(answerModel[2].getAnsText());
            option4Text.setText(answerModel[3].getAnsText());

            if(position == 0)
            {
                backBtn.setVisibility(Button.GONE);
            }else
            {
                backBtn.setVisibility(Button.VISIBLE);
            }

            if(position+1 == qResposne.length)
            {
                nextBtn.setVisibility(Button.GONE);
                endTest.setText("SUBMIT TEST");
            }
            else
            {
                nextBtn.setVisibility(Button.VISIBLE);
                endTest.setText("END TEST");
            }




        }
    }

    public void checkRadioBtnStatus(int pos, int index)
    {

        if(pos==1){
            radiobtn1.setChecked(true);
            radiobtn2.setChecked(false);
            radiobtn3.setChecked(false);
            radiobtn4.setChecked(false);
            try {

                seriesOfAnsers.set(index, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(pos == 2)
        {
            radiobtn1.setChecked(false);
            radiobtn2.setChecked(true);
            radiobtn3.setChecked(false);
            radiobtn4.setChecked(false);
            try {

                seriesOfAnsers.set(index, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(pos == 3){
            radiobtn1.setChecked(false);
            radiobtn2.setChecked(false);
            radiobtn3.setChecked(true);
            radiobtn4.setChecked(false);
            try {

                seriesOfAnsers.set(index, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(pos==4)
        {   radiobtn1.setChecked(false);
            radiobtn2.setChecked(false);
            radiobtn3.setChecked(false);
            radiobtn4.setChecked(true);
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
            rl1.setBackgroundColor(this.getResources().getColor(R.color.highLight_yello));
            rl2.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl3.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl4.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
        }
        else if(pos==2)
        {
            rl2.setBackgroundColor(this.getResources().getColor(R.color.highLight_yello));
            rl1.setBackgroundColor(this.getResources().getColor(R.color.faint_white));

            rl3.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl4.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
        }
        else if(pos==3)
        {
            rl3.setBackgroundColor(this.getResources().getColor(R.color.highLight_yello));
            rl1.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl2.setBackgroundColor(this.getResources().getColor(R.color.faint_white));

            rl4.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
        }
        else if(pos==4)
        {
            rl4.setBackgroundColor(this.getResources().getColor(R.color.highLight_yello));
            rl1.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl2.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl3.setBackgroundColor(this.getResources().getColor(R.color.faint_white));

        }
        else {

            rl1.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl2.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl3.setBackgroundColor(this.getResources().getColor(R.color.faint_white));
            rl4.setBackgroundColor(this.getResources().getColor(R.color.faint_white));

        }
    }


    public void reviewTest(int position)
    {
        radiobtn1.setChecked(false);
        radiobtn2.setChecked(false);
        radiobtn3.setChecked(false);
        radiobtn4.setChecked(false);


        System.out.print("checkbox " + position);

//        Toast.makeText(this, "checkbox -> " + position, Toast.LENGTH_SHORT).show();

        if(position == 1)
        {
            radiobtn1.setChecked(true);
            radiobtn1.setSelected(true);
        }
        else if(position == 2)
        {
            radiobtn2.setChecked(true);
            radiobtn2.setSelected(true);
        }
        else if(position == 3)
        {
            radiobtn3.setChecked(true);
            radiobtn3.setSelected(true);

        }else if(position == 4)
        {
            radiobtn4.setChecked(true);
            radiobtn4.setSelected(true);
        }


    }


    public void showAlert(boolean isCorrect, String correctAns) {

        if (isCorrect == true) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(QuestionActivity.this);
            builder1.setTitle("You are correct!!!");
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(QuestionActivity.this);
            builder1.setTitle("Wrong!!");
            builder1.setMessage("Correct answer is "+correctAns );
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(QuestionActivity.this, MainActivity.class));
        finish();

    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

}
