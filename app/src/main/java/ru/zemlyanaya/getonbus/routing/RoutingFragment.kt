package ru.zemlyanaya.getonbus.routing

import android.app.AlertDialog
import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_rooting.*
import kotlinx.android.synthetic.main.fragment_rooting.view.*
import ru.zemlyanaya.getonbus.IOnBackPressed
import ru.zemlyanaya.getonbus.R
import ru.zemlyanaya.getonbus.database.FavRoute


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RoutingFragment.OnGoInteractionListener] interface
 * to handle interaction events.
 * [FavRoutesRecyclerViewAdapter.OnCardClickListener] interface
 * to handle OnCardClick events.
 * Use the [RoutingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoutingFragment : Fragment(), IOnBackPressed {

    private lateinit var viewModel: RoutingViewModel
    private lateinit var adapter: FavRoutesRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    private var favRoutes: ArrayList<FavRoute>? = arrayListOf()

    private var onGoListener: OnGoInteractionListener? = null

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RoutingViewModel::class.java)
        viewModel.favRoutes.observe(viewLifecycleOwner, Observer { routes ->
            favRoutes?.let {dataChanged(routes)}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_rooting, container, false)

        val butGo = layout.butGo
        butGo.setOnClickListener {
            val from = textA.text.toString()
            val to = textB.text.toString()
            onGo(from, to)
        }

        adapter = FavRoutesRecyclerViewAdapter{
            textB.setText(it.destination)
        }

        recyclerView = layout.favRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        recyclerView.adapter = adapter
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

    private fun onGo(a: String, b: String ) {
        onGoListener?.onGoInteraction(a, b)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGoInteractionListener) {
            onGoListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onGoListener = null
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

                    val route = FavRoute(DEFAULT_KEY, name, to, icon.name)
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

                    val newRoute = FavRoute(DEFAULT_KEY, name, to, icon.name)
                    viewModel.edit(route, newRoute)
                } catch (e: Exception) {
                    showError(e.message.orEmpty())
                    viewModel.insert(route)
//                    adapter.restoreItem(route, position)
//                    recyclerView.scrollToPosition(position)
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
                Snackbar
                    .make(recyclerView, "Приключение успешно забыто.", Snackbar.LENGTH_SHORT)
                    .setAction(
                    "ВЕРНУТЬ"
                )  {
                        viewModel.insert(route)
                        adapter.restoreItem(route, position)
                        recyclerView.scrollToPosition(position )
                    }
                    .setActionTextColor(resources.getColor(R.color.textAccentColor))
                    .show()
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
    interface OnGoInteractionListener {
        fun onGoInteraction(a: String, b: String)
    }


    companion object {
        fun newInstance() = RoutingFragment()
    }
}
