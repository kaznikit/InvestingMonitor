package com.dotnet.nikit.investingmonitor.views.main.dialog_fragments

import android.os.Bundle
import android.widget.*
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.interfaces.OnCompleteAddingData
import com.dotnet.nikit.investingmonitor.models.Broker
import dagger.android.support.DaggerFragment

class BrokerAddFragment(parentFragment: DaggerFragment,
                        resource: Int, title: String) : BaseAddFragment(parentFragment, resource, title) {

    private var brokerNameTextView: EditText? = null
    private var brokerServiceFeeTextView: EditText? = null
    private var brokerTransactionFeeTextView: EditText? = null
    private var brokerIsIisCheckBox: CheckBox? = null
    private var acceptButton: Button? = null

    /*companion object {
        fun newInstance(parentFragment: DaggerFragment, resource: Int, title : String): BaseAddFragment {
            if (instance == null) {
                instance = BrokerAddFragment()
                instance!!.parentFragment = parentFragment
                instance!!.onCompleteListener = parentFragment as OnCompleteAddingData<Any>
                instance!!.resource = resource
                instance!!.title = title
            }
            return instance!!
        }
    }*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    override fun initViews() {
        brokerNameTextView = rootView?.findViewById(R.id.broker_add_name)
        brokerServiceFeeTextView = rootView?.findViewById(R.id.broker_service_fee)
        brokerTransactionFeeTextView = rootView?.findViewById(R.id.broker_transaction_fee)
        brokerIsIisCheckBox = rootView?.findViewById(R.id.broker_add_is_iis)

        acceptButton = rootView?.findViewById(R.id.accept_changes_button)
        acceptButton?.setOnClickListener {
            acceptButton?.isEnabled = false
            acceptBrokerData()
            acceptButton?.isEnabled = true
        }
    }

    private fun acceptBrokerData() {
        if (brokerNameTextView == null || brokerTransactionFeeTextView == null || brokerServiceFeeTextView == null) {
            return
        }

        if (brokerNameTextView?.text.isNullOrEmpty() || brokerTransactionFeeTextView?.text.isNullOrEmpty()) {
            return
        }

        if (brokerTransactionFeeTextView?.text?.toString()?.toFloatOrNull() == null) {
            return
        }

        var serviceFee = 0f
        if (!brokerServiceFeeTextView?.text.isNullOrEmpty()) {
            val fee = brokerServiceFeeTextView?.text?.toString()?.toFloatOrNull()
            if (fee != null) {
                serviceFee = fee
            }
        }

        var isIis = false
        if(brokerIsIisCheckBox?.isChecked != null){
            isIis = brokerIsIisCheckBox?.isChecked!!
        }

        onCompleteListener?.onCompleteAdding(
            Broker(
                null,
                brokerNameTextView?.text?.toString(),
                serviceFee,
                brokerTransactionFeeTextView?.text?.toString()?.toFloat()!!,
                isIis
            )
        )
        onDestroy()
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        rootView = null
        brokerNameTextView?.text?.clear()
        brokerTransactionFeeTextView?.text?.clear()
        brokerServiceFeeTextView?.text?.clear()
        brokerIsIisCheckBox?.isChecked = false
        dismiss()
    }
}