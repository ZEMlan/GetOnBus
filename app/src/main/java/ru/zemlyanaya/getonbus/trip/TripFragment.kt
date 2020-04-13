package ru.zemlyanaya.getonbus.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_trip.*
import kotlinx.android.synthetic.main.fragment_trip.view.*
import ru.zemlyanaya.getonbus.IOnBackPressed
import ru.zemlyanaya.getonbus.R


private const val ARG_A = "a"
private const val ARG_B = "b"


class TripFragment : Fragment(), IOnBackPressed {
    private var a: String? = null
    private var b: String? = null

    private lateinit var viewModel: TripViewModel

    override fun onBackPressed(): Boolean {
        return butBack.isPressed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            a = it.getString(ARG_A)
            b = it.getString(ARG_B)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_trip, container, false)
        val butBack = layout.butBack
        butBack.setOnClickListener { activity?.onBackPressed() }
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TripViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param a point from.
         * @param b point to.
         * @return A new instance of fragment RootingFragment.
         */
        @JvmStatic
        fun newInstance(a: String, b: String) =
            TripFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_A, a)
                    putString(ARG_B, b)
                }
            }
    }

}
