package wp.ds.datingsim.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import wp.ds.datingsim.R
import wp.ds.datingsim.ui.view.SwipeSurfaceView


//change name
class MainFragment:Fragment(R.layout.fragment_main_layout) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return SwipeSurfaceView(requireContext())
    }
}