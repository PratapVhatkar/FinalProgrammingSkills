package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFlat;

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

        int percen = intent.getIntExtra("percentage",0);

        TextView score = (TextView)findViewById(R.id.percentageLabel);
        score.setText(calculateResult(userAns,serverAns)+"%");






        //Review Test

        ButtonFlat reviewTest = (ButtonFlat)findViewById(R.id.reviewBtn);

        reviewTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                intent.putExtra("test_id",testid);
                intent.putExtra("serverAns",serverAns);
                intent.putExtra("userans",userAns);
                intent.putExtra("isReview",true);
                startActivity(intent);
            }
        });

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
