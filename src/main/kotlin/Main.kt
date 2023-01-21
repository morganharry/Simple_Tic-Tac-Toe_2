fun printField (field: MutableList<MutableList<Char>>) {
    println ("\n---A-B-C---")
    for (f in field.indices) {
        println("${f+1}| ${field[f].joinToString(" ")} |${f+1}")
    }
    println ("---A-B-C---\n")
}

fun checkFirst ():Char {
    var sign = ' '
    while (sign == ' ') {
        println("Who is first - X or O?")
        val first = readln().uppercase()
        println()
        when (first) {
            "X" -> sign = 'X'
            "O" -> sign = 'O'
            else -> println("Please, enter the sign: X or O\n")
        }
    }
    return sign
}

fun changeSign (sign:Char):Char {
    if (sign == 'X') return 'O'
    else return 'X'
}

fun filterMove (coordinates:String) : MutableList<Char> {
    val listMoveDigit = mutableListOf<Char>()
    val listMoveLetter = mutableListOf<Char>()
    val listMove= mutableListOf<Char>()
    for (i in coordinates.indices) {
        when {
            coordinates[i].isDigit()-> listMoveDigit.add(coordinates[i])
            coordinates[i].isLetter()-> listMoveLetter.add(coordinates[i])
        }
    }
    when {
        listMoveDigit.size == 0 || listMoveLetter.size == 0 -> {
            println("You wrote the wrong numbers of characters. Please, try again.")
            listMove.add('E')
        }
        listMoveDigit.size > 1 ||listMoveLetter.size > 1-> {
            println("You wrote the wrong numbers of characters. Please, try again.")
            listMove.add('E')
        }
        listMoveDigit.first() !in ('1'..'3') || listMoveLetter.first().uppercaseChar() !in ('A'..'C') -> {
            println("There is no letter (A, B, C) or number (1, 2, 3). Please, try again.")
            listMove.add('E')
        }
        else -> {
            listMove.addAll(listMoveLetter)
            listMove.addAll(listMoveDigit)
            println("Your move: ${listMove.joinToString("-")}")
        }
    }
    return listMove
}

fun convertionMove (listMove : MutableList<Char>, axis: Char): Int {
    var x = 0
    var y = 0
    for (i in listMove.indices) {
        when (listMove[i].uppercaseChar()) {
            'A'-> x = 0
            'B'-> x = 1
            'C'-> x = 2
            '1'-> y = 0
            '2'-> y = 1
            '3'-> y = 2
        }
    }
    if (axis == 'x') return x
    else return y
}

fun checkFree (x: Int, y: Int, field: MutableList<MutableList<Char>>): String {
    if (field[y][x] == '_') return "good"
    else {
        println ("This cell is nit free. Try again.\n")
        return "bad"
    }

}

fun checkWin (sign:Char, field: MutableList<MutableList<Char>>): String {
    var status = "move"
    when {
        (field[0][0] == field[0][1] && field[0][1] == field[0][2] && sign == field[0][2]) ||
        (field[1][0] == field[1][1] && field[1][1] == field[1][2] && sign == field[1][2]) ||
        (field[2][0] == field[2][1] && field[2][1] == field[2][2] && sign == field[2][2]) ||
        (field[0][0] == field[1][0] && field[1][0] == field[2][0] && sign == field[2][0]) ||
        (field[0][1] == field[1][1] && field[1][1] == field[2][1] && sign == field[2][1]) ||
        (field[0][2] == field[1][2] && field[1][2] == field[2][2] && sign == field[2][2]) ||
        (field[0][0] == field[1][1] && field[1][1] == field[2][2] && sign == field[2][2]) ||
        (field[0][2] == field[1][1] && field[1][1] == field[2][0] && sign == field[2][0])-> {
            status = "end"
            println ("Win! ${sign} - the winner!\n")
        }
    }
    return status
}

fun checkDraw (field: MutableList<MutableList<Char>>): String {
var status = "move"
    if (('_' !in field[0]) && ('_' !in field[1]) && ('_' !in field[2])) {
            status = "end"
            println ("Draw. Good game!\n")
        }
    return status
}

fun endOfGame (): String {
    var status = "next"
    println ("Choose next action: start new game or exit.\nPlease, enter: start or exit:")
    while (status == "next") {
        val read = readln()
        when (read){
            "start" -> status = read
            "exit" -> status = read
            else -> println ("Please, enter: start or exit:")
        }
    }
    return status
}

fun main() {
    var statusGame = "start"
    var statusMove = "bad"
    var sign = 'X'
    var field= MutableList(3) {MutableList<Char> (3){'_'}}
    var listMove = mutableListOf<Char>()
    var x = 0
    var y = 0
    println("Welcome to the game \"Tic-tac-Toe\"")
    while (statusGame != "exit") {
        when (statusGame) {
            "start" -> {
                field = MutableList(3) {MutableList<Char> (3){'_'}}
                printField(field)
                sign = checkFirst()
                statusGame = "move"
            }
            "move" -> {
                do  {
                    statusMove = "bad"
                    println("${sign}'s move! Please, enter the coordinates in the format letter (A, B, C) and number (1, 2, 3):")
                    var coordinates = readln()
                    println ()
                    listMove = filterMove (coordinates) // (E) or (A-1)
                    if (listMove.first() != 'E') {
                        x = convertionMove (listMove, 'x')
                        y = convertionMove (listMove, 'y')
                    }
                    if (listMove.first() != 'E') statusMove = checkFree (x, y, field)
                } while (statusMove != "good")

                field [y][x] = sign
                printField(field)
                statusGame = checkWin (sign, field)
                if (statusGame != "end") statusGame = checkDraw (field)
                if (statusGame == "move") {
                    sign = changeSign(sign)
                }
            }
            "end" -> {
                statusGame = endOfGame ()
            }
        }
    }
}