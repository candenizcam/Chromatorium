package com.pungo.chromatorium.fourthTest

import androidx.compose.runtime.Composable
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.ReadDataFileWithCallback
import com.pungo.chromatorium.tools.Rectangle
import kotlin.math.abs
import kotlin.math.roundToInt

object BatchReader {
    @Composable
    fun batchReader(path: String, callback: ( CardLevel)->Unit={}){
        ReadDataFileWithCallback(path){
            val lines = it.lines()
            val nodeClasses = lines[0].split(";").map {
                NodeClass(it)
            }

            val minX = nodeClasses.minOf { it.x} - 10
            val maxX = nodeClasses.maxOf { it.x} + 10
            val minY = nodeClasses.minOf { it.y } - 50
            val maxY = nodeClasses.maxOf { it.y} + 50
            val width = maxX-minX
            val height = maxY - minY

            val textRects = lines[1].split(";").map { TextRect(it) }


            val ellipse = nodeClasses.map {
                val r = textRects.first {tr-> it.strokeColour == it.strokeColour  }

                CardEllipse((it.x - minX)/width, (it.y - minY)/height, it.colourString, r.rect)
            }



            val links = lines[2].split(";").map {
                val splits = it.split(":")
                if(splits[0]=="lin"){

                    val (fr,to) = splits[1].split(",").map { it.split(".")[1].toInt() }

                    val c = CardLink(fr,to)
                    val rawPoints = mutableListOf<Point>()

                    rawPoints.add(ellipse[c.from-1].normalPoint)

                    for (i in 2 until splits.size){
                        rawPoints.add(Point(splits[i]).translated(-minX,-minY).rate(width,height))
                    }
                    rawPoints.add(ellipse[c.to-1].normalPoint)

                    val leds = mutableListOf<Point>()

                    val dec = rawPoints.map { it.scale(width,height) }
                    for (i in 0 until dec.size-1){
                        val w = abs(dec[i].x- dec[i+1].x)
                        val h = abs(dec[i].y- dec[i+1].y)

                        if(h>w){
                            val stepNo = (h/10).roundToInt()
                            val alpha = 1f/stepNo.toDouble()
                            for(j in 0..stepNo.toInt()){
                                //leds.add(Point(w/stepNo*j,h/stepNo*j ))

                                leds.add(dec[i].sum(dec[i+1],(alpha*j)))
                            }
                        }else{
                            val stepNo = w/10
                            val alpha = 1f/stepNo
                            for(j in 0..stepNo.toInt()){
                                //leds.add(Point(w/stepNo*j,h/stepNo*j ))
                                leds.add(dec[i].sum(dec[i+1],(alpha*j)))
                            }
                        }

                    }



                    val ll = leds.map { it.rate(width,height) }
                    c.setPoints(rawPoints,ll)

                    c
                }else {
                    throw Exception("Unknown type in links")
                }

            }


            //lin:fr.1,to.3;lin:fr.2,to.1:140,60;lin:fr.2,to.3:230,90:140,90:90,140:90,310;lin:fr.5,to.4:230,110:150,110:110,150:110,350:130,370

            //ell:#FFFFFF:1:280,160,40
            //val wordStrings = it.split(";")
            //val notGoodWords = wordStrings[1].lines()
            //this.words = wordStrings[0].lines().map {
            //    val s = it.split("_")
            //    Word(s[0],s[1], notGoodWords.contains(s[0])) }
            callback(CardLevel.fromBatchReader( (maxY-minY).toInt(),(maxX-minX).toInt(), ellipse, links))

        }
    }

    class TextRect(s: String){
        val strokeColour: String
        val rect: Rectangle
        init {
            val (type, strokeCol, dims) = s.split(":")
            strokeColour = strokeCol
            rect = Rectangle.fromIntString(dims)
        }
    }

    class NodeClass(s: String){
        var type: String
        var colourString: String
        var strokeColour: String
        var strokeWidth: Int
        var x: Float
        var y: Float
        var width: Float
        init {
            val (nodeType, colour, stroke, dimentions) = s.split(":")

            if (nodeType == "ell") {
                type = nodeType
                colourString = colour.split(",")[0]
                strokeColour = colour.split(",")[1]
                strokeWidth = stroke.toInt()
                val (x_in, y_in, diametre) = dimentions.split(",")
                x = x_in.toFloat() +  diametre.toFloat()/2
                y = y_in.toFloat()+  diametre.toFloat()/2
                width = diametre.toFloat()
            } else {
                throw Exception("Unknown type")
            }

        }
    }

}