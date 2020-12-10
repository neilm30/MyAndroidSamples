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
import com.country.information.uiscreens.adapater.CustomAdapter
import kotlinx.android.synthetic.main.activity_recyclerview.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * fragment class to display recyclerview .
 */
class CountryListFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var listAdapter: CustomAdapter

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
        initViews()
    }

    // initialize views using kotlin extensions
    private fun initViews() {
        //getting recyclerview from xml
        recyclerView_countrydetails.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(getItemDecoration())

            // initalize and set adapter
            initAdapter()

        }

        //getting swipe to refresh from xml
        swipeRefreshLayout?.apply {
            setOnRefreshListener {
                // code to refresh the list here.calling swipeContainer.setRefreshing(false) once the network request has completed successfully. clear items before appending in the new ones
                checkIfNetworkConnectionAvailable()
            }
        }

        subscribeToCountryListEvent()
    }

    // initalize and set adapater.The list will be empty for the first time
    private fun initAdapter() {
        CustomAdapter().apply {
            listAdapter = this
            recyclerView_countrydetails.adapter = listAdapter
            updateCountryListItems()
        }
    }


    // add divider between rows
    private fun getItemDecoration(): DividerItemDecoration {
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        context?.let { context ->
            ContextCompat.getDrawable(context, R.drawable.custom_divider)?.let {
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
                textView_error.visibility = View.GONE
                recyclerView_countrydetails.visibility = View.VISIBLE
            }
            else -> {
                textView_error.apply {
                    visibility = View.VISIBLE
                    recyclerView_countrydetails.visibility = View.INVISIBLE
                    text = getString(R.string.no_iternet_connection)
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    // subscribe and listen for response from viewmodel
    private fun subscribeToCountryListEvent() {

        //observe for success response and update listview
        mainViewModel.responseData.observe(viewLifecycleOwner, Observer {
            //set action bar title
            (activity as AppCompatActivity?)?.supportActionBar?.title = it.second

            //the data is available, add new items to your adapter
            listAdapter.updateCountryListItems(it.first)

            // dismiss the refresh loading
            swipeRefreshLayout.isRefreshing = false
        })

        // observe for any error response and show text with default message
        mainViewModel.errorData.observe(viewLifecycleOwner, Observer { errorText ->
            Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
            textView_error.text = errorText
            textView_error.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
        })
    }
}