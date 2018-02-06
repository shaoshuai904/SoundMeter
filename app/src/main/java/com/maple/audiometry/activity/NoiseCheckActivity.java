package com.maple.audiometry.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maple.audiometry.R;
import com.maple.audiometry.ui.chat.BrokenLineView;
import com.maple.audiometry.utils.ArrayUtils;
import com.maple.audiometry.utils.MediaRecorderDemo;
import com.maple.audiometry.utils.MediaRecorderDemo.NoiseValueUpdateCallback;
import com.maple.audiometry.utils.T;
import com.maple.audiometry.utils.permission.PermissionFragment;
import com.maple.audiometry.utils.permission.PermissionListener;
import com.maple.msdialog.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 噪音检测
 *
 * @author shaoshuai
 */
public class NoiseCheckActivity extends FragmentActivity {
    @BindView(R.id.tv_noise_value) TextView tv_noise_value;// 当前噪音值
    @BindView(R.id.tv_max_value) TextView tv_max_value;// 最大噪音值
    @BindView(R.id.tv_avg_value) TextView tv_avg_value;// 平均噪音值
    @BindView(R.id.tv_min_value) TextView tv_min_value;// 最小噪音值
    @BindView(R.id.ll_chart_view) LinearLayout ll_chart_view;// 折线图
    @BindView(R.id.ll_db_explain) LinearLayout ll_db_explain;// 分贝说明容器
    @BindView(R.id.tv_db_explain1) TextView tv_db_explain1;// 分贝说明1
    @BindView(R.id.tv_db_explain2) TextView tv_db_explain2;// 分贝说明2

    /**
     * 检测时间最大时间
     */
    private static final int checkTime = 15 * 1000;
    /**
     * 检测噪音的开始时间
     */
    private long startTime = 0;
    /**
     * 检测噪音工具类
     */
    private MediaRecorderDemo media;
    private BrokenLineView mBrokenLine;

    private double maxVolume = 0;
    private double minVolume = 99990;
    /**
     * 检测到的所有噪音分贝值
     */
    private List<Double> allVolume = new ArrayList<Double>();
    /**
     * 噪音分贝值 的说明文字
     */
    private String[] dbExplain;
    /**
     * 更新噪音标志
     */
    private static final int UPDATE_NOISE_VALUE = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_NOISE_VALUE:// 更新噪音值
                    double db = (Double) msg.obj;
                    long time = System.currentTimeMillis() - startTime;
                    if (time >= checkTime) {// 检测完成
                        media.stopRecord();
                        showDialog();
                    }
                    mBrokenLine.updateDate(ArrayUtils.sub(allVolume, mBrokenLine.maxCacheNum));
                    updateNoise(db);
                    break;
                case 2:// 进入下一个界面
                    break;
            }
        }

    };

    private void showDialog() {
        // 平均噪音分贝 > 40dB
        if (ArrayUtils.avg(allVolume) > 40) {
            new AlertDialog(NoiseCheckActivity.this)
                    .setScaleWidth(0.7)
                    .setMessage("您的监测环境不适合后面的测试，请您到较安静的环境下测试。")
                    .setLeftButton("取消", null)
                    .setRightButton("重新检测", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toMainPager();
                        }
                    })
                    .show();
        } else {
            new AlertDialog(NoiseCheckActivity.this)
                    .setScaleWidth(0.7)
                    .setMessage("您的测试环境良好，可以继续后面测试。")
                    .setLeftButton("取消", null)
                    .setRightButton("进入测试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toCheckEar();
                        }
                    })
                    .show();
        }
    }

    /**
     * 检测噪音
     */
    Runnable checkNoise = new Runnable() {
        @Override
        public void run() {
            // 波动较大。用的较多
            media = new MediaRecorderDemo(new NoiseValueUpdateCallback() {
                @Override
                public void onUpdateNoiseValue(double noiseValue) {
                    Message msg = Message.obtain();
                    msg.what = UPDATE_NOISE_VALUE;
                    msg.obj = noiseValue;
                    handler.sendMessage(msg);
                }
            });
            media.startRecord();
            startTime = System.currentTimeMillis();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise);
        ButterKnife.bind(this);

        mBrokenLine = new BrokenLineView(this);
        ll_chart_view.addView(mBrokenLine.execute(), new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        dbExplain = getResources().getStringArray(R.array.db_explain_arr);


        String[] permissionList = new String[]{Manifest.permission.RECORD_AUDIO};
        PermissionFragment.getPermissionFragment(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        handler.post(checkNoise);
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        T.showShort(getBaseContext(), "请开启录音权限！");
                    }

                    @Override
                    public void onPermissionDeniedDotAgain(String[] deniedPermissions) {
                    }
                })
                .checkPermissions(permissionList, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // // 按下的如果是BACK，同时没有重复
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog(NoiseCheckActivity.this)
                    .setScaleWidth(0.7)
                    .setTitle("是否退出检测？")
                    .setLeftButton("取消", null)
                    .setRightButton("退出", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .show();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (media != null) {
            media.stopRecord();// 停止检测
        }
        super.onDestroy();
    }

    /**
     * 去检查耳朵
     */
    private void toCheckEar() {
        finish();
        Intent intent = new Intent(NoiseCheckActivity.this, VoiceActivity.class);
        startActivity(intent);
    }

    /**
     * 返回主界面
     */
    private void toMainPager() {
        finish();
        // Intent intent = new Intent(NoiseCheckActivity.this,
        // MainActivity.class);
        // startActivity(intent);
    }

    /**
     * 更新噪音分贝值
     *
     * @param db
     */
    private void updateNoise(double db) {
        // Log.e("", "noiseValue：" + db);
        // 更新当前值
        tv_noise_value.setText((int) db + " dB");
        // 更新最大值
        if (db > maxVolume) {
            maxVolume = db;
            tv_max_value.setText("最高分贝:\n " + (int) maxVolume + " dB");
        }
        // 更新最小值
        if (db < minVolume && db != 0) {
            minVolume = db;
            tv_min_value.setText("最低分贝:\n " + (int) minVolume + " dB");
        }
        // 更新平均值
        if (db != 0) {
            allVolume.add(db);
            double avgVolume = ArrayUtils.avg(allVolume);
            tv_db_explain1.setText(dbExplain[(int) (avgVolume / 10)]);
            tv_db_explain2.setText(dbExplain[(int) (avgVolume / 10) + 1]);
            tv_avg_value.setText("平均分贝:\n " + (int) avgVolume + " dB");
        }
    }

}
