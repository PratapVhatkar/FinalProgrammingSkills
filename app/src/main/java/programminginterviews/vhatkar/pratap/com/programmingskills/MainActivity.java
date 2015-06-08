package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private boolean isVideoTutorial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isVideoTutorial", false);
        editor.commit();
    }

    public TechnologiesModel[] parse(String jsonLine) {

        System.out.println("\n\n\nIncoming response----->\n"+ jsonLine);
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("technologies");

        TechnologiesModel[] array = new TechnologiesModel[jarray.size()];

        for(int i=0 ; i < jarray.size();i++){

            JsonObject temp = jarray.get(i).getAsJsonObject();
            String str = temp.get("name").getAsString();
            int id = temp.get("id").getAsInt();
            String description = temp.get("description").getAsString();
            String iconname = temp.get("icon").getAsString();
            TechnologiesModel model = new TechnologiesModel(str,id,description,iconname);
            array[i] = model;
        }

        return array;
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();

        if(position == 0)
        {
//            Tracker tracker = ((GlobalState) getApplication()).getTracker(GlobalState.TrackerName.GLOBAL_TRACKER);
//            tracker.setScreenName("Home");
//            tracker.send(new HitBuilders.AppViewBuilder().build());

            setTitle("Home");
            DefaultSetup();
        }

        if(position==1)
        {

//
//            Intent intent = new Intent(MainActivity.this, TestHistory.class);
//            mNavigationDrawerFragment.startActivity(intent);
//            startActivity(intent);
//            finish();


//            Tracker tracker = ((GlobalState) getApplication()).getTracker(GlobalState.TrackerName.GLOBAL_TRACKER);
//            tracker.setScreenName("My Results");
//            tracker.send(new HitBuilders.AppViewBuilder().build());

            setTitle("My Results");
            setupTestHistory();


        }

        if(position == 2)
        {


//            Tracker tracker = ((GlobalState) getApplication()).getTracker(GlobalState.TrackerName.GLOBAL_TRACKER);
//            tracker.setScreenName("Video Tutorials");
//            tracker.send(new HitBuilders.AppViewBuilder().build());

            setTitle("Video Tutorials");
            isVideoTutorial = true;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isVideoTutorial",true);
            editor.commit();

            DefaultSetup();
          /*  Intent intent = new Intent(MainActivity.this, videotutorial.class);
            startActivity(intent);*/
        }
        else
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isVideoTutorial",false);
            editor.commit();
        }

        if(position == 3){

//            Tracker tracker = ((GlobalState) getApplication()).getTracker(GlobalState.TrackerName.GLOBAL_TRACKER);
//            tracker.setScreenName("Email");
//            tracker.send(new HitBuilders.AppViewBuilder().build());

            Intent mailer = new Intent(Intent.ACTION_SEND);
            mailer.setType("text/plain");
            mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback.orangebookstore@gmail.com"});
            mailer.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
//            mailer.putExtra(Intent.EXTRA_TEXT, "Write your message here..");
            startActivity(Intent.createChooser(mailer, "Send email..."));
        }
    }

    public void DefaultSetup()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restoredAuth = prefs.getString("auth", null);
        String restoredEmail = prefs.getString("email",null);

        //fill the listview
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://52.24.180.90/api/v1/technologies.json?"+"auth_token=" + restoredAuth+"&"+ "email="+restoredEmail;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ProgressBarCircularIndeterminate progressBar = (ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndeterminate);
                        progressBar.setVisibility(View.INVISIBLE);
                        System.out.println(response);
                        ListView listView = (ListView) findViewById(R.id.listView);
                        CustomAdapter mAdapter = new CustomAdapter(MainActivity.this, parse(response),isVideoTutorial);
                        listView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void setupTestHistory() {
        setTitle("My Results");
//        setIcon(R.mipmap.ic_launcher);

        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restoredAuth = prefs.getString("auth", null);
        String restoredEmail = prefs.getString("email", null);

        String url ="http://52.24.180.90/api/v1/users/my_results.json?" + "auth_token=" + restoredAuth+"&"+ "email="+restoredEmail;
//        http://localhost:3000/api/v1/tests/my_tests.json?
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
//                        ProgressBarCircularIndeterminate progressBar = (ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndeterminate);
//                        progressBar.setVisibility(View.INVISIBLE);
                        ListView listView = (ListView) findViewById(R.id.listView);
                        TestHistoryAdapter mAdapter = new TestHistoryAdapter(MainActivity.this, parseHistory(response));
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


    public TestHistorymodel[] parseHistory(String jsonLine)
    {
        System.out.println("\n\n\nIncoming response----->\n" + jsonLine);
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("users");

        TestHistorymodel[] array = new TestHistorymodel[jarray.size()];

        for(int i=0 ; i < jarray.size();i++){

            JsonObject temp = jarray.get(i).getAsJsonObject();
            String testName = "";
            int test_id = temp.get("id").getAsInt();


            int user_id = 0;//temp.get("user_id").getAsInt();

            String date = "";

            int user_attempt = 0;
            String score = temp.get("score").getAsString();
            String test_mode = temp.get("test_mode").getAsString();;

            boolean isPaid = false;
            String price = "";
            int techId = 0;
            int questionattempt = 0;

            date = temp.get("updated_at").getAsString();

            if(!temp.get("test").isJsonNull()) {
                JsonObject test_result = temp.get("test").getAsJsonObject();
                testName = test_result.get("name").getAsString();
                isPaid = test_result.get("is_paid").getAsBoolean();
                price = test_result.get("price").getAsString();
                techId = test_result.get("technology_id").getAsInt();
                questionattempt = test_result.get("total_questions").getAsInt();
                user_attempt =  test_result.get("users_attempted").getAsInt();
            }

            TestHistorymodel model = new TestHistorymodel(testName,test_id,user_id,isPaid,price,techId,user_attempt,questionattempt,score,date,test_mode);
            array[i] = model;
        }

        return array;
    }



    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
