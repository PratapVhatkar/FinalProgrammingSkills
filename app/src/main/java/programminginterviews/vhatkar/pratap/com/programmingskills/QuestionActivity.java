package programminginterviews.vhatkar.pratap.com.programmingskills;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        listview = (HorizontialListView) findViewById(R.id.listview);
        listview.setScrollContainer(false);

        RequestQueue queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        int para = intent.getIntExtra("test_id", 0);
        
        String url ="http://testmyskills.herokuapp.com/api/v1/questions.json?test_id=" + para;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        QuestionAdapter mAdapter = new QuestionAdapter(QuestionActivity.this, parse(response));
                        listview.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.print("Errors--->"+error.toString());
            }
        });

        queue.add(stringRequest);

       //Nextbtn
       final Button nextBtn= (Button)findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                listview.post(new Runnable() {
                    public void run() {
                        int position = listview.getFirstVisiblePosition();
                        position = position + 1;
                        listview.setSelection(position);
                        Toast.makeText(QuestionActivity.this, "next -> " + position, Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

        //Backbtn
        final Button endTest= (Button)findViewById(R.id.btnEndtest);

        endTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                listview.post(new Runnable() {
                    public void run() {

                        String s = "End this test ?";
                        String ss = "all the progress will be losed";

                        final Dialog dialog = new Dialog(QuestionActivity.this,s,ss);
                        dialog.show();
                        moveTaskToBack(true);

                        ButtonFlat acceptButton = dialog.getButtonAccept();
                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                getApplicationContext().startActivity(intent);
                            }
                        });

                        ButtonFlat cancelButton = dialog.getButtonCancel();
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                });
            }

        });

        //End Test

        final Button backBtn= (Button)findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                listview.post(new Runnable() {
                    public void run() {
                        int position = listview.getFirstVisiblePosition();
                        position = position -1 ;
                        listview.setSelection(position);
                        Toast.makeText(QuestionActivity.this, "back -> " + position  , Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });



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
