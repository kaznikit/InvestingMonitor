package com.dotnet.nikit.investingmonitor.views.main.dialog_fragments

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.mappers.BrokerMapper
import com.dotnet.nikit.investingmonitor.models.Broker
import com.dotnet.nikit.investingmonitor.models.Portfolio
import com.dotnet.nikit.investingmonitor.view_models.main.navigation.NavListViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class PortfolioAddFragment(parentFragment: DaggerFragment,
                           resource: Int, title: String) : BaseAddFragment(parentFragment, resource, title) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var navListViewModel: NavListViewModel

    private var brokersSpinner: Spinner? = null
    private var brokers: List<Broker>? = null
    private var brokerNames: ArrayList<String>? = null
    private var addButton: Button? = null
    private var portfolioNameEditText: EditText? = null
    private var adapter: ArrayAdapter<String>? = null

    /*companion object {
        fun newInstance(parentFragment: DaggerFragment, resource: Int, title : String): BaseAddFragment {
            if (instance == null) {
                instance = PortfolioAddFragment()
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
        brokerNames = ArrayList()
        navListViewModel =
            ViewModelProvider(this, viewModelFactory).get(NavListViewModel::class.java)

        brokersSpinner = rootView?.findViewById(R.id.portfolio_add_broker_spinner)

        navListViewModel.getBrokers().observe(activity!!, Observer {
            brokers = BrokerMapper.mapBrokersList(it)
            brokerNames!!.clear()
            for (brok in brokers!!) {
                brokerNames!!.add(brok.name!!)
            }

            if (adapter == null) {
                adapter = ArrayAdapter(
                    context!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    brokerNames!!
                )
            }
            brokersSpinner?.adapter = adapter
            adapter?.notifyDataSetChanged()
        })

        portfolioNameEditText = rootView?.findViewById(R.id.portfolio_add_name)
        addButton = rootView?.findViewById(R.id.btn_add_portfolio)
        addButton?.setOnClickListener {
            addButton?.isEnabled = false
            addPortfolioData()
            addButton?.isEnabled = true
        }
    }

    private fun addPortfolioData() {
        if (portfolioNameEditText?.text?.isEmpty()!! || brokersSpinner?.selectedItem.toString()
                .isEmpty()
        ) {
            return
        }

        onCompleteListener?.onCompleteAdding(
            Portfolio(
                null,
                portfolioNameEditText?.text.toString(),
                brokers?.first { x -> x.name == brokersSpinner?.selectedItem.toString() }?.id
            )
        )
        onDestroy()
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        rootView = null
        portfolioNameEditText?.text?.clear()
        brokersSpinner?.setSelection(0)
        dismiss()
    }
}