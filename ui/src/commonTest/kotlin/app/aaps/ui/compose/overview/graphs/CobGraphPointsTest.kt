package app.aaps.ui.compose.overview.graphs

import app.aaps.core.interfaces.overview.graph.CobGraphData
import app.aaps.core.interfaces.overview.graph.GraphDataPoint
import kotlin.test.Test
import kotlin.test.assertEquals

class CobGraphPointsTest {

    @Test
    fun `COB points switch to the declining prediction`() {
        val data = CobGraphData(
            cob = listOf(
                GraphDataPoint(1_000L, 12.0),
                GraphDataPoint(2_000L, 10.0),
                GraphDataPoint(2_500L, 9.5)
            ),
            failOverPoints = emptyList(),
            predictions = listOf(
                GraphDataPoint(2_000L, 10.0),
                GraphDataPoint(3_000L, 8.0),
                GraphDataPoint(4_000L, 6.0)
            )
        )

        assertEquals(
            listOf(
                GraphDataPoint(1_000L, 12.0),
                GraphDataPoint(2_000L, 10.0),
                GraphDataPoint(3_000L, 8.0),
                GraphDataPoint(4_000L, 6.0)
            ),
            data.allPoints()
        )
    }
}
