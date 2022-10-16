package curtin.edu.prac7

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var back : Button
    private lateinit var next : Button
    private val contactFrag : ContactFragment = ContactFragment()
    private val photoFrag : PhotoFragment = PhotoFragment()
    private val inputFrag : InputFragment = InputFragment()
    private val thumbFrag : ThumbnailFragment = ThumbnailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        next = findViewById(R.id.next_btn)
        back = findViewById(R.id.back_btn)
        back.visibility = View.INVISIBLE //hides back button
        next.setOnClickListener{
            //changes top fragment to contact fragment and bottom to thumb
            nextButton()
        }

        back.setOnClickListener {
            backButton()
        }
    }
    private fun goToContactFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerViewInputs, contactFrag)
            .commit()
    }

    private fun goThumbFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerViewPhot, thumbFrag)
            .commit()
    }

    private fun goPhotoFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerViewPhot, photoFrag)
            .commit()
    }

    private fun goInputFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerViewInputs, inputFrag)
            .commit()
    }

    private fun nextButton(){
        goPhotoFragment()
        goToContactFragment()
        back.visibility = View.VISIBLE
        next.visibility = View.INVISIBLE

    }

    private fun backButton(){
        goThumbFragment()
        goInputFragment()
        back.visibility = View.INVISIBLE
        next.visibility = View.VISIBLE
    }
}