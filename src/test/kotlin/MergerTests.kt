import com.github.mrreallyyo.api.ItemFilterMerger
import com.github.mrreallyyo.api.MergerOptions
import com.github.mrreallyyo.api.RuleComparator
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

            it.rules.rule?.forEach { rule ->
                // replace line break and spaces from deserialization
                rule.nameOverride = rule.nameOverride?.replace(Regex("\\R\\s*"), " ")
            }
        }

        merged.rules.rule.forEachIndexed { index, rule ->
            assertEquals(mergedExpected.rules!!.rule!!.get(index), rule)
        }


        assertEquals(mergedExpected, merged)


    }

    @Test
    fun merge2() {


        val filterDir = File("C:\\Users\\chris\\AppData\\LocalLow\\Eleventh Hour Games\\Last Epoch\\Filters")

        val filters = listOf("lich_bone.xml", "necro_minion.xml", "lich_seal.xml")


        val loadedFilters = filters.map { name ->
            File(filterDir, name).inputStream().use { stream -> ItemFilter.load(stream).apply { fileName = name } }
        }


        val opts = MergerOptions(
            baseFilters = loadedFilters,
            overrideColors = false
        )
        val merger = ItemFilterMerger(options = opts)

        val merged = merger.mergeFilter()

        val rule10 = merged.rules.rule.get(46)
        val rule7 = merged.rules.rule.get(47)

        val comp = RuleComparator()
        comp.compare(rule10, rule7)


        File(filterDir, "merged.xml").outputStream().use {
            merged.write(it)
        }


    }


}