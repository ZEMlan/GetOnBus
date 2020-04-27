package ru.zemlyanaya.getonbus.mainactivity.routing

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_routing.*
import kotlinx.android.synthetic.main.fragment_routing.view.*
import ru.zemlyanaya.getonbus.IOnBackPressed
import ru.zemlyanaya.getonbus.R
import ru.zemlyanaya.getonbus.mainactivity.database.FavRoute


class RoutingFragment : Fragment(), IOnBackPressed {

    private val viewModel: RoutingViewModel by viewModels()

    private lateinit var adapter: FavRoutesRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private var favRoutes: ArrayList<FavRoute>? = arrayListOf()

    private lateinit var stopsSearchAdapter: ArrayAdapter<String>
    private var stops: MutableList<String> = ArrayList()

    private val icons = listOf(R.drawable.ic_heart, R.drawable.ic_home, R.drawable.ic_work)

    private var onGoListener: OnGoInteractionListener? = null

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.favRoutes.observe(viewLifecycleOwner, Observer { routes ->
            favRoutes?.let {dataChanged(routes)}
        })

        viewModel.postLiveData.observe(viewLifecycleOwner, Observer { posts ->
            if (posts != null) {
                Log.d("SUCSESS", posts[0])
                this.stops = posts
                stopsSearchAdapter.clear()
                stopsSearchAdapter.addAll(posts)
            }
            else
                Log.d("FAILURE", "Nothing")
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_routing, container, false)

        val butGo = layout.butGo
        butGo.setOnClickListener {
            val from = textA.text.toString()
            val to = textB.text.toString()
            onGo(from, to)
        }

        val textInternet = layout.textInternet

        val butCheckConnection =  layout.butCheckConnection
        butCheckConnection.setOnClickListener {
            textInternet.text = "проверяем"
            val hasInternet = viewModel.hasInternetConnection()
            textInternet.text = if(hasInternet) "есть" else "нет"
        }

        val autoTextA = layout.textA
        val autoTextB = layout.textB
        stopsSearchAdapter = ArrayAdapter(layout.context, android.R.layout.select_dialog_item, stops)
        autoTextA.setAdapter(stopsSearchAdapter)
        autoTextB.setAdapter(stopsSearchAdapter)

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


    private fun dataChanged(new: List<FavRoute>?){
        recyclerView.adapter = this.adapter
        adapter.setData(new.orEmpty() as ArrayList<FavRoute>)
    }

    private fun onGo(a: String, b: String ) {
        onGoListener?.onGoInteraction(a, b)
    }

    override fun onDetach() {
        super.onDetach()
        onGoListener = null
    }

    private fun showWarning(){
        val builder = androidx.appcompat.app.AlertDialog.Builder(layoutInflater.context)
        builder.setTitle("ПРЕДУПРЕЖДЕНИЕ")
            .setIcon(R.drawable.ic_warning)
            .setMessage("Не удалось подключиться к серверу! Приложение не будет работать.\n\n" +
                    "Проверьте интернет-соединение и перезагрузите приложение.")
            .setPositiveButton("Понятно") { dialog, _ -> run { dialog.cancel() } }
            .create()
            .show()
    }

    private fun enableSwipeToEditAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(recyclerView.context) {
            override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.bindingAdapterPosition
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
        val inputTo: AutoCompleteTextView = view.findViewById(R.id.textTo)
        inputTo.setAdapter(stopsSearchAdapter)
        val inputIcon: ImageButton = view.findViewById(R.id.dialogIcon)
        var i = 0
        inputIcon.setOnClickListener {
            i = if(i == 2) 0 else ++i
            inputIcon.setImageResource(icons[i])
        }

        builder.setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                try {
                    val name = inputName.text.toString()
                    val to = inputTo.text.toString()
                    val icon = icons[i]

                    if(name == "" || to == "")
                        throw Exception("Заполните все поля!")

                    val route = FavRoute(DEFAULT_KEY, name, to, icon)
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
        val inputTo: AutoCompleteTextView = view.findViewById(R.id.textTo)
        inputTo.setAdapter(stopsSearchAdapter)
        inputTo.setText(route.destination)
        val inputIcon: ImageButton = view.findViewById(R.id.dialogIcon)
        inputIcon.setImageResource(route.icon)
        var i = 0
        inputIcon.setOnClickListener {
            i = if(i == 2) 0 else ++i
            inputIcon.setImageResource(icons[i])
        }

        builder.setView(view)
            .setPositiveButton("Изменить"
            ) { _, _ ->
                try {
                    val name = inputName.text.toString()
                    val to = inputTo.text.toString()
                    val icon = icons[i]

                    if(name == "" || to == "")
                        throw Exception("Заполните все поля!")

                    val newRoute = FavRoute(DEFAULT_KEY, name, to, icon)
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

    interface OnGoInteractionListener {
        fun onGoInteraction(a: String, b: String)
    }

}
