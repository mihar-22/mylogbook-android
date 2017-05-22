package com.mylb.mylogbook.presentation.test.espresso

import android.support.test.espresso.Root
import android.view.WindowManager
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class ViewMatchers {
    companion object {

        @JvmStatic fun isToast(): Matcher<Root> {
            return object : TypeSafeMatcher<Root>() {

                override fun matchesSafely(item: Root): Boolean {
                    val type = item.windowLayoutParams.get().type

                    if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                        val windowToken = item.decorView.windowToken
                        val appWindowToken = item.decorView.applicationWindowToken

                        return windowToken == appWindowToken
                    }

                    return false
                }

                override fun describeTo(description: Description) {
                    description.appendText("is toast")
                }
            }
        }
    }
}
