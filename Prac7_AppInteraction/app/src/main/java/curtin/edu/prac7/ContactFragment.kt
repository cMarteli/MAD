package curtin.edu.prac7

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
private const val REQUEST_CONTACT = 2
private const val REQUEST_READ_CONTACT_PERMISSION = 3
class ContactFragment : Fragment() {

    lateinit var pickContact: Button
    lateinit var more: Button
    lateinit var email: TextView
    lateinit var name: TextView
    lateinit var phone: TextView
    var contactId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pickContact = view.findViewById(R.id.pickContact)
        email = view.findViewById(R.id.emailView)
        name = view.findViewById(R.id.nameView)
        phone = view.findViewById(R.id.phoneView)
        more = view.findViewById(R.id.moreButton)
        name.visibility = View.INVISIBLE
        email.visibility = View.INVISIBLE
        phone.visibility = View.INVISIBLE
        more.visibility = View.INVISIBLE


        pickContact.setOnClickListener {pickContactButtonClicked() }
        more.setOnClickListener { view ->
            if (ContextCompat.checkSelfPermission(
                    view.context,
                    Manifest.permission.READ_CONTACTS
                )
                !== PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    (activity as MainActivity),
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_READ_CONTACT_PERMISSION
                )
            } else {
                showEmail()
                showPhone()
            }

        }
    }

    private fun pickContactButtonClicked() {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.data = ContactsContract.Contacts.CONTENT_URI
        startActivityForResult(intent, REQUEST_CONTACT)
    }

    private fun showEmail() { //Displays Email
        var result = ""
        val emailUri = Email.CONTENT_URI
        val queryFields = arrayOf(
            Email.ADDRESS
        )
        val whereClause = Email.CONTACT_ID + "=?"
        val whereValues = arrayOf(contactId.toString())
        val c = context?.contentResolver?.query(
            emailUri, queryFields, whereClause, whereValues, null) as Cursor
        c.use { c ->
            c.moveToFirst()
            do {
                if (c.count > 0) { //fixes crash if email not present
                    val emailAddress = c.getString(0)
                    result = "$result$emailAddress "
                }
            } while (c.moveToNext())
        }
        email.text = result
        email.visibility = View.VISIBLE
    }
    private fun showPhone() { //Displays phone number
        var result = ""
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val queryFields = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val whereClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?"
        val whereValues = arrayOf(contactId.toString())
        val c = context?.contentResolver?.query(
            phoneUri, queryFields, whereClause, whereValues, null) as Cursor
        c.use { c ->
            c.moveToFirst()
            do {
                if (c.count > 0) { //fixes crash if phoneNo not present
                    val phoneNo = c.getString(0)
                    result = "$result$phoneNo "
                }
            } while (c.moveToNext())
        }
        phone.text = result
        phone.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK) {
            val contactUri = data?.data
            val queryFields = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
            )
            val c = contactUri?.let {
                context?.contentResolver?.query(
                    it, queryFields, null, null, null
                )
            }
            c?.use { c ->
                    if (c.count > 0) {
                        c.moveToFirst()
                        contactId = c.getInt(0) // ID first
                        val contactName = c.getString(1) // Name second
                        name.visibility = View.VISIBLE
                        val display = "ID: $contactId \n$contactName"
                        name.text = display
                        more.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_READ_CONTACT_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,"Contact Reading Permission Granted",Toast.LENGTH_SHORT
                ).show()
                showEmail()
                showPhone()
            }
        }
    }


}