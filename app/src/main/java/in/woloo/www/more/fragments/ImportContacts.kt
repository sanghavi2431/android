package `in`.woloo.www.more.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import com.google.gson.annotations.Expose
import `in`.woloo.www.utils.Logger
import jagerfield.mobilecontactslibrary.Contact.Contact
import jagerfield.mobilecontactslibrary.FieldContainer.FieldsContainer
import jagerfield.mobilecontactslibrary.Utilities.Utilities

class ImportContacts
    (private val activity: Activity) {
    private var contactsIdMap: LinkedHashMap<Long, Contact>? = null

    @Expose
    private var contacts: ArrayList<Contact>? = null

    fun getContacts(): ArrayList<Contact>? {
        return mobileContacts
    }

    fun insertContact(contact: Contact) {
        contacts!!.add(contact)
    }

    private val keyWord: Array<String>?
        get() = null

    private val filter: String?
        get() = null

    private val columns: Array<String>
        get() {
            val columns: MutableSet<String> =
                HashSet()

            val fieldsContainer = FieldsContainer()
            columns.addAll(fieldsContainer.elementsColumns)
            columns.add(ContactsContract.RawContacts.CONTACT_ID)
            columns.add(ContactsContract.Data.MIMETYPE)
            columns.add(ContactsContract.Data.PHOTO_URI)

            return columns.toTypedArray<String>()
        }

    val mobileContacts: ArrayList<Contact>?
        get() {
            val flag = hasPermission(Manifest.permission.READ_CONTACTS)

            if (!flag) {
                Logger.i(
                    Utilities.TAG_LIB,
                    "Missing permission READ_CONTACTS"
                )
                return ArrayList()
            }

            val cursor: Cursor? = activity.contentResolver
                .query(
                    ContactsContract.Data.CONTENT_URI, // The content URI of the words table
                    columns,                      // The columns to return for each row
                    filter,                       // Selection criteria
                    keyWord,                      // Selection criteria
                    ContactsContract.Data.DISPLAY_NAME // The sort order for the returned rows
                )


            if (cursor != null) {
                var id: Long
                var photoUri: String?
                var columnIndex: String?
                contactsIdMap = LinkedHashMap()
                contacts = ArrayList()

                while (cursor.moveToNext()) {
                    id = Utilities.getLong(
                        cursor,
                        ContactsContract.RawContacts.CONTACT_ID
                    )

                    var contact = contactsIdMap!![id]
                    if (contact == null) {
                        contact = Contact(id)
                        contactsIdMap!![id] = contact
                        insertContact(contact)
                    }

                    try {
                        photoUri =
                            Utilities.getColumnIndex(
                                cursor,
                                ContactsContract.Data.PHOTO_URI
                            )
                        if (photoUri != null && !photoUri.isEmpty()) {
                            contact.photoUri = photoUri
                        }
                    } catch (e: Exception) {
                        photoUri = ""
                    }

                    columnIndex =
                        Utilities.getColumnIndex(
                            cursor,
                            ContactsContract.Data.MIMETYPE
                        )
                    contact.execute(cursor, columnIndex)
                }

                cursor.close()
            }

            if (contacts!!.isEmpty()) {
                Logger.i(
                    Utilities.TAG_LIB,
                    "Lib: No contacts found"
                )
            }

            return contacts
        }

    @Synchronized
    fun hasPermission(permission: String?): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        if (permission == null || permission.isEmpty()) {
            return false
        }

        val permissionsArray = arrayOf(permission)

        for (i in permissionsArray.indices) {
            if (activity.checkSelfPermission(permissionsArray[i]) == PackageManager.PERMISSION_DENIED) {
                Logger.w(
                    Utilities.TAG_LIB,
                    "$permission permission is missing."
                )
                return false
            } else {
                return true
            }
        }

        return false
    }
}



