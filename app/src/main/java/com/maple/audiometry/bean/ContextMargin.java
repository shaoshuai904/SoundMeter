package com.maple.audiometry.bean;

/**
 * 上下文边距
 * 
 * @author shao
 * 
 */
public class ContextMargin {
	public int bottom = 0;
	public int left = 0;
	public int right = 0;
	public int top = 0;

	public ContextMargin() {
	}

	/**
	 * 设置统一边距
	 * 
	 * @param paramInt
	 */
	public ContextMargin(int paramInt) {
		this.left = paramInt;
		this.top = paramInt;
		this.right = paramInt;
		this.bottom = paramInt;
	}

	/**
	 * 设置左上右下边距
	 */
	public ContextMargin(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	/**
	 * 更新边距
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void updateMargin(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

}
