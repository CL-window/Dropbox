package cn.kejin.gitbook

import android.os.Bundle
import cn.kejin.gitbook.base.CustomStatusBarActivity

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/9
 */
class BookDetailActivity : CustomStatusBarActivity()
{
    companion object {
        val TAG = "BookDetail"

        val INTENT_KEY = "WWWBook"
    }

    override fun getLayoutId(): Int  = R.layout.activity_book_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
    }
}