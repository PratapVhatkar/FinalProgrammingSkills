package programminginterviews.vhatkar.pratap.com.programmingskills;

/**
 * Created by pratap on 15/5/15.
 */
public class TestListModel {


    //{\"tests\":[{\"id\":1,\"name\":\"Test 1\",\"is_paid\":false,\"price\":0.0,\"user_id\":1,\"technology_id\":1}]}"
    private int testid;
    private String name;
    private  boolean isPaid;
    private String price;
    private int user_id;
    private int technologyId;
    private int questionAttept;
    private int userAttempt;
    private boolean is_user_purchased;


    TestListModel(String myTest_name,int myTest_id,int myUserid,boolean Paid,String myPrice, int mytechnologyId,int questionAttept,int userAttempt,boolean isPurchased)
    {
        this.testid = myTest_id;
        this.technologyId = mytechnologyId;
        this.name = myTest_name;
        this.isPaid = Paid;
        this.price = myPrice;
        this.user_id = myUserid;
        this.questionAttept = questionAttept;
        this.userAttempt = userAttempt;
        this.is_user_purchased = isPurchased;
    }

    public boolean Is_user_purchased()
    {
       return this.is_user_purchased;
    }


    public int getUserAttempt()
    {
        return this.userAttempt;
    }

    public int getQuestionAttept()
    {
        return this.questionAttept;
    }

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

}
