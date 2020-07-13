package com.dotnet.nikit.investingmonitor.views.main.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dotnet.nikit.investingmonitor.R
import com.dotnet.nikit.investingmonitor.interfaces.OnSelectListViewItem
import com.dotnet.nikit.investingmonitor.view_models.main.Status
import com.dotnet.nikit.investingmonitor.view_models.main.navigation.NavSettingsViewModel
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.nav_settings_fragment.*
import javax.inject.Inject

class NavSettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var navSettingsViewModel: NavSettingsViewModel

    private var settingsAdapter : ArrayAdapter<String>? = null

    private var settingsArray : ArrayList<String>? = null

    private var onSelectListViewItemListener : OnSelectListViewItem<String>? = null

    override fun onCreateView(inflater: LayoutInflater, settingsFrag: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.nav_settings_fragment, settingsFrag, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        onSelectListViewItemListener = activity as OnSelectListViewItem<String>

        navSettingsViewModel = ViewModelProvider(this, viewModelFactory).get(NavSettingsViewModel::class.java)

        settingsArray = ArrayList()
        settingsAdapter = ArrayAdapter(activity!!, R.layout.invest_listview_item, settingsArray!!)
        settings_listview.adapter = settingsAdapter

        navSettingsViewModel.getSettings().observe(this.viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    settingsArray?.clear()
                    for(setting in it.data!!) {
                        settingsArray?.add(setting)
                    }
                    settingsAdapter?.notifyDataSetChanged()
                }
                Status.LOADING -> {
                    Log.d("Settings", "Идет загрузка данных")
                }
                Status.ERROR -> {
                    Log.e("Settings", "Ошибка при загрузке настроек")
                }
            }
        })

        settings_listview.setOnItemClickListener { adapterView, view, i, l ->
            when(i){
                0 -> {
                    onSelectListViewItemListener?.onSelectListViewItem("Брокеры")
                }
            }
        }
    }

    private fun setupToolbar() {
        (activity as DaggerAppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val textView = activity!!.findViewById<TextView>(R.id.toolbar_header_textView)
        textView.text = "Настройки"
        val button = activity!!.findViewById<Button>(R.id.toolbar_add_button)
        button.visibility = View.GONE
    }
}