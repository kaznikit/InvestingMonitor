package com.dotnet.nikit.investingmonitor.views.main.dialog_fragments

import android.app.DatePickerDialog
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import dagger.android.support.DaggerFragment
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseAddAssetFragment(parentFragment: DaggerFragment,
                                    resource: Int, title: String) : BaseAddFragment(parentFragment, resource, title) {

    var adapter: ArrayAdapter<String>? = null
    var currencies: ArrayList<String>? = null

    var nameEditText: EditText? = null
    var tickerEditText: EditText? = null
    var buyPriceEditText: EditText? = null
    var buyDateEditText: EditText? = null
    var currencySpinner: Spinner? = null
    var currentPriceEditText: EditText? = null
    var sellPriceEditText: EditText? = null
    var sellDateEditText: EditText? = null
    var addButton: Button? = null

    var isInitialized = false

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    fun checkFields(): Boolean {
        if (tickerEditText?.text?.isEmpty()!! ||
            buyDateEditText?.text?.isEmpty()!! ||
            buyPriceEditText?.text?.isEmpty()!! ||
            nameEditText?.text?.isEmpty()!! ||
            currencySpinner?.selectedItem.toString().isEmpty()
        ) {
            return false
        }
        return true
    }

    fun showDatePickerDialog(editTextView : EditText){
        with(editTextView, {
            setOnTouchListener { view, motionEvent ->
                if(motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val dpd = DatePickerDialog(
                        context!!,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            setText("$dayOfMonth/$monthOfYear/$year")
                        },
                        year,
                        month,
                        day
                    )
                    dpd.show()
                }
                return@setOnTouchListener true
            }
        })
    }
}