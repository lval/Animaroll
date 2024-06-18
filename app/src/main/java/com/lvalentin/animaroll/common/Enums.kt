package com.lvalentin.animaroll.common

object Enums {

    enum class MediaType {
        IMAGE,
        VIDEO,
    }

    enum class PrefBackground(val id: Int) {
        BLUR(1),
        BLACK(2),
        WHITE(3),
    }

    enum class PrefDisplayTime(val id: Int) {
        OFF(1),
        TOP_END(2),
        BOTTOM_END(3),
        TOP_START(4),
        BOTTOM_START(5),
    }

    enum class PrefMediaType(val id: Int) {
        All(1),
        IMAGES(2),
        VIDEOS(3),
    }

    enum class PrefMediaFit(val id: Int) {
        ORIGINAL(1),
        COVER(2),
        FILL(3),
    }

    enum class PrefTransition(val id: Int) {
        ROTATE(3),
        SLIDE(4),
        SCROLL(5),
        SCALE(6),
        FADE(2),
        RANDOM(1),
    }

    enum class RecyclerViewType(val id: Int) {
        HEADER(0),
        ITEM(1),
    }

}