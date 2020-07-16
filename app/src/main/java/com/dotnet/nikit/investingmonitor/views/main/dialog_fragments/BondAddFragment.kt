package com.dotnet.nikit.investingmonitor.views.main.dialog_fragments

import android.os.Bundle
import android.widget.*
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.enums.CurrencyEnum
import com.dotnet.nikit.investingmonitor.models.Bond
import com.dotnet.nikit.investingmonitor.util.Utils
import dagger.android.support.DaggerFragment
import kotlin.collections.ArrayList

class BondAddFragment(parentFragment: DaggerFragment,
                      resource: Int, title: String
) : BaseAddAssetFragment(parentFragment, resource, title) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }


    override fun initViews() {

        if(!isInitialized) {
            nameEditText = rootView?.findViewById(R.id.add_name)!!
            tickerEditText = rootView?.findViewById(R.id.add_ticker)!!
            buyPriceEditText = rootView?.findViewById(R.id.add_buy_price)!!
            buyDateEditText = rootView?.findViewById(R.id.add_buy_date)!!
            currencySpinner = rootView?.findViewById(R.id.add_currency_spinner)!!
            currentPriceEditText = rootView?.findViewById(R.id.add_current_price)!!
            sellPriceEditText = rootView?.findViewById(R.id.add_sell_price)!!
            sellDateEditText = rootView?.findViewById(R.id.add_sell_date_price)!!
            addButton = rootView?.findViewById(R.id.accept_changes_button)!!

            showDatePickerDialog(buyDateEditText!!)
            showDatePickerDialog(sellDateEditText!!)

            addButton?.setOnClickListener {
                addButton?.isEnabled = false
                addBondData()
                addButton?.isEnabled = true
            }

            currencies = ArrayList()
            for (currency in CurrencyEnum.values()) {
                currencies?.add(currency.name)
            }
            adapter =
                ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, currencies!!)
            currencySpinner?.adapter = adapter

            isInitialized = true
        }
    }

    private fun addBondData() {
        if (!checkFields()) {
            Toast.makeText(activity, "Заполнены не все поля!", Toast.LENGTH_SHORT).show()
            return
        }

        var currentPrice: Float? = 0f
        if (!currentPriceEditText?.text.isNullOrEmpty()) {
            currentPrice = currentPriceEditText?.text?.toString()?.toFloatOrNull()
        }

        onCompleteListener?.onCompleteAdding(
            Bond(
                null,
                tickerEditText?.text.toString(),
                nameEditText?.text.toString(),
                buyPriceEditText?.text.toString().toFloat(),
                Utils.formatStringToDate(buyDateEditText?.text?.toString())!!,
                CurrencyEnum.valueOf(currencySpinner?.selectedItem.toString()),
                currentPrice,
                AssetTypeEnum.Bond
            )
        )

        onDestroy()
        dismiss()
    }
}