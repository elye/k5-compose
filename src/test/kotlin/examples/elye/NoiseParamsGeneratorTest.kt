package examples.elye

import org.junit.Assert
import org.junit.Test

class NoiseParamsGeneratorTest {

    @Test
    fun `Test Noise Value Generator Loop Value`() {
        lateinit var  noiseParamsGenerator: NoiseParamsGenerator
        var resultString = ""

        fun given() {
            noiseParamsGenerator = NoiseParamsGenerator(10, 1f)
        }
        fun whenever() {
            repeat(16) {
                val (x, y) = noiseParamsGenerator
                resultString += "($x: $y) "
                noiseParamsGenerator.change()
            }
        }
        fun then() {
            val expectedResult = "(0.0: 0.0) (0.0: 1.0) (0.0: 2.0) (0.0: 3.0) (0.0: 4.0) (0.0: 5.0) (0.0: 4.0) (0.0: 3.0) (0.0: 2.0) (0.0: 1.0) (1.0: 0.0) (1.0: 1.0) (1.0: 2.0) (1.0: 3.0) (1.0: 4.0) (1.0: 5.0)"
            Assert.assertEquals(
                "Result is not incremented as expected",
                expectedResult,
                resultString.trim()
            )
        }

        given()
        whenever()
        then()
    }
}
