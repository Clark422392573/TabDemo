package com.example.clark.tabdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.clark.tabdemo.zoomheader.MainParticularsActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {

    @InjectView(R.id.main_fragment_recyclerview)
    RecyclerView mainFragmentRecyclerview;
    @InjectView(R.id.fragment_main)
    RelativeLayout fragmentMain;

    public static MainFragment getInstance(int position) {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pos", String.valueOf(position));
        // Activity重新创建时，会重新构建它所管理的Fragment，
        // 原先的Fragment的字段值将会全部丢失，
        // 但是通过 Fragment.setArguments(Bundle bundle)方法设置的bundle会保留下来。
        // 所以尽量使用 Fragment.setArguments(Bundle bundle)方式来传递参数
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        mainFragmentRecyclerview.setAdapter(new ListAdapter());
        mainFragmentRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(inflater.inflate(R.layout.item_main_fragment, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainParticularsActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
