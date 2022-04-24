package com.pungo.chromatorium

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.pungo.chromatorium.game.*
import kotlin.math.roundToInt

@Composable
fun ThirdTest() {
    val level by remember{mutableStateOf(Level(""))}
    //val level = Level("")

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        level.draw( 0f,0f,LocalConfiguration.current.screenWidthDp.toFloat(),LocalConfiguration.current.screenHeightDp.toFloat())
    }


}



class Level(s: String){
    val levelItems = mutableListOf<LevelItem>()
    val levelGrid: LevelGrid
    val connectionMatrix: ConnectionMatrix
    val levelColor: Chroma
    init {
        levelGrid= LevelGrid(5,3)
        val colourDepth = 16
        val colours = listOf(
            Chroma(colourDepth, colourDepth,0,0),
            Chroma(colourDepth,0,0,colourDepth),
            Chroma(colourDepth,0,0,0),
            Chroma(colourDepth,0,0,0))
        val shapes = listOf(
            Shape(4),
            Shape(5),
            Shape(4),
            Shape(6)
        )
        val centres = listOf(
            Pair(1,1),
            Pair(2,3),
            Pair(3,2),
            Pair(5,2)
        )
        val mutables = listOf(
            false, false, true, true
        )
        levelColor = Chroma(colourDepth,(colourDepth*0.25f).roundToInt(),0,(colourDepth*0.75f).roundToInt())
        val targetColour = listOf(
            null, null, null, levelColor
        )


        for(i in colours.indices){
            levelItems.add(
                LevelItem(shapes[i], colours[i],centres[i].first,centres[i].second, 0.6f, 0.5f, mutables[i],targetColour[i] )
            )
        }

        connectionMatrix = ConnectionMatrix(levelItems.size)

    }


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

    fun checkWin(): Boolean {

        levelItems.forEach {
            if(it.targetColour!=null){

                if(!(it.targetColour!!.equals(it.chroma))){
                    return false
                }
            }

        }
        return true
    }

    fun updateChromas(){
        levelItems.forEachIndexed {index, it->
            //val c = connectionMatrix.getColumn(index+1)
            levelItems[index].chroma = itemColour(index)

        }
        levelItems.forEach {
            print(it.chroma)
        }
    }

    fun itemColour(n: Int): Chroma {
        if(!levelItems[n].mutable){
            return levelItems[n].chroma
        }else{
            val c = connectionMatrix.getColumn(n+1)
            if(c.sum()==0){
                return levelItems[n].chroma.getBlack()
            }
            val l  =List(c.size) { index ->
                if(c[index]!=0){
                    itemColour(index)
                } else{
                    levelItems[n].chroma.getBlack()
                }

            }
            return Chroma.vectorSum(l,c)
        }
    }


    @Composable
    fun draw(left: Float, top: Float, width: Float, height: Float){
        val drawWidth = levelGrid.gridWidth(width,height)
        val drawHeight  = drawWidth/levelGrid.widthToHeight

        var firstPoint by remember { mutableStateOf(Point(0.0,0.0)) }

        var secondPoint by remember { mutableStateOf(Point(0.0,0.0)) }
        var selectedIndex by remember { mutableStateOf(-1) }
        var tapPoint by remember {
            mutableStateOf(Point(0.0,0.0))
        }

        val dm = dragModifier(
            firstContact = {x,y->
                tapPoint = Point(x,y)

            },
            dragStart = {
                val gridPoint = levelGrid.ratedToGridCoordinates(tapPoint)

                val f = levelItems.indexOfFirst{ it.hitContains(gridPoint.x,gridPoint.y) }
                if(f!=-1){
                    selectedIndex = f
                    firstPoint = levelGrid.gridToRatedCoordinates(levelItems[f].gridCoordinatesCentre)
                    secondPoint = tapPoint
                }else{
                    firstPoint = tapPoint
                    secondPoint = tapPoint
                }
            },
            drag = {x,y ->
                secondPoint = secondPoint.translated(x,y)
            },
            dragEnd = {

                if(selectedIndex!=-1){
                    val gridPoint = levelGrid.ratedToGridCoordinates(secondPoint)
                    val f = levelItems.indexOfFirst{ it.hitContains(gridPoint.x,gridPoint.y) }
                    if((f>=0) && (f!=selectedIndex)){
                        val target = levelItems[f]
                        if(target.mutable){
                            connectionMatrix[selectedIndex+1,f+1] = 1
                            //target.chroma = Chroma(4,Color.LightGray)
                        }
                    }
                    selectedIndex=-1

                }else{
                    connectionMatrix.getConnections().forEach {
                        val p1Rated = levelGrid.gridToRatedCoordinates(levelItems[it.first-1].gridCoordinatesCentre)
                        val p2Rated = levelGrid.gridToRatedCoordinates(levelItems[it.second-1].gridCoordinatesCentre)
                        val l1 = Line(firstPoint,secondPoint)
                        val l2 = Line(p1Rated,p2Rated)

                        val r = l1.intersect(l2)
                        if(r){
                            connectionMatrix[it.first,it.second] = 0
                        }


                    }
                }
                secondPoint = Point(-1.0,-1.0)
                firstPoint = Point(-1.0,-1.0)
                updateChromas()

                if(checkWin()){
                    val s = connectionMatrix.getConnections().size
                    println("won with $s")
                    // TODO win

                }


            }
        )

        //LaunchedEffect(key1 = selectedIndex){
        //    updateChromas()
        //}

        Box(modifier = Modifier
            .size(drawWidth.dp, drawHeight.dp)
            .then(dm)){
            levelGrid.Checkerboard(Color(0xFF061626),Color(0xFF160626))


            Canvas(modifier = Modifier.fillMaxSize()){
                /*
                drawRect(levelColor.generateColour(),
                    levelGrid.topLeftPoint(levelGrid.rows,1,size.width,size.height).offset,
                    Size(size.width,size.height/levelGrid.cols)
                )

                 */

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

                    drawPath(it.shape.getPath(Point(x,y),levelGrid.singleSide(drawWidth)*it.radius), color = it.chroma.generateColour())
                    val contour = if(!it.mutable){
                        it.chroma.generateColour()
                    }else if(it.targetColour!=null){
                        it.targetColour!!.generateColour()
                    }else{
                        it.chroma.getBlack().generateColour()
                    }

                    drawPath(it.shape.getPath(Point(x,y),levelGrid.singleSide(drawWidth)*it.radius), color = contour, style = Stroke(width=8f))


                }


            }
        }
    }
}


class LevelItem(val shape: Shape, var chroma: Chroma, val r: Int, val c: Int, val radius: Float, val hitBox: Float, var mutable: Boolean, var targetColour: Chroma? = null){
    fun hitContains(x: Float, y: Float): Boolean {
        return Point(x-(c-0.5),y-(r-0.5)).length()>=hitBox
    }

    fun hitContains(x: Double, y: Double): Boolean {
        return Point(x-(c-0.5),y-(r-0.5)).length()<=hitBox
    }

    fun hitContains(p: Point): Boolean{
        return hitContains(p.x,p.y)
    }

    val gridCoordinatesCentre: Point
        get() {
            return Point(c-0.5,r-0.5)
        }


}



class LevelGrid(val rows: Int, val cols: Int){
    val widthToHeight: Float
    get() {
        return cols.toFloat()/rows.toFloat()
    }

    fun gridWidth( width: Float, height: Float): Float {
        return if(height*widthToHeight>width){
            width
        }else{
            height*widthToHeight
        }
    }



    fun horizontalPoint(c: Int, width: Float): Float {
        return width/cols.toFloat()*(c-0.5f)
    }

    fun verticalPoint(r: Int, height: Float): Float {
        return height/rows.toFloat()*(r-0.5f)
    }

    fun gridToRatedCoordinates(p: Point) : Point{
        return p.scale(1f/cols.toFloat(), 1f/rows.toFloat())
    }


    /** This function takes a float point (0-1.0) and converts it to grid coordinates
     */
    fun ratedToGridCoordinates(p: Point): Point {
        return p.scale(cols.toFloat(),rows.toFloat() )
    }

    fun topLeftPoint(r: Int, c: Int, width: Float, height: Float): Point {
        return Point(width/cols.toFloat()*(c-1f),height/rows.toFloat()*(r-1f))
    }

    fun singleSide(width: Float): Float {
        return width/cols
    }

    fun getValue(r: Int, c: Int){

    }


    @Composable
    fun Checkerboard(c1: Color = Color.LightGray, c2: Color= Color.DarkGray){
        Canvas(modifier = Modifier.fillMaxSize()){
            for(i in 0 until rows){
                for(j in 0 until cols){
                    val c = if((i+j)%2==0){
                        c1
                    }else{
                        c2
                    }
                    val p = topLeftPoint(i+1,j+1, size.width,size.height)

                    drawRect(c, p.offset, size= Size(singleSide(size.width),singleSide(size.width)))

                }

            }
        }
    }

}