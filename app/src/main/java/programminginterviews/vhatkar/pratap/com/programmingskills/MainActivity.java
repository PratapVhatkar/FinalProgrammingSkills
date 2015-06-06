package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isVideoTutorial", false);
        editor.commit();

     //   getSupportActionBar().setIcon(R.mipmap.ic_launcher);

//        //Get the Listview
//        String [] technologies = {"C","C++","Java","Objective C","Swift","Android","Database","TomCat","Javascript","HTML","CSS","Ruby on Rails","Jenkins"  };
//        ListView listView = (ListView) findViewById(R.id.listView);
//        ArrayList<String> itemArray = new ArrayList<String>();
//        for (String s : technologies) {
//            itemArray.add(s);
//        }
//        CustomAdapter mAdapter = new CustomAdapter(this, null);
//        listView.setAdapter(mAdapter);

        // Navigation Drawer Setup
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

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


        if(position==1)
        {
            Intent intent = new Intent(MainActivity.this, TestHistory.class);
            startActivity(intent);
//            finish();

        }

        if(position == 2)
        {

            isVideoTutorial = true;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isVideoTutorial",true);
            editor.commit();

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
            Intent mailer = new Intent(Intent.ACTION_SEND);
            mailer.setType("text/plain");
            mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{"name@email.com"});
            mailer.putExtra(Intent.EXTRA_SUBJECT, "subject");
            mailer.putExtra(Intent.EXTRA_TEXT, "bodytext");
            startActivity(Intent.createChooser(mailer, "Send email..."));
        }
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
