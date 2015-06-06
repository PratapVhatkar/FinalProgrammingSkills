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


public class videotutorial extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videotutorial);

/*
        videoModel videoModel1 = new videoModel("https://www.youtube.com/watch?v=afNHG3jyPU4","Apple Developer Conference 2015");
        videoModel videoModel2 = new videoModel("https://www.youtube.com/watch?v=dyK9swyt154","Google IO 2015");

        videoModel[] array = new videoModel[2];
        array[0] = videoModel1;
        array[1] = videoModel2;


        ListView listView = (ListView) findViewById(R.id.list);

        videoAdapter mAdapter = new videoAdapter(getApplicationContext(), array);
        listView.setAdapter(mAdapter);


        */

        Intent intent = getIntent();
        int para = intent.getIntExtra("techid", 0);

        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restoredAuth = prefs.getString("auth", null);
        String restoredEmail = prefs.getString("email", null);

        //"http://localhost:3000/api/v1/videos.json?auth_token=#{user.auth_token}&email=#{user.email}&technology_id=1"
        String url ="http://52.24.180.90/api/v1/videos.json?technology_id="+para+"&auth_token=" + restoredAuth+"&"+ "email="+restoredEmail;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
//                        ProgressBarCircularIndeterminate progressBar = (ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndeterminate);
//                        progressBar.setVisibility(View.INVISIBLE);
                        ListView listView = (ListView) findViewById(R.id.list);
                        videoAdapter mAdapter = new videoAdapter(getApplicationContext(), parse(response));
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


    public videoModel[] parse(String jsonLine) {

        System.out.println("\n\n\nIncoming response----->\n" + jsonLine);
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("videos");

        videoModel[] array = new videoModel[jarray.size()];

        for(int i=0 ; i < jarray.size();i++){

            JsonObject temp = jarray.get(i).getAsJsonObject();
            String str = temp.get("name").getAsString();
            int id = temp.get("id").getAsInt();
            String url = temp.get("url").getAsString();
            videoModel model = new videoModel(url,str,id);
            array[i] = model;
        }

        return array;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_videotutorial, menu);
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
