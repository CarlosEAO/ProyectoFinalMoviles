package com.example.proyectofinal


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query

/**
 * A simple [Fragment] subclass.
 */
class HistoryFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        Toast.makeText(activity,auth.currentUser!!.email.toString(), Toast.LENGTH_SHORT).show()

        val consultations = db.collection("consultations")
        consultations
            .whereEqualTo("userEmail",auth.currentUser!!.email.toString())
            .orderBy("dateSort", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                Toast.makeText(activity,documents.size().toString(), Toast.LENGTH_SHORT).show()
                for(document in documents){
                    Toast.makeText(activity,document.data.get("date").toString(), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(activity,"error", Toast.LENGTH_SHORT).show()
            }

    }
}
