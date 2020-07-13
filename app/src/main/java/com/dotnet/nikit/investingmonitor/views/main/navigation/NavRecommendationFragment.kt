package com.dotnet.nikit.investingmonitor.views.main.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotnet.nikit.investingmonitor.R
import dagger.android.support.DaggerFragment

class NavRecommendationFragment : DaggerFragment() {
    override fun onCreateView(inflater: LayoutInflater, recommendFrag: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.nav_recommendation_fragment, recommendFrag, false)
    }
}