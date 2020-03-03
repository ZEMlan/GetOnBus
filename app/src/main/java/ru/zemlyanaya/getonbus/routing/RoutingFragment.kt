package ru.zemlyanaya.getonbus.routing

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
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
class RoutingFragment : Fragment(){
    private var a: String? = null
    private var b: String? = null

    private lateinit var viewModel: RoutingViewModel
    private lateinit var adapter: FavRoutesRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    private var favRoutes: ArrayList<FavRoute>? = arrayListOf()

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            a = it.getString(ARG_A)
            b = it.getString(ARG_B)
        }
        viewModel = ViewModelProviders.of(this).get<RoutingViewModel>(RoutingViewModel::class.java)
        viewModel.favRoutes.observe(this, Observer { routes ->
            favRoutes?.let {dataChanged(routes)}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_rooting, container, false)

        adapter = FavRoutesRecyclerViewAdapter()
        recyclerView = layout.favRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        adapter.setData(favRoutes as ArrayList<FavRoute>)

        val itemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 400
        itemAnimator.removeDuration = 400
        itemAnimator.moveDuration = 400

        recyclerView.itemAnimator = itemAnimator
        enableSwipeToEditAndUndo()

        val fab: FloatingActionButton = layout.findViewById(R.id.favFab)
        fab.setOnClickListener { createAddDialog().show() }
        return layout
    }

    private fun showComputing(){
        //TODO("show progress dialog")
    }

    private fun hideComputing(){
        //TODO("hide progress dialog")
    }

    private fun dataChanged(new: List<FavRoute>?){
        recyclerView.adapter = this.adapter
        adapter.setData(new.orEmpty() as ArrayList<FavRoute>)
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

    private fun enableSwipeToEditAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(recyclerView.context) {
            override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                val item: FavRoute = adapter.getData()[position]
                adapter.removeItem(position)
                view?.let { createEditDialog(item, position).show() }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun createAddDialog(): AlertDialog {
        val builder = AlertDialog.Builder(context, R.style.AppTheme_DialogStyle)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.add_fav_route_dialog, null)

        val inputName: TextInputEditText = view.findViewById(R.id.textName)
        val inputTo: TextInputEditText = view.findViewById(R.id.textTo)
        val inputIcon: ImageButton = view.findViewById(R.id.dialogIcon)

        builder.setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                try {
                    val name = inputName.text.toString()
                    val to = inputTo.text.toString()
                    val icon = Icons.Heart

                    if(name == "" || to == "")
                        throw Exception("Заполните все поля!")

                    val route = FavRoute(name, to, icon.name)
                    viewModel.insert(route)
                } catch (e: Exception) {
                    showError(e.message.orEmpty())
                }

            }
            .setNegativeButton("Отмена") {_, _ -> }
        return builder.create()
    }

    private fun createEditDialog(route: FavRoute, position: Int): AlertDialog{
        val builder = AlertDialog.Builder(context, R.style.AppTheme_DialogStyle)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.edit_fav_route_dialog, null)

        val inputName: TextInputEditText = view.findViewById(R.id.textName)
        inputName.setText(route.name)
        val inputTo: TextInputEditText = view.findViewById(R.id.textTo)
        inputTo.setText(route.destination)
        val inputIcon: ImageButton = view.findViewById(R.id.dialogIcon)

        builder.setView(view)
            .setPositiveButton("Изменить"
            ) { _, _ ->
                try {
                    val name = inputName.text.toString()
                    val to = inputTo.text.toString()
                    val icon = Icons.Heart

                    if(name == "" || to == "")
                        throw Exception("Заполните все поля!")

                    val newRoute = FavRoute(name, to, icon.name)
                    viewModel.change(route, newRoute)
                } catch (e: Exception) {
                    showError(e.message.orEmpty())
                    viewModel.insert(route)
                    adapter.restoreItem(route, position)
                    recyclerView.scrollToPosition(position)
                }
            }
            .setNeutralButton(
                "Отмена"
            ) { _, _ ->
                viewModel.insert(route)
                adapter.restoreItem(route, position)
                recyclerView.scrollToPosition(position)
            }
            .setNegativeButton(
                "Удалить"
            ) {_, _ ->
                viewModel.delete(route)
            }
        return builder.create()
    }

    private fun showError(e: String){
        Snackbar.make(recyclerView, e, Snackbar.LENGTH_SHORT)
            .show()
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
