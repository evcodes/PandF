package com.eddyvarela.peter_and_friends

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.eddyvarela.peter_and_friends.screen_fragments.AccountFragment
import com.eddyvarela.peter_and_friends.screen_fragments.CreatePosting
import com.eddyvarela.peter_and_friends.screen_fragments.HomeFragment
import com.eddyvarela.peter_and_friends.screen_fragments.MailFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener)

        showFragmentByTag(HomeFragment.TAG, false)
    }

    private fun showFragmentByTag(tag: String, toBackStack: Boolean) {
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            when (tag) {
                HomeFragment.TAG -> fragment = HomeFragment()
                MailFragment.TAG -> fragment = MailFragment()
                CreatePosting.TAG -> fragment = CreatePosting()
                AccountFragment.TAG -> fragment = AccountFragment()
            }
        }

        if (fragment != null) {
            val ft = supportFragmentManager
                .beginTransaction()
            ft.replace(R.id.fragmentContainer, fragment!!, tag)
            if (toBackStack) {
                ft.addToBackStack(null)
            }
            ft.commit()
        }
    }

    private val myOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home_tab -> {
                showFragmentByTag(HomeFragment.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.mail_tab -> {
                showFragmentByTag(MailFragment.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.settings_tab-> {
                showFragmentByTag(AccountFragment.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.posting_tab-> {
                showFragmentByTag(CreatePosting.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
