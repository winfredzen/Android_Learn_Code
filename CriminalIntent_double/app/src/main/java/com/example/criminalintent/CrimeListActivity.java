package com.example.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.criminalintent.mode.Crime;

/*
 列表activity
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() { //主从用于界面
        return R.layout.activity_masterdetail;
    }

    //CrimeListFragment.Callbacks
    @Override
    public void onCrimeSelected(Crime crime) {

        if (findViewById(R.id.detail_fragment_container) == null) { //不存在

            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);

        } else { //表示为主从界面

            Fragment detailFragment = CrimeFragment.newInstance(crime.getId());

            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, detailFragment).commit();

        }

    }

    @Override
    public void onCrimeUpdated(Crime crime) {

        CrimeListFragment listFragment = (CrimeListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        listFragment.updateUI();

    }
}
