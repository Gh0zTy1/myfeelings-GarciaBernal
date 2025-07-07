package garcia.carlosdamian.myfeeligns_garciacarlosdamian

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import garcia.carlosdamian.myfeeligns_garciacarlosdamian.utilities.CustomBarDrawable
import garcia.carlosdamian.myfeeligns_garciacarlosdamian.utilities.CustomCircleDrawable
import garcia.carlosdamian.myfeeligns_garciacarlosdamian.Emociones
import garcia.carlosdamian.myfeeligns_garciacarlosdamian.utilities.JSONFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verysad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val guardarButton: Button = findViewById(R.id.guardarButton)
        val veryHappyButton: ImageButton = findViewById(R.id.veryHappyButton)
        val happyButton: ImageButton = findViewById(R.id.happyButton)
        val neutralButton: ImageButton = findViewById(R.id.neutralButton)
        val sadButton: ImageButton = findViewById(R.id.sadButton)
        val verySadButton: ImageButton = findViewById(R.id.verySadButton)

        jsonFile = JSONFile()
        fetchingData()

        if (!data) {
            val emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            val graph: ConstraintLayout = findViewById(R.id.graph)
            graph.background = fondo

            val graphVeryHappy: View = findViewById(R.id.graphVeryHappy)
            val graphHappy: View = findViewById(R.id.graphHappy)
            val graphNeutral: View = findViewById(R.id.graphNeutral)
            val graphSad: View = findViewById(R.id.graphSad)
            val graphVerySad: View = findViewById(R.id.graphVerySad)

            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0F, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0F, R.color.deepBlue, verysad))
        } else {
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener {
            guardar()
        }

        veryHappyButton.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }
        happyButton.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }
        neutralButton.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }
        sadButton.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }
        verySadButton.setOnClickListener {
            verysad++
            iconoMayoria()
            actualizarGrafica()
        }
    }


    fun fetchingData() {
        try {
            val json: String = jsonFile?.getData(this) ?: ""
            if (json.isNotBlank()) {
                this.data = true
                val jsonArray = JSONArray(json)
                this.lista = parseJson(jsonArray)

                for (i in lista) {
                    when (i.nombre) {
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verysad = i.total
                    }
                }
            } else {
                this.data = false
            }
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones> {
        val lista = ArrayList<Emociones>()
        for (i in 0 until jsonArray.length()) {
            try {
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                val emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun actualizarGrafica() {
        val total = veryHappy + happy + neutral + sad + verysad
        if (total == 0.0F) {
            return
        }

        val pVH: Float = (veryHappy * 100 / total)
        val pH: Float = (happy * 100 / total)
        val pN: Float = (neutral * 100 / total)
        val pS: Float = (sad * 100 / total)
        val pVS: Float = (verysad * 100 / total)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue, verysad))

        val fondo = CustomCircleDrawable(this, lista)
        val graph: ConstraintLayout = findViewById(R.id.graph)
        graph.background = fondo

        val graphVeryHappy: View = findViewById(R.id.graphVeryHappy)
        val graphHappy: View = findViewById(R.id.graphHappy)
        val graphNeutral: View = findViewById(R.id.graphNeutral)
        val graphSad: View = findViewById(R.id.graphSad)
        val graphVerySad: View = findViewById(R.id.graphVerySad)

        graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", pVS, R.color.deepBlue, verysad))
    }

    fun iconoMayoria() {
        val icon: ImageView = findViewById(R.id.icon)
        val maxFeeling = maxOf(veryHappy, happy, neutral, sad, verysad)

        if (maxFeeling > 0) {
            when (maxFeeling) {
                veryHappy -> icon.setImageDrawable(getDrawable(R.drawable.ic_sentiment_very_happy_40dp))
                happy -> icon.setImageDrawable(getDrawable(R.drawable.ic_sentiment_happy_40dp))
                neutral -> icon.setImageDrawable(getDrawable(R.drawable.ic_sentiment_neutral_40dp))
                sad -> icon.setImageDrawable(getDrawable(R.drawable.ic_sentiment_sad_40dp))
                verysad -> icon.setImageDrawable(getDrawable(R.drawable.ic_sentiment_very_dissatisfied_black_40dp))
            }
        }
    }

    fun guardar() {
        val jsonArray = JSONArray()
        for (i in lista) {
            val j = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje.toDouble())
            j.put("color", i.color)
            j.put("total", i.total.toDouble())
            jsonArray.put(j)
        }

        jsonFile?.saveData(this, jsonArray.toString())
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }
}