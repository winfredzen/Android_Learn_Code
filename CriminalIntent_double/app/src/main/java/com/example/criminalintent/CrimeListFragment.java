package com.example.criminalintent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.criminalintent.mode.Crime;
import com.example.criminalintent.mode.CrimeLab;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

/**
 * 列表Fragment
 */
public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    private CrimeAdapter mAdapter;

    //是否显示副标题
    private boolean mSubtitleVisible;

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";


    private Callbacks mCallbacks;

    //定义接口
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Context context) {//该方法是在fragment附加给activity时调用的
        super.onAttach(context);

        mCallbacks = (Callbacks) context; //设置回调
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //有menu
        setHasOptionsMenu(true);

        //解决层级式导航所带来的问题
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);

        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtileItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtileItem.setTitle(R.string.hide_subtitle);
        } else {
            subtileItem.setTitle(R.string.show_subtitle);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_crime:

//                Crime crime = new Crime();
//                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent intent = CrimePagerActivity.newIntent(getContext(), crime.getId());
//                startActivity(intent);

                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                updateUI();
                mCallbacks.onCrimeSelected(crime);


                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;

                //表示要重新创建menu
                getActivity().invalidateOptionsMenu();;

                updateSubtitle();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //保存变量
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    //更新副标题
    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI() {

        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();//数据改变

        }

        updateSubtitle();


    }


    //有特点
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;

        private TextView mDateTextView;

        private ImageView mSolvedImageView;

        private Crime mCrime;

        //这里自定义了初始化方法
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent)  {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            itemView.setOnClickListener(this); //点击

            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);

        }

        public void bind(Crime crime) {

            mCrime = crime;

            mTitleTextView.setText(crime.getTitle());

            mDateTextView.setText(DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA).format(crime.getDate()));


            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);

        }

        //点击事件
        @Override
        public void onClick(View v) {

            //Toast.makeText(getActivity(), mCrime.getTitle()+" clicked!", Toast.LENGTH_SHORT).show();

            //启动另一个activity
//            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
//            startActivity(intent);

//            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
//            startActivity(intent);


            mCallbacks.onCrimeSelected(mCrime);


        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new CrimeHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {

            holder.bind(mCrimes.get(position));

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }
}

