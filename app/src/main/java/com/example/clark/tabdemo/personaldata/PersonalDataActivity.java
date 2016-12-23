package com.example.clark.tabdemo.personaldata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.clark.tabdemo.R;
import com.example.clark.tabdemo.tablayout.SegmentTabLayout;
import com.example.clark.tabdemo.utils.OnScrollListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PersonalDataActivity extends AppCompatActivity implements View.OnClickListener, OnScrollListener.ScrollViewListener {

    @InjectView(R.id.img_personal_bk)
    ImageView imgPersonalBk;
    @InjectView(R.id.my_Scroll_personal)
    OnScrollListener myScrollPersonal;
    @InjectView(R.id.rlayout_personal)
    RelativeLayout rlayoutPersonal;
    @InjectView(R.id.llayout_personal)
    LinearLayout llayoutPersonal;

    private ImageView imgPersonalIcon;
    private Button btnLocal, btnPhotograph, btnCancel;
    private PopupWindow mChangePopupWindow;

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;

    private Bitmap photo;
    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.inject(this);
        imgPersonalIcon = (ImageView) findViewById(R.id.img_personal_icon);
        imgPersonalIcon.setOnClickListener(this);
        imgPersonalBk.setOnClickListener(this);
        slidePictureColor();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_personal_bk:
                finish();
                break;

            case R.id.img_personal_icon:
                changeHeadPopupWindow();
                mChangePopupWindow.showAtLocation(imgPersonalIcon, Gravity.BOTTOM, 0, 0);
                mChangePopupWindow.setOutsideTouchable(true);
                break;

            case R.id.local_btn:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent, ALBUM_REQUEST_CODE);
                mChangePopupWindow.dismiss();
                break;

            case R.id.photograph_btn:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                mChangePopupWindow.dismiss();
                break;

            case R.id.cancel_btn:
                mChangePopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void finish() {
        if (photo != null) {
            // 有头像
            Intent intent = new Intent();
            intent.putExtra("photo", bytes);
            Log.d("ClarkData", "bytes---->" + bytes);
            setResult(RESULT_OK, intent);
        } else {
            // 没有头像
            setResult(0);
        }
        super.finish();
    }

    private void changeHeadPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.personal_change_head_popuwindow, null);
        mChangePopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mChangePopupWindow.setAnimationStyle(R.style.change_popupwindow_anim_style);
        mChangePopupWindow.setContentView(view);
        mChangePopupWindow.setOutsideTouchable(true);
        mChangePopupWindow.setFocusable(true);

        btnLocal = (Button) view.findViewById(R.id.local_btn);
        btnPhotograph = (Button) view.findViewById(R.id.photograph_btn);
        btnCancel = (Button) view.findViewById(R.id.cancel_btn);

        btnLocal.setOnClickListener(this);
        btnPhotograph.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    /**
     * 开始裁剪
     */
    private void startCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");// 进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", imgPersonalIcon.getWidth());
        intent.putExtra("outputY", imgPersonalIcon.getWidth());
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    /**
     * 判断sdcard卡是否可用
     * return布尔类型 true 可用 false 不可用
     */
    private boolean isSDCardCanUser() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                startCrop(data.getData());
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
                startCrop(Uri.fromFile(picture));
                break;
            case CROP_REQUEST_CODE:
                if (data == null) {
                    // TODO 如果之前以后有设置过显示之前设置的图片 否则显示默认的图片
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    photo = extras.getParcelable("data");
                    // 可以捕获内存缓冲区的数据，转换成字节数组
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // 此处可以把Bitmap保存到sd卡中
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                    // 把图片显示在ImageView控件上
                    imgPersonalIcon.setImageBitmap(photo);
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 返回的是一个字节数组
                    bytes = stream.toByteArray();
                    Log.d("ClarkData", "bytes:" + bytes);
                }
                break;
            default:
                break;
        }
    }

    private int height;

    private void slidePictureColor() {
        ViewTreeObserver observer = rlayoutPersonal.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlayoutPersonal.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = rlayoutPersonal.getHeight();
                rlayoutPersonal.getWidth();
                myScrollPersonal.setScrollViewListener(PersonalDataActivity.this);
            }
        });
    }

    @Override
    public void onScrollChanged(OnScrollListener scrollView, int x, int y, int oldx, int oldy) {
        if (y <= height) {
            float scale = (float) y / height;
            float alpha = (255 * scale);
            llayoutPersonal.setBackgroundColor(Color.argb((int) alpha, 0x2C, 0x97, 0xDE ));
        }
    }
}
