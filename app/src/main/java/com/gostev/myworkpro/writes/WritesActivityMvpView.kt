package com.gostev.myworkpro.writes

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gostev.myworkpro.models.WriteModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
 interface WritesActivityMvpView: MvpView {
      fun showWrites(result: ArrayList<WriteModel>)
 }