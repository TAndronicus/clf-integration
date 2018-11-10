package jb.converter

import jb.config.Constants
import jb.util.BUtils

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Arrays

object BParser {

    @JvmStatic
    fun main(args: Array<String>) {
        val pathToSources = "src/main/resources/source"
        val rootCatalog = File(pathToSources)
        val permutations = BUtils.getAllPermutations()
        for (file in rootCatalog.listFiles()!!) {
            if (file.isDirectory) continue
            try {
                for (permutation in permutations) convertFile(file, permutation)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    @Throws(IOException::class)
    fun convertFile(file: File, columns: IntArray) {
        createDirIfNone()
        val lines = Files.readAllLines(Paths.get(file.path))
        val datasetInfo = DatasetInfo(lines).invoke()
        val average = datasetInfo.average
        val square = datasetInfo.square
        //int[] columns = selectFeatures(square);
        try {
            PrintWriter(File(generatePath(file, columns))).use { printWriter ->
                var counter = 0
                for (line in lines) {
                    if (line.startsWith("@") || line.startsWith("%")) {
                        continue
                    }
                    val values = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val newline = appendLabel(values[values.size - 3])
                    var loopCounter = 0
                    for (i in 0 until values.size - 3) {
                        if (i != columns[0] && i != columns[1]) {
                            continue
                        }
                        if (Constants.normalize) {
                            newline.append(" " + (loopCounter + 1) + ":" + (java.lang.Double.valueOf(values[i].trim { it <= ' ' }) - average!![i]) / square!![i])
                        } else {
                            newline.append(" " + (loopCounter + 1) + ":" + java.lang.Double.valueOf(values[i].trim { it <= ' ' }))
                        }
                        loopCounter++
                    }
                    for (i in 0..1) {
                        newline.append(" " + (i + 3) + ":" + values[values.size - 2 + i])
                    }
                    printWriter.println(newline.toString())
                    counter++
                    if (counter > 100) {
                        printWriter.flush()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun appendLabel(value: String): StringBuilder {
        val newline = StringBuilder()
        newline.append(value)
        return newline
    }

    private fun generatePath(file: File, columns: IntArray): String {
        return Constants.convertedSourcesPath + "//" + file.name.split("[.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + "_" + columns[0] + "_" +
                columns[1] + "_converted." + file.path.split("[.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    }

    private fun createDirIfNone() {
        val root = File(Constants.convertedSourcesPath)
        if (!root.exists()) root.mkdir()
    }

    private fun selectFeatures(square: DoubleArray): IntArray {
        val copyOfSquare = Arrays.copyOf(square, square.size)
        Arrays.sort(copyOfSquare)
        val columns = IntArray(2)
        for (i in square.indices) {
            for (j in columns.indices) {
                if (Arrays.binarySearch(copyOfSquare, square[i]) == square.size - 1 - j) {
                    columns[j] = i
                }
            }
        }
        return columns
    }

    private class DatasetInfo(private val lines: List<String>) {
        var average: DoubleArray? = null
            private set
        var square: DoubleArray? = null
            private set

        operator fun invoke(): DatasetInfo {
            /*var valuesCounter = 0
            average = null
            square = null
            for (line in lines) {
                if (line.startsWith("@") || line.startsWith("%")) {
                    continue
                }
                val values = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (average == null) {
                    average = DoubleArray(values.size - 3)
                    square = DoubleArray(values.size - 3)
                }
                for (i in 0 until values.size - 3) {
                    average[i] += java.lang.Double.valueOf(values[i].trim { it <= ' ' })
                    square[i] += Math.pow(java.lang.Double.valueOf(values[i].trim { it <= ' ' }), 2.0)
                }
                valuesCounter++
            }
            for (i in average!!.indices) {
                average[i] = average!![i] / valuesCounter
                square[i] = Math.sqrt(square!![i] / valuesCounter)
            }*/
            return this
        }
    }
}
