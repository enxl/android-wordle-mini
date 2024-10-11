package com.laine.palabrinas

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Components
    private lateinit var buttonTry:Button
    private lateinit var hint: TextView
    private lateinit var textEntry:EditText
    private lateinit var tryListText: TextView;
    private lateinit var livesIndicator:TextView
    private lateinit var restartButton:Button

    // Other fields
    private lateinit var solution: String;
    private var word: String = "-----";
    private var lives:Int = 10;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        run()
    }

    private fun run() {
        initializeComponents()
        generateWord()
        setUpListeners()
    }

    private fun setUpListeners() {
        buttonTry.setOnClickListener { pressButton() }
        restartButton.setOnClickListener { restart() }
    }

    private fun generateWord() {
        val words = listOf(
            "perro", "gatos", "raton", "tigre", "zorro", "hojas", "libro", "autos", "avion", "lento",
            "dulce", "salud", "mundo", "cielo", "lugar", "joven", "viejo", "plata", "campa", "largo",
            "banco", "silla", "mesas", "piano", "arbol", "flama", "grito", "fuego", "lanza", "nubes",
            "rosas", "campo", "costa", "coral", "tallo", "firme", "corre", "leche", "verde", "rojas",
            "blusa", "sobra", "nieve", "llama", "fruta", "playa", "rueda", "radio", "cabra", "vocal",
            "letra", "densa", "luces", "golpe", "raton", "pared", "lunes", "fresa", "plano", "hecho",
            "justo", "vieja", "cuota", "sable", "locas", "siglo", "poeta", "digna", "amigo", "sueño",
            "piano", "suelo", "puedo", "linda", "jugar", "tocar", "dieta", "donde", "razon", "mirar",
            "soñar", "poder", "carga", "llave", "funda", "chica", "arena", "humor", "curva", "tenis",
            "corte", "queso", "nieve", "falda", "campo", "punta", "luces", "perla", "casas", "vuelo"
        )
        solution = words.random()
    }

    private fun tryWord(userWord:String) {
        if(!checkTextField()) {
            textEntry.text.clear()
            return;
        }
        val userWordAsList:MutableList<Char> = word.toMutableList();
        for(i in userWord.indices) {
            if(userWord[i] == solution[i]) {
                userWordAsList[i] = solution[i]
            }
        }
        val stringBuilder = StringBuilder()
        for (letter in userWordAsList) {
            stringBuilder.append(letter)
        }
        word = stringBuilder.toString()
        if (userWord != solution) {
            lives--
        }
        textEntry.text.clear();
        updateHint(userWord)
        checkGameState();
    }

    private fun checkTextField():Boolean {
        if(textEntry.text.isBlank()) {
            Toast.makeText(this, "El texto no debe estar en blanco.",Toast.LENGTH_SHORT).show()
            return false
        }
        if(textEntry.text.length != 5) {
            Toast.makeText(this, "Palabra inválida.",Toast.LENGTH_SHORT).show()
            return false
        }
        return true;
    }

    private fun updateHint(userWord:String) {
        hint.text = word
        livesIndicator.text = lives.toString();
        tryListText.text = buildString {
            append(userWord)
            append("\n")
            append(tryListText.text.toString())
        };
    }

    private fun initializeComponents() {
        textEntry = findViewById(R.id.textEntry)
        buttonTry = findViewById(R.id.enterButton)
        hint = findViewById(R.id.word)
        livesIndicator = findViewById(R.id.livesIndicator)
        tryListText = findViewById(R.id.wordList)
        restartButton = findViewById(R.id.restartButton)
    }

    private fun pressButton() {
        val userText:String = textEntry.text.toString()
        tryWord(userText)
    }

    private fun checkGameState() {
        if(lives == 0 && word != solution) {
            hint.text = solution;
            hint.setBackgroundColor(0xFFFF0000.toInt())
            textEntry.isEnabled = false;
            return
        }
        if(word == solution) {
            hint.setBackgroundColor(0xFF008000.toInt())
            textEntry.isEnabled = false;
            return
        }
    }

    private fun isDarkMode(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun restart() {
        textEntry.isEnabled = true
        generateWord();
        lives = 10;
        word = "-----"
        hint.text = "-----"
        tryListText.text = ""
        hint.setBackgroundColor(0x00000000.toInt())
        livesIndicator.text = lives.toString()
    }

}