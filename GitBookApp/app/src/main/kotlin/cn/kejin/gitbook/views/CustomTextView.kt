package cn.kejin.gitbook.views

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import cn.kejin.gitbook.MainApp
import cn.kejin.gitbook.R

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

        val robotoThin by lazy {
            Typeface.createFromAsset(MainApp.instance.assets, ROBOTO_THIN)
        }
        val robotoRegular by lazy {
            Typeface.createFromAsset(MainApp.instance.assets, ROBOTO_REGULAR)
        }
        val robotoLight by lazy {
            Typeface.createFromAsset(MainApp.instance.assets, ROBOTO_LIGHT)
        }
    }

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this (context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
        if (!isInEditMode && context != null && attrs != null) {
            val typeArr = context.obtainStyledAttributes(attrs, R.styleable.CustomText, defStyleAttr, 0)

            val thin: Boolean = typeArr.getBoolean(R.styleable.CustomText_thin, false)
            val regular: Boolean = typeArr.getBoolean(R.styleable.CustomText_regular, false)

            if (regular) {
                typeface = robotoRegular
            }
            else if (thin) {
                typeface = robotoThin
            }
            else {
                typeface = robotoLight
            }
        }
    }

}