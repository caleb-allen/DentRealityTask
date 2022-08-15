package xyz.wim.dentrealitytask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.wim.dentrealitytask.ui.main.MainFragment
import xyz.wim.dentrealitytask.ui.map.MapFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapFragment.newInstance())
                .commitNow()
        }
    }
}