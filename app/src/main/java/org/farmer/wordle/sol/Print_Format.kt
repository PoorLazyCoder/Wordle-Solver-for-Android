package sol


fun main( ) {


    var words = mutableListOf <String>()
    words.addAll(dicWords1)
    words.addAll(dicWords2)
    words.addAll(dicWords3)

    words.sort()


    var sb: StringBuffer = StringBuffer()
    var counter = 1

    words.forEach {
        sb.append( "\"$it\", ")
        if (counter++ % 6 == 0)
            sb.append("\n")
    }

    println(sb.toString())


    println("\n\n${words.size}")
}