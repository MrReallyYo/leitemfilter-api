import com.github.mrreallyyo.api.definitions.ItemFilter
import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull

class SerializationTests {

    @Test
    fun read() {


        val filterFiles = listOf("ALL AFFIXES.xml","player1.xml", "player2.xml", "merged.xml", "levelrule.xml", "lich_solo.xml")

        val testDir = File("testfiles").apply { mkdirs() }
        filterFiles.forEach { filterFile ->
            val filter = SerializationTests::class.java.classLoader.getResourceAsStream(filterFile)?.use {
                ItemFilter.load(it)
            }
            assertNotNull(filter, filterFile)



            File(testDir, filterFile).outputStream().use {
                filter.write(it)
            }

        }

    }


}