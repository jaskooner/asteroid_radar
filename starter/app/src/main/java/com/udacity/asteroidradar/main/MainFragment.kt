package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    val TAG = MainFragment::class.qualifiedName

//    private val viewModel: MainViewModel by lazy {
//        ViewModelProvider(this).get(MainViewModel::class.java)
//    }

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        //The ViewModelProviders (plural) is deprecated.
        //ViewModelProviders.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
        ViewModelProvider(this, MainViewModelFactory(activity.application)).get(MainViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        binding.asteroidRecycler.adapter = PhotoLinearAdapter(
            PhotoLinearAdapter.OnClickListener{
                viewModel.displayToAsteroidDetails(it)
            }
        )

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })

        return binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
