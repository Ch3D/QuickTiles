package com.ch3d.android.quicktiles

import android.provider.Settings
import android.provider.Settings.System.SCREEN_BRIGHTNESS
import android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE
import android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
import android.service.quicksettings.Tile

/**
 * Created by Dmitry on 4/5/2016.
 */
class BrightnessModeTileService : BaseTileService(BrightnessModeTileService.AUTO) {

    override fun setMode(state: TileState): Boolean {
        val brightness = mCurrentState.secondaryValue
        var result = Settings.System.putInt(contentResolver, SCREEN_BRIGHTNESS_MODE, mCurrentState.primaryValue)
        if (brightness >= 0) {
            result = result and Settings.System.putInt(contentResolver, SCREEN_BRIGHTNESS, brightness)
        }
        return result
    }

    private val brightnessMode: Int
        get() = Settings.System.getInt(contentResolver, SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_AUTOMATIC)

    private val brightnessLevel: Int
        get() = Settings.System.getInt(contentResolver, SCREEN_BRIGHTNESS, DEFAULT_BRIGHTNESS)

    override fun updateTile(tile: Tile) {
        if (brightnessMode == SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            mCurrentState = AUTO
        } else {
            if (brightnessLevel == BRIGHTNESS_HIGH) {
                mCurrentState = BRIGHT
            } else {
                mCurrentState = MEDIUM
            }
        }

        updateState(tile, mCurrentState)
    }

    override fun onTileClick(tile: Tile) {
        when (mCurrentState) {
            AUTO -> updateState(tile, MEDIUM)
            MEDIUM -> updateState(tile, BRIGHT)
            BRIGHT -> updateState(tile, AUTO)
        }
    }

    companion object {
        private val BRIGHTNESS_AUTO = -1
        private val BRIGHTNESS_MEDIUM = 132
        private val BRIGHTNESS_HIGH = 255
        private val DEFAULT_BRIGHTNESS = 122

        val AUTO = TileState.build {
            drawableId = R.drawable.vector_tile_brightness_auto
            primaryValue = SCREEN_BRIGHTNESS_MODE_AUTOMATIC
            secondaryValue = BRIGHTNESS_AUTO
            titleResId = R.string.state_brightness_auto
        }

        val MEDIUM = TileState.build {
            state = Tile.STATE_ACTIVE
            drawableId = R.drawable.vector_tile_brightness_medium
            primaryValue = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            secondaryValue = BRIGHTNESS_MEDIUM
            titleResId = R.string.state_brightness_internediate
        }

        val BRIGHT = TileState.build {
            state = Tile.STATE_ACTIVE
            drawableId = R.drawable.vector_tile_brightness_full
            primaryValue = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            secondaryValue = BRIGHTNESS_HIGH
            titleResId = R.string.state_brightness_enabled
        }
    }
}
