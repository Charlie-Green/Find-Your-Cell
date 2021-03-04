package by.sviazen.prisoner.util.draw

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log


class MultipleColorsDrawable(
    private val colors: List<Int>
): Drawable() {

    private val paint = Paint()


    override fun setColorFilter(colorFilter: ColorFilter?) {
        Log.e(LOGTAG, "setColorFilter not implementeed.")
    }

    override fun setAlpha(alpha: Int) {
        Log.e(LOGTAG, "setAlpha not implementeed.")
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun draw(canvas: Canvas) {
        val pixelsPerColor = bounds.height().toFloat() / colors.size

        for(j in colors.indices) {
            val y1 = j*pixelsPerColor
            val y2 = y1 + pixelsPerColor

            paint.color = colors[j]
            canvas.drawRect(0f, y1, bounds.width().toFloat(), y2, paint)
        }
    }


    companion object {
        private val LOGTAG = MultipleColorsDrawable::class.java.simpleName
    }
}