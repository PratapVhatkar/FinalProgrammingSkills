package programminginterviews.vhatkar.pratap.com.programmingskills;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.carrier.CarrierMessagingService;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.Fragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import static com.android.volley.Request.*;
//implements
//ConnectionCallbacks, OnConnectionFailedListener,
// ResultCallback<LoadPeopleResult>, View.OnClickListener,
// CheckBox.OnCheckedChangeListener, GoogleApiClient.ServerAuthCodeCallback
//public class PreLogin extends ActionBarActivity i
public class PreLogin extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult>, View.OnClickListener,
        CheckBox.OnCheckedChangeListener, GoogleApiClient.ServerAuthCodeCallbacks,fbFragment.OnFragmentInteractionListener, FacebookCallback<LoginResult> {
    private static final String TAG = "android-plus-quickstart";
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;
    private static final String SAVED_PROGRESS = "sign_in_progress";
    private static final String WEB_CLIENT_ID = "1000424538924-1gru0kpmc9sdi0vt43g9ma1doqdmvcl1.apps.googleusercontent.com";
    private static final String SERVER_BASE_URL = "SERVER_BASE_URL";
    private static final String EXCHANGE_TOKEN_URL = SERVER_BASE_URL + "/exchangetoken";
    private static final String SELECT_SCOPES_URL = SERVER_BASE_URL + "/selectscopes";
    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    private int mSignInError;
    private boolean mRequestServerAuthCode = false;
    private boolean mServerHasToken = true;
    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;
    private ListView mCirclesListView;
    private ArrayAdapter<String> mCirclesAdapter;
    private ArrayList<String> mCirclesList;
    private CallbackManager callbackManager;
    private LoginButton mFbLoginButton;
    private boolean signedIn;
    private SharedPreferences sharedPref;
    private AccessTokenTracker accessTokenTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);

        if (checkAuthEmailExists()) {

            Intent intents = new Intent(PreLogin.this, MainActivity.class);
            startActivity(intents);
            finish();
//            finish();

        }
        else {
            setContentView(R.layout.activity_pre_login);
            updateWithToken(AccessToken.getCurrentAccessToken());
            ButtonFlat toRegistration = (ButtonFlat) findViewById(R.id.btnRegistration);
            toRegistration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//to registration
                    Intent intent = new Intent(PreLogin.this, Registration.class);
                    startActivity(intent);
//                    finish();
                }
            });
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "programminginterviews.vhatkar.pratap.com.programmingskills",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (NameNotFoundException e) {
            } catch (NoSuchAlgorithmException e) {
            }












        /*
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
// App code
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                Log.e("Token", accessToken.toString());
                signedIn = true;


                Profile profile = Profile.getCurrentProfile();







                Toast.makeText(PreLogin.this, "Welcome " + profile.getName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PreLogin.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancel() {
// App code
            }
            @Override
            public void onError(FacebookException exception) {
// App code
            }
            AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                    updateWithToken(newAccessToken);
                }
            };
        });



        */


            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

            loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));

            callbackManager = CallbackManager.Factory.create();

            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    // Application code
                                    Log.v("LoginActivity", response.toString());
                                    String email = null;
                                    String user_id = null;
                                    String name = null;
                                    try {
                                        email = object.getString("email");
                                        user_id = object.getString("id");
                                        name = object.getString("name");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        socialNetworkRequest(email, "facebook", user_id, name);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Toast.makeText(PreLogin.this, "Welcome" + name, Toast.LENGTH_SHORT).show();

//                                JsonParser parser = new JsonParser();
//                                JsonObject rootObj = parser.parse(object.toString()).getAsJsonObject();
////
////                                JsonElement jelement = new JsonParser().parse(response.toString());
////                                JsonObject jobject = jelement.getAsJsonObject();
//
//                                JsonObject userObject2 = rootObj.getAsJsonObject("Response").getAsJsonObject();
//                                JsonObject userObject = userObject2.getAsJsonObject("graphObject").getAsJsonObject();
//
//                                String user_id = userObject.get("id").getAsString();
//                                String auth = AccessToken.getCurrentAccessToken().toString();
//                                String name = userObject.get("name").getAsString();
//                                String email = userObject.get("email").getAsString();
//
//                                JSONObject js = new JSONObject();



/*
//                                provider: 'facebook', uid: '12457122', name: 'somename', email: 'someemail@test.com'
                                JSONObject jsonobject_one = new JSONObject();
                                try {
                                    jsonobject_one.put("email", email);
                                    jsonobject_one.put("name", name);
                                    jsonobject_one.put("id", user_id);
                                    jsonobject_one.put("auth", AccessToken.getCurrentAccessToken().toString());
                                    js.put("user", jsonobject_one);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "http://testmyskills.herokuapp.com/api/v1/registrations/social_registration", js,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println(response);
                                                Toast.makeText(PreLogin.this, "Sucess", Toast.LENGTH_SHORT).show();

                                                JsonElement jelement = new JsonParser().parse(response.toString());
                                                JsonObject jobject = jelement.getAsJsonObject();

                                                boolean flag = false;
                                                try {
                                                    if (jobject.getAsJsonObject("user") == null) {
                                                        flag = false;
                                                    } else {
                                                        flag = true;
                                                    }
                                                } catch (Exception e) {
                                                    flag = false;
                                                }


                                                if (flag) {
                                                    JsonObject userObject = jobject.getAsJsonObject("user").getAsJsonObject();
                                                    int user_id = userObject.get("id").getAsInt();
                                                    String role = userObject.get("role").getAsString();
                                                    String auth = userObject.get("auth_token").getAsString();
                                                    String name = userObject.get("name").getAsString();
                                                    String email = userObject.get("email").getAsString();

                                                    AppDelegate g = (AppDelegate) getApplication();
                                                    g.setName(name);
                                                    g.setUserAuth(auth);
                                                    g.setUserid(user_id);
                                                    g.setCurrentUserEmail(email);

                                                    Intent intent = new Intent(PreLogin.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    getApplicationContext().startActivity(intent);
                                                } else {
//                                    JsonObject userObject = jobject.getAsJsonObject("user").getAsJsonObject();
                                                    String str = jobject.get("message").getAsString();
                                                    Toast.makeText(PreLogin.this, str, Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(PreLogin.this, "Error ->" + error.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                                queue.add(jsObjRequest);

                                Toast.makeText(PreLogin.this, "Facebook -> " + response.toString(), Toast.LENGTH_SHORT).show();*/
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();


                }

                @Override
                public void onCancel() {
                    // App code
                    Log.v("LoginActivity", "cancel");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.v("LoginActivity", exception.getCause().toString());
                }
            });


            ButtonRectangle btn = (ButtonRectangle) findViewById(R.id.userLogin);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final EditText useremail = (EditText) findViewById(R.id.passwordtextfield);
                    final EditText email = (EditText) findViewById(R.id.username);

                    JSONObject js = new JSONObject();
                    try {
                        JSONObject jsonobject_one = new JSONObject();
                        jsonobject_one.put("email", email.getText().toString());
                        jsonobject_one.put("password", useremail.getText().toString());
                        js.put("user", jsonobject_one);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "http://52.24.180.90/api/v1/sessions.json", js,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println(response);
                                    Toast.makeText(PreLogin.this, "Sucess", Toast.LENGTH_SHORT).show();

                                    JsonElement jelement = new JsonParser().parse(response.toString());
                                    JsonObject jobject = jelement.getAsJsonObject();

                                    boolean flag = false;
                                    try {
                                        if (jobject.getAsJsonObject("user") == null) {
                                            flag = false;
                                        } else {
                                            flag = true;
                                        }
                                    } catch (Exception e) {
                                        flag = false;
                                    }


                                    if (flag) {
                                        JsonObject userObject = jobject.getAsJsonObject("user").getAsJsonObject();
                                        int user_id = userObject.get("id").getAsInt();
                                        String role = userObject.get("role").getAsString();
                                        String auth = userObject.get("auth_token").getAsString();
                                        String name = userObject.get("name").getAsString();
                                        String email = userObject.get("email").getAsString();

                                        AppDelegate g = (AppDelegate) getApplication();
                                        g.setName(name);
                                        g.setUserAuth(auth);
                                        g.setUserid(user_id);
                                        g.setCurrentUserEmail(email);
                                        saveAuthEmail(auth, email);

                                        Intent intent = new Intent(PreLogin.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getApplicationContext().startActivity(intent);
                                        finish();
                                    } else {
//                                    JsonObject userObject = jobject.getAsJsonObject("user").getAsJsonObject();
                                        String str = jobject.get("message").getAsString();
                                        Toast.makeText(PreLogin.this, str, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(PreLogin.this, "Error ->" + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                    queue.add(jsObjRequest);

                }
            });


            mGoogleApiClient = buildGoogleApiClient();
            mGoogleApiClient.connect();


            mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);

            mSignInButton.setOnClickListener(this);
            if (savedInstanceState != null) {
                mSignInProgress = savedInstanceState
                        .getInt(SAVED_PROGRESS, STATE_DEFAULT);
                Intent intent = new Intent(PreLogin.this, MainActivity.class);
                startActivity(intent);
                finish();
            }


            this.findViewById(R.id.sign_in_button).setOnClickListener(this);
        }
        }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(PreLogin.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 0);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 0);
        }
    }

    private GoogleApiClient buildGoogleApiClient() {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);
        if (mRequestServerAuthCode) {
            checkServerAuthConfiguration();
            builder = builder.requestServerAuthCode(WEB_CLIENT_ID, this);
        }
        return builder.build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "onConnected");
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
//
//        Intent intent = new Intent(PreLogin.this, MainActivity.class);
//        startActivity(intent);
//        finish();


// Intent intent = new Intent(PreLogin.this, MainActivity.class);
// startActivity(intent);
// finish();
        mSignInButton.setEnabled(false);
// Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
// System.out.print("Person data --->" + currentUser.getObjectType());
//// System.out.print("Name -->"+ currentUser.getName() + " " + currentUser.getDisplayName() );
// // Toast.makeText(PreLogin.this, "Welcome " + currentUser.getName(), Toast.LENGTH_LONG).show();
// Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
// .setResultCallback(this);
        mSignInProgress = STATE_DEFAULT;
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
// After that fetch data
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi
                    .getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();


            currentPerson.getId();

            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);

            String providername = "Google";
            String id = currentPerson.getId();
            String name = currentPerson.getDisplayName();

//            socialNetworkRequest(accountName,providername,id,name);

            try {
                socialNetworkRequest(accountName, providername, id, name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            { provider: 'facebook', uid: '12457122', name: 'somename', email: 'someemail@test.com' }

            Log.i("personName", personName);
// Intent intent = new Intent(PreLogin.this, MainActivity.class);
// startActivity(intent);
// finish();
        }
    }


    public void socialNetworkRequest(String email, String provider, String id, String name) throws JSONException {

//        - { provider: 'facebook', uid: '12457122', name: 'somename', email: 'someemail@test.com' }


//        JsonObject js = new JsonObject();
//        js.add("provider", provider);
//        js.add("uid", id);
//        js.add("name", name);
//        js.add("email", email);

        JSONObject js = new JSONObject();
        js.put("provider", provider);
        js.put("uid", id);
        js.put("name", name);
        js.put("email", email);


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "http://52.24.180.90/api/v1/registrations/social_registration.json", js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);


                        JsonElement jelement = new JsonParser().parse(response.toString());
                        JsonObject jobject = jelement.getAsJsonObject();


                        Intent intent1 = new Intent(PreLogin.this, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent1);
                        finish();

                        boolean flag = false;
                        try {
                            if (jobject.getAsJsonObject("user") == null) {
                                flag = false;
                            } else {
                                flag = true;
                            }
                        } catch (Exception e) {
                            flag = false;
                        }


                        if (flag) {
                            JsonObject userObject = jobject.getAsJsonObject("user").getAsJsonObject();
                            int user_id = userObject.get("id").getAsInt();
                            String role = userObject.get("role").getAsString();
                            String auth = userObject.get("auth_token").getAsString();
                            String name = userObject.get("name").getAsString();
                            String email = userObject.get("email").getAsString();

                            AppDelegate g = (AppDelegate) getApplication();
                            g.setName(name);
                            g.setUserAuth(auth);
                            g.setUserid(user_id);
                            g.setCurrentUserEmail(email);

                            saveAuthEmail(auth,email);

                            Intent intent = new Intent(PreLogin.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                            finish();

                            Toast.makeText(PreLogin.this,userObject.toString() , Toast.LENGTH_SHORT).show();

                        } else {
//                                    JsonObject userObject = jobject.getAsJsonObject("user").getAsJsonObject();
                            String str = jobject.get("message").getAsString();
                            Toast.makeText(PreLogin.this, str, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PreLogin.this, "Error ->" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(jsObjRequest);



}


    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        System.out.print(" People Data --->" + peopleData.toString());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pre_login, menu);
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
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            Log.w(TAG, "API Unavailable.");
            System.out.print("API Unavaiable");
        } else if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();
            if (mSignInProgress == STATE_SIGN_IN) {
                System.out.print("In progress");
                resolveSignInError();
            }
        }
    }
    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            createErrorDialog().show();
        }
    }
    public Dialog createErrorDialog() {
        if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    mSignInError,
                    this,
                    RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.e(TAG, "Google Play services resolution cancelled");
                            mSignInProgress = STATE_DEFAULT;
                            mStatus.setText(R.string.status_signed_out);
                        }
                    });
        } else {
            return new AlertDialog.Builder(this)
                    .setMessage(R.string.play_services_error)
                    .setPositiveButton(R.string.close,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e(TAG, "Google Play services error could not be "
                                            + "resolved: " + mSignInError);
                                    mSignInProgress = STATE_DEFAULT;
// mStatus.setText(R.string.status_signed_out);
                                }
                            }).create();
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }
    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting())
        {
            switch (v.getId()) {
                case R.id.sign_in_button:
                    mSignInProgress = STATE_SIGN_IN;
                    mGoogleApiClient.connect();
            }
        }
    }
    @Override
    public void onSuccess(LoginResult loginResults) {
        AccessToken.getCurrentAccessToken();
        System.out.print("Sucess...");
        Intent intent = new Intent(PreLogin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
// If the error resolution was successful we should continue
// processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
// If the error resolution was not successful or the user canceled,
// we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
                }
                if (!mGoogleApiClient.isConnecting()) {
// If Google Play services resolved the issue with a dialog then
// onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
//manage login result
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public CheckResult onCheckServerAuthorization(String idToken, Set<Scope> scopeSet) {
        Log.i(TAG, "Checking if server is authorized.");
        Log.i(TAG, "Mocking server has refresh token: " + String.valueOf(mServerHasToken));
        if (!mServerHasToken) {
// Server does not have a valid refresh token, so request a new
// auth code which can be exchanged for one. This will cause the user to see the
// consent dialog and be prompted to grant offline access. This callback occurs on a
// background thread so it is OK to do synchronous network access.
// Ask the server which scopes it would like to have for offline access. This
// can be distinct from the scopes granted to the client. By getting these values
// from the server, you can change your server's permissions without needing to
// recompile the client application.
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(SELECT_SCOPES_URL);
            HashSet<Scope> serverScopeSet = new HashSet<Scope>();
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int responseCode = httpResponse.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                if (responseCode == 200) {
                    String[] scopeStrings = responseBody.split(" ");
                    for (String scope : scopeStrings) {
                        Log.i(TAG, "Server Scope: " + scope);
                        serverScopeSet.add(new Scope(scope));
                    }
                } else {
                    Log.e(TAG, "Error in getting server scopes: " + responseCode);
                }
            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error in getting server scopes.", e);
            } catch (IOException e) {
                Log.e(TAG, "Error in getting server scopes.", e);
            }
// This tells GoogleApiClient that the server needs a new serverAuthCode with
// access to the scopes in serverScopeSet. Note that we are not asking the server
// if it already has such a token because this is a sample application. In reality,
// you should only do this on the first user sign-in or if the server loses or deletes
// the refresh token.
            return CheckResult.newAuthRequiredResult(serverScopeSet);
        } else {
// Server already has a valid refresh token with the correct scopes, no need to
// ask the user for offline access again.
            return CheckResult.newAuthNotRequiredResult();
        }
    }
    @Override
    public boolean onUploadServerAuthCode(String idToken, String serverAuthCode) {
// Upload the serverAuthCode to the server, which will attempt to exchange it for
// a refresh token. This callback occurs on a background thread, so it is OK
// to perform synchronous network access. Returning 'false' will fail the
// GoogleApiClient.connect() call so if you would like the client to ignore
// server failures, always return true.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(EXCHANGE_TOKEN_URL);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("serverAuthCode", serverAuthCode));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());
            Log.i(TAG, "Code: " + statusCode);
            Log.i(TAG, "Resp: " + responseBody);
// Show Toast on UI Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(PreLogin.this, responseBody, Toast.LENGTH_LONG).show();
                }
            });
            return (statusCode == 200);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error in auth code exchange.", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Error in auth code exchange.", e);
            return false;
        }
    }
    private void checkServerAuthConfiguration() {
// Check that the server URL is configured before allowing this box to
// be unchecked
        if ("WEB_CLIENT_ID".equals(WEB_CLIENT_ID) ||
                "SERVER_BASE_URL".equals(SERVER_BASE_URL)) {
            Log.w(TAG, "WEB_CLIENT_ID or SERVER_BASE_URL configured incorrectly.");
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.configuration_error))
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        }
    }
    @Override
    public void onCancel() {
    }
    @Override
    public void onError(FacebookException e) {
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }


    public void saveAuthEmail(String auth,String email)
    {
        if(!checkAuthEmailExists())
        {
            //save

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email",email);
            editor.putString("auth",auth);
            editor.commit();
//            editor.apply();
        }

    }

    boolean checkAuthEmailExists() {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restoredAuth = prefs.getString("auth", null);
        String restoredEmail = prefs.getString("email",null);

        if(restoredAuth == null)
            return  false;

        if(restoredEmail == null)
            return false;

        return true;

    }


}