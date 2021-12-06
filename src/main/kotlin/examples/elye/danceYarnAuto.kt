package examples.elye

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerMoveFilter
import k5
import math.Vector2D
import math.noise3D
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.abs

fun danceYarnAuto() = k5 {
    val noiseParamsGenerator = NoiseParamsGenerator()
    val mouseVector = Vector2D()

    show(modifier = Modifier
        .pointerMoveFilter(onMove = {
            mouseVector.x = it.x
            mouseVector.y = it.y
            false })) {
        it.apply {
            noiseParamsGenerator.change()
            val (m2d, m3d) = noiseParamsGenerator
            var offset = 0.0
            for (i in 0 until getMouseXCoordinate(mouseVector)) {
                drawYarnLine(dimensFloat, offset, m2d, m3d, this, mouseVector)
                offset += 0.002
            }
        }
    }
}

private fun getMouseXCoordinate(mouseVector: Vector2D) =
    max(0f, mouseVector.x - 2).toInt()

private fun drawYarnLine(
    size: Size,
    offset: Double,
    m2d: Double,
    m3d: Double,
    drawScope: DrawScope,
    mouseVector: Vector2D
) {
    val (x, y) = generateYarnLineCoordinates(size, m2d, m3d, offset)
    val (red, green, blue) = generateYarnLineColor(m2d, m3d, offset)
    val path = createYarnLinePathFromCoordinates(x, y)
    drawScope.drawPath(
        path,
        Color(red, green, blue),
        alpha = getMouseYCoordinate(mouseVector, size),
        style = Stroke(width = 0.3f)
    )
}

private fun getMouseYCoordinate(mouseVector: Vector2D, size: Size) =
    max(min(mouseVector.y / size.height, 1f), 0f)

private fun createYarnLinePathFromCoordinates(
    x: FloatArray,
    y: FloatArray
): Path {
    val path = Path()
    path.moveTo(x[0], y[0])
    path.cubicTo(x[1], y[1], x[2], y[2], x[2], y[2])
    return path
}

private fun generateYarnLineColor(
    m2d: Double,
    m3d: Double,
    offset: Double
): Triple<Int, Int, Int> {
    fun color(variant: Double) = 0xFF - (0xFF * noise3D(variant, m2d, m3d)).toInt()
    val red = color(offset + 35)
    val green = color(offset + 25)
    val blue = color(offset + 15)
    return Triple(red, green, blue)
}

private fun generateYarnLineCoordinates(
    size: Size,
    m2d: Double,
    m3d: Double,
    offset: Double
): Pair<FloatArray, FloatArray> {
    fun noiseX(variant: Double) = 2 * size.width * noise3D(variant, m2d, m3d)
    fun noiseY(variant: Double) = 2 * size.height * noise3D(offset + variant, m2d, m3d)
    val x = FloatArray(4) { index -> noiseX(offset + 5 + index * 10).toFloat() }
    val y = FloatArray(4) { index -> noiseY(offset + 5 + index * 10).toFloat() }
    return Pair(x, y)
}

class NoiseParamsGenerator(
    private val constantLooper: Int = 10000,
    private val constantNoiseWeight: Float = 0.002f) {

    private val upDownCounter = UpDownCounter(constantLooper * constantLooper)

    private val incrementalValue: Int
        get() = upDownCounter.incrementalValue

    private val noise2Dparam: Int
        get() = iterateZeroToLoop()
    private val noise3Dparam: Int
        get() = iterateZeroToHalfLoopAndReverse()

    fun change() = upDownCounter.change()

    private fun iterateZeroToHalfLoopAndReverse() =
        abs(abs(incrementalValue % constantLooper - constantLooper / 2) - constantLooper / 2)
    private fun iterateZeroToLoop() = incrementalValue / constantLooper


    operator fun component1(): Double {
        return (noise2Dparam * constantNoiseWeight).toDouble()
    }

    operator fun component2(): Double {
        return (noise3Dparam * constantNoiseWeight).toDouble()
    }
}

class UpDownCounter(private val maxValue: Int) {
    var incrementalValue = 0
    private var isUp = true

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
    private fun isReachingMax() = incrementalValue > maxValue
}
