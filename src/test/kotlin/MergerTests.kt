import com.github.mrreallyyo.api.ItemFilterMerger
import com.github.mrreallyyo.api.MergerOptions
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
        player1.fileName = "player1.xml"

        val player2 = SerializationTests::class.java.classLoader.getResourceAsStream("player2.xml")!!.use {
            ItemFilter.load(it)
        }
        player2.fileName = "player2.xml"


        val mergedExpected = SerializationTests::class.java.classLoader.getResourceAsStream("merged.xml")!!.use {
            ItemFilter.load(it)
        }


        val opts = MergerOptions(
            baseFilters = listOf(player1, player2)
        )
        val merger = ItemFilterMerger(options = opts)

        val merged = merger.mergeFilter()

        /*val testDir = File("testfiles").apply { mkdirs() }

        File(testDir, "merged.xml").outputStream().use {
            merged.write(it)
        }*/

        listOf(mergedExpected, merged).forEach {
            // contains generated stamp and will always fail
            it.description = null

            it.rules?.rule?.forEach { rule ->
                // replace line break and spaces from deserialization
                rule.nameOverride = rule.nameOverride?.replace(Regex("\\R\\s*"), " ")
            }
        }

        merged.rules?.rule?.forEachIndexed { index, rule ->
            assertEquals(mergedExpected.rules!!.rule!!.get(index), rule)
        }


        assertEquals(mergedExpected, merged)


    }
}