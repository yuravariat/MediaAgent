package haifa.university.youtube_provider;

/**
 * Created by yura on 24/02/2016.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Information about the playlist item's privacy status.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the YouTube Data API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class PlaylistItemStatus{

    /**
     * This resource's privacy status.
     * The value may be {@code null}.
     */
    @SerializedName("privacyStatus")
    public java.lang.String privacyStatus;

}