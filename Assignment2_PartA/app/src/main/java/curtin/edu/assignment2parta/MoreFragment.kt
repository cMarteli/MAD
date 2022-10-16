package curtin.edu.assignment2parta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import curtin.edu.assignment2parta.data.UserItem


/**
 * A simple [Fragment] subclass.
 */
class MoreFragment : Fragment() {
    lateinit var moreInfo : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moreInfo = view.findViewById(R.id.more_textView)
    }

    fun setMoreView(selected : UserItem) {
        val txtString ="""    Name: ${selected.name}
    Phone: ${selected.phone}
    Address: ${selected.address.suite} ${selected.address.street}, ${selected.address.city} ${selected.address.zipcode}
    email: ${selected.email}
    Website: ${selected.website}
    Company: ${selected.company.name} - ${selected.company.catchPhrase}"""

        moreInfo.text = txtString
    }
}