package com.pungo.chromatorium.fourthTest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.pungo.chromatorium.game.Point
import com.pungo.chromatorium.tools.Matrix

/** This class contains information and operations about the level on the highest level, it should delegate as much as it can
 * this constructor is not private, but do not call it unless you know what you are doing, companion constructors are recommended more heavily
 *
 */
class CardLevel() {
    val nodes = mutableListOf<CardNode>()
    val links = mutableListOf<CardLink>()
    var cardLevelGrid = CardLevelGrid(0,0)
    init {



    }

    /*
    fun DrawScope.drawNode(cn: CardNode){
        drawCircle(
            color = Color.Green,
            radius = 25f,
            center = cn.normalPoint.scale(this.size.width,this.size.height).offset
            //secondPoint.scale(this.size.width,this.size.height).offset
        )
    }

     */


    /** This function updates colours based on connections
     *
     */
    fun updateColours(){


        var connections = Matrix(nodes.size,nodes.size)

        links.forEach {
            if(it.state==1){
                val weight = if((nodes[it.from-1] as CardEllipse).assignedColour!=null) 1.0 else 0.5

                connections[it.from,it.to] = weight
            }else if(it.state == 2){
                val weight = if((nodes[it.to-1] as CardEllipse).assignedColour!=null) 1.0 else 0.5

                connections[it.to,it.from] = weight
            }
        }






        val sourceIndex = mutableListOf<Int>()
        val colours = mutableListOf<Chromini>()
        nodes.forEachIndexed {index,it->
            it as CardEllipse
            if(it.assignedColour!=null){
                sourceIndex.add(index+1)
                colours.add(it.assignedColour)
            }
        }

        val resultVectors = sourceIndex.indices.map {i->
            val smallCon = connections.copy()
            for(j in sourceIndex.indices){
                if(i==j) continue
                smallCon.setRow(sourceIndex[j],0.0)
                smallCon.setCol(sourceIndex[j],0.0)
            }

            val smallPower = smallCon.powerSeriesSum(100)


            val v = Matrix(1,nodes.size)
            v[1,sourceIndex[i]] = 1.0
            (v* smallPower).values.map {
                colours[i]*it
            }
        }


        for(i in nodes.indices){
            var r = resultVectors[0][i]
            for (j in 1 until resultVectors.size){
                r += resultVectors[j][i]
            }
            nodes[i].paint(r)

        }

    }

    @Composable
    fun draw(frameWidth: Float, frameHeight: Float){
        val (drawWidth, drawHeight) = cardLevelGrid.fitInto(frameWidth,frameHeight)
        var firstPoint by remember { mutableStateOf(Point(0.0,0.0)) }
        //var closestPoints = remember {
        //    mutableStateListOf<Point>()
        //}


        val dm = dragModifier(
            firstContact = {x,y->
                val distances = links.map {
                    Pair(it.distance(Point(x,y)),it)
                }
                val (closestDistance, closestLine) = distances.minByOrNull { it.first }!!

                if(closestDistance<0.1){
                    closestLine.nextState()
                }
                firstPoint = Point(x,y)
                updateColours()
                //closestPoints.clear()
                //val points = links.forEach() {

                //   closestPoints.add(it.closestPoint(Point(x,y)))
                //}



                //val closestLineIndex = distances.indexOf(distances.minOrNull()!!)

            },
            dragStart = {

            },
            dragEnd = {

            },
            drag = {x,y->

            }


        )


        Box(modifier = Modifier
            .background(Color(3, 40, 67))
            .padding(cardLevelGrid.padding.dp)
            .size(drawWidth.dp, drawHeight.dp)
            .then(dm)

            ){
            //levelGrid.Checkerboard(Color(0xFF4000BB), Color(0xFF4000BB))
            //cardLevelGrid.drawGrid(10)


            Canvas(modifier = Modifier.size((drawWidth).dp, (drawHeight).dp)){


                links.forEach {
                    it.draw(this)
                }

                nodes.forEach {
                    it.draw(this)
                }


                drawCircle(Color.Red,
                    25f,

                    firstPoint.scale(this.size.width,this.size.height).offset)

                /*
                               closestPoints.forEach {
                                   drawCircle(Color.Cyan,
                                       25f,
                                       it.scale(this.size.width,this.size.height).offset
                                   )
                               }

                                */




                /*

                if(selectedIndex>=0) {
                    drawLine(
                        start = firstPoint.scale(this.size.width,this.size.height).offset,
                        end = secondPoint.scale(this.size.width,this.size.height).offset,
                        color = levelItems[selectedIndex].chroma.generateColour(),
                        strokeWidth = 4f
                    )

                    drawCircle(
                        color = levelItems[selectedIndex].chroma.generateColour(),
                        radius = 25f,
                        center = secondPoint.scale(this.size.width,this.size.height).offset

                    )

                }else{
                    if(firstPoint.y!=-1.0){
                        drawLine(
                            start = firstPoint.scale(this.size.width,this.size.height).offset,
                            end = secondPoint.scale(this.size.width,this.size.height).offset,
                            color = Color.White,
                            strokeWidth = 4f
                        )
                    }

                }

                connectionMatrix.getConnections().forEach {

                    val p1= levelGrid.gridToRatedCoordinates(levelItems[it.first-1].gridCoordinatesCentre)
                    val p2= levelGrid.gridToRatedCoordinates(levelItems[it.second-1].gridCoordinatesCentre)

                    drawLine(
                        start = p1.scale(this.size.width,this.size.height).offset,
                        end = p2.scale(this.size.width,this.size.height).offset,
                        color = levelItems[it.first-1].chroma.generateColour(),
                        strokeWidth = 4f
                    )


                }

                levelItems.forEach {
                    val y = levelGrid.verticalPoint(it.r,this.size.height)
                    val x = levelGrid.horizontalPoint(it.c,this.size.width)

                    drawPath(it.shape.getPath(Point(x,y),levelGrid.singleSide(this.size.width)*it.radius), color = it.chroma.generateColour())
                    val contour = if(!it.mutable){
                        it.chroma.generateColour()
                    }else if(it.targetColour!=null){
                        it.targetColour!!.generateColour()
                    }else{
                        it.chroma.getBlack().generateColour()
                    }

                    drawPath(it.shape.getPath(Point(x,y),levelGrid.singleSide(this.size.width)*it.radius), color = contour, style = Stroke(width=8f))


                    // uncomment next line to see hitboxes
                    // drawCircle(it.chroma.generateColour(),levelGrid.singleSide(this.size.width)*it.hitBox,Point(x,y).offset, style = Stroke(width=4f)  )


                    val s = colorTextFormat.chromaText(it.chroma)
                    if(s!=""){

                        drawText(drawContext, s, x, y+levelGrid.singleSide(this.size.width)*it.radius*1.5f,
                            color= it.chroma.getWhite().generateColour(),
                            typeface = ResourcesCompat.getFont(context , R.font.breeserifregular))

                        if(it.targetColour!=null){
                            val tc = it.targetColour!!
                            val s = colorTextFormat.chromaText(tc)
                            drawText(drawContext, s, x, y-levelGrid.singleSide(this.size.width)*it.radius*1.4f,
                                color= tc.generateColour(),
                                typeface = ResourcesCompat.getFont(context , R.font.breeserifregular))
                        }
                    }


                }

                 */
            }
        }
    }

    /** functions related to tracking tap on the gamefield
     * first contact x,y are rated (cos im awesome)
     */
    fun dragModifier(firstContact: (Float, Float)->Unit, dragStart: ()->Unit,drag: (Float, Float)->Unit, dragEnd: ()->Unit): Modifier {
        return Modifier
            .pointerInput(Unit) {
                val h = this.size.height
                val w = this.size.width


                detectTapGestures(onPress = {
                    val x = it.x//-left
                    val y = it.y// - top
                    firstContact(x / w, y / h)
                })


            }
            .pointerInput(Unit) {
                val h = this.size.height
                val w = this.size.width
                detectDragGestures(onDragEnd = {
                    dragEnd()
                }, onDragStart = {
                    dragStart()
                }) { change, dragAmount ->
                    change.consumeAllChanges()
                    drag(dragAmount.x / w, dragAmount.y / h)
                }
            }
    }


























    companion object{
        fun testLevel(): CardLevel {
            val cl = CardLevel()
            cl.cardLevelGrid = CardLevelGrid(10,10,1f)
            val nodes = listOf("1,1","2,2","2,5","3,5","5,5")
            val links = listOf("1,2","2,3")

            cl.nodes.addAll(
                nodes.map {
                    val sl = it.split(",")
                    //CardNode(cl.cardLevelGrid.ratedX(sl[0].toInt()),cl.cardLevelGrid.ratedY(sl[1].toInt()))
                    val (a,b) = cl.cardLevelGrid.ratedPair(sl[0].toInt(),sl[1].toInt())
                    CardNode(a,b)
                }
            )

            cl.links.addAll(
                links.map {
                    val sl = it.split(",")
                    CardLink(sl[0].toInt(),sl[1].toInt())
                }
            )


            return cl
        }

        fun fromBatchReader(r: Int, c: Int, shapes: List<CardEllipse>, links: List<CardLink>): CardLevel {
            val cl = CardLevel()
            cl.cardLevelGrid = CardLevelGrid(r,c,r/10f)
            cl.nodes.addAll(
                shapes
            )

            cl.links.addAll(
                links
            )

            return cl
        }


    }

}