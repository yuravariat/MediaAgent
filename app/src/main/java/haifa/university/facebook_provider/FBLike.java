package haifa.university.facebook_provider;

import java.util.Date;

/**
 * Created by yura on 02/11/2015.
 */
public class FBLike {
    public long id;
    public From from;
    public String name;
    public String link;
    public String cover_photo;
    public int count;
    public String type;
    public Date created_time;
    public Date updated_time;
    public boolean can_upload;

    public class From{
        public String name;
        public long idl;
    }
}