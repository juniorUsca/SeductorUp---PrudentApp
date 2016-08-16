package com.debugcc.prudentapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.debugcc.prudentapp.R;

import java.util.Calendar;


public class CalendarFragment extends Fragment {

    final String TAG = "CalendarFragment";
    private Calendar mCalendarToday;
    private int mMonth;
    private int mYear;

    private int mCurrentMonth;
    private int mCurrentYear;

    private static final int MIDDLE = 500;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;


    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        /// inicializamos algun servicio
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// se pueden obtener argumentos
        mCalendarToday = Calendar.getInstance();
        mMonth = mCalendarToday.get(Calendar.MONTH);
        mYear = mCalendarToday.get(Calendar.YEAR);

        Log.e(TAG, "onCreate: " + mMonth + " " + mYear);
        mCurrentMonth = mMonth;
        mCurrentYear = mYear;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        /*LinearLayout fragContainer = (LinearLayout) view.findViewById(R.id.llFragmentContainer);
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setId(12345);
        getFragmentManager().beginTransaction().add(ll.getId(), MonthFragment.newInstance(), "someTag1").commit();
        //getFragmentManager().beginTransaction().add(ll.getId(), MonthFragment.newInstance(), "someTag2").commit();
        fragContainer.addView(ll);*/


        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(MIDDLE, false);


        return view;
    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_ITEMS = 1001;
        FragmentManager fm;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
            //fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @Override
        public Fragment getItem(int position) {

            int pos = (position-MIDDLE);
            Log.e(TAG, "position: " + position);
            Log.e(TAG, "getItem: " + mCurrentMonth);
            mCurrentMonth = mMonth + pos;
            mCurrentMonth = mCurrentMonth % 12;
            if(mCurrentMonth<0){
                mCurrentMonth = 12 + mCurrentMonth;
            }

            mCurrentYear = mYear + ((mMonth + pos) / 12 );
            if((mMonth + pos)<0){
                mCurrentYear = mYear + ((mMonth + pos + 1 ) / 12 - 1);
            }

            MonthFragment f = MonthFragment.newInstance(mCurrentMonth,mCurrentYear);
            f.set_fragmentManager(fm);
            f.set_pagerAdapter(this);
            return f;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int pos = position % 1000;
            switch (pos) {
                case 0:
                    return "Calendario";
                case 1:
                    return "Calendario";
                case 2:
                    return "Calendario";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object item) {
            return PagerAdapter.POSITION_NONE;
        }

    }


}
