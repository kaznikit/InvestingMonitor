package com.dotnet.nikit.investingmonitor.views.main.navigation

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.enums.CurrencyEnum
import com.dotnet.nikit.investingmonitor.interfaces.OnCompleteAddingData
import com.dotnet.nikit.investingmonitor.listview_adapters.DividendsRecyclerAdapter
import com.dotnet.nikit.investingmonitor.mappers.DividendMapper
import com.dotnet.nikit.investingmonitor.models.Dividend
import com.dotnet.nikit.investingmonitor.models.Share
import com.dotnet.nikit.investingmonitor.view_models.main.navigation.NavListViewModel
import com.dotnet.nikit.investingmonitor.views.main.dialog_fragments.DividendAddFragment
import com.google.android.material.button.MaterialButton
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.atan

class AssetInfoFragment : DaggerFragment(), OnCompleteAddingData<Dividend> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var navListViewModel: NavListViewModel

    private var rootview: View? = null

    private var dividends : List<Dividend>? = null
    private var dividendsListView : RecyclerView? = null
    private var dividendsAdapter: DividendsRecyclerAdapter? = null

    private var id : Int? = null
    private var name : String? = null
    private var buyPrice : String? = null
    private var currentPrice : String? = null
    private var priceChange : String? = null

    private var nameTextView : TextView? = null
    private var buyPriceTextView : TextView? = null
    private var currentPriceTextView : TextView? = null
    private var priceChangeTextView : TextView? = null

    private var addDividendFragment : DividendAddFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootview = inflater.inflate(R.layout.asset_info_fragment, container, false)
        id = arguments?.getInt("id")
        name = arguments?.getString("name")
        buyPrice = arguments?.getString("buyPrice")
        currentPrice = arguments?.getString("currentPrice")
        priceChange = arguments?.getString("priceChange")
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navListViewModel = ViewModelProvider(this, viewModelFactory).get(NavListViewModel::class.java)

        dividendsListView = activity?.findViewById(R.id.dividends_list_view)
        nameTextView = activity?.findViewById(R.id.asset_info_name_text_view)
        buyPriceTextView = activity?.findViewById(R.id.asset_info_buy_price_text_view)
        currentPriceTextView = activity?.findViewById(R.id.asset_info_current_price_text_view)
        priceChangeTextView = activity?.findViewById(R.id.asset_info_price_change_text_view)

        nameTextView?.text = name
        buyPriceTextView?.text = buyPrice
        currentPriceTextView?.text = currentPrice
        priceChangeTextView?.text = priceChange

        dividendsAdapter = DividendsRecyclerAdapter(activity!!, object : DividendsRecyclerAdapter.OnItemClickListener{
            override fun onItemClick() {
                showAddDividendFragment()
            }
        })

        val headerView = layoutInflater.inflate(R.layout.dividends_listview_header, null)
        dividendsListView?.layoutManager = LinearLayoutManager(rootview?.context)
        dividendsListView?.adapter = dividendsAdapter
        dividendsAdapter?.addHeaderView(headerView)

        val footerView = layoutInflater.inflate(R.layout.add_dividend_button, null)

        dividendsAdapter?.addFooterView(footerView)

        dividends = ArrayList()

        navListViewModel.getDividends().observe(this.viewLifecycleOwner, Observer{
            dividends = DividendMapper.mapDividendsList(it)
            dividendsAdapter?.clearItems()
            dividendsAdapter?.setItems(dividends!!)
        })

        navListViewModel.getDividendsByAssetId(id!!)
    }

    private fun showAddDividendFragment() {
        if (addDividendFragment == null) {
            addDividendFragment = DividendAddFragment(this, R.layout.dividend_add_fragment, "Добавление дивиденда")
        }

        addDividendFragment?.addAssetId(id!!)

        if (addDividendFragment!!.isAdded) {
            (activity as DaggerAppCompatActivity).supportFragmentManager.fragments.clear()
        }
        addDividendFragment?.show((activity as DaggerAppCompatActivity).supportFragmentManager,
            "dividend_add_fragment")
    }

    override fun onCompleteAdding(data: Dividend) {
        navListViewModel.addDividend(data)
        navListViewModel.getDividendsByAssetId(id!!)
    }
}