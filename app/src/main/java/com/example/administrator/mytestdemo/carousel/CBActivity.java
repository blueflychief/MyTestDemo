package com.example.administrator.mytestdemo.carousel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.carousel.cb.ConvenientBanner;
import com.example.administrator.mytestdemo.util.BitmapCallback;
import com.example.administrator.mytestdemo.util.BitmapUtils;
import com.example.administrator.mytestdemo.util.KLog;
import com.example.administrator.mytestdemo.widge.kenburnsview.KenBurnsView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class CBActivity extends ActionBarActivity implements BitmapCallback {
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private KenBurnsView kenBurnsView;//顶部广告栏控件
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private List<String> networkImages;
    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };


    private ImageView ivBlur;
    private int radius = 20;

    boolean isStart = false;
    private ScaleAnimation mScaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cb);
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        kenBurnsView = (KenBurnsView) findViewById(R.id.KenBurnsView);
        ivBlur = (ImageView) findViewById(R.id.ivBlur);
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Animation animation = AnimationUtils.loadAnimation(CBActivity.this, R.anim.anim_zoom_in);
//                ivBlur.clearAnimation();
//                ivBlur.startAnimation(animation);
                KLog.i("开始");
                mScaleAnimation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                mScaleAnimation.setDuration(4000);
                mScaleAnimation.setInterpolator(new OvershootInterpolator());
                mScaleAnimation.setFillAfter(true);
                ivBlur.clearAnimation();

                BitmapUtils.loadBluredBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_blur), radius, CBActivity.this);
            }
        }, 2000);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap) {
        if (!isStart) {
            ivBlur.startAnimation(mScaleAnimation);
        }

        ivBlur.setImageBitmap(bitmap);

        KLog.i("---radius:" + radius);
        if (radius < 2) {
            KLog.i("结束");
            return;
        }

        isStart = true;
        radius -= 1;
//        BitmapUtils.loadBluredBitmap(BitmapFactory.decodeResource(getResources(), radius < 30 ? R.drawable.ic_test_4_1 : R.drawable.ic_test_4_2), radius, CBActivity.this);
        BitmapUtils.loadBluredBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_blur), radius, CBActivity.this);
    }


    private void init() {
//
//        loadTestDatas();
//
//        ImageLoader.loadImage(this, "https://unsplash.it/1024/768", kenBurnsView);
//        networkImages = Arrays.asList(images);
//        convenientBanner
////                .setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
////                    @Override
////                    public NetworkImageHolderView createHolder() {
////                        return new NetworkImageHolderView();
////                    }
////                }, networkImages)
//                .setPages(new CBViewHolderCreator<LocalImageHolderView>() {
//                    @Override
//                    public LocalImageHolderView createHolder() {
//                        return new LocalImageHolderView();
//                    }
//                }, localImages)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                //本地图片例子
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
//                //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
////                .setOnPageChangeListener(this)//监听翻页事件
////                .setManualPageable(false);//设置不能手动影响
//                .setOnItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int position) {
//                        Toast.makeText(CBActivity.this, "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
//                    }
//                });

        //网络加载例子
//        networkImages=Arrays.asList(images);
//        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
//            @Override
//            public NetworkImageHolderView createHolder() {
//                return new NetworkImageHolderView();
//            }
//        },networkImages);


//手动New并且添加到ListView Header的例子
//        ConvenientBanner mConvenientBanner = new ConvenientBanner(this,false);
//        mConvenientBanner.setMinimumHeight(500);
//        mConvenientBanner.setPages(
//                new CBViewHolderCreator<LocalImageHolderView>() {
//                    @Override
//                    public LocalImageHolderView createHolder() {
//                        return new LocalImageHolderView();
//                    }
//                }, localImages)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
//                        //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnItemClickListener(this);
//        listView.addHeaderView(mConvenientBanner);
    }


    /*
    加入测试Views
    * */
    private void loadTestDatas() {
        //本地图片集合
        for (int position = 0; position < 7; position++)
            localImages.add(getResId("ic_test_" + position, R.drawable.class));
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(5000);

        if (kenBurnsView != null) {
            kenBurnsView.resume();
        }
    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
        if (kenBurnsView != null) {
            kenBurnsView.pause();
        }
    }


}
