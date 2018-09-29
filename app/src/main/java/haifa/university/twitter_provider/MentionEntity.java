package haifa.university.twitter_provider;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura on 24/02/2016.
 */
public class MentionEntity extends Entity {
    @SerializedName("id")
    public final long id;
    @SerializedName("id_str")
    public final String idStr;
    @SerializedName("name")
    public final String name;
    @SerializedName("screen_name")
    public final String screenName;

    public MentionEntity(long id, String idStr, String name, String screenName, int start, int end) {
        super(start, end);
        this.id = id;
        this.idStr = idStr;
        this.name = name;
        this.screenName = screenName;
    }
}
