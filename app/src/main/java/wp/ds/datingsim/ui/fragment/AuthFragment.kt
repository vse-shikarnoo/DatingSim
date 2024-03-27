package wp.ds.datingsim.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import wp.ds.datingsim.R
import wp.ds.datingsim.databinding.FragmentAuthLayoutBinding
import wp.ds.datingsim.utils.toast

class AuthFragment : Fragment(R.layout.fragment_auth_layout) {

    private val binding: FragmentAuthLayoutBinding by viewBinding(FragmentAuthLayoutBinding::bind)
    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth


        if (auth.currentUser!=null){
            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToMainFragment())
        }


        binding.authButtonProcced.setOnClickListener {
            //val pattern = Regex("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{10}\$")
            //val checkPhoneRegex = pattern.containsMatchIn(binding.authEditTextPhone.text)
            val check =
                binding.authEditTextEmail.text.isNotBlank() && binding.authEditTextPassword.text.isNotBlank()
            if (check) {
                val email = binding.authEditTextEmail.text.toString()
                val password = binding.authEditTextPassword.text.toString()
                createUserWithEmailAndPassword(email,password)
                toast("Well done")
            } else {
                //binding.authEditTextPhone.setBackgroundColor(resources.getColor(R.color.red))
                toast("Cringe")
            }
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToMainFragment())
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    toast("Authentication failed.")
                    //updateUI(null)
                }
            }
    }

    companion object {
        const val TAG = "FRAGMENT_AUTH"
    }

}