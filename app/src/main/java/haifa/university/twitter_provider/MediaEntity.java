package haifa.university.twitter_provider;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura on 24/02/2016.
 */
public class MediaEntity extends UrlEntity {
    @SerializedName("id")
    public final long id;
    @SerializedName("id_str")
    public final String idStr;
    @SerializedName("media_url")
    public final String mediaUrl;
    @SerializedName("media_url_https")
    public final String mediaUrlHttps;
    @SerializedName("sizes")
    public final MediaEntity.Sizes sizes;
    @SerializedName("source_status_id")
    public final long sourceStatusId;
    @SerializedName("source_status_id_str")
    public final String sourceStatusIdStr;
    @SerializedName("type")
    public final String type;

    public MediaEntity(String url, String expandedUrl, String displayUrl, int start, int end, long id, String idStr, String mediaUrl, String mediaUrlHttps, MediaEntity.Sizes sizes, long sourceStatusId, String sourceStatusIdStr, String type) {
        super(url, expandedUrl, displayUrl, start, end);
        this.id = id;
        this.idStr = idStr;
        this.mediaUrl = mediaUrl;
        this.mediaUrlHttps = mediaUrlHttps;
        this.sizes = sizes;
        this.sourceStatusId = sourceStatusId;
        this.sourceStatusIdStr = sourceStatusIdStr;
        this.type = type;
    }

    public static class Size {
        @SerializedName("w")
        public final int w;
        @SerializedName("h")
        public final int h;
        @SerializedName("resize")
        public final String resize;

        public Size(int w, int h, String resize) {
            this.w = w;
            this.h = h;
            this.resize = resize;
        }
    }

    public static class Sizes {
        @SerializedName("medium")
        public final MediaEntity.Size medium;
        @SerializedName("thumb")
        public final MediaEntity.Size thumb;
        @SerializedName("small")
        public final MediaEntity.Size small;
        @SerializedName("large")
        public final MediaEntity.Size large;

        public Sizes(MediaEntity.Size thumb, MediaEntity.Size small, MediaEntity.Size medium, MediaEntity.Size large) {
            this.thumb = thumb;
            this.small = small;
            this.medium = medium;
            this.large = large;
        }
    }
}