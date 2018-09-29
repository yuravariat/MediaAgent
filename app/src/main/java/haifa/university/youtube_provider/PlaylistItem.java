package haifa.university.youtube_provider;

/**
 * Created by yura on 24/02/2016.
 */

import com.google.gson.annotations.SerializedName;

/**
 * A playlistItem resource identifies another resource, such as a video, that is included in a
 * playlist. In addition, the playlistItem  resource contains details about the included resource
 * that pertain specifically to how that resource is used in that playlist.
 *
 * YouTube uses playlists to identify special collections of videos for a channel, such as:   -
 * uploaded videos  - favorite videos  - positively rated (liked) videos  - watch history  - watch
 * later  To be more specific, these lists are associated with a channel, which is a collection of a
 * person, group, or company's videos, playlists, and other YouTube information.
 *
 * You can retrieve the playlist IDs for each of these lists from the  channel resource  for a given
 * channel. You can then use the   playlistItems.list method to retrieve any of those lists. You can
 * also add or remove items from those lists by calling the   playlistItems.insert and
 * playlistItems.delete methods. For example, if a user gives a positive rating to a video, you
 * would insert that video into the liked videos playlist for that user's channel.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the YouTube Data API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class PlaylistItem {

    /**
     * The contentDetails object is included in the resource if the included item is a YouTube video.
     * The object contains additional information about the video.
     * The value may be {@code null}.
     */
    @SerializedName("contentDetails")
    public PlaylistItemContentDetails contentDetails;

    /**
     * Etag of this resource.
     * The value may be {@code null}.
     */
    @SerializedName("contentDetails")
    public java.lang.String etag;

    /**
     * The ID that YouTube uses to uniquely identify the playlist item.
     * The value may be {@code null}.
     */
    @SerializedName("id")
    public java.lang.String id;

    /**
     * Identifies what kind of resource this is. Value: the fixed string "youtube#playlistItem".
     * The value may be {@code null}.
     */
    @SerializedName("kind")
    public java.lang.String kind;

    /**
     * The snippet object contains basic details about the playlist item, such as its title and
     * position in the playlist.
     * The value may be {@code null}.
     */
    @SerializedName("snippet")
    public PlaylistItemSnippet snippet;

    /**
     * The status object contains information about the playlist item's privacy status.
     * The value may be {@code null}.
     */
    @SerializedName("status")
    public PlaylistItemStatus status;
}
