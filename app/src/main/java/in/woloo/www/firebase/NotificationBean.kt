/*
 * Copyright (c) 2018 - 5 - 1. JetSynthesys Pvt. Ltd.
 * @Rahul Pawar
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package `in`.woloo.www.firebase


class NotificationBean {
    @JvmField
    var title: String? = null
    @JvmField
    var description: String? = null
    @JvmField
    var category: String? = null

    /*
    public NotificationBean(String title, String description, String category, String date, String notificationImageUrl, String landingUrl, String read) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.landingUrl = landingUrl;
        this.notificationImageUrl = notificationImageUrl;
        this.notificationId = notificationId;
        this.read = read;
    }
*/
    @JvmField
    var date: String? = null
    @JvmField
    var landingUrl: String? = null
    @JvmField
    var read: String? = null
    var click_url: String? = null
    var parent_tab: String? = null
    var tab_id: String? = null
    var content_id: String? = null

    var notificationId: String? = null

    @JvmField
    var notificationImageUrl: String? = null
}
