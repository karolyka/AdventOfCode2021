import java.io.File

abstract class DayZero(inputFileName: String) {

    protected val inputLines = File(inputFileName).readLines()

    init {
        println(this.javaClass.simpleName)
        @Suppress("LeakingThis")
        preprocess()
        @Suppress("LeakingThis")
        task1()
        @Suppress("LeakingThis")
        task2()
    }

    protected open fun preprocess() {}

    protected abstract fun task1(): Long

    protected abstract fun task2(): Long
}