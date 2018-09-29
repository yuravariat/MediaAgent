package haifa.university.youtube_provider;

import com.google.gson.annotations.SerializedName;

/**
 * Internal representation of thumbnails for a YouTube resource.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the YouTube Data API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class ThumbnailDetails {

    /**
     * The default image for this resource.
     * The value may be {@code null}.
     */
    @SerializedName("default")
    public Thumbnail default__;

    /**
     * The high quality image for this resource.
     * The value may be {@code null}.
     */
    @SerializedName("high")
    public Thumbnail high;

    /**
     * The maximum resolution quality image for this resource.
     * The value may be {@code null}.
     */
    @SerializedName("maxres")
    public Thumbnail maxres;

    /**
     * The medium quality image for this resource.
     * The value may be {@code null}.
     */
    @SerializedName("medium")
    public Thumbnail medium;

    /**
     * The standard quality image for this resource.
     * The value may be {@code null}.
     */
    @SerializedName("standard")
    public Thumbnail standard;
}