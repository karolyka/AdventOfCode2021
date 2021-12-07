// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import kotlin.reflect.full.primaryConstructor

//@Composable
//@Preview
//fun app() {
//    var text by remember { mutableStateOf("Hello, World!") }
//
//    DesktopMaterialTheme {
//        (3..4).forEach {
//            val dayClass = Class.forName("Day$it").kotlin
//            println(dayClass)
//
//            Button(onClick = {
//                Day03("Day03_example.txt")
//                Day03("Day03.txt")
//            }) {
//                Text("Day 03")
//            }
//        }
//        Button(onClick = {
//            Day03("Day03_example.txt")
//            Day03("Day03.txt")
//        }) {
//            Text("Day 03")
//        }
//
////        Button(onClick = {
////            text = "Hello, Desktop!"
////        }) {
////            Text(text)
////        }
//    }
//}
//
//fun main0() = application {
//    Window(onCloseRequest = ::exitApplication) {
//        app()
//    }
//}

fun main() {
    (7 downTo 7).forEach {
        "Day%02d".format(it).also { nr ->
            val dayClass = Class.forName(nr).kotlin
            dayClass.primaryConstructor!!.call("${nr}_example.txt")
            dayClass.primaryConstructor!!.call("${nr}.txt")
        }
    }
}
