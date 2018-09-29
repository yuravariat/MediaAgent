package haifa.university.twitter_provider;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura on 24/02/2016.
 */
public class UrlEntity extends Entity {
    @SerializedName("url")
    public final String url;
    @SerializedName("expanded_url")
    public final String expandedUrl;
    @SerializedName("display_url")
    public final String displayUrl;

    public UrlEntity(String url, String expandedUrl, String displayUrl, int start, int end) {
        super(start, end);
        this.url = url;
        this.expandedUrl = expandedUrl;
        this.displayUrl = displayUrl;
    }
}
