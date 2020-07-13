package com.dotnet.nikit.investingmonitor.listview_adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.TextView
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.interfaces.OnGroupItemSelected

class AssetsListviewAdapter(
    private var context: Context,
    private var childList: ArrayList<ArrayList<AssetListViewItem>>,
    private var parents: ArrayList<String>,
    private var onGroupItemSelected: OnGroupItemSelected
) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        return childList[groupPosition]
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertCopy = convertView
        if (convertCopy == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertCopy = inflater.inflate(R.layout.assets_listview_item_parent, parent, false)
        }

        val parentTextvew = convertCopy?.findViewById(R.id.assets_item_parent_text_view) as TextView
        parentTextvew.text = parents[groupPosition]

        val addAssetButton = convertCopy.findViewById<Button>(R.id.add_asset_button)
        addAssetButton.setOnClickListener {
            onGroupItemSelected.onSelectGroupItem(groupPosition)
        }

        return convertCopy
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        if (childList.getOrNull(groupPosition) != null) {
            return childList[groupPosition].size
        }
        return 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        if (childList.getOrNull(groupPosition)?.getOrNull(childPosition) != null) {
            return childList[groupPosition][childPosition]
        }
        return ""
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertCopy = convertView
        if (convertCopy == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertCopy = inflater.inflate(R.layout.assets_listview_item_child, parent, false)
        }

        val childNameTextView =
            convertCopy?.findViewById<TextView>(R.id.assets_list_view_child_name_text_view)
        childNameTextView?.text = (getChild(groupPosition, childPosition) as AssetListViewItem).name

        val childBuyPriceTextView = convertCopy?.findViewById<TextView>(R.id.assets_list_view_child_buy_price_text_view)
        childBuyPriceTextView?.text = (getChild(groupPosition, childPosition) as AssetListViewItem).buyPrice

        val childCurrentPriceTextView = convertCopy?.findViewById<TextView>(R.id.assets_list_view_child_current_price_text_view)
        childCurrentPriceTextView?.text = (getChild(groupPosition, childPosition) as AssetListViewItem).currentPrice

        val childChangePriceTextView = convertCopy?.findViewById<TextView>(R.id.assets_list_view_child_price_change_text_view)
        childChangePriceTextView?.text = (getChild(groupPosition, childPosition) as AssetListViewItem).priceChange

        return convertCopy!!
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return childList.size
    }
}