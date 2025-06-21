package com.example.angelacalculadora

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.angelacalculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var firstNumber = 0.0
    private var operation: String? = null
    private var lastButton: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setMyListener()

        // Esto de abajo no lo tenemos en cuenta
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setMyListener(){
        binding.button0.setOnClickListener(this)
        binding.button1.setOnClickListener(this)
        binding.button2.setOnClickListener(this)
        binding.button3.setOnClickListener(this)
        binding.button4.setOnClickListener(this)
        binding.button5.setOnClickListener(this)
        binding.button6.setOnClickListener(this)
        binding.button7.setOnClickListener(this)
        binding.button8.setOnClickListener(this)
        binding.button9.setOnClickListener(this)
        binding.buttonSuma.setOnClickListener(this)
        binding.buttonResta.setOnClickListener(this)
        binding.buttonMulti.setOnClickListener(this)
        binding.buttonDiv.setOnClickListener(this)
        binding.buttonComa.setOnClickListener(this)
        binding.buttonCE.setOnClickListener(this)
        binding.buttonIgual.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view){
            // si el btn1 nos vamos al método onNumberPressed y le pasamos por parámetro el texto que tiene el botón
            binding.button0 -> onNumberPressed(binding.button0.text.toString())
            binding.button1 -> onNumberPressed(binding.button1.text.toString())
            binding.button2 -> onNumberPressed(binding.button2.text.toString())
            binding.button3 -> onNumberPressed(binding.button3.text.toString())
            binding.button4 -> onNumberPressed(binding.button4.text.toString())
            binding.button5 -> onNumberPressed(binding.button5.text.toString())
            binding.button6 -> onNumberPressed(binding.button6.text.toString())
            binding.button7 -> onNumberPressed(binding.button7.text.toString())
            binding.button8 -> onNumberPressed(binding.button8.text.toString())
            binding.button9 -> onNumberPressed(binding.button9.text.toString())
            binding.buttonSuma -> onOperationPressed(binding.buttonSuma.text.toString())
            binding.buttonResta -> onOperationPressed(binding.buttonResta.text.toString())
            binding.buttonMulti -> onOperationPressed(binding.buttonMulti.text.toString())
            binding.buttonDiv -> onOperationPressed(binding.buttonDiv.text.toString())
            binding.buttonComa -> onNumberPressed(binding.buttonComa.text.toString()) // !!!!!!!!
            binding.buttonCE -> onClearPressed()
            binding.buttonIgual -> onEqualPressed()
        }

    }

    private fun onNumberPressed(number: String){

        if((binding.textView?.text.toString() == "0" && number != ",") || (this.operation != null && this.lastButton!=null)
            || binding.textView?.text.toString().contains("No se puede dividir entre 0")) {

            binding.textView.text = number

        } else {
            //ponemos textView? porque puede ser nula al iniciar la app
            binding.textView.text = "${binding.textView?.text}$number"
        }

        this.lastButton = null
    }

    private fun onOperationPressed(operation:String){

        // si ya hemos pulsado anteriormente una operacion
        if(this.operation != null){

            // segun la operacion presionada || sumamos* el num presionado con el número q estaba antes
            when(this.operation){

                // Ponemos replace() para remplazar la "," por "." ya que los doubles tienen que ser con "."
                "+" -> firstNumber += binding.textView?.text.toString().replace(',', '.').toDouble()
                "-" -> firstNumber -= binding.textView?.text.toString().replace(",", ".").toDouble()
                "*" -> firstNumber *= binding.textView?.text.toString().replace(",", ".").toDouble()
                "/" -> firstNumber /= binding.textView?.text.toString().replace(",", ".").toDouble()

            }

            // si intenta dividir entre 0
            if( binding.textView?.text.toString().replace(',', '.').toDouble() == 0.0 && this.operation == "/") {
                binding.textView.text = "No se puede dividir entre 0"

            // si no hay errores
            } else {
                binding.textView.text = firstNumber.toString()
            }

        // si se ha pulsado una operacion por primera vez
        } else {
            // establecemos la operacion
            this.operation = operation
            // almacenamos el primer número cogiendo desde el textView
            firstNumber = binding.textView?.text.toString().replace(',', '.').toDouble()
            // ponemos el texto del textView a 0
            binding.textView?.setText("0")

        }
        this.lastButton = operation
    }

    private fun onEqualPressed(){

        // firstNumber = el resultado de la operacion (last num pressed [operation] firstnumber)
        firstNumber = when (this.operation){
            "+" -> firstNumber + binding.textView?.text.toString().replace(',', '.').toDouble()
            "-" -> firstNumber - binding.textView?.text.toString().replace(',', '.').toDouble()
            "*" -> firstNumber * binding.textView?.text.toString().replace(',', '.').toDouble()
            "/" -> firstNumber / binding.textView?.text.toString().replace(',', '.').toDouble()
            else -> 0.0
        }

       // Control de errores
       binding.textView.text = if( binding.textView?.text.toString().replace(',', '.').toDouble()== 0.0 && this.operation == "/"){
           "No se puede dividir entre 0"
       } else {
           firstNumber.toString()
       }
        this.operation = null
        this.lastButton = binding.buttonIgual.text.toString()

    }

    private fun onClearPressed(){
        binding.textView?.text = "0"
        firstNumber = 0.0
        this.lastButton = null
        this.operation = null
    }

    // Para que se guarden los datos cuando cambiemos la orientacion
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putString("screenText", binding.textView?.text.toString())
        outState?.putDouble("firstNumberOnInstance", firstNumber)
        outState?.putString("lastButtonOnInstance", this.lastButton)
        outState?.putString("operationOnInstance", this.operation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.textView?.text = savedInstanceState.getString("screenText")
        firstNumber = savedInstanceState.getDouble("firstNumberOnInstance")
        this.lastButton = savedInstanceState.getString("lastButtonOnInstance")
        this.operation = savedInstanceState.getString("operationOnInstance")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "OnDestroy")
    }

}