package com.dotnet.nikit.investingmonitor.views.main.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.interfaces.OnCompleteAddingData
import com.dotnet.nikit.investingmonitor.interfaces.OnGroupItemSelected
import com.dotnet.nikit.investingmonitor.interfaces.OnSelectListViewItem
import com.dotnet.nikit.investingmonitor.listview_adapters.AssetListViewItem
import com.dotnet.nikit.investingmonitor.listview_adapters.AssetsListviewAdapter
import com.dotnet.nikit.investingmonitor.models.Bond
import com.dotnet.nikit.investingmonitor.models.Share
import com.dotnet.nikit.investingmonitor.view_models.main.navigation.NavListViewModel
import com.dotnet.nikit.investingmonitor.views.main.dialog_fragments.BondAddFragment
import com.dotnet.nikit.investingmonitor.views.main.dialog_fragments.ShareAddFragment
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.IllegalArgumentException
import kotlin.math.roundToInt

class NavListFragment : DaggerFragment(), OnCompleteAddingData<Any>, OnGroupItemSelected {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var navListViewModel: NavListViewModel

    private var assetsListviewAdapter: AssetsListviewAdapter? = null
    private var assetsExpandableListView: ExpandableListView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var rootview: View? = null

    private var portfolioId: Int = -1
    private lateinit var portfolioName: String

    val parentsList = ArrayList<String>()
    val childsList = ArrayList<ArrayList<AssetListViewItem>>()

    val shareList = ArrayList<AssetListViewItem>()
    val bondList = ArrayList<AssetListViewItem>()
    val etfList = ArrayList<AssetListViewItem>()

    private var needsToUpdateAssetsList = true

    private var addShareFragment: ShareAddFragment? = null
    private var addBondFragment: BondAddFragment? = null

    private var onSelectListViewItemListener: OnSelectListViewItem<AssetListViewItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater, listFrag: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootview = inflater.inflate(R.layout.nav_list_fragment, listFrag, false)
        portfolioId = arguments?.getInt("id") ?: throw IllegalArgumentException("Айди портфеля не может быть null.")
        portfolioName = arguments?.getString("name") ?: throw IllegalArgumentException("Не найдено название портфеля")
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        onSelectListViewItemListener = activity as OnSelectListViewItem<AssetListViewItem>

        navListViewModel =
            ViewModelProvider(this, viewModelFactory).get(NavListViewModel::class.java)

        assetsExpandableListView = activity?.findViewById(R.id.assets_expandable_list_view)

        swipeRefreshLayout = activity?.findViewById(R.id.assets_swipe_refresh_layout)

        childsList.add(0, shareList)
        childsList.add(1, bondList)
        childsList.add(2, etfList)

        parentsList.add("Акции")
        parentsList.add("Облигации")
        parentsList.add("ПАИ")

        navListViewModel.getSharesForPortfolio().observe(this.viewLifecycleOwner, Observer {
            shareList.clear()
            for (share in it) {
                shareList.add(
                    AssetListViewItem(
                        share.id ?: throw IllegalArgumentException("Айди акции не может быть null."),
                        share.name,
                        share.buyPrice.toString(),
                        share.currentPrice?.toString(),
                        getPriceChange(share.buyPrice, share.currentPrice),
                        AssetTypeEnum.Share
                    )
                )
            }
            assetsListviewAdapter?.notifyDataSetChanged()
        })

        navListViewModel.getBondsForPortfolio().observe(this.viewLifecycleOwner, Observer {
            bondList.clear()
            for(bond in it){
                bondList.add(AssetListViewItem(bond.id ?: throw IllegalArgumentException("Айди облигации не может быть null."),
                    bond.name,
                    bond.buyPrice.toString(),
                    bond.currentPrice?.toString(),
                    getPriceChange(bond.buyPrice, bond.currentPrice),
                    AssetTypeEnum.Bond))
            }
            assetsListviewAdapter?.notifyDataSetChanged()
        })

        assetsListviewAdapter = AssetsListviewAdapter(activity!!, childsList, parentsList, this)
        assetsExpandableListView?.setAdapter(assetsListviewAdapter)

        swipeRefreshLayout?.setOnRefreshListener {
            swipeRefreshLayout?.isRefreshing = true
            needsToUpdateAssetsList = true
            navListViewModel.getAssetsByPortfolioId(portfolioId)
        }

        assetsExpandableListView?.setOnChildClickListener { expandableListView, view, i, i2, l ->
            when (i) {
                0 -> onSelectListViewItemListener?.onSelectListViewItem(shareList[i2])
                1 -> onSelectListViewItemListener?.onSelectListViewItem(bondList[i2])
                2 -> onSelectListViewItemListener?.onSelectListViewItem(etfList[i2])
            }
            return@setOnChildClickListener true
        }

        navListViewModel.getAssetsByPortfolioId(portfolioId)

    }

    private fun setupToolbar() {
        (activity as DaggerAppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val textView = activity!!.findViewById<TextView>(R.id.toolbar_header_textView)
        textView.text = "Активы"

        val button = activity!!.findViewById<Button>(R.id.toolbar_add_button)
        button.visibility = View.GONE
    }

    private fun showAddShareFragment() {
        if (addShareFragment == null) {
            addShareFragment =
                ShareAddFragment(this, R.layout.share_add_fragment, "Добавление акции")
        }

        if (addShareFragment!!.isAdded) {
            (activity as DaggerAppCompatActivity).supportFragmentManager.fragments.clear()
        }

        addShareFragment?.show(
            (activity as DaggerAppCompatActivity).supportFragmentManager,
            "share_add_fragment"
        )
    }

    private fun showAddBondFragment() {
        if (addBondFragment == null) {
            addBondFragment =
                BondAddFragment(this, R.layout.bond_add_fragment, "Добавление облигации")
        }

        if (addBondFragment!!.isAdded) {
            (activity as DaggerAppCompatActivity).supportFragmentManager.fragments.clear()
        }

        addBondFragment?.show(
            (activity as DaggerAppCompatActivity).supportFragmentManager,
            "bond_add_fragment"
        )
    }

    override fun onCompleteAdding(data: Any) {
        when (data) {
            is Share -> {
                navListViewModel.addShare(data, portfolioId)
            }
            is Bond -> {
                navListViewModel.addBond(data, portfolioId)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        parentsList.clear()
        childsList.clear()
        return true
    }

    private fun getPriceChange(buyPrice: Float, currentPrice: Float?): String {
        if (currentPrice == null || currentPrice == 0f) {
            return ""
        }
        return if (buyPrice > currentPrice) {
            "-${((1 - currentPrice / buyPrice) * 100).roundToInt()}%"
        } else {
            "${(currentPrice / buyPrice * 100).roundToInt()}".substring(1, 3) + "%"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        parentsList.clear()
        childsList.clear()
        shareList.clear()
        bondList.clear()
        etfList.clear()
        needsToUpdateAssetsList = true
        assetsListviewAdapter = null
    }

    /**
     * При нажатии на кнопку добавить в assetsExpandableListView
     */
    override fun onSelectGroupItem(groupPosition: Int) {
        when (groupPosition) {
            0 -> {
                showAddShareFragment()
            }
            1 -> {
                showAddBondFragment()
            }
        }
    }
}