package haifa.university.twitter_provider;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Created by yura on 24/02/2016.
 */
public class TweetEntities {
    @SerializedName("urls")
    public final List<UrlEntity> urls;
    @SerializedName("user_mentions")
    public final List<MentionEntity> userMentions;
    @SerializedName("media")
    public final List<MediaEntity> media;
    @SerializedName("hashtags")
    public final List<HashtagEntity> hashtags;

    public TweetEntities(List<UrlEntity> urls, List<MentionEntity> userMentions, List<MediaEntity> media, List<HashtagEntity> hashtags) {
        this.urls = this.getSafeList(urls);
        this.userMentions = this.getSafeList(userMentions);
        this.media = this.getSafeList(media);
        this.hashtags = this.getSafeList(hashtags);
    }

    private <T> List<T> getSafeList(List<T> entities) {
        return entities == null? Collections.EMPTY_LIST:Collections.unmodifiableList(entities);
    }
}
