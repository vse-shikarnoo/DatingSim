package wp.ds.datingsim.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import wp.ds.datingsim.R
import wp.ds.datingsim.model.SwipeResult
import kotlin.concurrent.Volatile
import kotlin.math.sqrt


class SwipeSurfaceView(
    context: Context
) : SurfaceView(context), SurfaceHolder.Callback, Runnable {

    private var bmp = getBitmapFromDrawable(context, R.drawable.ic_android_black_24dp)

    var thread: Thread? = null
    var surfaceHolder: SurfaceHolder? = null

    // Рисуем холст
    private var mCanvas: Canvas? = null

    @Volatile
    var running = false

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    private var centerPoint: Pair<Float, Float> = Pair(0f, 0f)
    private var endPoint: Pair<Float, Float> = centerPoint
    private var result: SwipeResult = SwipeResult.NULL
    private val nullRadius = 150f


    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                running = true
                endPoint = Pair(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                endPoint = Pair(event.x, event.y)
            }

            MotionEvent.ACTION_UP -> {
                //callback
                endPoint = centerPoint
                //running = false
            }
        }
        return true
    }

    init {
        surfaceHolder = holder
        surfaceHolder?.addCallback(this)
        isFocusable = true;
        keepScreenOn = true;
        isFocusableInTouchMode = true;
        mPaint = Paint()
        mPaint.setColor(Color.WHITE)
        mPaint.setStyle(Paint.Style.FILL)
        mPaint.setStrokeWidth(5f)
        mPaint.setAntiAlias(true)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        running = true;
        // Открываем дочерний поток

        thread = Thread(this)
        thread!!.start()

        mCanvas = surfaceHolder?.lockCanvas()
        endPoint = Pair(mCanvas!!.width.toFloat() / 2, mCanvas!!.height.toFloat() / 2)
        surfaceHolder?.unlockCanvasAndPost(mCanvas)

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        running = false
    }

    override fun run() {
        while (running) {
            drawSwipe()
        }
    }

    private fun drawSwipe() {
        try {

            // Получаем объект холста
            mCanvas = surfaceHolder?.lockCanvas()
            getCenterPoint()

            //Рисование
            result = check()
            Log.d("DrawSwipe", result.name + endPoint)
            mCanvas?.drawColor(result.rgb.toColorInt())
            // Рисуем фон
            //mCanvas?.drawCircle(centerPoint.first, centerPoint.second, nullRadius, mPaint)
//            mCanvas?.drawLine(0f, 0f, mCanvas!!.width.toFloat(), mCanvas!!.height.toFloat(), mPaint)
//            mCanvas?.drawLine(mCanvas!!.width.toFloat(), 0f, 0f, mCanvas!!.height.toFloat(), mPaint)

            mCanvas?.drawBitmap(bmp!!,endPoint.first-100, endPoint.second, mPaint)


        } catch (e: Exception) {
            Log.e("DrawError", e.stackTraceToString() + bmp)
        } finally {
            if (mCanvas != null) {
                // Освобождаем объект холста и отправляем холст
                surfaceHolder?.unlockCanvasAndPost(mCanvas)
            }
        }
    }

    private fun getCenterPoint() {
        val w = (mCanvas!!.width.div(2)).toFloat()
        val h = (mCanvas!!.height.div(2)).toFloat()
        centerPoint = Pair(w, h)
    }

    private fun check(): SwipeResult {

        val len =
            sqrt((endPoint.first - centerPoint.first) * (endPoint.first - centerPoint.first) + (endPoint.second - centerPoint.second) * (endPoint.second - centerPoint.second))

        if (len < nullRadius) {
            return SwipeResult.NULL
        }

        val x = endPoint.first
        val y = -endPoint.second

        val w = mCanvas!!.width
        val h = -mCanvas!!.height

        val d1 = x * h - y * w
        val d2 = (x - w) * h - y * (-w)

        return if (d1 > 0) {
            if (d2 > 0) {
                SwipeResult.DISLIKE
            } else {
                SwipeResult.INFO
            }
        } else {
            if (d2 > 0) {
                SwipeResult.SUPERLIKE
            } else {
                SwipeResult.LIKE
            }
        }
    }

    fun getBitmapFromDrawable(context: Context?, @DrawableRes drawableId: Int): Bitmap? {
        return when (val drawable = ContextCompat.getDrawable(context!!, drawableId)) {
            is BitmapDrawable -> {
                drawable.bitmap
            }

            is VectorDrawable, is VectorDrawableCompat -> {
                val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }

            else -> {
                throw IllegalArgumentException("unsupported drawable type")
            }
        }
    }
}