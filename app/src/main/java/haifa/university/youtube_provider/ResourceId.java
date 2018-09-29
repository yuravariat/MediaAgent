package haifa.university.youtube_provider;

/**
 * Created by yura on 24/02/2016.
 */

import com.google.gson.annotations.SerializedName;

/**
 * A resource id is a generic reference that points to another YouTube resource.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the YouTube Data API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class ResourceId{

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * channel. This property is only present if the resourceId.kind value is youtube#channel.
     * The value may be {@code null}.
     */
    @SerializedName("channelId")
    public java.lang.String channelId;

    /**
     * The type of the API resource.
     * The value may be {@code null}.
     */
    @SerializedName("kind")
    public java.lang.String kind;

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * playlist. This property is only present if the resourceId.kind value is youtube#playlist.
     * The value may be {@code null}.
     */
    @SerializedName("playlistId")
    public java.lang.String playlistId;

    /**
     * The ID that YouTube uses to uniquely identify the referred resource, if that resource is a
     * video. This property is only present if the resourceId.kind value is youtube#video.
     * The value may be {@code null}.
     */
    @SerializedName("videoId")
    public java.lang.String videoId;
}