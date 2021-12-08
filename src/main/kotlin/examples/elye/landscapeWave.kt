package examples.elye

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerMoveFilter
import k5
import math.Vector2D
import math.noise1D
import math.noise2D
import math.noise3D

fun landscapeWave() {
    val loop = 1000
    val mouseVector = Vector2D()
    k5 {
        show(
            modifier =
            Modifier.pointerMoveFilter(onMove = {
                mouseVector.x = it.x
                mouseVector.y = it.y
                false
            })
        ) {
            val slices = 250
            for (offset in 0 until ((mouseVector.y / dimensFloat.height) * slices).toInt()) {
                for (x in 0 until loop) {
                    val noiseInputX = x * 0.002
                    val noiseInputY = offset * 0.02
                    val noiseInputZ = mouseVector.x * 0.02
                    val valueY = dimensFloat.height - noise3D(noiseInputX, noiseInputY, noiseInputZ) * dimensFloat.height
                    val colorRed = (noise1D(noiseInputX) * 0xFF).toInt()
                    val colorGreen = (noise2D(0.0, noiseInputY) * 0xFF).toInt()
                    val colorBlue = (noise3D(0.0, 0.0, noiseInputZ) * 0xFF).toInt()

                    val pointVector2D = Vector2D(x.toFloat() / loop * dimensFloat.width, valueY.toFloat())
                    it.drawPoints(
                        listOf(
                            Offset(
                            pointVector2D.x,
                            pointVector2D.y)
                        ),
                        PointMode.Points,
                        Color(colorRed, colorGreen, colorBlue, 0xFF),
                        2f
                    )
                }
            }
        }
    }
}
