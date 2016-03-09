package cn.kejin.gitbook

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
class BaseActivity : AppCompatActivity()
{
    /**
     * prevent start activity twice
     */
    protected var mStartFlag = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mStartFlag = false;
    }

    override fun startActivity(intent: Intent?) {
        if (!mStartFlag) {
            super.startActivity(intent)
            mStartFlag = true;
        }
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        if (!mStartFlag) {
            super.startActivity(intent, options)
            mStartFlag = true;
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        if (!mStartFlag) {
            super.startActivityForResult(intent, requestCode, options)
            mStartFlag = true;
        }
    }
}