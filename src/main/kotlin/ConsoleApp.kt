import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory


@Suppress("DEPRECATION")

class ConsoleApp(path: String) {

    private var type: String = ""
    private var addressMap: MutableMap<Int, Address> = mutableMapOf()
    private val cityMap = mutableMapOf<String, City>()
    private val pathToFile: String = path
    private var copyMap: MutableMap<String, Int> = mutableMapOf()


    fun run() {
        println("Start of reading!")
        type =
            "${pathToFile[pathToFile.length - 3]}" + "${pathToFile[pathToFile.length - 2]}" + "${pathToFile[pathToFile.length - 1]}"

        if (type == "csv") { //Reading csv file
            println("You choose .csv file")
            val reader = Files.newBufferedReader(Paths.get(pathToFile))

            val csvParser = CSVParser(
                reader, CSVFormat.newFormat(';')
                    .withHeader("city", "street", "house", "floor")
                    .withIgnoreHeaderCase()
                    .withTrim()
            )
            for ((i, csvRecord) in csvParser.withIndex()) {

                if (i != 0) {
                    val temp = Address(
                        csvRecord.get("city"),
                        csvRecord.get("street"),
                        csvRecord.get("house"),
                        csvRecord.get("floor")
                    )
                    addressMap[i] = temp
                }
            }


        } else if (type == "xml") {//Reading xml file
            println("You choose .xml file")
            val builderFactory = DocumentBuilderFactory.newInstance()
            val docBuilder = builderFactory.newDocumentBuilder()
            val document = docBuilder.parse(File(pathToFile))
            val nodeList = document.getElementsByTagName("item")
            for (i in 0 until nodeList.length) {
                val tempAddress = Address(
                    nodeList.item(i).attributes.getNamedItem("city").nodeValue,
                    nodeList.item(i).attributes.getNamedItem("street").nodeValue,
                    nodeList.item(i).attributes.getNamedItem("house").nodeValue,
                    nodeList.item(i).attributes.getNamedItem("floor").nodeValue
                )
                addressMap[i + 1] = tempAddress
            }


        }
    }


    fun getStat() {

        for (index in 1..addressMap.size) {
            if (cityMap[(addressMap[index] ?: return).city] != null) {

                if ((cityMap[(addressMap[index] ?: return).city] ?: return).streets[(addressMap[index]
                        ?: return).street] != null
                ) {

                    if (((cityMap[(addressMap[index] ?: return).city] ?: return).streets[(addressMap[index]
                            ?: return).street] ?: return).homes[(addressMap[index]
                            ?: return).house] != null
                    ) {

                        if (copyMap[addressMap[index].toString()] == null) {
                            copyMap[addressMap[index].toString()] = 2
                        } else {
                            val temp = (copyMap[addressMap[index].toString()] ?: return).toInt() + 1
                            copyMap[addressMap[index].toString()] = temp
                        }

                    } else {
                        val tempHome = Home(
                            (addressMap[index] ?: return).house, (addressMap[index]
                                ?: return).floor
                        )
                        ((cityMap[(addressMap[index] ?: return).city] ?: return).streets[(addressMap[index]
                            ?: return).street] ?: return).homes[(addressMap[index]
                            ?: return).house] = tempHome
                        val temp = (addressMap[index] ?: return).floor.toInt()
                        (cityMap[(addressMap[index] ?: return).city] ?: return).counter[temp - 1]++
                    }


                } else {
                    val tempHome = Home(
                        (addressMap[index] ?: return).house, (addressMap[index]
                            ?: return).floor
                    )
                    val tempHouseMap = mutableMapOf(
                        (addressMap[index] ?: return).house to tempHome
                    )
                    Street((addressMap[index] ?: return).street, tempHouseMap)
                    (cityMap[(addressMap[index] ?: return).city] ?: return).streets[(addressMap[index]
                        ?: return).street] =
                        Street((addressMap[index] ?: return).street, tempHouseMap)
                    val temp = (addressMap[index] ?: return).floor.toInt()
                    (cityMap[(addressMap[index] ?: return).city] ?: return).counter[temp - 1]++
                }


            } else {
                val tempHome = Home(
                    (addressMap[index] ?: return).house, (addressMap[index]
                        ?: return).floor
                )
                val tempHouseMap = mutableMapOf(
                    (addressMap[index] ?: return).house to tempHome
                )
                val tempStreet = Street((addressMap[index] ?: return).street, tempHouseMap)
                val tempStreetMap = mutableMapOf(
                    (addressMap[index] ?: return).street to tempStreet
                )
                cityMap[(addressMap[index] ?: return).city] =
                    City((addressMap[index] ?: return).city, tempStreetMap, mutableListOf(0, 0, 0, 0, 0))
                val temp = (addressMap[index] ?: return).floor.toInt()
                (cityMap[(addressMap[index] ?: return).city] ?: return).counter[temp - 1]++


            }
        }
        val cityList = cityMap.values.toList()
        for (i in cityList.indices) {
            println("Town ${cityList[i].name}:")
            for (j in 0..4) {
                println("Homes ${j + 1} floors: ${cityList[i].counter[j]}")
            }
        }
        println("Copies of addresses:\n${copyMap}")

    }

}

