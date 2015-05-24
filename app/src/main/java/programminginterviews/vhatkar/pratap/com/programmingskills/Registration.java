package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonRectangle;

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

                //fill params

                JSONObject params2 = new JSONObject();
                try
                {
                    JSONObject params = new JSONObject();
                    params.put("email", email.getText().toString());
                    params.put("password", password.getText().toString());
                    params.put("password_confirmation",confirmPassword.getText().toString());
                    params.put("name",useremail.getText().toString());
                    params2.put("user",params);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//                    queue.add(sr)

                System.out.print("Parameters--->" + params2.toString());

                //create future request object
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                //create JsonObjectRequest
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "http://testmyskills.herokuapp.com/api/v1/registrations.json", params2, null, null);
                //add request to volley
                queue.add(jsObjRequest);
                //pop request off when needed
                try {
                    JSONObject response = future.get();//blocking code
                    System.out.print("Request response" + response);


                } catch (Exception e) {
                    e.printStackTrace();
                }

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
