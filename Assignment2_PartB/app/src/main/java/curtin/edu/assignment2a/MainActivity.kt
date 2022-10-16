package curtin.edu.assignment2a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch

/**
 * [MainActivity] Hold fragments and switch to allow user to select
 * number of columns
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val colSwitch = findViewById(R.id.switcher) as Switch
        colSwitch.showText = true

        val isFragmentContainerEmpty = savedInstanceState == null
        if(isFragmentContainerEmpty) {
            startPhotoGallery(1)
        }
        //Swaps between 2 or 3 columns
        colSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                startPhotoGallery(2)
            }
            else {
                startPhotoGallery(1)
            }
        }
    }
    private fun startPhotoGallery(colCount : Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, PhotoGalleryFragment.newInstance(colCount))
            .commit()
    }
}