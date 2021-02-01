package com.buzzdynegamingteam.pricetrend.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.SearchFragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchFragment : Fragment(), SearchResultAdapter.OnItemClickListener {
    private val TAG = "SearchFragment"

    private lateinit var viewModel: SearchViewModel
    private lateinit var resultAdapter: SearchResultAdapter
    private lateinit var searchItem: MenuItem

    private lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<SearchFragmentBinding>(inflater, R.layout.search_fragment, container, false)
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        resultAdapter = SearchResultAdapter(this)
        bind.recyclerSearchResults.adapter = resultAdapter
        bind.recyclerSearchResults.layoutManager = GridLayoutManager(this.context, 3)

        fab = bind.btnFab

        viewModel.getResultList.observe(viewLifecycleOwner, Observer { resList ->
            resultAdapter.setResultList(resList)

            bind.textTip.visibility = if (resList.isEmpty()) View.VISIBLE else View.INVISIBLE
        })


        viewModel.getQuery.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                (requireActivity() as AppCompatActivity).supportActionBar?.title = "Search Result for \"$it\""
            }
        })

        setHasOptionsMenu(true)

        fab.setOnClickListener {
            searchItem.expandActionView()
        }

        return bind.root
    }

    override fun onItemClick(position: Int) {
        val listingDocID = viewModel.getListingDocIDfromPos(position) ?: ""

        Log.e(TAG, "ListingDocID ($listingDocID) clicked")
//        Toast.makeText(requireActivity(), "ListingDocID ($listingDocID) clicked", Toast.LENGTH_SHORT).show()

        val action = SearchFragmentDirections.actionSearchFragmentToSearchDetailFragment(listingDocID)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        searchItem = menu.findItem(R.id.action_search)

        val searchView = searchItem.actionView as SearchView

        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextFocusChangeListener { _, b ->
            fab.visibility = if(b) View.GONE else View.VISIBLE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)

                val text = query?:""

                if(text == "m") {
                    Log.e(TAG, "onCreateView: querying ALL")
                    viewModel.getAllListings()
                } else {
                    Log.e(TAG, "onCreateView: querying kywd = $text")
                    viewModel.getListingsByQuery(text)
                }

                searchItem.collapseActionView()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }
}