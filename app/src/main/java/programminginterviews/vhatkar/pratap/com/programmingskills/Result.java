package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFlat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Result extends ActionBarActivity {


    ArrayList<Integer> serverAns = new ArrayList<Integer>();
    ArrayList<Integer> userAns = new ArrayList<Integer>();

    private  int testid;
    TextView textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Get values

        Intent intent = getIntent();
        userAns = intent.getIntegerArrayListExtra("userans");
        serverAns = intent.getIntegerArrayListExtra("serverAns");
        testid =  intent.getIntExtra("test_id", 0);

        final String modelString = intent.getStringExtra("savedResponse");

        int percen = intent.getIntExtra("percentage",0);
        TextView score = (TextView)findViewById(R.id.percentageLabel);
        score.setText(calculateResult(userAns,serverAns)+"%");


        ButtonFlat home = (ButtonFlat)findViewById(R.id.toHome);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        //Review Test

        ButtonFlat reviewTest = (ButtonFlat)findViewById(R.id.reviewBtn);

        reviewTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra("test_id",testid);
                intent.putExtra("serverAns",serverAns);
                intent.putExtra("userans",userAns);
                intent.putExtra("isReview",true);
                intent.putExtra("savedResponse",modelString);
                startActivity(intent);
            }
        });

        SendResult(calculateResult(userAns,serverAns),testid);
    }

    public void SendResult(int percentage,int id )
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restoredAuth = prefs.getString("auth", null);
        String restoredEmail = prefs.getString("email", null);

        //fill the listview
        RequestQueue queue = Volley.newRequestQueue(Result.this);
//                String url ="http://testmyskills.herokuapp.com/api/v1/tests/create_my_test.json?"+"test_id="+para +"&auth_token=" + restoredAuth+"&"+ "email="+restoredEmail;

        JSONObject js = new JSONObject();
        JSONObject jsonobject_one = new JSONObject();
        try {
            jsonobject_one.put("email", restoredEmail);
            jsonobject_one.put("auth_token",restoredAuth);
            jsonobject_one.put("test_id",id);
            jsonobject_one.put("score",percentage);
            jsonobject_one.put("completed",true);
            //  { auth_token: user.auth_token, email: user.email, test_id: 2, score: 83, result: 'Passed', completed: true }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,"http://52.24.180.90/api/v1/users/save_test_results.json",jsonobject_one,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
//                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Result.this, "Response ->" + response.toString(), Toast.LENGTH_SHORT).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Result.this, "Error ->" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jsObjRequest);
    }


    public int calculateResult(ArrayList<Integer> userAnsLists,ArrayList<Integer> serverAnss)
    {

        int totalQuestions = serverAnss.size();

        int totalCorrect = 0;

        for(int i = 0 ;i < serverAnss.size() ; i++)
        {
            if(serverAnss.get(i) == userAnsLists.get(i))
            {
                totalCorrect = totalCorrect + 1;
            }
        }

        textStatus = (TextView)findViewById(R.id.ansTextStatus);
        textStatus.setText(totalCorrect +" are correct out of "+ totalQuestions);

        int x = (int)(((double)totalCorrect/(double)totalQuestions) * 100);

        return x;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
