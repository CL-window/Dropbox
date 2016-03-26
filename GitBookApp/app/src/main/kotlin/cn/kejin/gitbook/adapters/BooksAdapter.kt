package cn.kejin.gitbook.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import cn.kejin.gitbook.R
import cn.kejin.gitbook.common.displayAvatar
import cn.kejin.gitbook.networks.Models

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/16
 */
class BooksAdapter(activity: Activity) :
        BaseRecyclerAdapter<Models.WWWBook, BooksAdapter.BookViewHolder>(activity)
{
    override fun onCreateViewHolder(parent: ViewGroup?, type: Int): BookViewHolder? {
        return BookViewHolder(inflateView(R.layout.item_book, parent))
    }

    inner class BookViewHolder(itemView: View) :
            BaseRecyclerAdapter.BaseViewHolder<Models.WWWBook>(itemView)
    {
        override fun bindView(model: Models.WWWBook, pos: Int) {
            val title = findView(R.id.title) as TextView
            title.text = model.title

            val star = findView(R.id.starNum) as TextView
            star.text = model.star_num

            val time = findView(R.id.pubTime) as TextView
            time.text = model.pub_time

            val summary = findView(R.id.summary) as TextView
            summary.text = model.summary

            val avatar = findView(R.id.avatar) as ImageView
            displayAvatar(model.author.avatar, avatar)

            val author = findView(R.id.author) as TextView
            author.text = model.author.name

            itemView.setOnClickListener({ startBookDetailActivity() })
            findView(R.id.authorLayout).setOnClickListener({ startAuthorActivity() })
        }
    }

    fun startAuthorActivity(){
        // TODO:
    }

    fun startBookDetailActivity() {
        // TODO:
    }
}