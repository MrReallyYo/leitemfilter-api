import com.github.mrreallyyo.api.ItemFilterMerger
import com.github.mrreallyyo.api.definitions.ItemFilter
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class MergerTests {

    @Test
    fun merge() {


        val player1 = SerializationTests::class.java.classLoader.getResourceAsStream("player1.xml")!!.use {
            ItemFilter.load(it)
        }
        val player2 = SerializationTests::class.java.classLoader.getResourceAsStream("player2.xml")!!.use {
            ItemFilter.load(it)
        }

        val mergedExpected = SerializationTests::class.java.classLoader.getResourceAsStream("merged.xml")!!.use {
            ItemFilter.load(it)
        }

        val merged = ItemFilterMerger.mergeFilter(listOf(player1, player2), listOf(17, 14, 12))

        // contains generated stamp and will always fail
        mergedExpected.description = null
        merged.description = null



        assertEquals(mergedExpected, merged)
    }
}