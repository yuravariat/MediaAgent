package haifa.university.youtube_provider;

/**
 * Created by yura on 24/02/2016.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Basic details about a playlist, including title, description and thumbnails.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the YouTube Data API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class PlaylistItemSnippet {

    /**
     * The ID that YouTube uses to uniquely identify the user that added the item to the playlist.
     * The value may be {@code null}.
     */
    @SerializedName("channelId")
    public java.lang.String channelId;

    /**
     * Channel title for the channel that the playlist item belongs to.
     * The value may be {@code null}.
     */
    @SerializedName("channelTitle")
    public java.lang.String channelTitle;

    /**
     * The item's description.
     * The value may be {@code null}.
     */
    @SerializedName("description")
    public java.lang.String description;

    /**
     * The ID that YouTube uses to uniquely identify the playlist that the playlist item is in.
     * The value may be {@code null}.
     */
    @SerializedName("playlistId")
    public java.lang.String playlistId;

    /**
     * The order in which the item appears in the playlist. The value uses a zero-based index, so the
     * first item has a position of 0, the second item has a position of 1, and so forth.
     * The value may be {@code null}.
     */
    @SerializedName("position")
    public java.lang.Long position;

    /**
     * The date and time that the item was added to the playlist. The value is specified in ISO 8601
     * (YYYY-MM-DDThh:mm:ss.sZ) format.
     * The value may be {@code null}.
     */
    @SerializedName("publishedAt")
    public com.google.api.client.util.DateTime publishedAt;

    /**
     * The id object contains information that can be used to uniquely identify the resource that is
     * included in the playlist as the playlist item.
     * The value may be {@code null}.
     */
    @SerializedName("resourceId")
    public ResourceId resourceId;

    /**
     * A map of thumbnail images associated with the playlist item. For each object in the map, the
     * key is the name of the thumbnail image, and the value is an object that contains other
     * information about the thumbnail.
     * The value may be {@code null}.
     */
    @SerializedName("thumbnails")
    public ThumbnailDetails thumbnails;

    /**
     * The item's title.
     * The value may be {@code null}.
     */
    @SerializedName("title")
    public java.lang.String title;
}
