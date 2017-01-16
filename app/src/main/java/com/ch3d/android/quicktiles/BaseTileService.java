package com.ch3d.android.quicktiles;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

/**
 * Created by Dmitry on 6/20/2016.
 */
public abstract class BaseTileService extends TileService {

    public static final int DEFAULT_VALUE = -1;
    protected TileState mCurrentState = null;

    protected BaseTileService(TileState defaultState) {
        mCurrentState = defaultState;
    }

    @Override
    public void onStartListening() {
        onUpdateTile(getQsTile());
    }

    protected abstract void onUpdateTile(Tile tile);

    protected boolean ensurePermissions() {
        if (!Settings.System.canWrite(this)) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return false;
        }
        return true;
    }

    protected void updateState(Tile tile, TileState newState) {
        mCurrentState = newState;
        tile.setLabel(getResources().getString(mCurrentState.getTitleResId()));
        tile.setIcon(Icon.createWithResource(this, mCurrentState.getDrawableId()));
        setMode(mCurrentState);
        tile.updateTile();
    }

    protected abstract boolean setMode(TileState state);

    @Override
    public void onClick() {
        if (ensurePermissions()) {
            onTileClick(getQsTile());
        }
    }

    protected abstract void onTileClick(Tile qsTile);
}