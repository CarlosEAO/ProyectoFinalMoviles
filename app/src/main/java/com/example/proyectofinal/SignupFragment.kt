package com.example.proyectofinal


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.fragment_signup.*
import android.widget.Spinner



/**
 * A simple [Fragment] subclass.
 */
class SignupFragment : Fragment() {

    private var navController: NavController? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var birthDateEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val birthDateEditText : EditText = view.findViewById(R.id.birthDateEditText)

        birthDateEditText.setOnClickListener{
            //TODO EL CODIGO PARA UNA FECHA ES LO QUE ESTA EN ESTE LISTENER

            val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
                var selectedDate = day.toString() + " / " + (month + 1).toString() + " / " + year.toString()
                birthDateEditText.setText(selectedDate)
            })

            newFragment.show(activity!!.supportFragmentManager, "datePicker")
        }

        val signupButton: Button = view.findViewById<Button>(R.id.signupButton)
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings


        navController = Navigation.findNavController(view)


        //Guardar registro en la base de datos
        //Importante!!!
        signupButton.setOnClickListener{

            var name: String = view.findViewById<EditText>(R.id.nameSignupEditText).text.toString()
            var apellidoP: String = view.findViewById<EditText>(R.id.apellidoPaterno).text.toString()
            var apellidoM: String = view.findViewById<EditText>(R.id.apellidoMaterno).text.toString()
            val spinner = view.findViewById(R.id.spinner) as Spinner
            val blood = spinner.selectedItem.toString()
            Toast.makeText(activity,blood,Toast.LENGTH_LONG).show()
            var height: Number = view.findViewById<EditText>(R.id.estatura).text.toString().toDouble()
            var size: Number = view.findViewById<EditText>(R.id.talla).text.toString().toDouble()
            var weight: Number = view.findViewById<EditText>(R.id.weightSignupEditText).text.toString().toDouble()
            var birthday: String = view.findViewById<EditText>(R.id.birthDateEditText).text.toString()
            var email: String = view.findViewById<EditText>(R.id.emailSignupEditText).text.toString()
            var password: String = view.findViewById<EditText>(R.id.passwordSignupEditText).text.toString()

            val newUser = hashMapOf(
                "name" to name,
                "apellidoP" to apellidoP,
                "apellidoM" to apellidoM,
                "blood" to blood,
                "height" to height,
                "size" to size,
                "weight" to weight,
                "birthday" to birthday,
                "email" to email

            )


            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(activity,"User created", Toast.LENGTH_SHORT).show()

                        val users = db.collection("users")
                        users.document(email).set(newUser)
                            .addOnSuccessListener {
                                Toast.makeText(activity,"User inserted to db", Toast.LENGTH_SHORT).show()
                                navController!!.navigate(R.id.action_signupFragment_to_mainFragment)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(activity,"User not inserted to db", Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity,"USER NOT CREATED", Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

}
