package cn.kejin.gitbook.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/16
 */
abstract class BaseRecyclerAdapter<Model, Holder: BaseRecyclerAdapter.BaseViewHolder<Model>>
                                    (val activity : Activity) :RecyclerView.Adapter<Holder>()
{
    private var data : MutableList<Model> = mutableListOf()

    fun set(list : Collection<Model>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun append(model: Model) {
        val index = data.size
        data.add(index, model)
        notifyItemInserted(index)
    }

    fun add(index : Int, model: Model) {
        data.add(index, model)
        notifyItemInserted(index)
    }

    fun removeAt(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    fun remove(model:Model) {
        val index = data.indexOf(model)
        if (index in 0..data.size-1) {
            data.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

//    override fun onCreateViewHolder(group: ViewGroup?, type: Int): BaseViewHolder? {
//        throw UnsupportedOperationException()
//    }
    fun inflateView(id : Int, parent:ViewGroup? = null) = activity.layoutInflater.inflate(id, parent, false)

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: Holder?, pos: Int)
            = if (pos in 0..data.size-1 && holder != null) {holder.bindView(data[pos], pos) } else {}

    abstract class BaseViewHolder<Model>(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun findView(id : Int) = itemView.findViewById(id)

        abstract fun bindView(model : Model, pos: Int)
    }
}