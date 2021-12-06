package examples.elye

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerMoveFilter
import k5
import math.Vector2D
import math.noise3D
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.abs

fun danceYarnAuto() = k5 {
    val noiseValuesGenerator = NoiseParamsGenerator()
    val mouseVector = Vector2D()
    show(modifier = Modifier
        .pointerMoveFilter(onMove = {
            mouseVector.x = it.x
            mouseVector.y = it.y
            false })) {
        it.apply {
            noiseValuesGenerator.change()
            val (m2d, m3d) = noiseValuesGenerator
            var offset = 0.0
            for (i in 0 until max(0f, mouseVector.x - 2).toInt()) {
                fun noiseX(variant: Double) = 2 * dimensFloat.width * noise3D(variant, m2d, m3d)
                fun noiseY(variant: Double) = 2 * dimensFloat.height * noise3D(offset + variant, m2d, m3d)
                fun color(variant: Double) =  0xFF - (0xFF * noise3D(variant, m2d, m3d)).toInt()
                val x = FloatArray(4) { index -> noiseX(offset + 5 + index * 10).toFloat() }
                val y = FloatArray(5) { index -> noiseY(offset + 5 + index * 10).toFloat() }
                val path = Path()
                path.moveTo(x[0], y[0])
                path.cubicTo(x[1], y[1], x[2], y[2], x[2], y[2])
                val red = color(offset + 35)
                val green = color(offset + 25)
                val blue = color(offset + 15)
                drawPath(path,
                    Color(red, green, blue),
                    alpha = max(min(mouseVector.y/dimensFloat.height, 1f), 0f),
                    style = Stroke(width = 0.3f))
                offset += 0.002
            }
        }
    }
}

class NoiseParamsGenerator(
    private val constantLooper: Int = 10000,
    private val constantNoiseWeight: Float = 0.002f) {

    private var incrementalValue = 0
    private var isUp = true
    private val noise2Dparam: Int
        get() = iterateZeroToLoop()
    private val noise3Dparam: Int
        get() = iterateZeroToHalfLoopAndReverse()

    private fun iterateZeroToHalfLoopAndReverse() =
        abs(abs(incrementalValue % constantLooper - constantLooper / 2) - constantLooper / 2)
    private fun iterateZeroToLoop() = incrementalValue / constantLooper

    fun change() {
        if (isUp) {
            if (isReachingMax()) {
                reverseChange()
            } else {
                incrementalValue++
            }
        } else {
            if (isReachingMin()) {
                reverseChange()
            } else {
                incrementalValue--
            }
        }
    }

    private fun reverseChange() { isUp = !isUp }
    private fun isReachingMin() = incrementalValue <= 0
    private fun isReachingMax() = incrementalValue > constantLooper * constantLooper

    operator fun component1(): Double {
        return (noise2Dparam * constantNoiseWeight).toDouble()
    }

    operator fun component2(): Double {
        return (noise3Dparam * constantNoiseWeight).toDouble()
    }
}
