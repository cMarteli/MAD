package curtin.edu.prac7

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class InputFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialButton = view.findViewById<Button>(R.id.dial_btn)
        val viewMapButton = view.findViewById<FloatingActionButton>(R.id.map_btn)
        val phoneTextBox = view.findViewById<EditText>(R.id.phoneNum_txt)
        val latitudeTextBox = view.findViewById<EditText>(R.id.latitude_txt)
        val longitudeTextBox = view.findViewById<EditText>(R.id.longitude_txt)

        dialButton.isEnabled = false //disables dial button
        var phone = 123456
        phoneTextBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //enables button if not empty
                if(!phoneTextBox.text.isNullOrBlank()) {
                    dialButton.isEnabled = !phoneTextBox.text.isNullOrBlank()
                    phone = Integer.parseInt(s.toString())
                }
                else{
                    dialButton.isEnabled = false //disables dial button
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
        //Dial button click
        dialButton.setOnClickListener{
            dialButtonClicked(phone)
        }
        //Map button click
        viewMapButton.setOnClickListener {
            //checks for blank
            if(latitudeTextBox.text.isNullOrBlank() || longitudeTextBox.text.isNullOrBlank()){
                Toast.makeText(this.context, "Invalid latitude or longitude!", Toast.LENGTH_SHORT).show()
            }
            else{
                val latitude = latitudeTextBox.text.toString().toDouble()
                val longitude = longitudeTextBox.text.toString().toDouble()
                viewMapButtonClicked(latitude, longitude)
            }
        }
    }

    private fun dialButtonClicked(inPhone : Int) {
        val uri = Uri.parse(String.format("tel:%d", inPhone))
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        intent.data = uri
        startActivity(intent)
    }

    private fun viewMapButtonClicked(inLatitude : Double, inLongitude : Double) {
        val uri = Uri.parse(String.format("geo:%f,%f", inLatitude, inLongitude))
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = uri
        startActivity(intent)
    }


}