package com.enterprise.fragmentcompose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragmentOne()

    }

    private fun showFragmentOne() {

        val transaction = supportFragmentManager?.beginTransaction()
        val fragmentOne = FragmentOne.newInstance("test1", "test2")
        transaction?.replace(R.id.frameLayout, fragmentOne)
        transaction?.addToBackStack(null)
        transaction?.commit()

    }


}