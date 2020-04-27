package ru.zemlyanaya.getonbus.mainactivity.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_trip.*
import kotlinx.android.synthetic.main.fragment_trip.view.*
import ru.zemlyanaya.getonbus.IOnBackPressed
import ru.zemlyanaya.getonbus.R


private const val ARG_A = "a"
private const val ARG_B = "b"


class TripFragment : Fragment(), IOnBackPressed {
    private val viewModel: TripViewModel by activityViewModels()

    private var a: String? = null
    private var b: String? = null

    private var possibleRoutes: ArrayList<String>? = arrayListOf()

    private lateinit var adapter: TripBusRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textNow : TextView

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

        textNow = layout.textNow
        textNow.setOnClickListener { viewModel.nextInstruction() }

        recyclerView = layout.tripRecyclerView
        adapter = TripBusRecyclerAdapter {
            viewModel.nextInstruction()
        }
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        recyclerView.adapter = adapter

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.possibleRoutes.observe(viewLifecycleOwner, Observer { routes ->
            possibleRoutes?.let {showPossibleRoutes(routes)}
        })
        viewModel.currentInstruction.observe(viewLifecycleOwner, Observer { instructions ->
            showCurrentInstructions(instructions)
        })
    }

    private fun showPossibleRoutes(new: List<String>?){
        textNow.text = "Сядьте на один из маршрутов:"
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter = this.adapter
        adapter.setData(new.orEmpty())
    }

    private fun showCurrentInstructions(new : String){
        recyclerView.visibility = View.GONE
        textNow.text = new
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
