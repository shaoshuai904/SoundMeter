package com.maple.audiometry.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.maple.audiometry.R;
import com.maple.audiometry.ui.base.BaseActivity;
import com.maple.audiometry.utils.ArrayUtils;
import com.maple.audiometry.utils.AudioTrackManager;
import com.maple.audiometry.utils.T;
import com.maple.msdialog.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试界面
 *
 * @author shaoshuai
 */
public class TestActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.tv_play_des) TextView tv_play_des;
    @BindView(R.id.tv_current_hz) TextView tv_current_hz;// 当前频率
    @BindView(R.id.tv_current_db) TextView tv_current_db;// 当前分贝
    @BindView(R.id.bt_play) Button bt_play;// 播放
    @BindView(R.id.bt_yes) Button bt_yes;// 可听到
    @BindView(R.id.bt_no) Button bt_no;// 听不到
    @BindView(R.id.tv_explain) TextView tv_explain;// 说明


    private static final int defCurDB = 10;
    private static final int defCurHZ = 0;
    private int[] hzArr = new int[]{1000, 2000, 4000, 8000, 500, 250};
    private int[] dBArr = new int[]{-10, -5, 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50,
            55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120};
    private int[][] lDBMinVal = new int[6][2];
    private int[][] rDBMinVal = new int[6][2];
    /** 当前分贝索引 */
    int curDB = defCurDB;
    /** 当前频率索引 */
    int curHZ = defCurHZ;
    /** 是否第一次听到 */
    boolean isFirst = true;
    /** 是否测试左耳 */
    boolean isLeft = true;
    /** 左耳测试完成 */
    boolean leftCheckOver = false;
    /** 右耳测试完成 */
    boolean rightCheckOver = false;

    AudioTrackManager audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        isLeft = bundle.getBoolean("isLeft");
        if (isLeft) {
            tv_explain.setText("左耳");
        } else {
            tv_explain.setText("右耳");
        }

        bt_play.setOnClickListener(this);
        bt_yes.setOnClickListener(this);
        bt_no.setOnClickListener(this);

    }

    @Override
    protected void onRestart() {
        if (audio != null)
            audio.stop();
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog(TestActivity.this)
                    .setTitle("是否退出当前测试？")
                    .setLeftButton("取消", null)
                    .setRightButton("退出", v -> finish())
                    .show();
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_play:// 播放
                start(hzArr[curHZ], dBArr[curDB], isLeft);
                // toResult();
                break;
            case R.id.bt_yes:// 可听到
                yesAction();
                break;
            case R.id.bt_no:// 听不到
                if (isLeft) {
                    if (lDBMinVal[curHZ][0] != 0) {
                        isFirst = false;// 不是第一次了
                    }
                } else {
                    if (rDBMinVal[curHZ][0] != 0) {
                        isFirst = false;// 不是第一次了
                    }
                }
                curDB = revise(curDB + 1, 0, dBArr.length - 1);
                tv_current_db.setText("当前分贝:\n " + dBArr[curDB] + " dB");
                // updateDB(hzArr[curHZ], dBArr[curDB]);
                break;
            default:
                break;
        }

    }

    /**
     * 听到-执行动作
     */
    private void yesAction() {
        if (isFirst) {// 第一次
            if (isLeft) {
                lDBMinVal[curHZ][0] = dBArr[curDB];// 保存左耳当前频率的第一次听到数据
            } else {
                rDBMinVal[curHZ][0] = dBArr[curDB];// 保存左耳当前频率的第一次听到数据
            }
            curDB = revise(curDB - 2, 0, dBArr.length - 1);// 更换分贝 （ 减10分贝）
            tv_current_db.setText("当前分贝:\n " + dBArr[curDB] + " dB");
            // updateDB(hzArr[curHZ], dBArr[curDB]);
        } else {// 第二次测试左耳最低值
            int sub = 0;
            if (isLeft) {
                lDBMinVal[curHZ][1] = dBArr[curDB];// 保存当前频率的第二次听到数据
                // 同第一次做对比。
                sub = lDBMinVal[curHZ][1] - lDBMinVal[curHZ][0];
                Log.e("计算左耳差值", lDBMinVal[curHZ][0] + "减去"
                        + lDBMinVal[curHZ][1] + " = " + sub);
                // 取两次平均值，更新视图
                // mMelodyView.updateData(curHZ,
                // ArrayUtils.avg(lDBMinVal[curHZ]),
                // isLeft);// 更新折线图
            } else {
                // Log.e("左耳第二次听到", hzArr[curHZ] + "Hz记录:" + dBArr[curDB]);
                rDBMinVal[curHZ][1] = dBArr[curDB];// 保存当前频率的第二次听到数据
                // 同第一次做对比。
                sub = rDBMinVal[curHZ][1] - rDBMinVal[curHZ][0];
                // Log.e("计算右耳差值", rDBMinVal[curHZ][0] + "减去"
                // + rDBMinVal[curHZ][1] + " = " + sub);
                // 取两次平均值，更新视图
                // mMelodyView.updateData(curHZ,
                // ArrayUtils.avg(rDBMinVal[curHZ]),
                // isLeft);// 更新折线图
            }
            if (Math.abs(sub) > 20) {
                T.showShort(getBaseContext(), "因为您的两次测试结果相差巨大，需要重测该频率");
                // 如果相差巨大，重新测试。
                lDBMinVal[curHZ][0] = 0;
                lDBMinVal[curHZ][1] = 0;
                curDB = defCurDB;// 重置分贝
                isFirst = true;// 又是第一次
                tv_play_des.setText(hzArr[curHZ] + " Hz\n" + dBArr[curDB] + " dB");
                tv_current_hz.setText("当前频率:\n " + hzArr[curHZ] + " Hz");
                tv_current_db.setText("当前分贝:\n " + dBArr[curDB] + " dB");

                // mMelodyView.updateData(curHZ, dBArr[curDB], isLeft);// 更新折线图
                // updateDB(hzArr[curHZ], dBArr[curDB]);
            } else {
                // 如果相差不大。进入下一频率
                if (curHZ < hzArr.length - 1) {
                    curHZ++;// 更换频率
                    curDB = defCurDB;// 重置分贝
                    isFirst = true;// 又是第一次
                    tv_play_des.setText(hzArr[curHZ] + " Hz\n" + dBArr[curDB] + " dB");
                    tv_current_hz.setText("当前频率:\n " + hzArr[curHZ] + " Hz");
                    tv_current_db.setText("当前分贝:\n " + dBArr[curDB] + " dB");

                    // mMelodyView.updateData(curHZ, dBArr[curDB], isLeft);//
                    // 更新折线图
                    // updateDB(hzArr[curHZ], dBArr[curDB]);
                } else {
                    // 切换耳朵。或者进入结果页面
                    if (isLeft) {// 当前测试完成-左耳。
                        leftCheckOver = true;
                        if (rightCheckOver) {
                            Log.e("TestActivity", "进入结果页面");
                            toResult();
                        } else {
                            checkRight();// 测试右耳
                        }
                    } else {// 当前测试完成-右耳
                        rightCheckOver = true;// 标记右耳检测完毕
                        if (leftCheckOver) {
                            Log.e("TestActivity", "进入结果页面");
                            toResult();
                        } else {
                            checkLeft();// 测试左耳
                        }
                    }
                }
            }
        }
    }

    /**
     * 检测右耳
     */
    private void checkRight() {
        isLeft = false;
        isFirst = true;
        rDBMinVal = new int[6][2];
        curDB = defCurDB;
        curHZ = defCurHZ;
        tv_play_des.setText(hzArr[curHZ] + " Hz\n" + dBArr[curDB] + " dB");
        tv_current_hz.setText("当前频率:\n " + hzArr[curHZ] + " Hz");
        tv_current_db.setText("当前分贝:\n " + dBArr[curDB] + " dB");
        tv_explain.setText("右耳");
    }

    /**
     * 检查左耳
     */
    private void checkLeft() {
        isLeft = true;
        isFirst = true;
        lDBMinVal = new int[6][2];
        curDB = defCurDB;
        curHZ = defCurHZ;
        tv_play_des.setText(hzArr[curHZ] + " Hz\n" + dBArr[curDB] + " dB");
        tv_current_hz.setText("当前频率:\n " + hzArr[curHZ] + " Hz");
        tv_current_db.setText("当前分贝:\n " + dBArr[curDB] + " dB");
        tv_explain.setText("左耳");
    }

    /**
     * 去结果页面
     */
    private void toResult() {
        int[] lDB = new int[6];
        int[] rDB = new int[6];
        for (int i = 0; i < lDB.length; i++) {
            lDB[i] = ArrayUtils.avg(lDBMinVal[i]);
            rDB[i] = ArrayUtils.avg(rDBMinVal[i]);
        }

        finish();
        Intent intent = new Intent(TestActivity.this, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putIntArray("left", lDB);// 左耳听力数据
        bundle.putIntArray("right", rDB);// 右耳听力数据
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 播放声音
     *
     * @param hz     频率角标
     * @param isLeft 是否是左耳
     */
    public void start(int hz, int db, boolean isLeft) {
        if (audio != null)
            audio.stop();
        audio = new AudioTrackManager();
        audio.setRate(hz, db);// 设置频率
        if (isLeft) {
            audio.start(AudioTrackManager.LEFT);
        } else {
            audio.start(AudioTrackManager.RIGHT);
        }
        audio.play();
    }

    /**
     * 修正值，防止越界
     *
     * @param min
     * @param max
     */
    private int revise(int num, int min, int max) {
        if (num < min) {
            num = min;
        }
        if (num > max) {
            num = max;
        }
        return num;
    }

}
