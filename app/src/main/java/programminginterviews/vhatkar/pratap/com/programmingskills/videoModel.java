package programminginterviews.vhatkar.pratap.com.programmingskills;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pratap on 4/6/15.
 */
public class videoModel
{

    private String url;
    private String name;
    private int id;

    videoModel(String url , String names , int vidid)
    {
        this.url = url;
        this.name = names;
        this.id = vidid;
    }

    public int getId(){return this.id;}

    public String getUrl()
    {
        return this.url;
    }

    public String getName()
    {
        return this.name;
    }

    public String getThumbnailurl()
    {
//        http://img.youtube.com/vi/VIDEO_ID/default.jpg
        String finalurl =  "http://img.youtube.com/vi/" + getYoutubeVideoId(this.url) + "/hqdefault.jpg";
        return finalurl;
    }



    public static String getYoutubeVideoId(String youtubeUrl)
    {
        String video_id="";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
        {

            String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches())
            {
                String groupIndex1 = matcher.group(7);
                if(groupIndex1!=null && groupIndex1.length()==11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }


}
