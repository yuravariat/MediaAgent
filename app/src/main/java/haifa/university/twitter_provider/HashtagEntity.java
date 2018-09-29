package haifa.university.twitter_provider;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura on 24/02/2016.
 */
public class HashtagEntity extends Entity {
    @SerializedName("text")
    public final String text;

    public HashtagEntity(String text, int start, int end) {
        super(start, end);
        this.text = text;
    }
}