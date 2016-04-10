package cn.kejin.gitbook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.text.SpannableString
import android.text.Spanned
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import cn.kejin.gitbook.base.BaseActivity
import cn.kejin.gitbook.entities.WWWBook
import kotlinx.android.synthetic.main.activity_book_detail.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/9
 */
class BookDetailActivity : BaseActivity()
{
    companion object {
        val TAG = "BookDetail"

        val INTENT_WWW_BOOK_KEY = "WWWBook"

        fun startMe(activity: Activity, book: WWWBook) {
            val intent = Intent(activity, BookDetailActivity::class.java)
            intent.putExtra(INTENT_WWW_BOOK_KEY, book)
            activity.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int  = R.layout.activity_book_detail

    override fun getMenuLayoutId(): Int  = R.menu.menu_book_detail


    val book: WWWBook by lazy {
        intent?.getSerializableExtra(INTENT_WWW_BOOK_KEY) as WWWBook
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.title = book.title
        setSupportActionBar(toolbar)

        bookTitle.text = book.title
        bookDesc.text = book.summary

        val nameSpan = SpannableString(book.author.name);
        nameSpan.setSpan(UnderlineSpan(), 0, nameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        authorName.text = nameSpan

        /**
         * 初始化viewPager
         */
        val pageAdapter = IntroPageAdapter()
        viewPager.adapter = pageAdapter

        toolbar_layout.title = book.title
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                //
            }

        })
//        tabLayout.setupWithViewPager(viewPager)
    }

    /**
     * 总共两个页面, 一个 About, 一个 Table of Contents
     */
    inner class IntroPageAdapter : PagerAdapter() {
        val pageLayouts = listOf(
                R.layout.layout_book_about_page,
                R.layout.layout_book_tables_page
        )

//        val pageViews = mutableListOf<View?>(null, null)

        override fun instantiateItem(container: ViewGroup?, position: Int): Any? {
            val view = layoutInflater.inflate(pageLayouts[position], container, false)
            if (view != null) {
                when (position) {
                    0 -> initializeAboutPage(view)
                    1 -> initializeTablesPage(view)
                }
            }
            return view
        }

        fun initializeAboutPage(view: View) {
            // TODO:
        }

        fun initializeTablesPage(view: View) {
            //TODO:
        }

        override fun destroyItem(container: ViewGroup?, position: Int, obj: Any?) {
            if (obj != null) {
                container?.removeView(obj as View)
            }
        }

        override fun isViewFromObject(view: View?, obj: Any?): Boolean {
            return view == obj
        }

        override fun getCount(): Int = pageLayouts.size
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}