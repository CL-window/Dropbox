package cn.kejin.gitbook.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import cn.kejin.gitbook.AllTopicsActivity
import cn.kejin.gitbook.R
import cn.kejin.gitbook.SearchActivity

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
class ExploreFragment : BaseMainFragment()
{
    companion object {
        val TAG = "ExploreFragment"
    }

    private var isRefreshing = false
    set(value) {
        if (value != field) {
            if (value) {
                swipeRefresh.post({swipeRefresh.isRefreshing=true})
            }
            else {
                swipeRefresh.isRefreshing = false
            }
            field = value
        }
    }

    lateinit var swipeRefresh : SwipeRefreshLayout
    lateinit var topicsLayout : LinearLayout
    lateinit var topicsHorList : RecyclerView
    lateinit var booksList : RecyclerView

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

        booksList = view.findViewById(R.id.booksRecyclerView) as RecyclerView

        refreshDataFromServer()
    }

    /**
     * refresh all data from server
     */
    fun refreshDataFromServer() {
        isRefreshing = true;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        if (view == null) {
            return;
        }

//        swipeRefresh.setDistanceToTriggerSync(200);
    }

}