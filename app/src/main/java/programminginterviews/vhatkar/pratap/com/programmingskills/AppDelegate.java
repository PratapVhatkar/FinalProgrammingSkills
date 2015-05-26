package programminginterviews.vhatkar.pratap.com.programmingskills;

import android.app.Application;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by pratap on 25/5/15.
 */
public class AppDelegate extends Application {

    private JSONObject userdata;

    private int userid;

    private String userAuth;

    private String userrole;

    private String name;

    private String email;

    public int getCurrentUserID(){
        return this.userid;
    }

    public String getCurrentUserAuth(){
        return this.userAuth;
    }

    public String getCurrentUserRole()
    {
        return this.userrole;
    }

    public String getCurrentUserName()
    {
        return this.name;
    }

    public void setUserid(int i)
    {
        this.userid = i;
    }

    public void setUserAuth(String usera)
    {
        this.userAuth = usera;
    }

    public void setUserrole(String role)
    {
        this.userrole = role;
    }

    public void setName(String nameUser)
    {
        this.name = nameUser;
    }

    public String getCurrentUserEmail()
    {
        return this.email;
    }

    public void setCurrentUserEmail(String e)
    {
        this.email = e;
    }

    public JSONObject getUserdata()
    {
        return this.userdata;
    }

    public void setUserdata(JSONObject userd)
    {
        this.userdata = userd;
    }
}
