package com.example.proyectofinal


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private var navController: NavController? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view : View = inflater.inflate(R.layout.fragment_login, container, false)

        setHasOptionsMenu(true)
        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        //CHECAR QUE NO HAYA NINGUNA SESION INICIADA
        val currentUser = auth.currentUser
        if (currentUser != null){
            navController!!.navigate(R.id.action_loginFragment_to_mainFragment)
        }


        val loginButton: Button = view.findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener{

            var email: String = view.findViewById<EditText>(R.id.emailLoginEditText).text.toString()
            var password: String = view.findViewById<EditText>(R.id.passwordLoginEditText).text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        Toast.makeText(activity,"User logged in", Toast.LENGTH_SHORT).show()
                        navController!!.navigate(R.id.action_loginFragment_to_mainFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity,"USER NOT logged in", Toast.LENGTH_SHORT).show()
                    }

                }

        }

        view.findViewById<Button>(R.id.toSignupButton).setOnClickListener{
            Toast.makeText(activity,"CLIC", Toast.LENGTH_SHORT).show()
            navController!!.navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }
}
