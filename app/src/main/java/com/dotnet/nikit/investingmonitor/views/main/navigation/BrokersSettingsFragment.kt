package com.dotnet.nikit.investingmonitor.views.main.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.interfaces.OnCompleteAddingData
import com.dotnet.nikit.investingmonitor.listview_adapters.BrokersRecycleAdapter
import com.dotnet.nikit.investingmonitor.mappers.BrokerMapper
import com.dotnet.nikit.investingmonitor.models.Broker
import com.dotnet.nikit.investingmonitor.view_models.main.navigation.NavListViewModel
import com.dotnet.nikit.investingmonitor.views.main.dialog_fragments.BrokerAddFragment
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BrokersSettingsFragment : DaggerFragment(), OnCompleteAddingData<Broker> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var navListViewModel: NavListViewModel

    private var recyclerView: RecyclerView? = null

    private var brokers : List<Broker>? = null
    private var brokersAdapter: BrokersRecycleAdapter? = null

    private var rootview: View? = null

    private var addBrokerFragment : BrokerAddFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        brokerFrag: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootview = inflater.inflate(R.layout.brokers_settings_fragment, brokerFrag, false)
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        initRecyclerView()

        navListViewModel =
            ViewModelProvider(this, viewModelFactory).get(NavListViewModel::class.java)

        navListViewModel.getBrokers().observe(this.viewLifecycleOwner, Observer {
            brokers = BrokerMapper.mapBrokersList(it)
            brokersAdapter?.clearItems()
            brokersAdapter?.setItems(brokers!!)
        })
    }

    private fun setupToolbar() {
        (activity as DaggerAppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val textView = activity!!.findViewById<TextView>(R.id.toolbar_header_textView)
        textView.text = "Брокеры"

        val button = activity!!.findViewById<Button>(R.id.toolbar_add_button)
        button.visibility = View.VISIBLE
        button.setOnClickListener {
            showAddBrokerFragment()
        }
    }

    private fun showAddBrokerFragment() {
        if (addBrokerFragment == null) {
            addBrokerFragment = BrokerAddFragment(this, R.layout.broker_add_fragment, "Добавление брокера")
        }

        if (addBrokerFragment!!.isAdded) {
            (activity as DaggerAppCompatActivity).supportFragmentManager.fragments.clear()
        }
        addBrokerFragment?.show((activity as DaggerAppCompatActivity).supportFragmentManager,
            "broker_add_fragment")
    }

    private fun initRecyclerView(){
        recyclerView = rootview?.findViewById(R.id.brokers_listview)
        val headerView = layoutInflater.inflate(R.layout.brokers_listview_header, null)

        recyclerView?.layoutManager = LinearLayoutManager(rootview!!.context)

        brokersAdapter = BrokersRecycleAdapter(activity!!, object : BrokersRecycleAdapter.OnItemClickListener{
            override fun onItemClick(broker: Broker) {
                /**
                 * открывать диалог для изменения брокера
                 */
            }
        })

        brokersAdapter?.addHeaderView(headerView)

        recyclerView?.adapter = brokersAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        closeFragment()
        return super.onOptionsItemSelected(item)
    }

    private fun closeFragment() {
        activity?.supportFragmentManager?.popBackStack()
    }

    override fun onCompleteAdding(data: Broker) {
        navListViewModel.addBroker(data)
        navListViewModel.getBrokers()
    }
}