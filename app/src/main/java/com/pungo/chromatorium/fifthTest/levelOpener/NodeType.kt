package com.pungo.chromatorium.fifthTest.levelOpener

enum class NodeType{
    NONE, SOURCE, SINK;

    companion object{
        fun fromString(s: String): NodeType {
            return when (s) {
                "5" -> {
                    SINK
                }
                "3" -> {
                    SOURCE
                }
                else -> {
                    NONE
                }
            }
        }
    }
}