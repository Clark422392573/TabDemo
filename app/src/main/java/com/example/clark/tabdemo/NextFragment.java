package com.example.clark.tabdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clark.tabdemo.tablayout.SegmentTabLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NextFragment extends Fragment {


    @InjectView(R.id.next_fragment_text)
    TextView nextFragmentText;
    @InjectView(R.id.next_fragment_tab)
    SegmentTabLayout nextFragmentTab;

    private String[] mTitles = {"翔大王", "万岁"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next, container, false);
        ButterKnife.inject(this, view);
        nextFragmentTab.setTabData(mTitles);
        nextFragmentTab.showDot(1);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.next_fragment_text)
    public void onClick() {
        Toast.makeText(getContext(), "next", Toast.LENGTH_SHORT).show();
    }
}
