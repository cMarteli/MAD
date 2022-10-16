/**
 * @author C Victor Marteli 19598552
 */
package edu.curtin.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this@MainActivity, MenuActivity::class.java)

        val loginBtn = findViewById<Button>(R.id.login_btn)
        loginBtn.setOnClickListener{
            startActivity(intent)
        }
    }

}