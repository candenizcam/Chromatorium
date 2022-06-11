package com.pungo.chromatorium.fifthTest.levelOpener

import com.pungo.chromatorium.fifthTest.GameLevel
import com.pungo.chromatorium.fifthTest.levelData.DecorEllipseData
import com.pungo.chromatorium.fifthTest.levelData.DecorLineData
import com.pungo.chromatorium.fifthTest.levelData.LevelData
import com.pungo.chromatorium.fifthTest.levelData.LevelEllipseData
import com.pungo.chromatorium.tools.Size

class LevelSetOpener() {
    var levelString = ""
    val gameLevels = mutableListOf<GameLevel>()
    val gameLevelDatas = mutableListOf<LevelData>()

    fun deploy(s: String){
        levelString = s
        val lines = s.lines()
        val ellipseLines = lines.filter { it.subSequence(0,4)=="ell." }
        val lineLines = lines.filter { it.subSequence(0,4)=="lin:" }
        val rawDecorEllipses = lines.filter { it.subSequence(0,6)=="decor." }.first().split(";").map { RawDecorEllipseData(it) }
        val rawDecorLines = lines.filter{it.length>8}.filter { it.subSequence(0,7)=="declin:" }.first().split(";").map { RawLineData(it) }
        val levels = ellipseLines.indices.map { RawLevelData(ellipseLines[it],lineLines[it]) }
        val levelDataList = mutableListOf<LevelData>()
        for(i in levels.indices){
            val thisLevelRaw = levels[i]
            var levelEllipses = listOf<LevelEllipseData>()
            val decorEllipses = mutableListOf<DecorEllipseData>()
            val decorLines = mutableListOf<DecorLineData>()
            for (j in levels.indices){
                if(i==j){
                    levelEllipses = levels[i].getLevelEllipses()
                }else{
                    decorEllipses.addAll(
                        levels[j].getDecorEllipses( thisLevelRaw.left, thisLevelRaw.top, thisLevelRaw.width, thisLevelRaw.height )
                    )
                    decorLines.addAll(
                        levels[j].getDecorLines( thisLevelRaw.left, thisLevelRaw.top, thisLevelRaw.width, thisLevelRaw.height)
                    )
                }
            }
            decorEllipses.addAll(
                rawDecorEllipses.mapNotNull {
                    it.getDecorEllipseData(
                        thisLevelRaw.left,
                        thisLevelRaw.top,
                        thisLevelRaw.width,
                        thisLevelRaw.height
                    )
                }
            )
            decorLines.addAll(
                rawDecorLines.mapNotNull { it.getDecorLine(thisLevelRaw.left,
                    thisLevelRaw.top,
                    thisLevelRaw.width,
                    thisLevelRaw.height) }
            )
            levelDataList.add(LevelData(thisLevelRaw.levelNo, thisLevelRaw.width, thisLevelRaw.height, levelEllipses, decorEllipses, thisLevelRaw.getLevelLines(),decorLines))
        }
        gameLevelDatas.addAll(levelDataList)
    }


    /** This function is where additional information to levels is added
     * possibly saved data
     */
    fun generateGameLevels(playfieldSize: Size){
        gameLevels.addAll(gameLevelDatas.map { GameLevel(it,playfieldSize) })
    }
}









