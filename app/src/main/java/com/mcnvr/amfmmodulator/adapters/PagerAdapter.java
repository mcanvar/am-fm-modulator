package com.mcnvr.amfmmodulator.adapters;

/**
 * Created by mevlut on 31.12.2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.mcnvr.amfmmodulator.fragments.CarrierGraphFragment;
import com.mcnvr.amfmmodulator.fragments.DataGraphFragment;
import com.mcnvr.amfmmodulator.fragments.MixGraphFragment;
import com.mcnvr.amfmmodulator.fragments.ModGraphFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DataGraphFragment tab1 = new DataGraphFragment();
                return tab1;
            case 1:
                CarrierGraphFragment tab2 = new CarrierGraphFragment();
                return tab2;
            case 2:
                ModGraphFragment tab3 = new ModGraphFragment();
                return tab3;
            case 3:
                MixGraphFragment tab4 = new MixGraphFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}