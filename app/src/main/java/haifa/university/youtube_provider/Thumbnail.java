package haifa.university.youtube_provider;

/**
 * Created by yura on 24/02/2016.
 */

import com.google.gson.annotations.SerializedName;

/**
 * A thumbnail is an image representing a YouTube resource.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the YouTube Data API. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Thumbnail{

    public Thumbnail(java.lang.String url,java.lang.Long height,java.lang.Long width){
        this.url = url;
        this.height = height;
        this.width = width;
    }
    /**
     * (Optional) Height of the thumbnail image.
     * The value may be {@code null}.
     */
    @SerializedName("height")
    public java.lang.Long height;

    /**
     * The thumbnail image's URL.
     * The value may be {@code null}.
     */
    @SerializedName("url")
    public java.lang.String url;

    /**
     * (Optional) Width of the thumbnail image.
     * The value may be {@code null}.
     */
    @SerializedName("width")
    public java.lang.Long width;
}
