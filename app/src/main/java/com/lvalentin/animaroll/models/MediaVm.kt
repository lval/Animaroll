package com.lvalentin.animaroll.models

data class MediaVm(
    val id: String,
    val name: String,
    val path: String,
    var itemsCnt: Int,
    var isSelected: Boolean
)
