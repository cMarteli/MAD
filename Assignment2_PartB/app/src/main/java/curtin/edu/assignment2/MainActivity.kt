package curtin.edu.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch

/**
 * [MainActivity] Hold fragments and switch to allow user to select
 * number of columns
 * @author Caio Marteli (19598552)
 * Marteli, C (2021) source code (Version 1.0) [Source code]. https://github.com/cMarteli/
 * Modified and improved October 2022
 *
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