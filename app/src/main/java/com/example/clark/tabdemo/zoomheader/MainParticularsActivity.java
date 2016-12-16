package com.example.clark.tabdemo.zoomheader;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.clark.tabdemo.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainParticularsActivity extends AppCompatActivity {

    @InjectView(R.id.particulars_recyclerView)
    RecyclerView particularsRecyclerView;
    @InjectView(R.id.rlayout_particulars_bottom)
    RelativeLayout rlayoutParticularsBottom;
    @InjectView(R.id.particulars_viewpager)
    ZoomHeaderViewPager particularsViewpager;
    @InjectView(R.id.particulars_zoomheader)
    ZoomHeaderView particularsZoomheader;
    @InjectView(R.id.activity_main_particulars)
    CoordinatorLayout activityMainParticulars;

    private boolean isFirst = true;
    public static int bottomY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_particulars);
        ButterKnife.inject(this);

        particularsViewpager.setAdapter(new Adapter());
        particularsViewpager.setOffscreenPageLimit(4);
        CtrlLinearLayoutManager layoutManager = new CtrlLinearLayoutManager(this);

        //未展开禁止滑动
        layoutManager.setScrollEnabled(false);
        particularsRecyclerView.setLayoutManager(layoutManager);
        particularsRecyclerView.setAdapter(new ListAdapter());
        particularsRecyclerView.setAlpha(0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst) {
            for (int i = 0; i < particularsViewpager.getChildCount(); i++) {
                View v = particularsViewpager.getChildAt(i).findViewById(R.id.ll_bottom);
                v.setY(particularsViewpager.getChildAt(i).findViewById(R.id.imageView).getHeight());
                v.setX(MarginConfig.MARGIN_LEFT_RIGHT);
                //触发一次dependency变化，让按钮归位
                particularsZoomheader.setY(particularsZoomheader.getY() - 1);
                isFirst = false;
            }
        }

        //隐藏底部栏]
        bottomY = (int) rlayoutParticularsBottom.getY();
        rlayoutParticularsBottom.setTranslationY(rlayoutParticularsBottom.getY() + rlayoutParticularsBottom.getHeight());
        particularsZoomheader.setBottomView(rlayoutParticularsBottom, bottomY);
    }

    class Adapter extends PagerAdapter {
        public Adapter() {
            views = new ArrayList<>();
            views.add(View.inflate(MainParticularsActivity.this, R.layout.item_img, null));
            views.get(0).findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainParticularsActivity.this, "buy", Toast.LENGTH_SHORT).show();
                }
            });
            views.add(View.inflate(MainParticularsActivity.this, R.layout.item_img, null));

            views.add(View.inflate(MainParticularsActivity.this, R.layout.item_img, null));
        }

        private ArrayList<View> views;
        private int[] imgs = {R.mipmap.cate, R.mipmap.pictwo, R.mipmap.picthree};

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            views.get(position).findViewById(R.id.imageView).setBackgroundResource(imgs[position]);
            container.addView(views.get(position));

            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (particularsZoomheader.isExpand()) {
            particularsZoomheader.restore(particularsZoomheader.getY());
        } else {
            finish();
        }
    }
}
