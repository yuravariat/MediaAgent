package haifa.university.twitter_provider;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by yura on 24/02/2016.
 */
public class Place {
    @SerializedName("attributes")
    public final Map<String, String> attributes;
    @SerializedName("bounding_box")
    public final Place.BoundingBox boundingBox;
    @SerializedName("country")
    public final String country;
    @SerializedName("country_code")
    public final String countryCode;
    @SerializedName("full_name")
    public final String fullName;
    @SerializedName("id")
    public final String id;
    @SerializedName("name")
    public final String name;
    @SerializedName("place_type")
    public final String placeType;
    @SerializedName("url")
    public final String url;

    public Place(Map<String, String> attributes, Place.BoundingBox boundingBox, String country, String countryCode, String fullName, String id, String name, String placeType, String url) {
        this.attributes = attributes;
        this.boundingBox = boundingBox;
        this.country = country;
        this.countryCode = countryCode;
        this.fullName = fullName;
        this.id = id;
        this.name = name;
        this.placeType = placeType;
        this.url = url;
    }

    public static class BoundingBox {
        public final List<List<List<Double>>> coordinates;
        public final String type;

        public BoundingBox(List<List<List<Double>>> coordinates, String type) {
            this.coordinates = coordinates;
            this.type = type;
        }
    }
}