package com.maple.audiometry.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.maple.audiometry.R;

import java.util.ArrayList;
import java.util.List;

import static com.maple.msdialog.utils.DensityUtils.dp2px;
import static com.maple.msdialog.utils.DensityUtils.sp2px;

/**
 * 歌曲旋律图
 *
 * @author shao
 */
public class MelodyView extends View {
    /** 网格线画笔 */
    private Paint paintLine;
    /** 矩形笔 */
    private Paint recPaint;

    private int modly = DEF_MODLY;
    public static final int ONLY_LEFT = 1;
    public static final int ONLY_RIGHT = 2;
    public static final int DEF_MODLY = 0;

    /** 上下文边距 */
    private ContextMargin margin = new ContextMargin();
    String[] xStr = {"1000HZ", "2000HZ", "4000HZ", "8000HZ", "500HZ", "250HZ"};
    /** X轴刻度数 */
    private int xLineCount = 6;
    /** Y轴刻度数 */
    private int yLineCount = 14;
    /** Y轴最大值 */
    private int yMaxValue = 120;
    /** Y轴最小值 */
    private int yMinValue = -10;
    /** X轴刻度点数组 */
    private int[] xAxisPointArr = null;
    /** Y轴刻度点数组 */
    private int[] yAxisPointArr = null;

    /** 指针图像列表 */
    private List<Drawable> pointerImgList = null;
    /** 指针线 */
    private Drawable tagImg = null;
    /** 视图的宽 */
    private float viewWidth;
    /** 视图的高 */
    private float viewHeight;
    /** 左耳测试数据 */
    private int[] leftDatas;
    /** 右耳测试数据 */
    private int[] rightDatas;
    private Context mContext;

    public MelodyView(Context context, int modly) {
        super(context);
        this.modly = modly;
        this.mContext = context;
        init();
    }

    public MelodyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public MelodyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    /**
     * 初始化画笔
     */
    private void init() {
        // 网格线画笔
        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.GREEN);
        paintLine.setFakeBoldText(true);
        paintLine.setStrokeWidth(1.5f);
        // 矩形画笔
        recPaint = new Paint();
        recPaint.setColor(Color.RED);
        recPaint.setTextAlign(Align.RIGHT);
        recPaint.setTextSize(sp2px(mContext, 10));// 设置字体大小
        recPaint.setTypeface(Typeface.SANS_SERIF);// 设置字体样式

        margin.updateMargin(dp2px(mContext, 20),
                dp2px(mContext, 12),
                dp2px(mContext, 12),
                dp2px(mContext, 12));// 初始化视图边距
        // 初始化数据
        leftDatas = new int[xLineCount];
        rightDatas = new int[xLineCount];
        for (int i = 0; i < xLineCount; i++) {
            leftDatas[i] = yMinValue;
            rightDatas[i] = yMinValue;
        }
        // 当前位置-图片
        // 初始化指针
        pointerImgList = new ArrayList<Drawable>();
        pointerImgList.add(ContextCompat.getDrawable(mContext, R.drawable.left_ear_tag));
        pointerImgList.add(ContextCompat.getDrawable(mContext, R.drawable.right_ear_tag));
        tagImg = pointerImgList.get(0);
    }

    /**
     * 获取左耳听力数据
     */
    public int[] getLeftDate() {
        return leftDatas;
    }

    /**
     * 获取右耳听力数据
     */
    public int[] getRightDate() {
        return rightDatas;
    }

    /**
     * 更新数据
     *
     * @param xIndex 索引
     * @param value  更新值
     */
    public void updateData(int xIndex, int value, boolean isLeft) {
        if (isLeft) {
            leftDatas[xIndex] = value;
            tagImg = pointerImgList.get(0);
        } else {
            rightDatas[xIndex] = value;
            tagImg = pointerImgList.get(1);
        }
        invalidate();// 更新界面
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFrame(canvas);// 画坐标系
        drawPoint(canvas);// 画点
        super.onDraw(canvas);
    }

    /**
     * 绘制坐标系
     */
    private void drawFrame(Canvas canvas) {
        paintLine.setColor(Color.GREEN);
        paintLine.setStrokeWidth(1.5f);
        // 绘制横线
        float yVol = yMaxValue - yMinValue;
        recPaint.setTextAlign(Align.RIGHT);// 右边是线
        for (int i = 0; i < yLineCount; i++) {
            canvas.drawLine(margin.getLeft(), yAxisPointArr[i], getWidth()
                    - margin.getRight(), yAxisPointArr[i], paintLine);
            // dB 刻度
            int yVal = (int) (yMinValue + (yVol / (yLineCount - 1)) * i);
            canvas.drawText(yVal + "",
                    margin.getLeft() - dp2px(mContext, 2),
                    yAxisPointArr[i] + dp2px(mContext, 4),
                    recPaint);
        }
        // 画竖线
        recPaint.setTextAlign(Align.CENTER);// 线中间
        for (int j = 0; j < xLineCount; j++) {
            canvas.drawLine(xAxisPointArr[j], margin.getTop(), xAxisPointArr[j],
                    this.getHeight() - margin.getBottom(), paintLine);
            // Hz 刻度
            canvas.drawText(xStr[j], xAxisPointArr[j], margin.getTop() - dp2px(mContext, 2), recPaint);
        }

    }

    /**
     * 绘点
     */
    private void drawPoint(Canvas canvas) {
        switch (modly) {
            case DEF_MODLY:
                // 画： 左耳-蓝叉-蓝色连线
                tagImg = pointerImgList.get(0);
                drawPointTag(canvas, xAxisPointArr[0], leftDatas[0]);
                paintLine.setColor(Color.BLUE);
                paintLine.setStrokeWidth(2.0f);
                for (int j = 0; j < xLineCount - 1; j++) {
                    // if (leftDatas[j + 1] != 0) {
                    canvas.drawLine(xAxisPointArr[j], getLevel(leftDatas[j]),
                            xAxisPointArr[j + 1], getLevel(leftDatas[j + 1]),
                            paintLine);
                    drawPointTag(canvas, xAxisPointArr[j + 1], leftDatas[j + 1]);
                    // }
                }
                // 画:右耳-红圆-红色连线
                tagImg = pointerImgList.get(1);
                drawPointTag(canvas, xAxisPointArr[0], rightDatas[0]);
                paintLine.setColor(Color.RED);
                paintLine.setStrokeWidth(2.0f);
                for (int j = 0; j < xLineCount - 1; j++) {
                    // if (rightDatas[j + 1] != 0) {
                    canvas.drawLine(xAxisPointArr[j], getLevel(rightDatas[j]),
                            xAxisPointArr[j + 1], getLevel(rightDatas[j + 1]),
                            paintLine);
                    drawPointTag(canvas, xAxisPointArr[j + 1], rightDatas[j + 1]);
                    // }
                }
                break;
            case ONLY_LEFT:
                // 画： 左耳-蓝叉-蓝色连线
                tagImg = pointerImgList.get(0);
                drawPointTag(canvas, xAxisPointArr[0], leftDatas[0]);
                paintLine.setColor(Color.BLUE);
                paintLine.setStrokeWidth(2.0f);
                for (int j = 0; j < xLineCount - 1; j++) {
                    // if (leftDatas[j + 1] != 0) {
                    canvas.drawLine(xAxisPointArr[j], getLevel(leftDatas[j]),
                            xAxisPointArr[j + 1], getLevel(leftDatas[j + 1]),
                            paintLine);
                    drawPointTag(canvas, xAxisPointArr[j + 1], leftDatas[j + 1]);
                    // }
                }
                break;
            case ONLY_RIGHT:
                // 画:右耳-红圆-红色连线
                tagImg = pointerImgList.get(1);
                drawPointTag(canvas, xAxisPointArr[0], rightDatas[0]);
                paintLine.setColor(Color.RED);
                paintLine.setStrokeWidth(2.0f);
                for (int j = 0; j < xLineCount - 1; j++) {
                    // if (rightDatas[j + 1] != 0) {
                    canvas.drawLine(xAxisPointArr[j], getLevel(rightDatas[j]),
                            xAxisPointArr[j + 1], getLevel(rightDatas[j + 1]),
                            paintLine);
                    drawPointTag(canvas, xAxisPointArr[j + 1], rightDatas[j + 1]);
                    // }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 画点标记
     */
    private void drawPointTag(Canvas canvas, int x, int y) {
        int left = x - tagImg.getIntrinsicWidth() / 2;
        int right = x + tagImg.getIntrinsicWidth() / 2;
        int top = getLevel(y) - tagImg.getIntrinsicHeight() / 2;
        int bot = getLevel(y) + tagImg.getIntrinsicHeight() / 2;
        // 设置界限
        tagImg.setBounds(left, top, right, bot);
        tagImg.draw(canvas);
    }

    /**
     * 根据Y的值得到在Y轴对应位置。
     */
    private int getLevel(int data) {
        // Y轴坐标 从上往下，递减
        // float tempInterval = (yMaxValue - yMinValue) / viewHeight; //
        // Y轴1像素对应的数值
        // int yPoint = (int) (viewHeight - (Math.abs(data - yMinValue))
        // / tempInterval + margin.top);
        // return yPoint;
        // Y轴坐标 从上往下，递增
        float tempInterval = (yMaxValue - yMinValue) / viewHeight; // Y轴1像素对应的数值
        int yPoint = (int) (viewHeight - (Math.abs(yMaxValue - data)) / tempInterval + margin.getTop());
        return yPoint;
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        measureMargin();
    }

    /**
     * 计算边距值
     */
    private void measureMargin() {
        // 计算X轴刻度
        viewWidth = this.getWidth() - margin.getLeft() - margin.getRight();
        xAxisPointArr = new int[xLineCount];
        for (int i = 0; i < xAxisPointArr.length; i++) {
            xAxisPointArr[i] = (int) (viewWidth / (xLineCount - 1) * i + margin.getLeft());
        }
        // 计算Y轴刻度
        viewHeight = this.getHeight() - margin.getTop() - margin.getBottom();
        yAxisPointArr = new int[yLineCount];
        for (int i = 0; i < yAxisPointArr.length; i++) {
            yAxisPointArr[i] = (int) (viewHeight / (yLineCount - 1) * i + margin.getTop());
        }
    }

    // --------------------------------------------------------------------------------------

    /**
     * 设置Y轴最大值,最小值
     */
    public void setYScope(int maxValue, int minValue) {
        yMaxValue = maxValue;
        yMinValue = minValue;
    }
}
