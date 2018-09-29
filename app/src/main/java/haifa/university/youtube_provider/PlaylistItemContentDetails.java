package haifa.university.youtube_provider;

/**
 * Created by yura on 24/02/2016.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Model definition for PlaylistItemContentDetails.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the YouTube Data API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class PlaylistItemContentDetails{

    /**
     * The time, measured in seconds from the start of the video, when the video should stop playing.
     * (The playlist owner can specify the times when the video should start and stop playing when the
     * video is played in the context of the playlist.) By default, assume that the video.endTime is
     * the end of the video.
     * The value may be {@code null}.
     */
    @SerializedName("endAt")
    public java.lang.String endAt;

    /**
     * A user-generated note for this item.
     * The value may be {@code null}.
     */
    @SerializedName("note")
    public java.lang.String note;

    /**
     * The time, measured in seconds from the start of the video, when the video should start playing.
     * (The playlist owner can specify the times when the video should start and stop playing when the
     * video is played in the context of the playlist.) The default value is 0.
     * The value may be {@code null}.
     */
    @SerializedName("startAt")
    public java.lang.String startAt;

    /**
     * The ID that YouTube uses to uniquely identify a video. To retrieve the video resource, set the
     * id query parameter to this value in your API request.
     * The value may be {@code null}.
     */
    @SerializedName("videoId")
    public java.lang.String videoId;
}