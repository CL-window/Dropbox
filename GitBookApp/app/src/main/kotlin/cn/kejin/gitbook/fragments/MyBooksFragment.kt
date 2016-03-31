package cn.kejin.gitbook.fragments

import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.kejin.gitbook.R
import cn.kejin.gitbook.adapters.BooksAdapter
import cn.kejin.gitbook.controllers.PageController
import cn.kejin.gitbook.entities.WWWExplorePage
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.HttpException
import cn.kejin.gitbook.networks.WWWApiImpl
import cn.kejin.gitbook.views.ExRecyclerView
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
class MyBooksFragment : BaseMainFragment()
{
    override fun getLayoutId(): Int = R.layout.fragment_my_books

    override fun initializeView(view: View)
    {

        val list = view.findViewById(R.id.list) as ExRecyclerView
        list.layoutManager = GridLayoutManager(mainActivity, 1)
        val booksAdapter = BooksAdapter(mainActivity)
        list.adapter = booksAdapter

        WWWApiImpl.instance.getExplorePage(0,
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