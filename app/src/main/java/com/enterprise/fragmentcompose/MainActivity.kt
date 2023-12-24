package com.enterprise.fragmentcompose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.enterprise.fragmentcompose.ui.theme.FragmentComposeTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showFragmentOne()

    }

    fun showFragmentOne() {

        val fragmentOne = FragmentOne.newInstance("test1", "test2")

        setContent {
            FragmentComposeTheme {
                FragmentContainer(
                    modifier = Modifier.fillMaxSize(),
                    fragmentManager = supportFragmentManager,
                    commit = { add(it, fragmentOne) }
                )
            }
        }


    }

    fun showFragmentTwo() {

        val fragmentTwo = FragmentTwo.newInstance("test3", "test4")

        setContent {
            FragmentComposeTheme {
                FragmentContainer(
                    modifier = Modifier.fillMaxSize(),
                    fragmentManager = supportFragmentManager,
                    commit = { add(it, fragmentTwo) }
                )
            }
        }

    }

    @Preview
    @Composable
    fun PreviewFragmentContainer() {
        FragmentComposeTheme {
            FragmentContainer(
                modifier = Modifier.fillMaxSize(),
                fragmentManager = supportFragmentManager,
                commit = { add(it, FragmentOne.newInstance("test1", "test2")) }
            )
        }
    }



    @Composable
    fun FragmentContainer(
        modifier: Modifier = Modifier,
        fragmentManager: FragmentManager,
        commit: FragmentTransaction.(containerId: Int) -> Unit
    ) {
        val containerId by rememberSaveable { mutableStateOf(View.generateViewId()) }
        var initialized by rememberSaveable { mutableStateOf(false) }
        AndroidView(
            modifier = modifier,
            factory = { context ->
                FragmentContainerView(context)
                    .apply { id = containerId }
            },
            update = { view ->
                if (!initialized) {

                    fragmentManager.commit { commit(view.id) }

                    initialized = true
                } else {
                    fragmentManager.onContainerAvailable(view)
                }
            }
        )
    }

    /** Access to package-private method in FragmentManager through reflection */
    private fun FragmentManager.onContainerAvailable(view: FragmentContainerView) {
        val method = FragmentManager::class.java.getDeclaredMethod(
            "onContainerAvailable",
            FragmentContainerView::class.java
        )
        method.isAccessible = true
        method.invoke(this, view)
    }


}