package cn.kejin.gitbook.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import cn.kejin.gitbook.R
import cn.kejin.gitbook.SearchActivity

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
class ExploreFragment : BaseFragment()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?)
    {
        if (menu != null) {
            inflater?.inflate(R.menu.frag_explore, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item?.itemId) {
            R.id.actionSearch -> startSearchActivity()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun getLayoutId(): Int = R.layout.fragment_explore

    override fun initializeView(view: View)
    {
        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        mActivity.setSupportActionBar(toolbar)
    }

    private fun startSearchActivity()
    {
        var intent = Intent(activity, SearchActivity::class.java)
        startActivity(intent)
    }
}