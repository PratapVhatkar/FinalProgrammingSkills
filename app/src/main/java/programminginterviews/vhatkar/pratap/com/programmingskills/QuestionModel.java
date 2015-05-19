package programminginterviews.vhatkar.pratap.com.programmingskills;

/**
 * Created by pratap on 15/5/15.
 */
public class QuestionModel {

    //"{\"questions\":[{\"id\":1,\"name\":\"subscript exceeds the size of array?\",\"test_id\":1}]}"

    private int questionid;
    private  String questionText;
    private int testId;
    private AnswerModel[] answerModel;

    QuestionModel(int queId, String queText, int tstId , AnswerModel[] ans){
        this.questionid = queId;
        this.questionText = queText;
        this.testId = tstId;
        this.answerModel = ans;
    }

    public int getQuestionid()
    {
        return this.questionid;
    }

    public String getQuestionText()
    {
        return this.questionText;
    }

    public AnswerModel[] getAnswerModel(){

        return this.answerModel;
    }

}
