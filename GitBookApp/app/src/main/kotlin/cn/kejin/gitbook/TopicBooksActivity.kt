package cn.kejin.gitbook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import cn.kejin.gitbook.base.BaseActivity

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/10
 */
class TopicBooksActivity: BaseActivity() {
    companion object {
        val TAG = "TopicBooksActivity"

        val INTENT_TOPIC_ID_KEY = "topic"

        fun startMe(activity: Activity, value: String) {
            val intent = Intent(activity, TopicBooksActivity::class.java)
            intent.putExtra(INTENT_TOPIC_ID_KEY, value)

            activity.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int  = R.layout.activity_topic_books

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}