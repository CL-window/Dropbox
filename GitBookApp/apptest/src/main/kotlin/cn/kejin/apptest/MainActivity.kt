package cn.kejin.apptest

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import cn.kejin.apptest.recyclerview.ExRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        var headerCount = 0
        var footerCount = 0
    }
    lateinit var adapter : Adapter
    var items : MutableList<String> = mutableListOf(
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i"
    )

    val header:View
        get() {
            val view = View.inflate(this, R.layout.layout_header, null)
            val header = view.findViewById(R.id.header) as TextView
            header.text = "Header: ${++headerCount}"

            return view
        }

    val footer:View
        get() {
            val view = View.inflate(this, R.layout.layout_footer, null)
            val footer = view.findViewById(R.id.footer) as TextView
            footer.text = "Footer: ${++footerCount}"

            return view
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = Adapter(this)
        exRecycler.addHeader(header)
        exRecycler.addFooter(footer)
        exRecycler.adapter = adapter
        exRecycler.itemActionListener = adapter
        exRecycler.setOnLoadMoreListener { exRecycler.endLoadMore() }
        exRecycler.layoutManager = LinearLayoutManager(this)
        exRecycler.enableItemTouchHelper()

        adapter.set(items)

        add.setOnClickListener {
            when(exRecycler.getItemCount() % 5) {
                2 -> {
                    exRecycler.addHeader(header)
                }
                1 -> {
                    exRecycler.addFooter(footer)
                }
                else -> {
                    adapter.append("Add")
                }
            }
        }

        set.setOnClickListener { if(adapter.itemCount > 2) {adapter.set(1, "set") }}
        remove.setOnClickListener {
            when (exRecycler.getItemCount() % 3) {

                else -> {
                    adapter.removeAt(0)
                }
            }
        }
        clear.setOnClickListener {
            adapter = Adapter(this)
            adapter.set(items)

            exRecycler.adapter = adapter
            exRecycler.itemActionListener = adapter
        }

    }

    class Adapter(activity: Activity) : ExRecyclerAdapter<String, Adapter.VH>(activity) {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH? {
            return VH(inflateView(R.layout.layout_item, parent)!!)
        }

        override fun onBindViewHolder(holder: VH?, position: Int) {
            holder?.bindView(data[position], position)
        }

        inner class VH(view: View) : ExRecyclerAdapter.ExViewHolder<String>(view) {
            override fun bindView(model: String, pos: Int) {
                val text = findView(R.id.text) as TextView
                text.text = model

                itemView.setOnTouchListener {
                    view, motionEvent ->
                    if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                        startDrag(this)
                    };
                    false
                }
            }
        }
    }
}
