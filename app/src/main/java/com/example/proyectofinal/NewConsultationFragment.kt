package com.example.proyectofinal


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 * A simple [Fragment] subclass.
 */
class NewConsultationFragment : Fragment() {
    private var navController: NavController? = null
    private lateinit var auth: FirebaseAuth

    private var dayy: Number = 0
    private  var monthh: Number = 0
    private  var yearr: Number = 0
    private  var dateSort: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_new_consultation, container, false)
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
        val currentUser = auth.currentUser


        val consultationDateEditText : EditText = view.findViewById(R.id.consultationDateEditText)

        consultationDateEditText.setOnClickListener{
            //TODO EL CODIGO PARA UNA FECHA ES LO QUE ESTA EN ESTE LISTENER

            val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
                var selectedDate = day.toString() + "/" + (month + 1).toString() + "/" + year.toString()
                consultationDateEditText.setText(selectedDate)
                dayy = day
                monthh = month +1
                yearr = year
            })

            Toast.makeText(activity,dateSort, Toast.LENGTH_SHORT).show()

            newFragment.show(activity!!.supportFragmentManager, "datePicker")
        }

        val sendConsultationButton: Button = view.findViewById<Button>(R.id.sendConsultationButton)
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings





        //Guardar registro en la base de datos
        //Importante!!!
        sendConsultationButton.setOnClickListener{

            var nameNutriologo: String = view.findViewById<EditText>(R.id.nutriologoNameEditText).text.toString()
            var size: Number = view.findViewById<EditText>(R.id.consultationSizeEditText).text.toString().toDouble()
            var weight: Number = view.findViewById<EditText>(R.id.consultationWeightEditText).text.toString().toDouble()
            var observations: String = view.findViewById<EditText>(R.id.consultationObservationsEditText).text.toString()
            dateSort = yearr.toString()
            if(monthh.toInt() < 10){
                dateSort += "0"
            }
            dateSort+=monthh.toString()
            if(dayy.toInt() < 10){
                dateSort += "0"
            }
            dateSort+=dayy.toString()
            var date: String = consultationDateEditText.text.toString()
            val newUser = hashMapOf(
                "nameNutriologo" to nameNutriologo,
                "size" to size,
                "weight" to weight,
                "observations" to observations,
                "day" to dayy,
                "month" to monthh,
                "year" to yearr,
                "date" to date,
                "email" to currentUser!!.email.toString(),

                "dateSort" to dateSort
            )

            val consultations = db.collection("consultations")
            consultations.document(currentUser!!.email.toString()+dayy.toString()+monthh.toString()+yearr.toString()).set(newUser)
                .addOnSuccessListener {
                    Toast.makeText(activity,"Se registro la consulta", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(activity,"Valió madre", Toast.LENGTH_SHORT).show()
                }

        }

    }

}
