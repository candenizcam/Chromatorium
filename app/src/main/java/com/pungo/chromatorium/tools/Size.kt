package com.pungo.chromatorium.tools

class Size(val width: Double, val height: Double) {
    constructor(width: Float, height: Float): this(width.toDouble(), height.toDouble())
    constructor(width: Int, height: Int): this(width.toDouble(), height.toDouble())

    val androidSize: androidx.compose.ui.geometry.Size
    get() {
        return androidx.compose.ui.geometry.Size(width.toFloat(),height.toFloat())
    }



    fun contains(other: Size, noBorders: Boolean = false): Boolean {
        return if (noBorders){
            this.width>other.width && this.height>other.height
        }else{
            this.width>=other.width && this.height>=other.height
        }

    }


    /** returns the scaled version of this that fits the other
     * (2,1).scaleToFit( (100,100) ) => (100,50)
     */
    fun scaleToFit(other: Size): Size {
        val widthRatio = width/other.width
        val heightRatio = height/other.height

        return if(widthRatio>heightRatio){
            Size(other.width,height/widthRatio)
        }else{
            Size(width/heightRatio,other.height)
        }


    }


}