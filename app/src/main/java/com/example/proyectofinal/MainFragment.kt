package com.example.proyectofinal


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

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
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main, container, false)
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
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        navController = Navigation.findNavController(view)

        var currentUser = auth.currentUser

        val currentUserInfo = db.collection("users").document(currentUser!!.email.toString())
        currentUserInfo.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    view.findViewById<TextView>(R.id.nameMainTextView).setText(document.data?.get("name").toString())
                    view.findViewById<TextView>(R.id.actualWeightMainTextView).setText(document.data?.get("actualWeight").toString())
                    view.findViewById<TextView>(R.id.actualSizeMainTextView).setText(document.data?.get("actualSize").toString())
                } else {
                }
            }
            .addOnFailureListener { exception ->
            }

        val consultations = db.collection("consultations")
        consultations
            .whereEqualTo("userEmail",auth.currentUser!!.email.toString())
            .orderBy("dateSort", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                Toast.makeText(activity,documents.size().toString(), Toast.LENGTH_SHORT).show()
                for(documentt in documents){
                    view.findViewById<TextView>(R.id.actualWeightMainTextView).setText(documentt.data?.get("weight").toString())
                    view.findViewById<TextView>(R.id.actualSizeMainTextView).setText(documentt.data?.get("size").toString())
                    Toast.makeText(activity,documentt.data.get("date").toString(), Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(activity,"error", Toast.LENGTH_SHORT).show()
            }

        view.findViewById<Button>(R.id.toHistoryButton).setOnClickListener{
            navController!!.navigate(R.id.action_mainFragment_to_historyFragment)
        }
        view.findViewById<Button>(R.id.toStatsButton).setOnClickListener{
            navController!!.navigate(R.id.action_mainFragment_to_statsFragment)
        }
        view.findViewById<Button>(R.id.toNewConsultationButton).setOnClickListener{
            navController!!.navigate(R.id.action_mainFragment_to_newConsultationFragment)
        }
    }

}
