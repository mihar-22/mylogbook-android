package com.mylb.mylogbook.presentation.ui.fragment.util

import android.content.Intent
import com.mylb.mylogbook.presentation.ui.activity.MainActivity
import com.mylb.mylogbook.presentation.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass

abstract class SimpleFragmentCompanion(kClass: KClass<out BaseFragment>) {

    protected val javaClass = kClass.java

    fun start(parent: BaseFragment, addToBackStack: Boolean = true) {
        val activity = (parent.activity as MainActivity)

        val transaction = activity.fragmentManager
                .beginTransaction()
                .replace(activity.mainFrameLayout.id, javaClass.newInstance())

        if (addToBackStack) transaction.addToBackStack(null)

        transaction.commit()
    }

}

abstract class FragmentCompanion<out IntentOptions>(
        val intentOptions: IntentOptions,
        kClass: KClass<out BaseFragment>
) : SimpleFragmentCompanion(kClass) {

    inline fun start(parent: BaseFragment, configure: IntentOptions.(Intent) -> Unit) {
        parent.intent = Intent().apply { configure(intentOptions, this) }

        start(parent)
    }

    inline fun start(parent: BaseFragment, addToBackStack: Boolean, configure: IntentOptions.(Intent) -> Unit) {
        parent.intent = Intent().apply { configure(intentOptions, this) }

        start(parent, addToBackStack)
    }

    inline fun <T> Intent.options(block: IntentOptions.(Intent) -> T): T = block(intentOptions, this)

}