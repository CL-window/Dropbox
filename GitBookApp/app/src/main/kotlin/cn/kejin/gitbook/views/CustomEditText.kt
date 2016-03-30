package cn.kejin.gitbook.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/14
 */
class CustomEditText: EditText
{
    constructor(context: Context?) : super(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : super (context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        if (CustomTextView.customTypeface == null && context != null) {
            CustomTextView.customTypeface = Typeface.createFromAsset(context.assets, CustomTextView.ROBOTO_LIGHT)
        }

        if (CustomTextView.customTypeface != null) {
            typeface = CustomTextView.customTypeface
        }

        isFocusable=true
        isFocusableInTouchMode=true
    }
}