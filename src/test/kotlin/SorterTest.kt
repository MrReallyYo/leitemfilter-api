import com.github.mrreallyyo.api.RuleComparator
import com.github.mrreallyyo.api.definitions.ItemFilter
import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull

class SorterTest {

    @Test
    fun read() {

        val test = "merged.xml"
        val filter = SorterTest::class.java.classLoader.getResourceAsStream(test)?.use {
            ItemFilter.load(it)
        }
        assertNotNull(filter)

        filter.rules.rule = filter.rules.rule.sortedWith(RuleComparator())

        val testDir = File("testfiles").apply { mkdirs() }



        File(testDir, "sorted_$test").outputStream().use {
            filter.write(it)
        }


    }


}