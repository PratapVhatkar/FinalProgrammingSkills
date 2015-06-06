package programminginterviews.vhatkar.pratap.com.programmingskills;

/**
 * Created by pratap on 30/5/15.
 */
public class TestHistorymodel {

    private int testid;
    private String name;
    private  boolean isPaid;
    private String price;
    private int user_id;
    private int technologyId;
    private int totalQuestion;
    private int userAttempt;
    private  String score;
    private String date;


    //

    TestHistorymodel(String myTest_name,int myTest_id,int myUserid,boolean Paid,String myPrice, int mytechnologyId,int attempt,int questioncount,String sco,String dat)
        {
            this.testid = myTest_id;
            this.technologyId = mytechnologyId;
            this.name = myTest_name;
            this.isPaid = Paid;
            this.price = myPrice;
            this.user_id = myUserid;
            this.userAttempt = attempt;
            this.totalQuestion = questioncount;
            this.score = sco;
            this.date = dat;
        }

        public String getDate(){return  this.date;}

        public int getTotalQuestion(){return  this.totalQuestion;}

        public int getUserAttempt() {return  this.userAttempt;}

        public int getTestid()
        {
            return this.testid;
        }

        public int getTechnologyId(){
            return this.technologyId;
        }

        public String getName()
        {
            return this.name;
        }

        public String getPrice()
        {
            return  this.price;
        }

        public int getUser_id()
        {
            return this.user_id;
        }

        public boolean isPaid()
        {
            return this.isPaid;
        }

        public String getScore(){return this.score;}

}
