package com.offline.continentalrecipesusingnavgraph.uishared

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.Px
import com.offline.continentalrecipesusingnavgraph.R

class ReticleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        /**
         * Color of reticle in the static state
         */
        private const val STATIC_STATE_COLOR = Color.WHITE


        /**
         * Reticle vertical and horizontal border measurement
         */
        @Dimension(unit = Dimension.DP)
        private const val RETICLE_VIEW_RECTANGLE_BORDER = 48.0f

        /**
         * Dimension used for the center target size i.e. the plus view in the center
         */
        @Dimension(unit = Dimension.DP)
        private const val TARGET_SIZE = 16.0f

        /**
         * Dimension used for the line width of the view
         */
        @Dimension(unit = Dimension.DP)
        private const val LINE_WIDTH = 1.0f

        /**
         * Dimension used for the corners of the rectangle view
         */
        @Dimension(unit = Dimension.DP)
        private const val NOTCH_SIZE = 16.0f
    }

    @Px
    internal val lineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LINE_WIDTH, Resources.getSystem().displayMetrics)

    @Px
    internal val targetSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TARGET_SIZE, Resources.getSystem().displayMetrics)

    @Px
    private val reticleRectangleBorder = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, RETICLE_VIEW_RECTANGLE_BORDER, Resources.getSystem().displayMetrics)

    @Px
    private val notchSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, NOTCH_SIZE, Resources.getSystem().displayMetrics)

    /**
     * [Paint] for the static state.
     */
    private val staticStatePaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply{
            color = STATIC_STATE_COLOR
            style = Paint.Style.FILL
            strokeWidth = lineWidth
            strokeCap = Paint.Cap.ROUND
        }



    init {
        this.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        this.contentDescription = context.getString(R.string.reticle_view_content_description)
    }

    /**
     * [RectF] that represents the static state of the reticle.
     */
    private var initialStateBounds: RectF? = null

    private fun initializeStaticState(w: Float, h: Float) {
        initialStateBounds = buildInitialStateBounds(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        initializeStaticState(w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawStaticState(canvas)
    }


    /**
     * Creates the bounds [RectF] for the static state
     */
    private fun buildInitialStateBounds(width: Float, height: Float): RectF {
        val bounds = RectF()

        with(bounds) {
            this.set(
                reticleRectangleBorder,
                (height / 2) - reticleRectangleBorder,
                width - reticleRectangleBorder,
                (height /2) + reticleRectangleBorder
            )
            inset(lineWidth, lineWidth)
        }

        return bounds
    }


    private fun drawStaticState(canvas: Canvas) {
        val bounds = initialStateBounds ?: return

        // TOP
        drawTopLeftCorner(canvas, bounds, staticStatePaint)
        drawTopRightCorner(canvas, bounds, staticStatePaint)

        // Bottom
        drawBottomLeftCorner(canvas, bounds, staticStatePaint)
        drawBottomRightCorner(canvas, bounds, staticStatePaint)

        // Target
        drawTarget(canvas, bounds, staticStatePaint)
    }

    /**
     * Draw the top left corner of the reticle
     */
    private fun drawTopLeftCorner(canvas: Canvas, bounds: RectF, paint: Paint) {
        // draw a line from top left to top right
        canvas.drawLine(bounds.left, bounds.top, bounds.left + notchSize, bounds.top, paint)

        // draw a line from top left to bottom left
        canvas.drawLine(bounds.left, bounds.top, bounds.left, bounds.top + notchSize, paint)
    }

    /**
     * Draw the top right corner of the reticle
     */
    private fun drawTopRightCorner(canvas: Canvas, bounds: RectF, paint: Paint) {
        // draw a line from top right to top left
        canvas.drawLine(bounds.right, bounds.top, bounds.right - notchSize, bounds.top, paint)

        // draw a line from top right to bottom right
        canvas.drawLine(bounds.right, bounds.top, bounds.right, bounds.top + notchSize, paint)
    }

    /**
     * Draw the bottom left corner of the reticle
     */
    private fun drawBottomLeftCorner(canvas: Canvas, bounds: RectF, paint: Paint) {
        // draw a line from bottom left to bottom right
        canvas.drawLine(bounds.left, bounds.bottom, bounds.left + notchSize, bounds.bottom, paint)

        // draw a line from bottom left to top left
        canvas.drawLine(bounds.left, bounds.bottom, bounds.left, bounds.bottom - notchSize, paint)
    }

    /**
     * Draw the bottom right corner of the reticle
     */
    private fun drawBottomRightCorner(canvas: Canvas, bounds: RectF, paint: Paint) {
        // draw a line from bottom right to bottom left
        canvas.drawLine(bounds.right, bounds.bottom, bounds.right - notchSize, bounds.bottom, paint)

        // draw a line from bottom right to top right
        canvas.drawLine(bounds.right, bounds.bottom, bounds.right, bounds.bottom - notchSize, paint)
    }

    /**
     * Draw the target of the reticle
     */
    private fun drawTarget(canvas: Canvas, bounds: RectF, paint: Paint) {
        paint.strokeWidth = lineWidth

        // draw vertical line of the target
        canvas.drawLine(bounds.centerX(), (bounds.centerY()) - (targetSize / 2), bounds.centerX(), (bounds.centerY()) + (targetSize / 2), paint)

        // draw horizontal line of the target
        canvas.drawLine(bounds.centerX() - (targetSize / 2), bounds.centerY(), bounds.centerX() + (targetSize / 2), bounds.centerY(), paint)
    }
}
