package com.eddyvarela.peter_and_friends.screen_fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eddyvarela.peter_and_friends.R

class AccountFragment: Fragment() {

    companion object {
        const val TAG="FragmentOne"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.account_fragment,container,false)
    }
}