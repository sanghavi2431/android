package `in`.woloo.www.mapdirection.mvp

import `in`.woloo.www.mapdirection.model.NavigationRewardsResponse

interface MapDirectionView {
    fun navigationRewardSuccess(navigationRewardsResponse: NavigationRewardsResponse?)
}
