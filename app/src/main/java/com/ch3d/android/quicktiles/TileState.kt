package com.ch3d.android.quicktiles

import android.service.quicksettings.Tile

/**
 * Created by Dmitry on 6/20/2016.
 */
data class TileState(val state: Int,
                     val drawableId: Int,
                     val primaryValue: Int,
                     val secondaryValue: Int,
                     val titleResId: Int) {

    private constructor(builder: Builder) :
            this(builder.state,
                    builder.drawableId,
                    builder.primaryValue,
                    builder.secondaryValue,
                    builder.titleResId)

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var state: Int = Tile.STATE_INACTIVE
        var drawableId: Int = 0
        var primaryValue: Int = 0
        var secondaryValue: Int = 0
        var titleResId: Int = 0

        fun build() = TileState(this)
    }
}
