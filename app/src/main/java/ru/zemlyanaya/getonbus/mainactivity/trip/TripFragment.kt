package ru.zemlyanaya.getonbus.mainactivity.trip

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.glomadrian.loadingballs.BallView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_trip.*
import kotlinx.android.synthetic.main.fragment_trip.view.*
import ru.zemlyanaya.getonbus.IOnBackPressed
import ru.zemlyanaya.getonbus.R
import ru.zemlyanaya.getonbus.Status
import ru.zemlyanaya.getonbus.mainactivity.ViewModelFactory
import ru.zemlyanaya.getonbus.mainactivity.data.api.RetrofitBuilder


class TripFragment(private val a: String, private val b: String) : Fragment(), IOnBackPressed {

    private lateinit var viewModel: TripViewModel

    private lateinit var adapter: TripBusRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textNow: TextView
    private lateinit var butNextStep: MaterialButton
    private lateinit var imageWalk: ImageView
    private lateinit var loadingView: BallView

    override fun onBackPressed(): Boolean {
        return butBack.isPressed
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
                RetrofitBuilder.apiService,
                null)
        ).get(TripViewModel::class.java)

        viewModel.possibleRoutes.observe(viewLifecycleOwner, Observer { resource ->
            when(resource.status){
                Status.LOADING -> showLoading()
                Status.ERROR -> showError()
                else -> showPossibleRoutes(resource.data)
            }

        })
        viewModel.currentInstruction.observe(viewLifecycleOwner, Observer { resource ->
            when(resource.status){
                Status.LOADING -> showLoading()
                Status.ERROR -> showError()
                else -> showCurrentInstructions(resource.data!!)
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_trip, container, false)
        val butBack = layout.butBack
        butBack.setOnClickListener {
            viewModel.cancelAllRequests()
            activity?.onBackPressed()
        }

        textNow = layout.textNow
        imageWalk = layout.imageWalk
        loadingView = layout.loadingView

        butNextStep = layout.butNextStep
        butNextStep.setOnClickListener { viewModel.getPossibleRoutes() }

        recyclerView = layout.tripRecyclerView
        adapter = TripBusRecyclerAdapter {
            viewModel.getInstruction(it)
        }
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        recyclerView.adapter = adapter

        return layout
    }

    override fun onStart() {
        super.onStart()

        viewModel.getInstruction(42)
    }


    private fun showLoading(){
        imageWalk.visibility = View.GONE
        butNextStep.visibility = View.GONE
        recyclerView.visibility = View.GONE

        loadingView.visibility = View.VISIBLE

        textNow.text = "Применяем магию..."
    }

    private fun showError(){
        loadingView.visibility = View.GONE

        val builder = AlertDialog.Builder(context, R.style.AppTheme_DialogStyle)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.error_dialog, null)

        builder.setView(view)
            .setCancelable(false)
            .setNeutralButton("Попробовать снова") { _, _ ->
               viewModel.getPossibleRoutes()
            }
            .create()
            .show()

    }

    private fun showPossibleRoutes(new: List<String>?){
        loadingView.visibility = View.GONE

        textNow.text = "Сядьте на один из маршрутов:"
        recyclerView.visibility = View.VISIBLE
        recyclerView.adapter = this.adapter
        adapter.setData(new.orEmpty())
    }

    private fun showCurrentInstructions(new : String){
        loadingView.visibility = View.GONE

        imageWalk.visibility = View.VISIBLE
        butNextStep.visibility = View.VISIBLE
        textNow.text = new
    }

}
