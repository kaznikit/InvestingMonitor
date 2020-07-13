package com.dotnet.nikit.investingmonitor.listview_adapters

import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum

data class AssetListViewItem(
    var id : Int,
    var name: String,
    var buyPrice: String,
    var currentPrice: String?,
    var priceChange: String?,
    var assetType : AssetTypeEnum
)