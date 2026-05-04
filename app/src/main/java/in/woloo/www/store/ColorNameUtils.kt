package `in`.woloo.www.store

import android.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

/*
object ColorNameUtils {

    private val knownColors = mapOf(
        "White" to Color.parseColor("#FFFFFF"),
        "Black" to Color.parseColor("#000000"),
        "Red" to Color.parseColor("#FF0000"),
        "Dark Red" to Color.parseColor("#8B0000"),
        "Green" to Color.parseColor("#00FF00"),
        "Dark Green" to Color.parseColor("#006400"),
        "Blue" to Color.parseColor("#0000FF"),
        "Dark Blue" to Color.parseColor("#00008B"),
        "Sky Blue" to Color.parseColor("#87CEEB"),
        "Navy" to Color.parseColor("#000080"),
        "Cyan" to Color.parseColor("#00FFFF"),
        "Magenta" to Color.parseColor("#FF00FF"),
        "Yellow" to Color.parseColor("#FFFF00"),
        "Orange" to Color.parseColor("#FFA500"),
        "Gold" to Color.parseColor("#FFD700"),
        "Pink" to Color.parseColor("#FFC0CB"),
        "Brown" to Color.parseColor("#A52A2A"),
        "Gray" to Color.parseColor("#808080"),
        "Dark Gray" to Color.parseColor("#A9A9A9"),
        "Light Gray" to Color.parseColor("#D3D3D3"),
        "Purple" to Color.parseColor("#800080"),
        "Violet" to Color.parseColor("#EE82EE"),
        "Teal" to Color.parseColor("#008080"),
        "Olive" to Color.parseColor("#808000")
    )


    fun getClosestColorName(hex: String): String {
        val targetColor = Color.parseColor(hex)
        val targetR = Color.red(targetColor)
        val targetG = Color.green(targetColor)
        val targetB = Color.blue(targetColor)

        var closestName = "Unknown"
        var closestDistance = Double.MAX_VALUE

        for ((name, color) in knownColors) {
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)

            val distance = sqrt(
                (r - targetR).toDouble().pow(2.0) +
                        (g - targetG).toDouble().pow(2.0) +
                        (b - targetB).toDouble().pow(2.0)
            )

            if (distance < closestDistance) {
                closestDistance = distance
                closestName = name
            }
        }

        return closestName
    }
}
*/



object ColorNameUtils {

    private data class NamedColor(val name: String, val r: Int, val g: Int, val b: Int)

    private val knownColors = listOf(
        NamedColor("Black", 0, 0, 0),
        NamedColor("White", 255, 255, 255),
        NamedColor("Red", 255, 0, 0),
        NamedColor("Lime", 0, 255, 0),
        NamedColor("Blue", 0, 0, 255),
        NamedColor("Yellow", 255, 255, 0),
        NamedColor("Cyan", 0, 255, 255),
        NamedColor("Magenta", 255, 0, 255),
        NamedColor("Silver", 192, 192, 192),
        NamedColor("Gray", 128, 128, 128),
        NamedColor("Maroon", 128, 0, 0),
        NamedColor("Olive", 128, 128, 0),
        NamedColor("Green", 0, 128, 0),
        NamedColor("Purple", 128, 0, 128),
        NamedColor("Teal", 0, 128, 128),
        NamedColor("Navy", 0, 0, 128)
        // Add more if needed
    )

    fun getClosestColorName(hexColor: String): String {
        return try {
            val color = Color.parseColor(hexColor)
            val r1 = Color.red(color)
            val g1 = Color.green(color)
            val b1 = Color.blue(color)

            knownColors.minByOrNull {
                val dr = it.r - r1
                val dg = it.g - g1
                val db = it.b - b1
                dr * dr + dg * dg + db * db
            }?.name ?: "Unknown"
        } catch (e: Exception) {
            "Invalid Color"
        }
    }
}

