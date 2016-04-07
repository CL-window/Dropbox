package cn.kejin.gitbook

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.kejin.gitbook.adapters.BaseRecyclerAdapter
import cn.kejin.gitbook.common.dismissSoftInputMethod
import cn.kejin.gitbook.common.error
import cn.kejin.gitbook.controllers.PageController
import cn.kejin.gitbook.controllers.PageDriver
import cn.kejin.gitbook.entities.*
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.HttpException
import cn.kejin.gitbook.networks.Net
import cn.kejin.gitbook.networks.RestApi
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.fragment_all_topics.*
import kotlinx.android.synthetic.main.layout_title_search.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/22
 */
class AllTopicsActivity : CustomStatusBarActivity()
{
    override fun getLayoutId(): Int = R.layout.fragment_all_topics

    private lateinit var pageDriver : PageDriver

    private val topicsAdapter : TopicsAdapter = TopicsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeViews()
    }

    fun initializeViews() {
        backButton.setOnClickListener { finish() }
        clearButton.setOnClickListener { searchEdit.setText("") }

        searchEdit.clearFocus()
        searchEdit.setOnEditorActionListener {
            textView, i, keyEvent ->
            dismissSoftInputMethod(this, textView.windowToken);
            searchTopic(textView.text.toString())
            true
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = topicsAdapter

        pageDriver = PageDriver(swipeRefresh, recyclerView, pageCallback)

        pageDriver.refresh()
    }

    private val pageCallback =  object : PageDriver.ICallback {
        override fun onRefreshFailed() {
            error(AllTopicsActivity::class.java, "refresh failed")
        }

        override fun onLoading(page: Int) {

            Net.restApi.getAllTopics(page, callback = object : HttpCallback<Topics>(Topics::class.java) {
                override fun onResponse(model: Topics?, exception: HttpException?) {
                    var result = PageController.Result.SUCCESS
                    if (exception == null) {
                        if (page == 0) {
                            topicsAdapter.clear()
                        }
                        topicsAdapter.addAll(model!!.list)
                        if (model.list.size < RestApi.DEF_PAGE_LIMIT) {
                            result = PageController.Result.NO_MORE
                        }
                    }
                    else {
                        exception.process(recyclerView)
                        result == PageController.Result.FAILED
                    }

                    pageDriver.finish(result)
                }
            })
        }
    }

    private fun searchTopic(key: String) {
        swipeRefresh.post({ swipeRefresh.isRefreshing = true })
        swipeRefresh.postDelayed({ swipeRefresh.isRefreshing = false }, 2000)
    }

    inner class TopicsAdapter : BaseRecyclerAdapter<ATopic, TopicsAdapter.TopicViewHolder>(this) {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicViewHolder? {
            return TopicViewHolder(inflateView(R.layout.item_topic_normal, parent))
        }

        inner class TopicViewHolder(itemView: View) : BaseViewHolder<ATopic>(itemView) {
            override fun bindView(model: ATopic, pos: Int) {
                (findView(R.id.topicName) as TextView).text = model.name
                (findView(R.id.topicNum) as TextView).text = "${model.books} Books"
            }
        }
    }


}