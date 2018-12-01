package fall2018.csc2017.gamecentre;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Viewpager Adapter for Scoreboard's TabView.
 *
 * This class contains only UI-related code. Thus, it is excluded from test.
 */
class ScoreboardPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    /**
     * Required View PagerAdapter constructor for FragmentPagerAdapter.
     *
     * @param manager the FragmentManager
     */
    public ScoreboardPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * Add a fragment to the View Pager.
     * @param fragment the fragment given
     * @param title the title of the fragment
     * @param position the fragment's position in the tab.
     */
    public void addFragment(Fragment fragment, String title, int position) {
        mFragmentList.add(position, fragment);
        mFragmentTitleList.add(position, title);
    }

    /**
     * Clear all the fragments of the view pager.
     */
    public void clearAllFragment(){
        mFragmentTitleList.clear();
        mFragmentList.clear();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}