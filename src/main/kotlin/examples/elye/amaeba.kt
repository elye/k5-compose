package examples.elye

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerMoveFilter
import k5
import math.Vector2D
import math.noise3D
import kotlin.math.abs

fun amoeba() {
    val mouseVector = Vector2D()
    val constVariant = 20

    val upDownCounter = UpDownCounter(1000)

    k5 {
        show(modifier = Modifier.pointerMoveFilter(onMove = {
            mouseVector.x = it.x
            mouseVector.y = it.y
            false
        })) {
            upDownCounter.change()
            for (y in 0 until dimensInt.height step 2) {
                for (x in 0 until dimensInt.width step 2) {
                    val noiseInputX = x * 0.02
                    val noiseInputY = y * 0.02
                    val param =
                        (abs(
                            noise3D(
                            noiseInputX, noiseInputY,
                            upDownCounter.incrementalValue * 0.05) - 0.1) * constVariant)
                            .toInt()

                    val color = if (param == (mouseVector.x/dimensFloat.height * constVariant).toInt()) {
                        0xff
                    } else 0x00
                    val pointVector2D = Vector2D(x.toFloat(), y.toFloat())
                    it.drawPoints(
                        listOf(Offset(pointVector2D.x, pointVector2D.y)),
                        PointMode.Points,
                        Color(color, 0, 0),
                        2f
                    )
                }
            }
        }
    }
}
