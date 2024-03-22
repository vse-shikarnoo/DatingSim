package wp.ds.datingsim.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import wp.ds.datingsim.R
import wp.ds.datingsim.viewmodel.SplashScreenViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen_layout) {

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.letsCheck()

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.check.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(SplashScreenFragmentDirections.actionStartingFragmentToMainFragment())
            } else {
                findNavController().navigate(SplashScreenFragmentDirections.actionStartingFragmentToAuthFragment())
            }
        }
    }
}