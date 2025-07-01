/*
 * Copyright (c) 2018 - 5 - 1. JetSynthesys Pvt. Ltd.
 * @Rahul Pawar
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package in.woloo.www.firebase;


public class NotificationBean {

    private String title;
    private String description;
    private String category;
    private String date;
    private String landingUrl;
    private String read;
    private String click_url;
    private String parent_tab;
    private String tab_id;
    private String content_id;

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = click_url;
    }

    public String getParent_tab() {
        return parent_tab;
    }

    public void setParent_tab(String parent_tab) {
        this.parent_tab = parent_tab;
    }

    public String getTab_id() {
        return tab_id;
    }

    public void setTab_id(String tab_id) {
        this.tab_id = tab_id;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    private String notificationId;

    public String getNotificationImageUrl() {
        return notificationImageUrl;
    }

    public void setNotificationImageUrl(String notificationImageUrl) {
        this.notificationImageUrl = notificationImageUrl;
    }

    private String notificationImageUrl;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLandingUrl() {
        return landingUrl;
    }

    public void setLandingUrl(String landingUrl) {
        this.landingUrl = landingUrl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
