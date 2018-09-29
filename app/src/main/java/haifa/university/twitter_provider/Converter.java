package haifa.university.twitter_provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yura on 17/03/2016.
 */
public class Converter {
    public static Coordinates ConvertCoordinatesFromTwitterObj(com.twitter.sdk.android.core.models.Coordinates t_coordinates){
        if(t_coordinates==null){
            return null;
        }
        Coordinates coordinates = new Coordinates(t_coordinates.getLongitude(),t_coordinates.getLatitude(),t_coordinates.type);
        return coordinates;
    }
    public static HashtagEntity ConvertHashtagEntityFromTwitterObj(com.twitter.sdk.android.core.models.HashtagEntity t_item) {
        if(t_item==null){
            return null;
        }
        HashtagEntity item = new HashtagEntity(t_item.text,t_item.getStart(),t_item.getEnd());
        return item;
    }
    public static List<HashtagEntity> ConvertHashtagEntitiesFromTwitterObjs(List<com.twitter.sdk.android.core.models.HashtagEntity> t_items) {
        if(t_items==null){
            return null;
        }
        List<HashtagEntity> items = new ArrayList<>();
        for (com.twitter.sdk.android.core.models.HashtagEntity t_item: t_items) {
            items.add(ConvertHashtagEntityFromTwitterObj(t_item));
        }
        return items;
    }
    public static MediaEntity ConvertMediaEntityFromTwitterObj(com.twitter.sdk.android.core.models.MediaEntity t_item) {
        if(t_item==null){
            return null;
        }
        MediaEntity.Sizes sizes = new MediaEntity.Sizes(
                new MediaEntity.Size(t_item.sizes.thumb.w,t_item.sizes.thumb.h,t_item.sizes.thumb.resize),
                new MediaEntity.Size(t_item.sizes.small.w,t_item.sizes.small.h,t_item.sizes.small.resize),
                new MediaEntity.Size(t_item.sizes.medium.w,t_item.sizes.medium.h,t_item.sizes.medium.resize),
                new MediaEntity.Size(t_item.sizes.large.w,t_item.sizes.large.h,t_item.sizes.large.resize)
        );
        MediaEntity item = new MediaEntity(t_item.url, t_item.expandedUrl, t_item.displayUrl, t_item.getStart(),t_item.getEnd(),
                t_item.id, t_item.idStr, t_item.mediaUrl, t_item.mediaUrlHttps, sizes, t_item.sourceStatusId, t_item.sourceStatusIdStr, t_item.type);
        return item;
    }
    public static List<MediaEntity> ConvertMediaEntitiesFromTwitterObjs(List<com.twitter.sdk.android.core.models.MediaEntity> t_items) {
        if(t_items==null){
            return null;
        }
        List<MediaEntity> items = new ArrayList<>();
        for (com.twitter.sdk.android.core.models.MediaEntity t_item: t_items) {
            items.add(ConvertMediaEntityFromTwitterObj(t_item));
        }
        return items;
    }
    public static MentionEntity ConvertMentionEntityFromTwitterObj(com.twitter.sdk.android.core.models.MentionEntity t_item) {
        if(t_item==null){
            return null;
        }
        MentionEntity item = new MentionEntity(t_item.id,t_item.idStr,t_item.name,t_item.screenName,t_item.getStart(),t_item.getEnd());
        return item;
    }
    public static List<MentionEntity> ConvertMentionEntitiesFromTwitterObjs(List<com.twitter.sdk.android.core.models.MentionEntity> t_items) {
        if(t_items==null){
            return null;
        }
        List<MentionEntity> items = new ArrayList<>();
        for (com.twitter.sdk.android.core.models.MentionEntity t_item: t_items) {
            items.add(ConvertMentionEntityFromTwitterObj(t_item));
        }
        return items;
    }
    public static Place ConvertPlaceFromTwitterObj(com.twitter.sdk.android.core.models.Place t_item) {
        if (t_item == null) {
            return null;
        }
        Place item = new Place(
                t_item.attributes,
                new Place.BoundingBox(t_item.boundingBox.coordinates, t_item.boundingBox.type),
                t_item.country,
                t_item.countryCode,
                t_item.fullName,
                t_item.id,
                t_item.name,
                t_item.placeType,
                t_item.url
        );
        return item;
    }
    public static TweetEntities ConvertTweetEntitiesFromTwitterObj(com.twitter.sdk.android.core.models.TweetEntities t_item) {
        if(t_item==null){
            return null;
        }
        TweetEntities tweetEntities = new TweetEntities(
                ConvertUrlEntitiesFromTwitterObjs(t_item.urls),
                ConvertMentionEntitiesFromTwitterObjs(t_item.userMentions),
                ConvertMediaEntitiesFromTwitterObjs(t_item.media),
                ConvertHashtagEntitiesFromTwitterObjs(t_item.hashtags)
        );
        return  tweetEntities;
    }
    public static TweeterUser ConvertTweeterUserFromTwitterObj(com.twitter.sdk.android.core.models.User t_user) {
        if (t_user == null) {
            return null;
        }
        TweeterUser user = new TweeterUser(
                t_user.contributorsEnabled,
                t_user.createdAt,
                t_user.defaultProfile,
                t_user.defaultProfileImage,
                t_user.description,
                t_user.email,
                null,//t_user.entities,
                t_user.favouritesCount,
                t_user.followRequestSent,
                t_user.followersCount,
                t_user.friendsCount,
                t_user.geoEnabled,
                t_user.id,
                t_user.idStr,
                t_user.isTranslator,
                t_user.lang,
                t_user.listedCount,
                t_user.location,
                t_user.name,
                t_user.profileBackgroundColor,
                t_user.profileBackgroundImageUrl,
                t_user.profileBackgroundImageUrlHttps,
                t_user.profileBackgroundTile,
                t_user.profileBannerUrl,
                t_user.profileImageUrl,
                t_user.profileImageUrlHttps,
                t_user.profileLinkColor,
                t_user.profileSidebarBorderColor,
                t_user.profileSidebarFillColor,
                t_user.profileTextColor,
                t_user.profileUseBackgroundImage,
                t_user.protectedUser,
                t_user.screenName,
                t_user.showAllInlineMedia,
                null, //t_user.status,
                t_user.statusesCount,
                t_user.timeZone,
                t_user.url,
                t_user.utcOffset,
                t_user.verified,
                t_user.withheldInCountries,
                t_user.withheldScope
        );
        return user;
    }
    public static List<TwitterTweet> ConvertTwitterTweetsFromTwitterObjs(List<com.twitter.sdk.android.core.models.Tweet> t_tweets){
        ArrayList<TwitterTweet> tweets = new ArrayList<>();
        if (t_tweets == null) {
            return tweets;
        }
        for (com.twitter.sdk.android.core.models.Tweet t_tweet:t_tweets) {
            tweets.add(ConvertTwitterTweetFromTwitterObj(t_tweet));
        }
        return tweets;
    }
    public static TwitterTweet ConvertTwitterTweetFromTwitterObj(com.twitter.sdk.android.core.models.Tweet t_tweet) {
        if (t_tweet == null) {
            return null;
        }
        TwitterTweet tweet = new TwitterTweet(
                ConvertCoordinatesFromTwitterObj(t_tweet.coordinates),
                t_tweet.createdAt,
                t_tweet.currentUserRetweet,
                ConvertTweetEntitiesFromTwitterObj(t_tweet.entities),
                t_tweet.favoriteCount,
                t_tweet.favorited,
                t_tweet.filterLevel,
                t_tweet.id,
                t_tweet.idStr,
                t_tweet.inReplyToScreenName,
                t_tweet.inReplyToStatusId,
                t_tweet.inReplyToStatusIdStr,
                t_tweet.inReplyToUserId,
                t_tweet.inReplyToUserIdStr,
                t_tweet.lang,
                ConvertPlaceFromTwitterObj(t_tweet.place),
                t_tweet.possiblySensitive,
                t_tweet.scopes,
                t_tweet.retweetCount,
                t_tweet.retweeted,
                null, //t_tweet.retweetedStatus,
                t_tweet.source,
                t_tweet.text,
                t_tweet.truncated,
                ConvertTweeterUserFromTwitterObj(t_tweet.user),
                t_tweet.withheldCopyright,
                t_tweet.withheldInCountries,
                t_tweet.withheldScope
        );
        return tweet;
    }
    public static UrlEntity ConvertUrlEntityFromTwitterObj(com.twitter.sdk.android.core.models.UrlEntity t_item) {
        if(t_item==null){
            return null;
        }
        UrlEntity item = new UrlEntity(t_item.url,t_item.expandedUrl,t_item.displayUrl,t_item.getStart(),t_item.getEnd());
        return item;
    }
    public static List<UrlEntity> ConvertUrlEntitiesFromTwitterObjs(List<com.twitter.sdk.android.core.models.UrlEntity> t_items) {
        if(t_items==null){
            return null;
        }
        List<UrlEntity> items = new ArrayList<>();
        for (com.twitter.sdk.android.core.models.UrlEntity t_item: t_items) {
            items.add(ConvertUrlEntityFromTwitterObj(t_item));
        }
        return items;
    }
}
