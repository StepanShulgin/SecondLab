@file:Suppress("DEPRECATION")

import java.io.File
import kotlin.system.measureTimeMillis

/*
C:\\Users\\stepa\\Downloads\\address.csv
C:\\Users\\stepa\\Downloads\\address.xml
C:\\Users\\stepa\\Downloads\\address1.xml Для проверки корректрости работы
 */

fun identifyTypeOfFIle(path: String): String {
    if (path.length < 4) return "Wrong file path"
    val type = "${path[path.length - 3]}" + "${path[path.length - 2]}" + "${path[path.length - 1]}"
    return if ((type == "csv" || type == "xml") && isFileExist(File(path))) type
    else {
        "Wrong file path"
    }

}

fun isFileExist(file: File): Boolean {
    return file.exists()
}

fun main() {
    println("Enter path or 'exit' to finish: ")
    var inputString = ""

    while (inputString != "exit") {
        inputString = readln()
        if (identifyTypeOfFIle(inputString) != "Wrong file path") {
            val variable = ConsoleApp(inputString)
            val elapsedTime = measureTimeMillis {
                variable.run()
            }
            println("Reading xml file took $elapsedTime ms")

            val gettingTime = measureTimeMillis {
                variable.getStat()
            }
            println("Getting stats took $gettingTime ms")

        } else if (inputString != "exit") println(identifyTypeOfFIle(inputString))
    }
}



