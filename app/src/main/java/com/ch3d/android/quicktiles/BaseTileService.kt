package com.ch3d.android.quicktiles

import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

/**
 * Created by Dmitry on 6/20/2016.
 */
abstract class BaseTileService constructor(defaultState: TileState) : TileService() {

    companion object {
        const val DEFAULT_VALUE = -1
    }

    private fun hasWriteSettingsPermission() = Settings.System.canWrite(this)

    protected var mCurrentState: TileState = defaultState

    override fun onStartListening() {
        if (hasWriteSettingsPermission()) {
            updateTile(qsTile)
        }
    }

    abstract fun updateTile(tile: Tile)

    protected fun updateState(tile: Tile, newState: TileState) {
        mCurrentState = newState
        setMode(mCurrentState)
        tile.apply {
            label = getString(mCurrentState.titleResId)
            icon = Icon.createWithResource(this@BaseTileService, mCurrentState.drawableId)
            state = newState.state
            updateTile()
        }
    }

    protected abstract fun setMode(state: TileState): Boolean

    override fun onClick() {
        if (!hasWriteSettingsPermission()) {
            startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    .apply {
                        data = Uri.parse("package:" + applicationContext.packageName)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
        } else {
            onTileClick(qsTile)
        }
    }

    protected abstract fun onTileClick(tile: Tile)
}
