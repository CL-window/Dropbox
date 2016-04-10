package cn.kejin.gitbook.fragments

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.kejin.android.views.ExRecyclerAdapter
import cn.kejin.android.views.ExRecyclerView
import cn.kejin.gitbook.R
import cn.kejin.gitbook.SearchActivity
import cn.kejin.gitbook.TopicBooksActivity
import cn.kejin.gitbook.base.BaseMainFragment
import cn.kejin.gitbook.common.dismissSoftInputMethod
import cn.kejin.gitbook.controllers.PageController
import cn.kejin.gitbook.controllers.PageDriver
import cn.kejin.gitbook.entities.ATopic
import cn.kejin.gitbook.entities.Topics
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.HttpException
import cn.kejin.gitbook.networks.Net
import cn.kejin.gitbook.networks.RestApi
import cn.kejin.gitbook.common.error
/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
class TopicsFragment : BaseMainFragment()
{
    override fun getLayoutId(): Int = R.layout.fragment_all_topics
    override fun getOptionsMenu(): Int = R.menu.menu_topics

    private lateinit var pageDriver : PageDriver

    private val topicsAdapter : TopicsAdapter by lazy {  TopicsAdapter(activity)   }

    override fun initializeView(view: View)
    {
//        view.findViewById(R.id.menuButton).setOnClickListener { navMenuCtrl.openDrawer() }

        val swipeRefresh = view.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout
        val recyclerView = view.findViewById(R.id.topicRecyclerView) as ExRecyclerView
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = topicsAdapter

        pageDriver = PageDriver(swipeRefresh, recyclerView, pageCallback)

        pageDriver.refresh()
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.searchAction -> {
                startActivity(SearchActivity::class.java)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private val pageCallback =  object : PageDriver.ICallback {
        override fun onRefreshFailed() {
            error(TopicsFragment::class.java, "refresh failed")
        }

        override fun onLoading(page: Int) {

            Net.restApi.getAllTopics(page, 2*RestApi.DEF_PAGE_LIMIT, callback = object : HttpCallback<Topics>(Topics::class.java) {
                override fun onResponse(model: Topics?, exception: HttpException?) {
                    var result = PageController.Result.SUCCESS
                    if (exception == null) {
                        if (page == 0) {
                            topicsAdapter.clear()
                        }
                        topicsAdapter.addAll(model!!.list)
                        if (model.list.size < 2*RestApi.DEF_PAGE_LIMIT) {
                            result = PageController.Result.NO_MORE
                        }
                    }
                    else {
                        exception.process(view)
                        result == PageController.Result.FAILED
                    }

                    pageDriver.finish(result)
                }
            })
        }
    }

    inner class TopicsAdapter(act: Activity) :
            ExRecyclerAdapter<ATopic, TopicsAdapter.TopicViewHolder>(act) {
        override fun onBindViewHolder(holder: TopicViewHolder?, position: Int) {
            holder?.bindView(data[position], position)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopicViewHolder? {
            return TopicViewHolder(inflateView(R.layout.item_topic_normal, parent))
        }

        inner class TopicViewHolder(itemView: View) : ExViewHolder<ATopic>(itemView) {
            override fun bindView(model: ATopic, pos: Int) {
                (findView(R.id.topicName) as TextView).text = model.name
                (findView(R.id.topicNum) as TextView).text = "${model.books} Books"

                itemView?.setOnClickListener {
                    TopicBooksActivity.startMe(activity, model.id)
                }
            }
        }
    }

}