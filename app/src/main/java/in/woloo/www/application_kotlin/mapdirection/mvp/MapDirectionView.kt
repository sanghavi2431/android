package `in`.woloo.www.application_kotlin.mapdirection.mvp

import `in`.woloo.www.application_kotlin.mapdirection.model.NavigationRewardsResponse

interface MapDirectionView {
    fun navigationRewardSuccess(navigationRewardsResponse: NavigationRewardsResponse?)
}
