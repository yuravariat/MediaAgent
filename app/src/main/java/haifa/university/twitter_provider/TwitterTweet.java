package haifa.university.twitter_provider;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura on 24/02/2016.
 */
public class TwitterTweet {
    public static final long INVALID_ID = -1L;
    @SerializedName("coordinates")
    public final Coordinates coordinates;
    @SerializedName("created_at")
    public final String createdAt;
    @SerializedName("current_user_retweet")
    public final Object currentUserRetweet;
    @SerializedName("entities")
    public final TweetEntities entities;
    @SerializedName("favorite_count")
    public final Integer favoriteCount;
    @SerializedName("favorited")
    public final boolean favorited;
    @SerializedName("filter_level")
    public final String filterLevel;
    @SerializedName("id")
    public final long id;
    @SerializedName("id_str")
    public final String idStr;
    @SerializedName("in_reply_to_screen_name")
    public final String inReplyToScreenName;
    @SerializedName("in_reply_to_status_id")
    public final long inReplyToStatusId;
    @SerializedName("in_reply_to_status_id_str")
    public final String inReplyToStatusIdStr;
    @SerializedName("in_reply_to_user_id")
    public final long inReplyToUserId;
    @SerializedName("in_reply_to_user_id_str")
    public final String inReplyToUserIdStr;
    @SerializedName("lang")
    public final String lang;
    @SerializedName("place")
    public final Place place;
    @SerializedName("possibly_sensitive")
    public final boolean possiblySensitive;
    @SerializedName("scopes")
    public final Object scopes;
    @SerializedName("retweet_count")
    public final int retweetCount;
    @SerializedName("retweeted")
    public final boolean retweeted;
    @SerializedName("retweeted_status")
    public final TwitterTweet retweetedStatus;
    @SerializedName("source")
    public final String source;
    @SerializedName("text")
    public final String text;
    @SerializedName("truncated")
    public final boolean truncated;
    @SerializedName("user")
    public final TweeterUser user;
    @SerializedName("withheld_copyright")
    public final boolean withheldCopyright;
    @SerializedName("withheld_in_countries")
    public final List<String> withheldInCountries;
    @SerializedName("withheld_scope")
    public final String withheldScope;

    public TwitterTweet(Coordinates coordinates, String createdAt, Object currentUserRetweet, TweetEntities entities, Integer favoriteCount, boolean favorited, String filterLevel, long id, String idStr, String inReplyToScreenName, long inReplyToStatusId, String inReplyToStatusIdStr, long inReplyToUserId, String inReplyToUserIdStr, String lang, Place place, boolean possiblySensitive, Object scopes, int retweetCount, boolean retweeted, TwitterTweet retweetedStatus, String source, String text, boolean truncated, TweeterUser user, boolean withheldCopyright, List<String> withheldInCountries, String withheldScope) {
        this.coordinates = coordinates;
        this.createdAt = createdAt;
        this.currentUserRetweet = currentUserRetweet;
        this.entities = entities;
        this.favoriteCount = favoriteCount;
        this.favorited = favorited;
        this.filterLevel = filterLevel;
        this.id = id;
        this.idStr = idStr;
        this.inReplyToScreenName = inReplyToScreenName;
        this.inReplyToStatusId = inReplyToStatusId;
        this.inReplyToStatusIdStr = inReplyToStatusIdStr;
        this.inReplyToUserId = inReplyToUserId;
        this.inReplyToUserIdStr = inReplyToUserIdStr;
        this.lang = lang;
        this.place = place;
        this.possiblySensitive = possiblySensitive;
        this.scopes = scopes;
        this.retweetCount = retweetCount;
        this.retweeted = retweeted;
        this.retweetedStatus = retweetedStatus;
        this.source = source;
        this.text = text;
        this.truncated = truncated;
        this.user = user;
        this.withheldCopyright = withheldCopyright;
        this.withheldInCountries = withheldInCountries;
        this.withheldScope = withheldScope;
    }
}
