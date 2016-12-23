package com.example.clark.tabdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clark.tabdemo.listener.OnTabSelectListener;
import com.example.clark.tabdemo.tablayout.SegmentTabLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShowFragment extends Fragment {

    @InjectView(R.id.show_fragment_tablayout)
    SegmentTabLayout showFragmentTablayout;
    @InjectView(R.id.show_fragment_viewpager)
    ViewPager showFragmentViewpager;

    private String[] mTitles = {"翔大王", "万岁", "万岁", "万万岁"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        ButterKnife.inject(this, view);

        showFragmentTablayout.setTabData(mTitles);
        showFragmentTablayout.showDot(1);
        showFragmentViewpager.setAdapter(new MyTabAdapter(getChildFragmentManager()));
        showFragmentTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showFragmentViewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        showFragmentViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showFragmentTablayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        showFragmentViewpager.setCurrentItem(0);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    class MyTabAdapter extends FragmentPagerAdapter {

        public MyTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return MainFragment.getInstance(mTitles.length);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
