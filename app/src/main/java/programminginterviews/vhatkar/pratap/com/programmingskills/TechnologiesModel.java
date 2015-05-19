package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.graphics.drawable.Drawable;

/**
 * Created by pratap on 14/5/15.
 */
public class TechnologiesModel {

    private String technologyName;
    private int technologyID;
    private String getTechnologyDescription;


    public TechnologiesModel(String name,int id,String description)
    {
        this.technologyName = name;
        this.technologyID = id;
        this.getTechnologyDescription = description;
    }


    public String getTechnologyName(){
        return this.technologyName;
    }

    public int getTechnologyID(){
        return this.technologyID;
    }

    public String getGetTechnologyDescription(){
        return this.getTechnologyDescription;
    }
}
