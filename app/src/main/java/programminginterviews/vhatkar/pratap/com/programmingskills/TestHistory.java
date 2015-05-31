package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class TestHistory extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);




        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restoredAuth = prefs.getString("auth", null);
        String restoredEmail = prefs.getString("email", null);

        String url ="http://testmyskills.herokuapp.com/api/v1/users/my_tests.json?" + "auth_token=" + restoredAuth+"&"+ "email="+restoredEmail;
//        http://localhost:3000/api/v1/tests/my_tests.json?
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
//                        ProgressBarCircularIndeterminate progressBar = (ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndeterminate);
//                        progressBar.setVisibility(View.INVISIBLE);
                        ListView listView = (ListView) findViewById(R.id.testList);
                        TestHistoryAdapter mAdapter = new TestHistoryAdapter(TestHistory.this, parse(response));
                        listView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.print("Errors--->"+error.toString());

            }
        });

        queue.add(stringRequest);
    }


    public TestHistorymodel[] parse(String jsonLine)
    {
        System.out.println("\n\n\nIncoming response----->\n" + jsonLine);
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("users");

        TestHistorymodel[] array = new TestHistorymodel[jarray.size()];

        for(int i=0 ; i < jarray.size();i++){

            JsonObject temp = jarray.get(i).getAsJsonObject();
            String testName = temp.get("name").getAsString();
            int test_id = temp.get("id").getAsInt();


            int user_id = 0;//temp.get("user_id").getAsInt();
            boolean isPaid = temp.get("is_paid").getAsBoolean();
            String price = temp.get("price").getAsString();
            int techId = temp.get("technology_id").getAsInt();
            int questionattempt = temp.get("total_questions").getAsInt();

            int user_attempt = temp.get("users_attempted").getAsInt();
            String score = "";
            if(!temp.get("test_result").isJsonNull()) {
                JsonObject test_result = temp.get("test_result").getAsJsonObject();

                if (test_result != null) {
                    score = test_result.get("score").getAsString();
                }
                else {
                    score = "Incomplete";
                }
            }

            TestHistorymodel model = new TestHistorymodel(testName,test_id,user_id,isPaid,price,techId,user_attempt,questionattempt,score);
            array[i] = model;
        }

        return array;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_test_history, menu);

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
