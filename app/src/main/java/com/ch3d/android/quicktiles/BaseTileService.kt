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
        val DEFAULT_VALUE = -1
    }

    protected var mCurrentState: TileState = defaultState

    override fun onStartListening() = onUpdateTile(qsTile)

    protected abstract fun onUpdateTile(tile: Tile)

    protected fun ensurePermissions(): Boolean {
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + applicationContext.packageName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return false
        }
        return true
    }

    protected fun updateState(tile: Tile, newState: TileState) {
        mCurrentState = newState
        tile.label = getString(mCurrentState.titleResId)
        tile.icon = Icon.createWithResource(this, mCurrentState.drawableId)
        setMode(mCurrentState)
        tile.state = newState.state
        tile.updateTile()
    }

    protected abstract fun setMode(state: TileState): Boolean

    override fun onClick() {
        if (ensurePermissions()) {
            onTileClick(qsTile)
        }
    }

    protected abstract fun onTileClick(qsTile: Tile)
}
