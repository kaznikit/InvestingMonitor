package com.dotnet.nikit.investingmonitor.views.main.dialog_fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.enums.AssetTypeEnum
import com.dotnet.nikit.investingmonitor.models.BaseAsset
import com.dotnet.nikit.investingmonitor.models.Dividend
import com.dotnet.nikit.investingmonitor.util.Utils
import dagger.android.support.DaggerFragment
import java.util.*

class DividendAddFragment(
    parentFragment: DaggerFragment,
    resource: Int, title: String
) : BaseAddFragment(parentFragment, resource, title) {

    private var dateEditText : EditText? = null
    private var paymentValueEditText : EditText? = null
    private var acceptButton: Button? = null

    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private var assetId : Int? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    override fun initViews() {
        dateEditText = rootView?.findViewById(R.id.dividend_add_date)
        paymentValueEditText = rootView?.findViewById(R.id.dividend_add_payment)
        acceptButton = rootView?.findViewById(R.id.accept_changes_button)

        showDatePickerDialog(dateEditText!!)

        acceptButton?.setOnClickListener {
            acceptButton?.isEnabled = false
            acceptDividendData()
            acceptButton?.isEnabled = true
        }
    }

    fun addAssetId(id : Int){
        assetId = id
    }

    private fun acceptDividendData(){
        if(paymentValueEditText?.text?.toString()?.toFloatOrNull() == null){
            return
        }

        if(dateEditText?.text == null){
            return
        }

        onCompleteListener?.onCompleteAdding(Dividend(Utils.formatStringToDate(dateEditText?.text?.toString())!!, paymentValueEditText?.text?.toString()!!.toFloat(), assetId!!))
    }

    private fun showDatePickerDialog(editTextView : EditText){
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