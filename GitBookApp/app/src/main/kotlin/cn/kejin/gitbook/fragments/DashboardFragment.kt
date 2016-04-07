package cn.kejin.gitbook.fragments

import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.kejin.android.views.ExRecyclerView
import cn.kejin.gitbook.R
import cn.kejin.gitbook.adapters.BooksAdapter
import cn.kejin.gitbook.controllers.PageController
import cn.kejin.gitbook.entities.WWWExplorePage
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.HttpException
import cn.kejin.gitbook.networks.Net
import cn.kejin.gitbook.networks.WWWApiImpl

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/1
 */
class DashboardFragment: BaseMainFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_dashboard

    override fun initializeView(view: View) {

        val list = view.findViewById(R.id.list) as ExRecyclerView
        list.layoutManager = GridLayoutManager(mainActivity, 1)
        val booksAdapter = BooksAdapter(mainActivity)
        list.adapter = booksAdapter

        Net.wwwApi.getExplorePage(0,
                object : HttpCallback<WWWExplorePage>(WWWExplorePage::class.java) {
                    override fun onResponse(model: WWWExplorePage?, exception: HttpException?) {
                        var result = PageController.Result.SUCCESS
                        if (exception == null) {
                            booksAdapter.set(model!!.books)
                        }
                        else {
                        }

                    }
                })
    }
}