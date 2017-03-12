package com.maple.audiometry.chat;

import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

/**
 * 折线图
 */
public class BrokenLineView extends View {

	private GraphicalView mChartView;
	/** 渲染器 */
	private XYMultipleSeriesRenderer mRenderer;
	/** 数据集合 */
	private XYMultipleSeriesDataset mDataset;
	/** 时间序列 */
	private TimeSeries timeSeries;
	XYSeries xySeries;
	CategorySeries catSeries;

	private Context mContext;

	/** 最大缓存数 */
	public int maxCacheNum = 100;
	/** X轴的时间缓存 */
	Date[] xCache = new Date[maxCacheNum];
	/** Y轴的值缓存 */
	int[] yCache = new int[maxCacheNum];

	public BrokenLineView(Context context) {
		super(context);
		mContext = context;
	}

	public GraphicalView execute() {
		if (mChartView == null) {
			// 时间戳样式
			mChartView = ChartFactory.getTimeChartView(mContext,
					getTimeSeriesDataset(), getDemoRenderer(), "mm:ss");
		}
		return mChartView;
	}

	// /**
	// * 更新视图
	// */
	// public void updateTimeChart(double yValue) {
	// mDataset.removeSeries(timeSeries);
	// Log.e("", "当前数组大小：" + timeSeries.getItemCount());
	// timeSeries.add(new Date(), yValue);
	// Log.e("", "添加" + timeSeries.getItemCount());
	// if (timeSeries.getItemCount() >= maxCacheNum) {
	// timeSeries.remove(0);
	// Log.e("", "删除：" + timeSeries.getItemCount());
	// }
	// // 在数据集中添加新的点集
	// mDataset.addSeries(timeSeries);
	// // 曲线更新
	// mChartView.invalidate();
	// }

	/**
	 * 更新视图
	 * 
	 * @param dates
	 */
	public void updateDate(double[] shorts) {
		mDataset.removeSeries(timeSeries);
		timeSeries.clear();
		for (int i = 0; i < shorts.length; i++) {
			timeSeries.add(new Date(), shorts[i]);
		}
		// 在数据集中添加新的点集
		mDataset.addSeries(timeSeries);
		// 曲线更新
		mChartView.invalidate();
	}

	// /**
	// * 更新视图
	// */
	// public void updateTimeChart(short[] shorts) {
	// mDataset.removeSeries(timeSeries);
	// for (int i = 0; i < shorts.length; i++) {
	// timeSeries.add(new Date(), shorts[i]);
	// }
	// while (timeSeries.getItemCount() >= maxCacheNum) {
	// timeSeries.remove(0);
	// }
	// // 在数据集中添加新的点集
	// mDataset.addSeries(timeSeries);
	// // 曲线更新
	// mChartView.invalidate();
	// }

	/**
	 * 获取渲染器
	 * 
	 * @return
	 */
	public XYMultipleSeriesRenderer getDemoRenderer() {
		mRenderer = new XYMultipleSeriesRenderer();
		// mRenderer.setChartTitle("随机数据");// 标题
		// mRenderer.setXTitle("时间"); // x轴说明
		mRenderer.setYTitle("噪音分贝强度");
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setAxisTitleTextSize(16);
		mRenderer.setAxesColor(Color.BLACK);
		mRenderer.setLabelsTextSize(15); // 数轴刻度字体大小
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setLegendTextSize(15); // 曲线说明
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setShowLegend(false);
		mRenderer.setMargins(new int[] { 5, 30, 15, 2 });// 上左下右{ 20, 30, 100, 0
															// })
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.RED);
		r.setChartValuesTextSize(15);
		r.setChartValuesSpacing(3);
		r.setPointStyle(PointStyle.POINT);
		r.setFillBelowLine(true);
		r.setFillBelowLineColor(Color.WHITE);
		r.setFillPoints(true);
		mRenderer.addSeriesRenderer(r);
		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setPanEnabled(false, false);
		mRenderer.setShowGrid(true);
		// mRenderer.setYLabels(10);// 设置Y轴默认显示个数
		mRenderer.setXLabels(0);// 设置X轴默认显示个数
		mRenderer.setYAxisMax(90);// 纵坐标最大值
		mRenderer.setYAxisMin(0);// 纵坐标最小值
		mRenderer.setInScroll(true);
		return mRenderer;
	}

	/**
	 * 获取时间戳的数据
	 * 
	 * @return
	 */
	public XYMultipleSeriesDataset getTimeSeriesDataset() {
		mDataset = new XYMultipleSeriesDataset();
		timeSeries = new TimeSeries("无用标题");
		// 初始化一部分数据
		for (int k = 0; k < maxCacheNum; k++) {
			timeSeries.add(new Date(), 0);
		}
		mDataset.addSeries(timeSeries);
		return mDataset;
	}

}
