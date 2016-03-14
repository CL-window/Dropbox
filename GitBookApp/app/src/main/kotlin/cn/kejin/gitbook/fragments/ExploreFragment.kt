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
    }

    override fun getLayoutId(): Int = R.layout.fragment_explore

    override fun initializeView(view: View)
    {
        view.findViewById(R.id.menuButton)?.setOnClickListener({mActivity.openDrawer() })
        view.findViewById(R.id.searchButton)?.setOnClickListener({startSearchActivity()})
    }

    private fun startSearchActivity()
    {
        var intent = Intent(activity, SearchActivity::class.java)
        startActivity(intent)
    }
}