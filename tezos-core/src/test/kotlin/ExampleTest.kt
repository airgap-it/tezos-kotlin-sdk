import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExampleTest {
    
    @Test
    fun addTest() {
        val example = Example()
        assertEquals(2, example.add(1, 1))
    }
}