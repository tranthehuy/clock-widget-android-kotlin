package com.mehannah.clock.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.mehannah.clock.AppSettings
import com.mehannah.clock.Options
import com.mehannah.clock.R
import com.mehannah.clock.Utils
import com.mehannah.clock.constants.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initOption(view, FONT_SIZE, R.raw.font_options, R.id.spFontSize)
        initOption(view, DATE_FORMAT, R.raw.date_formats, R.id.spDateFormat)
        initOption(view, TIME_FORMAT, R.raw.time_formats, R.id.spTimeFormat)
        initOption(view, TEXT_STYLE, R.raw.text_style, R.id.spTextStyle)
    }


    private fun initOption(view: View,  name: String, dataId: Int, componentId: Int) {
        val spinner = view.findViewById<Spinner>(componentId)

        val options =
            Utils.getJsonResource<Options>(view.context, dataId)
        val items = options.items.map { it -> it?.name ?: "" }
        if (items.isEmpty()) return;

        val spinnerArrayAdapter = Utils.createItemList(items, view.context)
        spinner.adapter = spinnerArrayAdapter

        val currentValue = AppSettings.getString(name, items[0])
        items.indexOf(currentValue)?.let{
            spinner.setSelection(it)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}