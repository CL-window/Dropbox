package cn.kejin.gitbook.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import cn.kejin.gitbook.R
import cn.kejin.gitbook.networks.Models

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/16
 */
class BooksAdapter(activity: Activity) :
        BaseRecyclerAdapter<Models.ABook, BooksAdapter.BookViewHolder>(activity)
{
    override fun onCreateViewHolder(parent: ViewGroup?, pos: Int): BookViewHolder? {
        return BookViewHolder(inflateView(R.layout.list_item_book, parent))
    }

    inner class BookViewHolder(itemView: View) :
            BaseRecyclerAdapter.BaseViewHolder<Models.ABook>(itemView)
    {
        override fun bindView(model: Models.ABook, pos: Int) {
            val title = findView(R.id.title) as TextView
            title.text = model.title

            val star = findView(R.id.starNum) as TextView
        }
    }
}