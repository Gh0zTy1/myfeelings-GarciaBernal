package garcia.carlosdamian.myfeeligns_garciacarlosdamian.utilities

import android.content.Context
import android.util.Log
import java.io.IOException

class JSONFile {
    private val MY_FEELINGS = "data.json"

    fun saveData(context: Context, json: String) {
        try {
            context.openFileOutput(MY_FEELINGS, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            Log.e("GUARDAR", "Error in Writing: " + e.localizedMessage)
        }
    }

    fun getData(context: Context): String {
        return try {
            context.openFileInput(MY_FEELINGS).bufferedReader().readLine()
        } catch (e: IOException) {
            Log.e("OBTENER", "Error in fetching data: " + e.localizedMessage)
            ""
        }
    }
}