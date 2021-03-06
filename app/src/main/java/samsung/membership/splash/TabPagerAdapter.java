package samsung.membership.splash;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by yumin on 2017-07-29.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    TabFragment1 tabFragment1 = new TabFragment1();
    TabFragment2 tabFragment2 = new TabFragment2();
    TabFragment3 tabFragment3 = new TabFragment3();

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return tabFragment1;
            case 1:
                return tabFragment2;
            case 2:
                return tabFragment3;
            default:
                return null;
        }
    }

    public TabFragment1 returnTabFragment() {
        return tabFragment1;
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
