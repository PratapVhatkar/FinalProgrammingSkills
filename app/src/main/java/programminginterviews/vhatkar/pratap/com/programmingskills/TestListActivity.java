package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;


public class TestListActivity extends Activity implements BillingProcessor.IBillingHandler {

//    //paypal setup
//    private static PayPalConfiguration config = new PayPalConfiguration()
//
//            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
//            // or live (ENVIRONMENT_PRODUCTION)
//            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
//            .clientId("AcsT1CFUhWufw4rNHkE1zFgu8-2L6IOaev82uqhLhLNgjm2t5A3trpML5wCptRpnfJ8YM5vBlgf1EzJH");


    private BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);


        bp = new BillingProcessor(TestListActivity.this, getString(R.string.in_app_billing_key)  ,TestListActivity.this);

        Intent intent = getIntent();
        int para = intent.getIntExtra("techid", 0);

        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String restoredAuth = prefs.getString("auth", null);
        String restoredEmail = prefs.getString("email", null);

        String url ="http://52.24.180.90/api/v1/tests.json?technology_id="+para+"&auth_token=" + restoredAuth+"&"+ "email="+restoredEmail;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
                        ProgressBarCircularIndeterminate progressBar = (ProgressBarCircularIndeterminate)findViewById(R.id.progressBarCircularIndeterminate);
                        progressBar.setVisibility(View.INVISIBLE);
                        ListView listView = (ListView) findViewById(R.id.testList);
                        CustomTestListAdapter mAdapter = new CustomTestListAdapter(TestListActivity.this, parse(response));
                        listView.setAdapter(mAdapter);

                        parse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.print("Errors--->"+error.toString());

            }
        });

        queue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }



    public TestListModel[] parse(String jsonLine)
    {
        System.out.println("\n\n\nIncoming response----->\n" + jsonLine);
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("tests");

        TestListModel[] array = new TestListModel[jarray.size()];

        for(int i=0 ; i < jarray.size();i++){

            JsonObject temp = jarray.get(i).getAsJsonObject();
            String testName = temp.get("name").getAsString();
            int test_id = temp.get("id").getAsInt();
            int questionattempt = temp.get("total_questions").getAsInt();
            int user_attempt = temp.get("users_attempted").getAsInt();
            boolean isPurchased = temp.get("is_purchased_by_user").getAsBoolean();

            int user_id = 0;//temp.get("user_id").getAsInt();
            boolean isPaid = temp.get("is_paid").getAsBoolean();
            String price = temp.get("price").getAsString();
            int techId = temp.get("technology_id").getAsInt();

            TestListModel model = new TestListModel(testName,test_id,user_id,isPaid,price,techId,questionattempt,user_attempt,isPurchased);
            array[i] = model;
        }

        return array;
    }

    public void onBuyPressed() {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

//        PayPalPayment payment = new PayPalPayment(new BigDecimal(".5"), "USD", "JAVA Test 1",
//                PayPalPayment.PAYMENT_INTENT_SALE);
//
//        Intent intent = new Intent(this, PaymentActivity.class);
//
//        // send the same configuration for restart resiliency
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//
//        startActivityForResult(intent, 0);

        bp.purchase(TestListActivity.this, "android.test.purchased");
        bp.consumePurchase("android.test.purchased");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_list, menu);
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
    public void onProductPurchased(String s, TransactionDetails transactionDetails) {

        System.out.print("Transaction details-->" +transactionDetails + "some string" + s  );
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int i, Throwable throwable) {

    }

    @Override
    public void onBillingInitialized() {

    }
}
