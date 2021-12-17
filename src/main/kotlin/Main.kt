import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun main() {
    (25 downTo 1).forEach {
        "Day%02d".format(it).also { nr ->
            var dayClass: KClass<*>? = null
            try {
                dayClass = Class.forName(nr).kotlin
            } catch (_: ClassNotFoundException) {

            }
            dayClass?.apply {
                primaryConstructor!!.call("input/${nr}_example.txt")
                primaryConstructor!!.call("input/${nr}.txt")
                return
            }
        }
    }
}
