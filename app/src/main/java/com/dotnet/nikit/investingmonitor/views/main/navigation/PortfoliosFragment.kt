package com.dotnet.nikit.investingmonitor.views.main.navigation

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.interfaces.OnCompleteAddingData
import com.dotnet.nikit.investingmonitor.interfaces.OnSelectListViewItem
import com.dotnet.nikit.investingmonitor.mappers.PortfolioMapper
import com.dotnet.nikit.investingmonitor.models.Portfolio
import com.dotnet.nikit.investingmonitor.view_models.main.navigation.NavListViewModel
import com.dotnet.nikit.investingmonitor.views.main.dialog_fragments.PortfolioAddFragment
import com.google.android.material.button.MaterialButton
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class PortfoliosFragment : DaggerFragment(), OnCompleteAddingData<Portfolio> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var navListViewModel: NavListViewModel

    private var portfolioAddFragment: PortfolioAddFragment? = null

    private var portfoliosAdapter: ArrayAdapter<String>? = null

    private var portfoliosListView: ListView? = null

    private var portfolios: List<Portfolio>? = null
    private var portfoliosArray: ArrayList<String>? = ArrayList()

    private var rootview: View? = null

    private var onSelectListViewItemListener: OnSelectListViewItem<Portfolio>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootview = inflater.inflate(R.layout.portfolio_listview_fragment, container, false)
        return rootview
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSelectListViewItemListener = activity as OnSelectListViewItem<Portfolio>

        setupToolbar()

        navListViewModel =
            ViewModelProvider(this, viewModelFactory).get(NavListViewModel::class.java)

        initPortfoliosListView()

        portfoliosListView?.setOnItemClickListener { adapterView, view, i, l ->
            onSelectListViewItemListener?.onSelectListViewItem(portfolios?.first { x ->
                x.name == adapterView?.adapter?.getItem(i)}!!)
        }
    }

    private fun initPortfoliosListView(){
        portfoliosListView = activity?.findViewById(R.id.portfolios_listview)

        if (portfoliosAdapter == null) {
            portfoliosAdapter =
                ArrayAdapter(activity!!, R.layout.invest_listview_item, portfoliosArray!!)
        }
        portfoliosListView?.adapter = portfoliosAdapter

        navListViewModel.getPortfolios().removeObservers(this.viewLifecycleOwner)
        navListViewModel.getPortfolios().observe(this.viewLifecycleOwner, Observer {
            portfolios = PortfolioMapper.mapPortfoliosList(it)
            portfoliosArray!!.clear()

            for (portfolio in portfolios!!) {
                portfoliosArray!!.add(portfolio.name!!)
            }

            portfoliosAdapter?.notifyDataSetChanged()
        })

        val inflater = activity?.layoutInflater?.inflate(R.layout.add_button,null)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        params.gravity = Gravity.CENTER_HORIZONTAL
        val btnAdd = inflater?.findViewById<MaterialButton>(R.id.add_button)
        btnAdd?.text = " добавить портфель"
        btnAdd?.layoutParams = params
        portfoliosListView?.addFooterView(btnAdd)
    }

    private fun setupToolbar() {
        (activity as DaggerAppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val textView = activity!!.findViewById<TextView>(R.id.toolbar_header_textView)
        textView.text = "Портфели"

        val button = activity!!.findViewById<Button>(R.id.toolbar_add_button)
        button.visibility = VISIBLE
        button.setOnClickListener {
            showAddPortfolioFragment()
        }
    }

    private fun showAddPortfolioFragment() {
        if (portfolioAddFragment == null) {
            portfolioAddFragment =
                PortfolioAddFragment(this, R.layout.portfolio_add_fragment, "Добавление портфеля")
        }

        if (portfolioAddFragment!!.isAdded) {
            (activity as DaggerAppCompatActivity).supportFragmentManager.fragments.clear()
        }
        portfolioAddFragment?.show(
            (activity as DaggerAppCompatActivity).supportFragmentManager,
            "portfolio_add_fragment"
        )
    }


    /**
     * При добавлении нового портфеля
     */
    override fun onCompleteAdding(data: Portfolio) {
        navListViewModel.addPortfolio(data)
        navListViewModel.getPortfolios()
    }
}