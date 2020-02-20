package ru.zemlyanaya.getonbus.routing

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_rooting.view.*

import ru.zemlyanaya.getonbus.R
import ru.zemlyanaya.getonbus.database.FavRoute


private const val ARG_A = "a"
private const val ARG_B = "b"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RoutingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RoutingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoutingFragment : Fragment() {
    private var a: String? = null
    private var b: String? = null

    private var favRoutes: ArrayList<FavRoute>? = arrayListOf(
        FavRoute("На ЮнIT", "Малопрудная, 5", null),
        FavRoute("Домой", "Домашняя, 66", null)
    )

    private lateinit var adapter: favRoutesRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    private var listener: OnFragmentInteractionListener? = null

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
        val layout = inflater.inflate(R.layout.fragment_rooting, container, false)
        adapter = favRoutesRecyclerViewAdapter()
        recyclerView = layout.favRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        adapter.setData(favRoutes as ArrayList<FavRoute>)

        val itemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 400
        itemAnimator.removeDuration = 400
        itemAnimator.moveDuration = 400

        recyclerView.itemAnimator = itemAnimator
        return layout
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
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
            RoutingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_A, a)
                    putString(ARG_B, b)
                }
            }
    }
}
