package cn.kejin.apptest.recyclerview

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/16
 */
abstract class ExRecyclerAdapter<Model, Holder: RecyclerView.ViewHolder>(val activity : Activity)
                            : RecyclerView.Adapter<Holder>(), ItemActionListener
{
    protected var data : MutableList<Model> = mutableListOf()

    override fun getItemCount() = data.size

    private fun isValidPosition(pos: Int) = pos in 0..data.size-1

    fun get(pos: Int): Model? {
        return if (isValidPosition(pos)) data[pos] else null
    }

    fun set(pos: Int, model: Model) {
        if (isValidPosition(pos)) {
            data[pos] = model
            notifyItemChanged(pos)
        }
    }

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

    fun addAll(list: Collection<Model>) {
        val insertPos = data.size
        data.addAll(list)
        notifyItemRangeInserted(insertPos, list.size)
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

    var itemTouchHelper : ItemTouchHelper? = null

    override fun onItemMove(from: Int, to: Int): Boolean {
        if (from != to &&
                isValidPosition(from) && isValidPosition(to)) {
            Collections.swap(data, from, to)
            notifyItemMoved(from, to)
            return true;
        }
        else {
            return false;
        }
    }

    override fun onItemSwiped(pos: Int) {
        if (isValidPosition(pos)) {
            removeAt(pos)
        }
    }

    override fun onItemSelected(holder: RecyclerView.ViewHolder, pos: Int) {
        holder.itemView?.setBackgroundColor(Color.LTGRAY)
    }

    override fun onItemCleared(holder: RecyclerView.ViewHolder, pos: Int) {
        holder.itemView?.setBackgroundColor(0)
    }

    fun inflateView(id : Int, parent: ViewGroup? = null)
            = activity.layoutInflater.inflate(id, parent, false)

    fun startDrag(holder: Holder) {
        itemTouchHelper?.startDrag(holder)
    }

    fun startSwipe(holder: Holder) {
        itemTouchHelper?.startSwipe(holder)
    }

//    override fun onBindViewHolder(holder: Holder?, pos: Int) {
//        if (holder != null) {
//            if (holder is ExViewHolder<*>) {
//                holder.bindView(data[pos], pos)
//            }
//        }
//    }

    abstract class ExViewHolder<Model>(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun findView(id : Int) = itemView.findViewById(id)

        abstract fun bindView(model : Model, pos: Int)
    }
}