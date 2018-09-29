package haifa.university.mediaagent.activities;

import android.content.Context;

import haifa.university.mediaagent.R;
import haifa.university.mediaagent.common.AppContext;

///
/// Tabmenu items
///
public enum MainLayoutTabType {
    Dashboard ("Dashboard"),
    SetTopBox ("Set-Top box"),
    BrowserHistory ("Browser History"),
    Facebook ("Facebook"),
    Twitter ("Twitter"),
    Youtube ("Youtube");

    private final String name;
    private MainLayoutTabType(String s) {
        name = s;
    }
    public boolean equalsName(String otherName) {
        return (otherName != null) && name.equals(otherName);
    }
    public String toString() {
        return this.name;
    }
    public String nameWithIcon(){
        Context context = AppContext.getContext();
        String icon = "";
        switch (this) {
            case Dashboard:
                icon = context.getString(R.string.fa_dashboard_icon);
                break;
            case SetTopBox:
                icon = context.getString(R.string.fa_desktop_icon);
                break;
            case BrowserHistory:
                icon = context.getString(R.string.fa_history_icon);
                break;
            case Facebook:
                icon = context.getString(R.string.fa_facebook_icon);
                break;
            case Twitter:
                icon = context.getString(R.string.fa_twitter_icon);
                break;
            case Youtube:
                icon = context.getString(R.string.fa_youtube_icon);
                break;
        }
        return ((icon.length() > 0 ) ? icon + " " : "") + name;
    }
}
