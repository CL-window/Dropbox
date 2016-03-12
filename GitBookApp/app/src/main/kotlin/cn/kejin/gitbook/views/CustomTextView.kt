package cn.kejin.gitbook.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */

/**
 * for custom font
 */
class CustomTextView(context: Context?,
                     attrs: AttributeSet?,
                     defStyleAttr: Int) : TextView(context, attrs, defStyleAttr)
{
    companion object {
        val CUS_TYPEFACE_PATH = "Roboto-Regular.ttf"
        var customTypeface : Typeface? = null
    }

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this (context, attrs, 0)

    init {
        if (customTypeface == null && context != null) {
            customTypeface = Typeface.createFromAsset(context.assets, CUS_TYPEFACE_PATH)
        }

        if (customTypeface != null) {
            typeface = customTypeface
        }
    }
}