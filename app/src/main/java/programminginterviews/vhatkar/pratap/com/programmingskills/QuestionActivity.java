package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RadioButton;
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
    private int para;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Backbtn
        endTest = (ButtonFlat) findViewById(R.id.btnEndtest);
        backBtn = (ButtonFlat) findViewById(R.id.backBtn);
        nextBtn = (ButtonFlat) findViewById(R.id.nextBtn);
        resultsScreenAns = null;
        userAnsList = null;

        listview = (HorizontialListView) findViewById(R.id.listview);
        listview.setScrollContainer(false);

        RequestQueue queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        para = intent.getIntExtra("test_id", 0);

        String url = "http://testmyskills.herokuapp.com/api/v1/questions.json?test_id=" + para;

        reviewAns = intent.getIntegerArrayListExtra("serverAns");
        userAns = intent.getIntegerArrayListExtra("userAns");
        final boolean isReview = getIntent().getExtras().getBoolean("isReview");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        mAdapter = new QuestionAdapter(QuestionActivity.this, parse(response), reviewAns, isReview, userAns);
                        listview.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.print("Errors--->" + error.toString());
            }
        });

        queue.add(stringRequest);

        //Nextbtn

        nextBtn = (ButtonFlat) findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                listview.post(new Runnable() {
                    public void run() {

                        if (listview.getFirstVisiblePosition() + 1 == totalListCount) {
                            //Last position
                            nextBtn.setVisibility(Button.GONE);
                            backBtn.setVisibility(Button.VISIBLE);
//                            submitButton.setVisibility(Button.VISIBLE);

                        } else {
                            nextBtn.setVisibility(Button.VISIBLE);
                            backBtn.setVisibility(Button.VISIBLE);
                            int position = listview.getFirstVisiblePosition();
                            position = position + 1;
                            listview.setSelection(position);
                            mAdapter.notifyDataSetChanged();

                        }

                    }
                });
            }

        });


        //End Test


        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                listview.post(new Runnable() {
                    public void run() {

                        if (listview.getFirstVisiblePosition() == 0) {
                            //Last position
                            backBtn.setVisibility(Button.GONE);

                        } else {

                            nextBtn.setVisibility(Button.VISIBLE);
                            int position = listview.getFirstVisiblePosition();
                            position = position - 1;

                            if (position == 0) {
                                backBtn.setVisibility(Button.GONE);
                            }

                            listview.setSelection(position);
                            mAdapter.notifyDataSetChanged();
                        }

//                        Toast.makeText(QuestionActivity.this, "back -> " + position  , Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });


        endTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                listview.post(new Runnable() {
                    public void run() {


                        String s = "End this test ?";
                        String ss = "all the progress will be losed";

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(QuestionActivity.this);
                        builder1.setMessage("End this test ?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(QuestionActivity.this, Result.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("userans", userAns);
                                        intent.putExtra("serverAns", serverAns);
                                        intent.putExtra("test_id", para);
                                        getApplicationContext().startActivity(intent);
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
            }

        });



        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listview.requestDisallowInterceptTouchEvent(true);

                return false;

            }
        });


    }

    public void LastQuestionDetected(int count,int position)
    {
        System.out.print("This is the Last question");

        if(position+1==count){
            isFinalQuestion = true;

            endTest.setText("SUBMIT TEST");

        }else {
            isFinalQuestion = false;
        }

        ButtonFlat status= (ButtonFlat)findViewById(R.id.btnQuestionStatus);
        status.setText(" " + (position+1) + "/" + count + " ");
        totalListCount = count;
    }


    public void selectedAns(ArrayList<Integer> mainList, ArrayList<Integer> ansList)
    {
        userAns =mainList;
        serverAns = ansList;
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



}
