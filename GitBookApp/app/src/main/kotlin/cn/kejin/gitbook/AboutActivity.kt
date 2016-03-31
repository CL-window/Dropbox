package cn.kejin.gitbook

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import cn.kejin.gitbook.adapters.BooksAdapter
import cn.kejin.gitbook.controllers.PageController
import cn.kejin.gitbook.entities.WWWExplorePage
import cn.kejin.gitbook.fragments.ExploreFragment
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.HttpException
import cn.kejin.gitbook.networks.WWWApiImpl
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/11
 */
class AboutActivity : BaseActivity()
{
    override fun getLayoutId(): Int = R.layout.activity_about
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        list.layoutManager = GridLayoutManager(this, 1)
        val booksAdapter = BooksAdapter(this)
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