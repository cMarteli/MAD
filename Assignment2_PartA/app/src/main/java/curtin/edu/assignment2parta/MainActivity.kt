package curtin.edu.assignment2parta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    lateinit var itemFragment : Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val moreFragment : Fragment = MoreFragment()
        itemFragment = ItemFragment.newInstance()

        val isFragmentContainerEmpty = savedInstanceState == null
        if(isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.item_fragment_container, itemFragment)
                .commit()
        }
        if(isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.more_fragmentContainerView, moreFragment, "moreFrag")
                .commit()
        }
    }

    //launches fragment on top half of screen
    fun startPostsFrag(id : Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.item_fragment_container, PostsFragment.newInstance(id), "postFrag")
            .commit()
    }
}