package com.country.information.uiscreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.country.information.R
import com.country.information.extensions.isConnectedToInternet
import com.country.information.networking.model.response.Rows
import com.example.myapplication.adapater.CustomAdapter
import kotlinx.android.synthetic.main.activity_recyclerview.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


/**
 * fragment class to display recyclerview .
 */
class CountryListFragment : Fragment() {

    private val mainViewModel: MainViewModel by sharedViewModel()
    private lateinit var listadapter: CustomAdapter
    private var rowsList = mutableListOf<Rows>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retain this fragment when activity is re-initialized
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    // initialize views using kotlin extensions
    private fun initViews(view: View) {
        //getting recyclerview from xml
        recyclerView_countrydetails.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(getItemDecoration())
            listadapter = CustomAdapter(rowsList)
            adapter = listadapter
        }

        //getting swipe to refresh from xml
        swipeRefreshLayout.apply {
            setOnRefreshListener {
                checkIfNetworkConnectionAvailable()
            }
        }

        subscribeToCountryListEvent()
    }

    // add divider between rows
    private fun getItemDecoration(): DividerItemDecoration {
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        context?.let {
            ContextCompat.getDrawable(it, R.drawable.custom_divider)?.let {
                itemDecoration.setDrawable(
                    it
                )
            }
        }
        return itemDecoration
    }

    // check if network available.If no internet show text informing user to try again
    private fun checkIfNetworkConnectionAvailable() {
        when (activity?.baseContext?.isConnectedToInternet()) {
            true -> {
                mainViewModel.createNetworkJob()
                textView.visibility = View.GONE
                recyclerView_countrydetails.visibility = View.VISIBLE
            }
            else -> {
                textView.apply {
                    visibility = View.VISIBLE
                    recyclerView_countrydetails.visibility = View.GONE
                    text = getString(R.string.no_iternet_connection)
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    // subscribe and listen for response from viewmodel
    private fun subscribeToCountryListEvent() {

        //onbserve for success response and update listview
        mainViewModel.responseData.observe(viewLifecycleOwner, Observer {
            //set action bar title
            (activity as AppCompatActivity?)?.supportActionBar?.title = it.second

          //  rowsList.clear()
            it.first.forEach {
                rowsList.add(it)
            }
            //progressbar.visibility = View.GONE
            listadapter.notifyDataSetChanged()

            // dismiss the refresh loading
            swipeRefreshLayout.isRefreshing = false
        })

        // observe for any error response and show text with default message
        mainViewModel.errorData.observe(viewLifecycleOwner, Observer { errorText ->
            Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
            textView.text = errorText
            textView.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
        })
    }
}