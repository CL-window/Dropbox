package cn.kejin.gitbook.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import cn.kejin.gitbook.AllTopicsActivity
import cn.kejin.gitbook.R
import cn.kejin.gitbook.SearchActivity
import cn.kejin.gitbook.adapters.BaseRecyclerAdapter
import cn.kejin.gitbook.adapters.BooksAdapter
import cn.kejin.gitbook.common.snack
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.Models
import cn.kejin.gitbook.networks.NetworkManager

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

    enum class Status {
        NONE,
        REFRESHING,
        LOADING_MORE,
        SUCCESS,
        FAILED,
        NO_MORE
    }

    private var currentStatus = Status.NONE
    set(value) {
        if (value != field) {
            when ((field to value)) {
                (Status.LOADING_MORE to Status.SUCCESS) -> {
                    //
                }
                (Status.LOADING_MORE to Status.NO_MORE) -> {
                    //
                }
                (Status.LOADING_MORE to Status.FAILED) -> {
                    //
                }

                (Status.SUCCESS to Status.LOADING_MORE) -> {
                    //
                }
                (Status.NO_MORE to Status.LOADING_MORE) -> {
                    //
                }
                (Status.FAILED to Status.LOADING_MORE) -> {
                    //
                }

                (Status.REFRESHING to Status.SUCCESS),
                (Status.REFRESHING to Status.FAILED),
                (Status.REFRESHING to Status.NO_MORE),
                (Status.REFRESHING to Status.NONE) -> {
                    swipeRefresh.isRefreshing = false
                }

                (Status.SUCCESS to Status.REFRESHING),
                (Status.FAILED to Status.REFRESHING) ,
                (Status.NO_MORE to Status.REFRESHING) ,
                (Status.NONE to Status.REFRESHING) ,
                (Status.LOADING_MORE to Status.REFRESHING) -> {
                    swipeRefresh.post({swipeRefresh.isRefreshing=true})
                }
            }
            field = value
        }
    }

    private var isRequesting = false;
    private var exploredPageIndex = -1
    set(value) {
        if (value >=0 && !isRequesting) {
            isRequesting = true
            if (value == 0) {
                currentStatus = Status.REFRESHING
            }
            else {
                currentStatus = Status.LOADING_MORE
            }

            NetworkManager.instance.getExplorePage(value,
                    object : HttpCallback<Models.WWWExplorePage>(Models.WWWExplorePage::class.java) {
                override fun onResponse(success: Boolean, model: Models.WWWExplorePage?, code: Int, msg: String) {
                    if (success) {
                        if (model!!.books.size < ONE_PAGE_BOOKS_NUM) {
                            currentStatus = Status.NO_MORE
                        }
                        else {
                            currentStatus = Status.SUCCESS
                            field = value;
                        }

                        bindModelToView(model)
                    }
                    else {
                        processException(code, msg, view)
                        currentStatus = Status.FAILED
                    }

                    isRequesting = false
                }
            })
        }
        else if (value == 0) {
            swipeRefresh.isRefreshing = false
        }
    }

    lateinit var swipeRefresh : SwipeRefreshLayout

    lateinit var topicsLayout : LinearLayout

    lateinit var topicsHorList : RecyclerView
    lateinit var topicsAdapter: TopicsAdapter

    lateinit var booksList : RecyclerView
    lateinit var booksAdapter : BooksAdapter

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

        topicsLayout = view.findViewById(R.id.topicsLayout) as LinearLayout

        topicsHorList = view.findViewById(R.id.topicsHorList) as RecyclerView
        topicsHorList.layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
        topicsAdapter = TopicsAdapter(mainActivity)
        topicsHorList.adapter = topicsAdapter

        booksList = view.findViewById(R.id.booksRecyclerView) as RecyclerView
        booksList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        booksAdapter = BooksAdapter(mainActivity)
        booksList.adapter = booksAdapter

        swipeRefresh.setOnRefreshListener { exploredPageIndex = 0 }
        exploredPageIndex = 0
    }

    /**
     * refresh all data from server
     */
    fun refreshDataFromServer() {
        exploredPageIndex = 0
    }

    fun bindModelToView(model : Models.WWWExplorePage)
    {
        if (exploredPageIndex == 0) {
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
                text.text = "${model.name} | ${model.num}"
            }
        }
    }
}