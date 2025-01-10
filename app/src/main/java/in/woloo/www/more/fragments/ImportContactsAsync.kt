package `in`.woloo.www.more.fragments

import android.app.Activity
import android.os.AsyncTask
import com.google.gson.annotations.Expose
import `in`.woloo.www.utils.Logger
import jagerfield.mobilecontactslibrary.Contact.Contact
import jagerfield.mobilecontactslibrary.Utilities.Utilities
import java.util.*

class ImportContactsAsync(
    private val activity: Activity,
    private val client: ICallback
) : AsyncTask<Void, Void, Void>() {

    @Expose
    private var contacts: ArrayList<Contact>? = null

    override fun onPostExecute(aVoid: Void?) {
        contacts?.let { client.mobileContacts(it) }
    }

    override fun doInBackground(vararg params: Void?): Void? {
        try {
            val importContacts = ImportContacts(activity)
            contacts = importContacts.getContacts()
            val str = ""
        } catch (e: Exception) {
            Logger.e(Utilities.TAG_LIB, e.message!!)
        }
        return null
    }

    interface ICallback {
        fun mobileContacts(contactList: ArrayList<Contact>)
    }
}
