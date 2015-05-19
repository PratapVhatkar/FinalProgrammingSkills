package programminginterviews.vhatkar.pratap.com.programmingskills;

/**
 * Created by pratap on 16/5/15.
 */
public class AnswerModel {


    private int ansId;
    private String ansText;
    private boolean isCorrect;
    private String ansPrefix;
    private int queId;

    AnswerModel(int id,String name, boolean flag, String prefix, int questionId)
    {
        this.ansId = id;
        this.ansText = name;
        this.isCorrect = flag;
        this.ansPrefix = prefix;
        this.queId = questionId;
    }


    public int getAnsId()
    {
        return this.ansId;
    }

    public String getAnsText()
    {
        return this.ansText;
    }

    public boolean isCorrectFlag()
    {
        return this.isCorrect;
    }

    public String getAnsPrefix()
    {
        return this.getAnsPrefix();
    }

    public int getQueId()
    {
        return this.getQueId();
    }
}
