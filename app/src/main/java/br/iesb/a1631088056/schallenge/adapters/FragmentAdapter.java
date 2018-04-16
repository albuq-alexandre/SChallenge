package br.iesb.a1631088056.schallenge.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.iesb.a1631088056.schallenge.fragments.CellBemFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CellBemFragment();
            case 1:
                return new CellBemFragment();
            case 2:
                return new CellBemFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            //
            //Your tab titles
            //
            case 0:return "Bens";
            case 1:return "Pendentes";
            case 2: return "Inventariados";
            default:return null;
        }
    }
}