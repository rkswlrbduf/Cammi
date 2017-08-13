package samsung.membership.splash;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by yumin on 2017-07-29.
 */

public class MainTabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public MainTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MainTabFragment1 tabFragment1 = new MainTabFragment1();
                return tabFragment1;
            case 1:
                MainTabFragment2 tabFragment2 = new MainTabFragment2();
                return tabFragment2;
            case 2:
                MainTabFragment3 tabFragment3 = new MainTabFragment3();
                return tabFragment3;
            case 3:
                MainTabFragment3 tabFragment4 = new MainTabFragment3();
                return tabFragment4;
            default:
                return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
