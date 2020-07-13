package com.dotnet.nikit.investingmonitor.listview_adapters

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.models.Dividend
import com.dotnet.nikit.investingmonitor.util.Utils
import com.google.android.material.button.MaterialButton

class DividendsRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var dividends = ArrayList<Dividend>()

    private var mHeaderViews: ArrayList<View> = ArrayList()

    private var mFooterViews: ArrayList<View> = ArrayList()

    private var listener : OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick()
    }

    companion object {
        private val TYPE_HEADER = -1
        private val TYPE_FOOTER = -2
        private var mContext: Context? = null
    }

    constructor(context: Context, onItemClickListener : OnItemClickListener) {
        listener = onItemClickListener
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_HEADER){

            val header = LayoutInflater.from(mContext)
                .inflate(R.layout.dividends_listview_header, parent, false)

            return ViewHeader(header)
        }

        if(viewType == TYPE_FOOTER){
            val footer = LayoutInflater.from(mContext).inflate(R.layout.add_dividend_button, parent, false)
            var button = footer?.findViewById<MaterialButton>(R.id.add_dividend_button)
            button?.setOnClickListener {
                listener?.onItemClick()
            }
            return ViewFooter(footer)
        }

        val view =
            LayoutInflater.from(mContext).inflate(R.layout.dividends_listview_item, parent, false)
        return DividendViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {

        if(position == 0){
            return TYPE_HEADER
        }
        if(position == itemCount - 1){
            return TYPE_FOOTER
        }
        return position
    }

    override fun getItemCount(): Int {
        return dividends.size + mHeaderViews.size + mFooterViews.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHeader || holder is ViewFooter) {
            return
        }

        val positionNew = position - mHeaderViews.size

        (holder as DividendViewHolder).bind(dividends[positionNew])
    }

    class DividendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var dateTextView: TextView? = null
        private var paymentValueTextView: TextView? = null

        init {
            dateTextView = itemView.findViewById(R.id.dividends_date_text_view)
            paymentValueTextView = itemView.findViewById(R.id.dividends_payment_value_text_view)
        }

        fun bind(dividend: Dividend) {
            dateTextView?.text = Utils.formatDateToString(dividend.date)
            paymentValueTextView?.text = dividend.payment.toString()
        }
    }

    class ViewHeader(view: View) : RecyclerView.ViewHolder(view)

    class ViewFooter(button : View) : RecyclerView.ViewHolder(button)

    fun setItems(dividends: Collection<Dividend>) {
        this.dividends.addAll(dividends)
        notifyDataSetChanged()
    }

    fun clearItems() {
        dividends.clear()
        notifyDataSetChanged()
    }

    fun addHeaderView(headerView: View) {
        mHeaderViews.add(headerView)
    }

    fun addFooterView(footerView : View) {
        mFooterViews.add(footerView)
    }

}