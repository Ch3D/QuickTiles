package com.ch3d.android.quicktiles;

import android.provider.Settings;
import android.service.quicksettings.Tile;

/**
 * Created by Dmitry on 4/5/2016.
 */
public class ScreenTimeoutTileService extends BaseTileService {

    private static final int DELAY_DISABLED = 15 * 1000;
    private static final int DELAY_INTERMEDIATE = 60 * 1000;
    private static final int DELAY_ENABLED = 120 * 1000;

    public ScreenTimeoutTileService() {
        super(DISABLED);
    }

    public static final TileState ENABLED = new TileState(R.drawable.vector_tile_timeout_enabled,
            DELAY_ENABLED,
            DEFAULT_VALUE,
            R.string.state_timeout_enabled);

    public static final TileState INTERMEDIATE = new TileState(R.drawable.vector_tile_timeout_intermediate,
            DELAY_INTERMEDIATE,
            DEFAULT_VALUE,
            R.string.state_timeout_internediate);

    public static final TileState DISABLED = new TileState(R.drawable.vector_tile_timeout_disabled,
            DELAY_DISABLED,
            DEFAULT_VALUE,
            R.string.state_timeout_disabled);

    private int getScreenTimeOut() {
        return Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT,
                DELAY_DISABLED);
    }

    @Override
    protected void onUpdateTile(Tile tile) {
        switch (getScreenTimeOut()) {
            case DELAY_DISABLED:
                mCurrentState = DISABLED;
                break;

            case DELAY_INTERMEDIATE:
                mCurrentState = INTERMEDIATE;
                break;

            case DELAY_ENABLED:
                mCurrentState = ENABLED;
                break;

            default:
                mCurrentState = DISABLED;
                break;
        }
        updateState(tile, mCurrentState);
    }

    @Override
    protected void onTileClick(Tile tile) {
        if (tile.getState() == Tile.STATE_ACTIVE) {
            switch (mCurrentState.getPrimaryValue()) {
                case DELAY_DISABLED:
                    updateState(tile, INTERMEDIATE);
                    break;

                case DELAY_INTERMEDIATE:
                    updateState(tile, ENABLED);
                    break;

                case DELAY_ENABLED:
                    updateState(tile, DISABLED);
                    break;
            }
        }
    }

    @Override
    protected boolean setMode(TileState state) {
        return Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, state.getPrimaryValue());
    }
}
