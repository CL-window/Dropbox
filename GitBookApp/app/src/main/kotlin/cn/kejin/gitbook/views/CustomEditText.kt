package cn.kejin.gitbook.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText
import cn.kejin.gitbook.R

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/14
 */
class CustomEditText: EditText
{
    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this (context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        if (!isInEditMode && context != null && attrs != null) {
            val typeArr = context.obtainStyledAttributes(attrs, R.styleable.CustomText, defStyleAttr, 0)

            val thin: Boolean = typeArr.getBoolean(R.styleable.CustomText_thin, false)
            val regular: Boolean = typeArr.getBoolean(R.styleable.CustomText_regular, false)

            if (regular) {
                typeface = CustomTextView.robotoRegular
            }
            else if (thin) {
                typeface = CustomTextView.robotoThin
            }
            else {
                typeface = CustomTextView.robotoLight
            }
        }
    }
}