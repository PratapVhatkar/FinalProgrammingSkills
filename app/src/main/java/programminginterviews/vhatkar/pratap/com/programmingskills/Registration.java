package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request.Method;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        ButtonRectangle toRegistration = (ButtonRectangle) findViewById(R.id.btnRegistration);

        toRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText useremail = (EditText) findViewById(R.id.username);
                final EditText password = (EditText) findViewById(R.id.password);
                final EditText confirmPassword = (EditText) findViewById(R.id.confirmpassword);
                final EditText email = (EditText) findViewById(R.id.email);

                JSONObject js = new JSONObject();
                try {
                    JSONObject jsonobject_one = new JSONObject();
                    jsonobject_one.put("email", email.getText().toString());
                    jsonobject_one.put("password", password.getText().toString());
                    jsonobject_one.put("password_confirmation", confirmPassword.getText().toString());
                    jsonobject_one.put("name", useremail.getText().toString());
                    js.put("user", jsonobject_one);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue queue =  Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,"http://testmyskills.herokuapp.com/api/v1/registrations.json",js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response);
                                Toast.makeText(Registration.this, "Sucess", Toast.LENGTH_SHORT).show();

                                JsonElement jelement = new JsonParser().parse(response.toString());
                                JsonObject jobject = jelement.getAsJsonObject();

                                boolean flag =false;
                                try
                                {
                                    if(jobject.getAsJsonObject("user")==null)
                                    {
                                        flag = false;
                                    }
                                    else
                                    {
                                        flag = true;
                                    }
                                }
                                catch (Exception e)
                                {
                                    flag = false;
                                }


                                if(flag) {
                                    JsonObject userObject = jobject.getAsJsonObject("user").getAsJsonObject();
                                    int user_id = userObject.get("id").getAsInt();
                                    String role = userObject.get("role").getAsString();
                                    String auth = userObject.get("auth_token").getAsString();
                                    String name = userObject.get("name").getAsString();
                                    String email = userObject.get("email").getAsString();

                                    AppDelegate g = (AppDelegate)getApplication();
                                    g.setName(name);
                                    g.setUserAuth(auth);
                                    g.setUserid(user_id);
                                    g.setCurrentUserEmail(email);

//                                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//                                    editor.putString("email", email);
//                                    editor.putString("auth", auth);
//                                    editor.apply();


                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email",email);
                                    editor.putString("auth",auth);
                                    editor.commit();

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    String restoredAuth = prefs.getString("auth", null);
                                    String restoredEmail = prefs.getString("email",null);

                                    Intent intent = new Intent(Registration.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(intent);
                                }
                                else
                                {
//                                    JsonObject userObject = jobject.getAsJsonObject("user").getAsJsonObject();
                                    String str = jobject.get("message").getAsString();
                                    Toast.makeText(Registration.this,str, Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Registration.this, "Error ->" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                queue.add(jsObjRequest);

            }
        });
    }





    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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
