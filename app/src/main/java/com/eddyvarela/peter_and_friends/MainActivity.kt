package com.eddyvarela.peter_and_friends

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.eddyvarela.peter_and_friends.data.Post
import com.eddyvarela.peter_and_friends.screen_fragments.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_posting_fragment.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener)

        showFragmentByTag(HomeFragment.TAG, false)

    }

    override fun onBackPressed() {
        super.onBackPressed()
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
            R.id.settings_tab -> {
                showFragmentByTag(AccountFragment.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.posting_tab -> {
                showFragmentByTag(CreatePosting.TAG, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
