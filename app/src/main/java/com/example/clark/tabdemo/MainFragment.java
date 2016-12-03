package com.example.clark.tabdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {

    @InjectView(R.id.main_fragment_text)
    TextView mainFragmentText;
    private String mTitle;

    public static MainFragment getInstance(String title) {
        MainFragment mMainFragment = new MainFragment();
        mMainFragment.mTitle = title;
        return mMainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.main_fragment_text)
    public void onClick() {

        Toast.makeText(getContext(), "main", Toast.LENGTH_SHORT).show();
    }
}
