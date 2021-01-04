package com.buzzdynegamingteam.pricetrend.search

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.buzzdynegamingteam.pricetrend.R
import com.buzzdynegamingteam.pricetrend.databinding.SearchFragmentBinding

class SearchFragment : Fragment(), SearchResultAdapter.OnItemClickListener {
    private val TAG = "SearchFragment"

    private lateinit var viewModel: SearchViewModel
    private lateinit var resultAdapter: SearchResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val bind = DataBindingUtil.inflate<SearchFragmentBinding>(inflater, R.layout.search_fragment, container, false)
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        resultAdapter = SearchResultAdapter(this)
        bind.recyclerSearchResults.adapter = resultAdapter
        bind.recyclerSearchResults.layoutManager = GridLayoutManager(this.context, 3)


        viewModel.getResultList.observe(viewLifecycleOwner, Observer { resList ->
            resultAdapter.setResultList(resList)

            bind.textTip.visibility = if (resList.isEmpty()) View.VISIBLE else View.INVISIBLE
        })

        bind.btnSearch.setOnClickListener{
            bind.inputSearch.clearFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)


            val query = bind.inputSearch.text.toString()
            Log.e(TAG, "onCreateView: querying kywd {$query}")
            viewModel.getListingsByTag(query)
        }

        return bind.root
    }

    override fun onItemClick(position: Int) {
        val listingDocID = viewModel.getListingDocIDfromPos(position)

        Log.e(TAG, "onItemClick: ListingDocID ($listingDocID) clicked")
        Toast.makeText(requireActivity(), "ListingDocID ($listingDocID) clicked", Toast.LENGTH_SHORT).show()
    }
}