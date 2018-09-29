package haifa.university.twitter_provider;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Created by yura on 24/02/2016.
 */
public class UserEntities {
    @SerializedName("url")
    public final UserEntities.UrlEntities url;
    @SerializedName("description")
    public final UserEntities.UrlEntities description;

    public UserEntities(UserEntities.UrlEntities url, UserEntities.UrlEntities description) {
        this.url = url;
        this.description = description;
    }

    public static class UrlEntities {
        @SerializedName("urls")
        public final List<UrlEntity> urls;

        public UrlEntities(List<UrlEntity> urls) {
            this.urls = this.getSafeList(urls);
        }

        private <T> List<T> getSafeList(List<T> entities) {
            return entities == null? Collections.EMPTY_LIST:Collections.unmodifiableList(entities);
        }
    }
}
