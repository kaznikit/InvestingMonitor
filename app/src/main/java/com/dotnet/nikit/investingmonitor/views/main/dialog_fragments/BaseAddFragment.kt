package com.dotnet.nikit.investingmonitor.views.main.dialog_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotnet.nikit.investingmonitor.interfaces.OnCompleteAddingData
import dagger.android.support.DaggerDialogFragment
import dagger.android.support.DaggerFragment

abstract class BaseAddFragment(parentFragment: DaggerFragment, resource: Int, title: String) :
    DaggerDialogFragment() {

    var instance: BaseAddFragment? = null
    var parentFragment: DaggerFragment? = parentFragment
    var rootView: View? = null
    var onCompleteListener: OnCompleteAddingData<Any>? = null
    var resource: Int? = resource
    var title : String? = title

    init {
        onCompleteListener = parentFragment as OnCompleteAddingData<Any>
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (rootView == null) {
            rootView = LayoutInflater.from(activity).inflate(resource!!, null, false)
        }
        return AlertDialog.Builder(activity).setView(rootView)
            .setTitle(title)
            .setCancelable(true).create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        addFrag: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (showsDialog) {
            return super.onCreateView(inflater, addFrag, savedInstanceState)
        }
        return rootView
    }

    override fun onDestroyView() {
        if (rootView != null) {
            val parentViewGroup = rootView?.parent as ViewGroup?
            parentViewGroup?.removeAllViews()
        }
        super.onDestroyView()
    }

    abstract fun initViews()
}