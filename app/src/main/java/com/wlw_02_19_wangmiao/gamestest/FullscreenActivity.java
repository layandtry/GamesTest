package com.wlw_02_19_wangmiao.gamestest;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private int recLen = 10;
    private TextView txtView;
    /**
     * 游戏部分的代码
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        txtView = (TextView)findViewById(R.id.timer);
        handler.postDelayed(runnable, 1000);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight = dm.heightPixels - 50;//获取屏幕高度
        ImageView book = (ImageView) findViewById(R.id.BookImage);
        ImageView dress = (ImageView) findViewById(R.id.DressImage);//找到裙子的id
        ImageView jeans = (ImageView) findViewById(R.id.JeansImage);//牛仔裤
        ImageView socks = (ImageView) findViewById(R.id.SocksImage);//袜子
        ImageView hoodies = (ImageView) findViewById(R.id.HoodiesImage);//袜子
        ImageView Tshirt = (ImageView)findViewById(R.id.TshirtImage);//短袖
        final View.OnTouchListener child;

        child = new View.OnTouchListener() {//对新创建的image进行监听

            int lastX, lastY;// 记录移动的最后的位置
            // 获取Action
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:// 按下
                        lastX = (int) event.getRawX();//获取事件发生时的坐标
                        lastY = (int) event.getRawY();
                        break;
                    /**
                     * layout(l,t,r,b) l Left position, relative to parent t Top
                     * position, relative to parent r Right position, relative to
                     * parent b Bottom position, relative to parent
                     */
                    case MotionEvent.ACTION_MOVE:   // 移动中动态设置位置
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }
                        v.layout(left, top, right, bottom);//将移动后的图相在新位置显示出来
                        Log.i("", "position:" + left + ", " + top + ", " + right
                                + ", " + bottom);


                        ((RelativeLayout.LayoutParams) (v.getLayoutParams())).leftMargin = left;

                        ((RelativeLayout.LayoutParams) (v.getLayoutParams())).topMargin = top;

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;

            }

        };

        dress.setOnTouchListener(new View.OnTouchListener() {

            int lastX, lastY;
            ImageView image;
            RelativeLayout.LayoutParams params;
            int left;
            int top;
            int right;
            int bottom;

            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:

                        RelativeLayout parentcontener = (RelativeLayout) findViewById(R.id.parentcontent);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        ImageView parent = (ImageView) findViewById(R.id.DressImage);
                        params = new RelativeLayout.LayoutParams(parent.getWidth(), parent
                                .getHeight());
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        image = new ImageView(FullscreenActivity.this);

                        image.setLayoutParams(params);
                        parentcontener.addView(image);
                        image.setImageResource(R.drawable.dress02);
                        image.setOnTouchListener(child);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        left = image.getLeft() + dx;
                        top = image.getTop() + dy;
                        right = image.getRight() + dx;
                        bottom = image.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + image.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - image.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + image.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - image.getHeight();
                        }
                        image.layout(left, top, right, bottom);
                        Log.i("", "position:" + left + ", " + top + ", " + right
                                + ", " + bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        params.leftMargin=left;
                        params.topMargin=top;
                        break;
                    case MotionEvent.ACTION_UP:
                        // params.setMargins(left, top, right, bottom);加上这句话，运行的时候可能出现小bug，但不是

                        //每次都出现，可测试一下

                        break;
                }
                return true;
            }
        });
        jeans.setOnTouchListener(new View.OnTouchListener() {

            int lastX, lastY;
            ImageView image;
            RelativeLayout.LayoutParams params;
            int left;
            int top;
            int right;
            int bottom;

            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:

                        RelativeLayout parentcontener = (RelativeLayout) findViewById(R.id.parentcontent);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        ImageView parent = (ImageView) findViewById(R.id.JeansImage);
                        params = new RelativeLayout.LayoutParams(parent.getWidth(), parent
                                .getHeight());
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        image = new ImageView(FullscreenActivity.this);

                        image.setLayoutParams(params);
                        parentcontener.addView(image);
                        image.setImageResource(R.drawable.jean01);
                        image.setOnTouchListener(child);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        left = image.getLeft() + dx;
                        top = image.getTop() + dy;
                        right = image.getRight() + dx;
                        bottom = image.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + image.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - image.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + image.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - image.getHeight();
                        }
                        image.layout(left, top, right, bottom);
                        Log.i("", "position:" + left + ", " + top + ", " + right
                                + ", " + bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        params.leftMargin=left;
                        params.topMargin=top;
                        break;
                    case MotionEvent.ACTION_UP:
                        // params.setMargins(left, top, right, bottom);加上这句话，运行的时候可能出现小bug，但不是

                        //每次都出现，可测试一下

                        break;
                }
                return true;
            }
        });

        socks.setOnTouchListener(new View.OnTouchListener() {

            int lastX, lastY;
            ImageView image;
            RelativeLayout.LayoutParams params;
            int left;
            int top;
            int right;
            int bottom;

            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:

                        RelativeLayout parentcontener = (RelativeLayout) findViewById(R.id.parentcontent);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        ImageView parent = (ImageView) findViewById(R.id.SocksImage);
                        params = new RelativeLayout.LayoutParams(parent.getWidth(), parent
                                .getHeight());
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        image = new ImageView(FullscreenActivity.this);

                        image.setLayoutParams(params);
                        parentcontener.addView(image);
                        image.setImageResource(R.drawable.socks);
                        image.setOnTouchListener(child);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        left = image.getLeft() + dx;
                        top = image.getTop() + dy;
                        right = image.getRight() + dx;
                        bottom = image.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + image.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - image.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + image.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - image.getHeight();
                        }
                        image.layout(left, top, right, bottom);
                        Log.i("", "position:" + left + ", " + top + ", " + right
                                + ", " + bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        params.leftMargin=left;
                        params.topMargin=top;
                        break;
                    case MotionEvent.ACTION_UP:
                        // params.setMargins(left, top, right, bottom);加上这句话，运行的时候可能出现小bug，但不是

                        //每次都出现，可测试一下

                        break;
                }
                return true;
            }
        });

        hoodies.setOnTouchListener(new View.OnTouchListener() {

            int lastX, lastY;
            ImageView image;
            RelativeLayout.LayoutParams params;
            int left;
            int top;
            int right;
            int bottom;

            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:

                        RelativeLayout parentcontener = (RelativeLayout) findViewById(R.id.parentcontent);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        ImageView parent = (ImageView) findViewById(R.id.HoodiesImage);
                        params = new RelativeLayout.LayoutParams(parent.getWidth(), parent
                                .getHeight());
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        image = new ImageView(FullscreenActivity.this);

                        image.setLayoutParams(params);
                        parentcontener.addView(image);
                        image.setImageResource(R.drawable.hoodies);
                        image.setOnTouchListener(child);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        left = image.getLeft() + dx;
                        top = image.getTop() + dy;
                        right = image.getRight() + dx;
                        bottom = image.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + image.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - image.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + image.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - image.getHeight();
                        }
                        image.layout(left, top, right, bottom);
                        Log.i("", "position:" + left + ", " + top + ", " + right
                                + ", " + bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        params.leftMargin=left;
                        params.topMargin=top;
                        break;
                    case MotionEvent.ACTION_UP:
                        // params.setMargins(left, top, right, bottom);加上这句话，运行的时候可能出现小bug，但不是

                        //每次都出现，可测试一下

                        break;
                }
                return true;
            }
        });

        Tshirt.setOnTouchListener(new View.OnTouchListener() {

            int lastX, lastY;
            ImageView image;
            RelativeLayout.LayoutParams params;
            int left;
            int top;
            int right;
            int bottom;

            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:

                        RelativeLayout parentcontener = (RelativeLayout) findViewById(R.id.parentcontent);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        ImageView parent = (ImageView) findViewById(R.id.TshirtImage);
                        params = new RelativeLayout.LayoutParams(parent.getWidth(), parent
                                .getHeight());
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        image = new ImageView(FullscreenActivity.this);

                        image.setLayoutParams(params);
                        parentcontener.addView(image);
                        image.setImageResource(R.drawable.t_shirt);
                        image.setOnTouchListener(child);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        left = image.getLeft() + dx;
                        top = image.getTop() + dy;
                        right = image.getRight() + dx;
                        bottom = image.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + image.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - image.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + image.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - image.getHeight();
                        }
                        image.layout(left, top, right, bottom);
                        Log.i("", "position:" + left + ", " + top + ", " + right
                                + ", " + bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        params.leftMargin=left;
                        params.topMargin=top;
                        break;
                    case MotionEvent.ACTION_UP:
                        // params.setMargins(left, top, right, bottom);加上这句话，运行的时候可能出现小bug，但不是

                        //每次都出现，可测试一下

                        break;
                }
                return true;
            }
        });

        book.setOnTouchListener(new View.OnTouchListener() {

            int lastX, lastY;
            ImageView image;
            RelativeLayout.LayoutParams params;
            int left;
            int top;
            int right;
            int bottom;

            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                Log.i("TAG", "Touch:" + ea);
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:

                        RelativeLayout parentcontener = (RelativeLayout) findViewById(R.id.parentcontent);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        ImageView parent = (ImageView) findViewById(R.id.BookImage);
                        params = new RelativeLayout.LayoutParams(parent.getWidth(), parent
                                .getHeight());
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        image = new ImageView(FullscreenActivity.this);

                        image.setLayoutParams(params);
                        parentcontener.addView(image);
                        image.setImageResource(R.drawable.books);
                        image.setOnTouchListener(child);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        left = image.getLeft() + dx;
                        top = image.getTop() + dy;
                        right = image.getRight() + dx;
                        bottom = image.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + image.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - image.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + image.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - image.getHeight();
                        }
                        image.layout(left, top, right, bottom);
                        Log.i("", "position:" + left + ", " + top + ", " + right
                                + ", " + bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        params.leftMargin=left;
                        params.topMargin=top;
                        break;
                    case MotionEvent.ACTION_UP:
                        // params.setMargins(left, top, right, bottom);加上这句话，运行的时候可能出现小bug，但不是

                        //每次都出现，可测试一下

                        break;
                }
                return true;
            }
        });


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try{
                if (recLen > 0){
                    recLen--;
                    txtView.setText("" + recLen);
                    handler.postDelayed(this, 1000);
                }else{
                    Toast.makeText(FullscreenActivity.this,"游戏失败！请重新开始！",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
