package cn.kejin.gitbook.views

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */

/**
 * for custom font
 */
class CustomTextView : TextView
{
    companion object {
        val ROBOTO_THIN = "Roboto-Thin.ttf"
        val ROBOTO_REGULAR = "Roboto-Regular.ttf"
        val ROBOTO_LIGHT = "Roboto-Light.ttf"

        var customTypeface : Typeface? = null
    }

    constructor(context: Context?) : super(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : super (context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    init {
        if (customTypeface == null && context != null) {
            customTypeface = Typeface.createFromAsset(context.assets, ROBOTO_LIGHT)
        }

        if (customTypeface != null) {
            typeface = customTypeface
        }

        ellipsize = TextUtils.TruncateAt.END
    }
}