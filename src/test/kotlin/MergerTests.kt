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
        player1.fileName = "player1.xml"

        val player2 = SerializationTests::class.java.classLoader.getResourceAsStream("player2.xml")!!.use {
            ItemFilter.load(it)
        }
        player2.fileName = "player2.xml"


        val mergedExpected = SerializationTests::class.java.classLoader.getResourceAsStream("merged.xml")!!.use {
            ItemFilter.load(it)
        }

        val merged = ItemFilterMerger.mergeFilter(listOf(player1, player2), listOf(17, 14, 12))

/*
        File("merged.xml").outputStream().use {
            merged.write(it)
        }
*/
        listOf(mergedExpected, merged).forEach {
            // contains generated stamp and will always fail
            it.description = null

            it.rules?.rule?.forEach {rule ->
                // replace line break and spaces from deserialization
                rule.nameOverride = rule.nameOverride?.replace(Regex("\\R\\s*"), " ")
            }
        }



        assertEquals(mergedExpected, merged)


    }
}