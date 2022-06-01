package com.pungo.chromatorium.fourthTest

import androidx.compose.runtime.Composable
import com.pungo.chromatorium.tools.Point
import com.pungo.chromatorium.tools.ReadDataFileWithCallback
import com.pungo.chromatorium.tools.Rectangle
import kotlin.math.abs
import kotlin.math.roundToInt

object BatchReader {
    @Composable
    fun dataReader(path: String, callback: (String)->Unit={}){
        ReadDataFileWithCallback(path){
            callback(it)
        }
    }

    fun stringToClasses(text: String): List<LevelData> {
        val lines = text.lines()
        val nodeClasses = lines[0].split(";").map {
            EllipseData(it)
        }
        val decorEll = lines[1].split(";").map {
            DecorEllipseData(it)
        }

        val textRects = lines[2].split(";").map {
            TextRectData(it)
        }

        val lins = lines[3].split(";").map {
            LineData(it)
        }

        val decLins = lines[4].split(";").map {
            DecorLine(it)
        }

        val opacities = nodeClasses.map { it.opacity }.distinct().sorted().reversed()

        val levels = opacities.map { i->


            LevelData(100-i,
                    nodeClasses.filter { it.opacity== i},
                    textRects.filter { it.opacity== i},
                    lins.filter { it.opacity== i}
                )
        }

        return levels






    }


    /*
    @Composable
    fun batchReader(path: String, callback: ( CardLevel)->Unit={}){
        ReadDataFileWithCallback(path){
            val lines = it.lines()
            //ell, decor, hxg, lin, declin
            val nodeClasses = lines[0].split(";").map {
                EllipseData(it)
            }
            val textRects = lines[2].split(";").map {
                TextRectData(it)
            }

            val minX = nodeClasses.minOf { it.x} - 10
            val maxX = nodeClasses.maxOf { it.x} + 10
            val minY = nodeClasses.minOf { it.y } - 50
            val maxY = nodeClasses.maxOf { it.y} + 50
            val width = maxX-minX
            val height = maxY - minY




            val ellipse = nodeClasses.map {
                val r = textRects.first {tr-> tr.strokeColour == it.strokeColour  }.rect

                val rc = Rectangle((r.x + it.width/2 - minX)/width, (r.y+ it.width/2  - minY)/height,r.w/width, r.h/height)

                CardEllipse((it.x - minX)/width, (it.y - minY)/height, it.colourString, rc)
            }



            val links = lines[3].split(";").map {
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

     */

    class LevelData(val no: Int,
                    val ellipse: List<EllipseData>,
                    val textRect: List<TextRectData>,
                    val lineData: List<LineData>){
        val minLeft: Float
        val maxRight: Float
        val minTop: Float
        val maxBottom: Float
        val width: Float
        val height: Float
        val ellipses: List<CardEllipse>
        val lines: List<CardLink>
        init {
            val margin = textRect.maxOf { it.rect.h }.toFloat() *3f
            val e1 = ellipse.minOf { it.left }
            val e2 = textRect.minOf { it.left }

            val relLin = lineData.filter { it.intermediatePoints.size>0 }

            minLeft = listOf( ellipse.minOf { it.left },textRect.minOf { it.left }, relLin.minOfOrNull { it.left }).filterNotNull() .minOf { it } - margin
            maxRight = listOf( ellipse.maxOf { it.right }, textRect.maxOf { it.right }, relLin.minOfOrNull { it.right } ).filterNotNull().maxOf { it } + margin
            minTop = listOf( ellipse.minOf { it.top },textRect.minOf { it.top }, relLin.minOfOrNull { it.top } ).filterNotNull().minOf { it } - margin
            maxBottom = listOf( ellipse.maxOf { it.bottom },textRect.maxOf { it.bottom }, relLin.minOfOrNull{ it.bottom } ).filterNotNull().maxOf { it } + margin
            width = maxRight - minLeft
            height = maxBottom - minTop

            ellipses = ellipse.map {
                val r = textRect.first {tr-> tr.strokeColour == it.strokeColour  }.rect
                val rc = Rectangle((r.x + it.width/2 - minLeft)/width, (r.y+ it.width/2  - minTop)/height,r.w/width, r.h/height)
                CardEllipse(it.id,(it.x - minLeft)/width, (it.y - minTop)/height, it.colourString, rc)
            }

            lines = lineData.map {ld->
                val rawPoints = mutableListOf<Point>()
                val f = ld.from
                val ids = ellipse.map { it.id + " sc: " + it.strokeColour + " op: " + it.opacity }
                println("level no: " + no.toString())
                println(f + ld.s)
                println(ids )

                rawPoints.add(ellipses.first { it.id == ld.from  }.normalPoint)

                ld.intermediatePoints.map {
                    rawPoints.add(it.translated(-minLeft,-minTop).rate(width,height))
                }
                rawPoints.add(ellipses.first { it.id == ld.to  }.normalPoint)


                val leds = mutableListOf<Point>()
                val dec = rawPoints.map { it.scale(width,height) }
                for (i in 0 until dec.size-1){
                    val w = abs(dec[i].x- dec[i+1].x)
                    val h = abs(dec[i].y- dec[i+1].y)

                    if(h>w){
                        val stepNo = (h/10).roundToInt()
                        val alpha = 1f/stepNo.toDouble()
                        for(j in 0..stepNo.toInt()){
                            leds.add(dec[i].sum(dec[i+1],(alpha*j)))
                        }
                    }else{
                        val stepNo = w/10
                        val alpha = 1f/stepNo
                        for(j in 0..stepNo.toInt()){
                            leds.add(dec[i].sum(dec[i+1],(alpha*j)))
                        }
                    }
                }


                CardLink(ld.from,ld.to).also {
                    it.setPoints(rawPoints,leds)
                }

            }



        }




    }

    class DecorLine(s: String){
        var opacity: Int
        var to: String
        var from: String
        var intermediatePoints = mutableListOf<String>()
        init {
            //declin:50:fr.2,to.1:150,160:130,180:130,320:180,360;
            val splits = s.split(":")
            opacity = splits[1].toInt()
            splits[2].split(",").also {
                from = it[0].split(".")[1]
                to = it[1].split(".")[1]
            }
            if(splits.size>3){
                for(i in 3 until splits.size){
                    intermediatePoints.add(splits[i]  )
                }


            }

        }
    }

    class DecorEllipseData(s: String){
        var x: Float
        var y: Float
        var width: Float
        var colour: String
        var opacity: Int
        var id: String
        init {
            // decor:#FFFFFF:50:260,340,40;
            val (type, colourIn, opacityIn, dims) = s.split(":")
            id = type.split(".")[1]
            colour = colourIn
            opacity = opacityIn.toInt()
            val (x_in, y_in, diametre) = dims.split(",")
            x = x_in.toFloat() +  diametre.toFloat()/2
            y = y_in.toFloat()+  diametre.toFloat()/2
            width = diametre.toFloat()
        }
    }

    class LineData(val s: String){
        var opacity: Int
        var to: String
        var from: String
        var intermediatePoints = mutableListOf<Point>()
        init {
            // lin:99:fr.2,to.1;lin:99:fr.2,to.3:140,60;lin:99:fr.3,to.1:220,100:150,100:100,150:100,300;
            val splits = s.split(":")
            opacity = splits[1].toInt()
            splits[2].split(",").also {
                from = it[0].split(".")[1]
                to = it[1].split(".")[1]
            }
            if(splits.size>3){
                for(i in 3 until splits.size){
                    intermediatePoints.add(Point(splits[i])  )
                }


            }

        }


        // warning, these do not include ellipses, so they cant be used alone
        val left: Float
            get() {
                return intermediatePoints.minOf { it.x }.toFloat()
            }
        val right: Float
            get() {
                return intermediatePoints.maxOf { it.x }.toFloat()
            }
        val top: Float
            get() {
                return intermediatePoints.minOf { it.y }.toFloat()
            }
        val bottom: Float
            get() {
                return intermediatePoints.maxOf { it.y }.toFloat()
            }

    }

    class TextRectData(s: String){
        var strokeColour: String
        val rect: Rectangle
        var opacity: Int
        init {
            val (type, strokeColourIn, opacityIn, dims) = s.split(":")
            opacity = opacityIn.toInt()
            strokeColour = strokeColourIn
            rect = Rectangle.fromIntString(dims)
        }

        val left: Float
            get() {
                return rect.x.toFloat()
            }
        val right: Float
            get() {
                return (rect.x + rect.w).toFloat()
            }
        val top: Float
            get() {
                return rect.y.toFloat()
            }
        val bottom: Float
            get() {
                return (rect.y + rect.h).toFloat()
            }
    }

    class EllipseData(s: String){
        var type: String
        var id: String
        var colourString: String
        var strokeColour: String
        var strokeWidth: Int
        var x: Float
        var y: Float
        var width: Float
        var opacity: Int
        init {
            //ell:#800000,#010200:99:5:40,320,40;
            val (nodeType, colour, opacityIn ,stroke, dimentions) = s.split(":")
            type = nodeType
            id = type.split(".")[1]
            colourString = colour.split(",")[0]
            strokeColour = colour.split(",")[1]
            strokeWidth = stroke.toInt()
            opacity = opacityIn.toInt()
            val (x_in, y_in, diametre) = dimentions.split(",")
            x = x_in.toFloat() +  diametre.toFloat()/2
            y = y_in.toFloat()+  diametre.toFloat()/2
            width = diametre.toFloat()

        }

        val left: Float
        get() {
            return x - width/2f
        }
        val right: Float
            get() {
                return x + width/2f
            }
        val top: Float
            get() {
                return x - width/2f
            }
        val bottom: Float
            get() {
                return x + width/2f
            }

    }

}