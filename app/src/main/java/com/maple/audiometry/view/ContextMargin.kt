package com.maple.audiometry.view

/**
 * 上下文边距
 *
 * @author shao
 */
class ContextMargin {
    var bottom = 0
    var left = 0
    var right = 0
    var top = 0

    /**
     * 更新边距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    fun updateMargin(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }

}
