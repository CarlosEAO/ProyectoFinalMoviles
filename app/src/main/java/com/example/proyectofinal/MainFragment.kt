package com.example.proyectofinal


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var navController: NavController? = null
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        navController = Navigation.findNavController(view)

        var currentUser = auth.currentUser

        val currentUserInfo = db.collection("users").document(currentUser!!.email.toString())
        currentUserInfo.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    view.findViewById<TextView>(R.id.userEmailTextView).setText(document.data?.get("name").toString())
                } else {
                }
            }
            .addOnFailureListener { exception ->
            }


        var logoutButton : Button = view.findViewById<Button>(R.id.logoutButton)

        logoutButton.setOnClickListener{
            auth.signOut()
            navController!!.navigate(R.id.action_mainFragment_to_loginFragment)
        }


    }

}
