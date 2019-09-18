package com.example.sharelibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_temperatura.*
import kotlinx.android.synthetic.main.fragment_temperatura.view.*

class TemperatureFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_temperatura, container, false)

        view.button.setOnClickListener {
            val t = editText.text.toString().toInt()
            val result = ((t*9/5)+32).toString()
            editText.setText(result)
        }

        return view
    }

}
