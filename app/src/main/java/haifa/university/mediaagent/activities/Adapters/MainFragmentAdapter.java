package haifa.university.mediaagent.activities.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import haifa.university.mediaagent.activities.MainFragments.FacebookFragment;
import haifa.university.mediaagent.activities.MainFragments.HistoryFragment;
import haifa.university.mediaagent.activities.MainFragments.SetTopBoxFragment;
import haifa.university.mediaagent.activities.MainFragments.TwitterFragment;
import haifa.university.mediaagent.activities.MainFragments.YouTubeFragment;
import haifa.university.mediaagent.activities.MainLayoutTabType;
import haifa.university.mediaagent.activities.MainFragments.MainFragment;

/**
 * Created by yura on 20/10/2015.
 */
/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        MainLayoutTabType tabType = MainLayoutTabType.values()[position];
        switch (tabType) {
            case BrowserHistory:
                return HistoryFragment.newInstance(position);
            case SetTopBox:
                return SetTopBoxFragment.newInstance(position);
            case Facebook:
                return FacebookFragment.newInstance(position);
            case Twitter:
                return TwitterFragment.newInstance(position);
            case Youtube:
                return YouTubeFragment.newInstance(position);
            default:
                return MainFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        // Show total pages.
        return MainLayoutTabType.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position < MainLayoutTabType.values().length) {
            return MainLayoutTabType.values()[position].nameWithIcon();
        }
        return null;
    }
    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}