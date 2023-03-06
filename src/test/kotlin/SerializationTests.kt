import com.github.mrreallyyo.api.definitions.ItemFilter
import kotlin.test.Test
import kotlin.test.assertNotNull

class SerializationTests {

    @Test
    fun read() {


        val filterFiles = listOf("player1.xml", "player2.xml", "merged.xml")


        filterFiles.forEach { filterFile ->
            val filter = SerializationTests::class.java.classLoader.getResourceAsStream(filterFile)?.use {
                ItemFilter.load(it)
            }
            assertNotNull(filter, filterFile)
        }

    }


}