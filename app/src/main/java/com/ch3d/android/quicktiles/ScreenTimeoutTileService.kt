package com.ch3d.android.quicktiles

import android.provider.Settings
import android.provider.Settings.System.SCREEN_OFF_TIMEOUT
import android.provider.Settings.System.putInt
import android.service.quicksettings.Tile

/**
 * Created by Dmitry on 4/5/2016.
 */
class ScreenTimeoutTileService : BaseTileService(ScreenTimeoutTileService.DISABLED) {

    private val screenTimeOut: Int
        get() = Settings.System.getInt(contentResolver,
                SCREEN_OFF_TIMEOUT,
                DELAY_DISABLED)

    override fun onUpdateTile(tile: Tile) {
        when (screenTimeOut) {
            DELAY_DISABLED -> mCurrentState = DISABLED

            DELAY_INTERMEDIATE -> mCurrentState = INTERMEDIATE

            DELAY_ENABLED -> mCurrentState = ENABLED

            else -> mCurrentState = DISABLED
        }
        updateState(tile, mCurrentState)
    }

    override fun onTileClick(tile: Tile) {
        when (mCurrentState.primaryValue) {
            DELAY_DISABLED -> updateState(tile, INTERMEDIATE)

            DELAY_INTERMEDIATE -> updateState(tile, ENABLED)

            DELAY_ENABLED -> updateState(tile, DISABLED)
        }
    }

    override fun setMode(state: TileState) = putInt(contentResolver, SCREEN_OFF_TIMEOUT,
            state.primaryValue)

    companion object {

        private val DELAY_DISABLED = 15 * 1000
        private val DELAY_INTERMEDIATE = 60 * 1000
        private val DELAY_ENABLED = 120 * 1000

        val ENABLED = TileState(Tile.STATE_ACTIVE,
                R.drawable.vector_tile_timeout_enabled,
                DELAY_ENABLED,
                BaseTileService.DEFAULT_VALUE,
                R.string.state_timeout_enabled)

        val INTERMEDIATE = TileState(Tile.STATE_ACTIVE,
                R.drawable.vector_tile_timeout_intermediate,
                DELAY_INTERMEDIATE,
                BaseTileService.DEFAULT_VALUE,
                R.string.state_timeout_internediate)

        val DISABLED = TileState(Tile.STATE_INACTIVE,
                R.drawable.vector_tile_timeout_disabled,
                DELAY_DISABLED,
                BaseTileService.DEFAULT_VALUE,
                R.string.state_timeout_disabled)
    }
}
