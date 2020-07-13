package com.dotnet.nikit.investingmonitor.listview_adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.models.Broker

class BrokersRecycleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>{

    interface OnItemClickListener {
        fun onItemClick(broker : Broker)
    }

    private var brokers = ArrayList<Broker>()

    private var listener : OnItemClickListener? = null

    private var mHeaderViews : ArrayList<View> = ArrayList()

    companion object {
        private const val TYPE_ITEM = -1
        private var mContext: Context? = null
    }

    constructor(context: Context, listener: OnItemClickListener){
        mContext = context
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType != TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            val header = LayoutInflater.from(mContext)
                .inflate(R.layout.brokers_listview_header, parent, false)

            return ViewHeader(header)
        }

        val view = LayoutInflater.from(mContext).inflate(R.layout.broker_listview_item, parent, false)
        return BrokerViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (mHeaderViews.size > position){
            position
        }
        else TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return brokers.size + mHeaderViews.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHeader){
            return
        }

        val positionNew = position - mHeaderViews.size

        (holder as BrokerViewHolder).bind(brokers[positionNew], listener!!)
    }

    class BrokerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var nameTextView: TextView? = null
        private var serviceFeeTextView : TextView? = null
        private var transactionFeeTextView : TextView? = null
        private var isIisCheckBox : CheckBox? = null

        private var id : Int? = null

        init {
            nameTextView = itemView.findViewById(R.id.broker_name)
            serviceFeeTextView = itemView.findViewById(R.id.broker_service_fee)
            transactionFeeTextView = itemView.findViewById(R.id.broker_transaction_fee)
            isIisCheckBox = itemView.findViewById(R.id.broker_is_iis)
        }

        fun bind(broker : Broker, listener: OnItemClickListener){
            id = broker.id

            nameTextView?.text = broker.name
            serviceFeeTextView?.text = broker.serviceFee.toString()
            transactionFeeTextView?.text = broker.transactionFee.toString()
            isIisCheckBox?.isChecked = broker.isIIS

            itemView.setOnClickListener {
                itemView.isSelected = true
                listener.onItemClick(broker)
            }
        }
    }

    class ViewHeader(view: View) : RecyclerView.ViewHolder(view)

    fun setItems(brokers : Collection<Broker>) {
        this.brokers.addAll(brokers)
        notifyDataSetChanged()
    }

    fun clearItems(){
        brokers.clear()
        notifyDataSetChanged()
    }

    fun addHeaderView(headerView : View){
        mHeaderViews.add(headerView)
    }


}