package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.android.gms.games.quest.Quest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


public class StartTestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        Intent intent = getIntent();
        final int para = intent.getIntExtra("test_id", 0);

        final Button backBtn= (Button)findViewById(R.id.startId);
        final ProgressBarCircularIndeterminate progressBar = (ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndeterminate);
        progressBar.setVisibility(View.INVISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String restoredAuth = prefs.getString("auth", null);
                String restoredEmail = prefs.getString("email", null);

                //fill the listview
                RequestQueue queue = Volley.newRequestQueue(StartTestActivity.this);
//                String url ="http://testmyskills.herokuapp.com/api/v1/tests/create_my_test.json?"+"test_id="+para +"&auth_token=" + restoredAuth+"&"+ "email="+restoredEmail;

                JSONObject js = new JSONObject();
                JSONObject jsonobject_one = new JSONObject();
                try {
                    jsonobject_one.put("email", restoredEmail);
                    jsonobject_one.put("auth_token",restoredAuth);
                    jsonobject_one.put("test_id",para);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,"http://testmyskills.herokuapp.com/api/v1/users/create_my_test.json",jsonobject_one,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(StartTestActivity.this, "Response ->" + response.toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StartTestActivity.this, QuestionActivity.class);
                                intent.putExtra("isReview",false);
                                intent.putExtra("test_id",para);
                                startActivity(intent);
                                finish();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(StartTestActivity.this, "Error ->" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                queue.add(jsObjRequest);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_test, menu);
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
