package com.example.nn4wchallenge.database.external


class DataTranslation {

    var redAmount = 0
    var greenAmount = 0
    var blueAmount = 0

    fun stringToRGB(stringValue: String)
    {

        val chunks : ArrayList<String> = ArrayList()

        var firstLetter : Char? = null
        var secondLetter : Char? = null

        for (item in 0 until stringValue.length)
        {
            if (firstLetter == null)
            {
                firstLetter = stringValue[item]
            }
            else if (secondLetter == null)
            {
                secondLetter = stringValue[item]

                chunks.add("$firstLetter$secondLetter")

                firstLetter = null
                secondLetter = null
            }
        }

        redAmount = Integer.parseInt(chunks[1], 16)
        greenAmount = Integer.parseInt(chunks[2], 16)
        blueAmount = Integer.parseInt(chunks[3], 16)
    }

    fun rgbToHexString(redAmount : Int, greenAmount : Int, blueAmount : Int) : String
    {
        var hexRed = Integer.toHexString(redAmount)
        if (hexRed.length == 1)
        {
            hexRed = "0$hexRed"
        }

        var hexGreen = Integer.toHexString(greenAmount)
        if (hexGreen.length == 1)
        {
            hexGreen = "0$hexGreen"
        }

        var hexBlue = Integer.toHexString(blueAmount)
        if (hexBlue.length == 1)
        {
            hexBlue = "0$hexBlue"
        }
        return "#ff$hexRed$hexGreen$hexBlue"
    }


}