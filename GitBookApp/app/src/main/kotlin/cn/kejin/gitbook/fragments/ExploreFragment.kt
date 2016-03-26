package cn.kejin.gitbook.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cn.kejin.gitbook.AllTopicsActivity
import cn.kejin.gitbook.R
import cn.kejin.gitbook.SearchActivity
import cn.kejin.gitbook.adapters.BaseRecyclerAdapter
import cn.kejin.gitbook.adapters.BooksAdapter
import cn.kejin.gitbook.common.dpToPx
import cn.kejin.gitbook.common.error
import cn.kejin.gitbook.controllers.PageController
import cn.kejin.gitbook.controllers.PageDriver
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.Models
import cn.kejin.gitbook.networks.NetworkManager
import cn.kejin.gitbook.views.ExRecyclerView

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
class ExploreFragment : BaseMainFragment()
{
    companion object {
        val TAG = "ExploreFragment"

        val ONE_PAGE_BOOKS_NUM = 9
    }

    lateinit var swipeRefresh : SwipeRefreshLayout

    lateinit var topicsLayout : LinearLayout

    lateinit var topicsHorList : RecyclerView
    lateinit var topicsAdapter: TopicsAdapter

    lateinit var booksList : ExRecyclerView
    lateinit var booksAdapter : BooksAdapter

    lateinit var pageDriver : PageDriver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int = R.layout.fragment_explore

    override fun initializeView(view: View)
    {
        view.findViewById(R.id.menuButton)?.setOnClickListener({ mainActivity.openDrawer() })
        view.findViewById(R.id.searchButton)?.setOnClickListener({startActivity(SearchActivity::class.java)})
        view.findViewById(R.id.allTopics)?.setOnClickListener({startActivity(AllTopicsActivity::class.java)})

        swipeRefresh = view.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh.setDistanceToTriggerSync(dpToPx(130f))

        topicsLayout = view.findViewById(R.id.topicsLayout) as LinearLayout

        topicsHorList = view.findViewById(R.id.topicsHorList) as RecyclerView
        topicsHorList.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
        topicsAdapter = TopicsAdapter(mainActivity)
        topicsHorList.adapter = topicsAdapter

        booksList = view.findViewById(R.id.booksRecyclerView) as ExRecyclerView
        booksList.layoutManager = GridLayoutManager(activity, 2)
        booksAdapter = BooksAdapter(mainActivity)
        booksList.adapter = booksAdapter

        pageDriver = PageDriver(swipeRefresh, booksList, pageCallback)
        pageDriver.refresh()
    }

    /**
     * load data from server
     */
    private val pageCallback  = object : PageDriver.ICallback {
        override fun onRefreshFailed() {
            error(TAG, "refresh failed")
        }

        override fun onLoading(page: Int) {
            NetworkManager.instance.getExplorePage(page,
                    object : HttpCallback<Models.WWWExplorePage>(Models.WWWExplorePage::class.java) {
                        override fun onResponse(success: Boolean, model: Models.WWWExplorePage?, code: Int, msg: String) {
                            var result = PageController.Result.SUCCESS
                            if (success) {
                                if (model!!.books.size < ONE_PAGE_BOOKS_NUM) {
                                    result = PageController.Result.NO_MORE
                                }

                                bindModelToView(page, model)
                            }
                            else {
                                processException(code, msg, view)
                                result = PageController.Result.FAILED
                            }

                            pageDriver.finish(result)
                        }
                    })
        }
    }

    fun bindModelToView(page : Int, model : Models.WWWExplorePage)
    {
        if (page == 0) {
            topicsAdapter.set(model.topics)
            booksAdapter.set(model.books)
        }
        else {
            booksAdapter.addAll(model.books)
        }
    }

    inner class TopicsAdapter(activity: Activity) :
            BaseRecyclerAdapter<Models.WWWTopic, TopicsAdapter.Holder>(activity)
    {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder? {
            return Holder(inflateView(R.layout.item_topic, parent))
        }

        inner class Holder(itemView: View) : BaseViewHolder<Models.WWWTopic>(itemView) {
            override fun bindView(model: Models.WWWTopic, pos: Int) {
                val text = findView(R.id.topic) as TextView;
                text.text = "${model.name}  | ${model.num}"
            }
        }
    }
}