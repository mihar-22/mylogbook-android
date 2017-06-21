package com.mylb.mylogbook.presentation.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mylb.mylogbook.presentation.presenter.Presenter


abstract class BaseFragment : Fragment() {

    abstract val presenter: Presenter?

    override fun onPause() {
        super.onPause()
        presenter?.pause()
    }

    override fun onResume() {
        super.onResume()
        presenter?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.destroy()
    }

}