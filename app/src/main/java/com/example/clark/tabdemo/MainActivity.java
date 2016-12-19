package com.example.clark.tabdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.clark.tabdemo.listener.OnTabSelectListener;
import com.example.clark.tabdemo.personaldata.PersonalDataActivity;
import com.example.clark.tabdemo.utils.ViewFindUtils;
import com.example.clark.tabdemo.satelitemenu.SatelliteMenu;
import com.example.clark.tabdemo.satelitemenu.SatelliteMenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @InjectView(R.id.view_pager)
    ViewPager viewPager;
    @InjectView(R.id.Segment_tab_layout)
    com.example.clark.tabdemo.tablayout.SegmentTabLayout SegmentTabLayout;
    @InjectView(R.id.img_main_icon)
    ImageView imgIcon;
    @InjectView(R.id.img_add)
    ImageView imgAdd;
    @InjectView(R.id.tool_bar)
    RelativeLayout toolBar;

    private DrawerLayout mDrawerLayout;

    private String[] mTitles = {"首页", "消息"};
    private View mDecorView;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private NextFragment nextFragment;

    private RelativeLayout mPersonalData;
    private ImageView imgHeaderIcon;

    private PopupWindow mPopupWindow;
    private ListView mPopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        sateMenu();

        mFragments.add(new MainFragment());
        mFragments.add(new NextFragment());

        mDecorView = getWindow().getDecorView();

        SegmentTabLayout = ViewFindUtils.find(mDecorView, R.id.Segment_tab_layout);
        viewPager = ViewFindUtils.find(mDecorView, R.id.view_pager);
        SegmentTabLayout.setTabData(mTitles);
        // 设置未读红点
        SegmentTabLayout.showDot(1);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        SegmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SegmentTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            Toast.makeText(MainActivity.this, "首页", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(0);
                            break;

                        case R.id.nav_messages:
                            Toast.makeText(MainActivity.this, "消息", Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(1);
                            break;

                        case R.id.nav_settings:
                            startActivityForResult(new Intent(MainActivity.this, PersonalDataActivity.class), 202);
                            break;
                    }

                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });

            View headView = navigationView.getHeaderView(0);
            mPersonalData = (RelativeLayout) headView.findViewById(R.id.rLayout_navigation_header);
            imgHeaderIcon = (ImageView) headView.findViewById(R.id.img_navigation_icon);
            mPersonalData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(MainActivity.this, PersonalDataActivity.class), 202);
                }
            });
        }
    }

    private void sateMenu() {
        SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
        List<SatelliteMenuItem> items = new ArrayList<>();
        items.add(new SatelliteMenuItem(0, R.mipmap.ic_1));
        items.add(new SatelliteMenuItem(1, R.mipmap.ic_3));
        items.add(new SatelliteMenuItem(2, R.mipmap.ic_4));
        items.add(new SatelliteMenuItem(3, R.mipmap.ic_5));
        items.add(new SatelliteMenuItem(4, R.mipmap.ic_6));
        items.add(new SatelliteMenuItem(5, R.mipmap.ic_2));
        items.add(new SatelliteMenuItem(6, R.mipmap.sat_item));
        menu.addItems(items);

        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            public void eventOccured(int id) {
                Toast.makeText(MainActivity.this, "id:" + id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.img_main_icon, R.id.img_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_main_icon:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.img_add:
                showPopupWindow();
                break;
        }
    }

    private void showPopupWindow() {
        String[] listContent = {"发起多人聊天", "添加好友", "扫一扫"};
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_window, null);
        mPopupWindow = new PopupWindow(contentView,
                500, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopList = (ListView) contentView.findViewById(R.id.list_view_in_pop_window);
        mPopList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listContent));
        mPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Toast.makeText(MainActivity.this, "发起多人聊天", Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    Toast.makeText(MainActivity.this, "添加好友", Toast.LENGTH_SHORT).show();
                } else if (position == 2) {
                    Toast.makeText(MainActivity.this, "扫一扫", Toast.LENGTH_SHORT).show();
                }
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setContentView(contentView);
        ColorDrawable colorDrawable = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(colorDrawable);
        // 产生背景变暗的效果
        backgroundAlpha(0.4f);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAsDropDown(toolBar, toolBar.getWidth() + 10, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            byte[] bytes = data.getByteArrayExtra("photo");
            Log.d("ClarkMainActivity", "bytes:" + bytes);
            Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imgIcon.setImageBitmap(photo);
            imgHeaderIcon.setImageBitmap(photo);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
