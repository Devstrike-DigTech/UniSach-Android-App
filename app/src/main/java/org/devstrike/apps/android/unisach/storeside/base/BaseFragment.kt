/*
 * Copyright (c) 2022.
 * Richard Uzor
 * For Afro Connect Technologies
 * Under the Authority of Devstrike Digital Ltd.
 *
 */

package org.devstrike.apps.android.unisach.storeside.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * The Base Fragment class is written to contain all repetitive functions written to set up a fragment in all created fragments
 * It defines the viewModel, fragment binding and repository which will all be overridden/ implemented when this class is extended
 * This class is extended in all fragments created in this project
 * Created by Richard Uzor  on 11/12/2022
 */

abstract class BaseFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepo> : Fragment() {

    protected lateinit var binding: B
    private lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val factory =
            ViewModelFactory(getFragmentRepo()) //the parameter is selected as the function below in order to get the actual repository

        // Inflate the layout for this fragment
        binding = getFragmentBinding(inflater, container)
        viewModel = ViewModelProvider(this, factory)[getViewModel()]

        return binding.root
    }

    //this function will be used to interact with the Base Repo in all fragments
    abstract fun getFragmentRepo(): R

    //this function will be called when we want to utilize viewBinding in any fragment
    abstract fun getViewModel(): Class<VM>

    //this function will be called whenever we want to inflate a fragment layout in any fragment
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B


}