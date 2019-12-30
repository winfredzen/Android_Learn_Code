package com.example.criminalintent;

import android.support.v4.app.Fragment;

/*
 列表activity
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }



}
