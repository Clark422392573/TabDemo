package com.example.clark.tabdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.clark.tabdemo.cardstackview.AllMoveDownAnimatorAdapter;
import com.example.clark.tabdemo.cardstackview.CardStackView;
import com.example.clark.tabdemo.cardstackview.TestStackAdapter;
import com.example.clark.tabdemo.cardstackview.UpDownAnimatorAdapter;
import com.example.clark.tabdemo.cardstackview.UpDownStackAnimatorAdapter;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NextFragment extends Fragment implements CardStackView.ItemExpendListener {

    public static Integer[] COLOR_DATAS = new Integer[]{
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_9,
            R.color.color_10,
            R.color.color_11,
            R.color.color_12,
            R.color.color_13,
            R.color.color_14,
            R.color.color_15,
            R.color.color_16,
            R.color.color_17,
            R.color.color_18,
            R.color.color_19,
            R.color.color_20,
            R.color.color_21,
            R.color.color_22,
            R.color.color_23,
            R.color.color_24,
            R.color.color_25,
            R.color.color_26
    };
    @InjectView(R.id.stackview_next_fragment)
    CardStackView mStackView;
    @InjectView(R.id.llayout_container_next_fragment)
    LinearLayout mActionButtonContainer;
    @InjectView(R.id.btn_pre_next_fragment)
    Button btnPreNextFragment;
    @InjectView(R.id.btn_next_next_fragment)
    Button btnNextNextFragment;

    private TestStackAdapter mTestStackAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next, container, false);
        ButterKnife.inject(this, view);
        mStackView.setItemExpendListener(this);
        mTestStackAdapter = new TestStackAdapter(getActivity());
        mStackView.setAdapter(mTestStackAdapter);
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mTestStackAdapter.updateData(Arrays.asList(COLOR_DATAS));
                    }
                }, 200
        );
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_all_down:
                mStackView.setAnimatorAdapter(new AllMoveDownAnimatorAdapter(mStackView));
                break;
            case R.id.menu_up_down:
                mStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(mStackView));
                break;
            case R.id.menu_up_down_stack:
                mStackView.setAnimatorAdapter(new UpDownStackAnimatorAdapter(mStackView));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemExpend(boolean expend) {
        mActionButtonContainer.setVisibility(expend ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.btn_pre_next_fragment, R.id.btn_next_next_fragment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pre_next_fragment:
                mStackView.pre();
                break;
            case R.id.btn_next_next_fragment:
                mStackView.next();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
