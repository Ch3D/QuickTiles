package com.ch3d.android.quicktiles;

import android.provider.Settings;
import android.service.quicksettings.Tile;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

/**
 * Created by Dmitry on 4/5/2016.
 */
public class BrightnessModeTileService extends BaseTileService {

    private static final int BRIGHTNESS_AUTO = -1;
    private static final int BRIGHTNESS_MEDIUM = 132;
    private static final int BRIGHTNESS_HIGH = 255;
    private static final int DEFAULT_BRIGHTNESS = 122;

    public static final TileState AUTO = new TileState(R.drawable.vector_tile_brightness_auto,
            SCREEN_BRIGHTNESS_MODE_AUTOMATIC,
            BRIGHTNESS_AUTO,
            R.string.state_brightness_auto);

    public static final TileState MEDIUM = new TileState(R.drawable.vector_tile_brightness_medium,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL,
            BRIGHTNESS_MEDIUM,
            R.string.state_brightness_internediate);

    public static final TileState BRIGHT = new TileState(R.drawable.vector_tile_brightness_full,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL,
            BRIGHTNESS_HIGH,
            R.string.state_brightness_enabled);

    public BrightnessModeTileService() {
        super(AUTO);
    }

    @Override
    protected boolean setMode(final TileState state) {
        final int brightness = mCurrentState.getSecondaryValue();
        boolean result = Settings.System.putInt(getContentResolver(),
                SCREEN_BRIGHTNESS_MODE,
                mCurrentState.getPrimaryValue());

        if (brightness >= 0) {
            result &= Settings.System.putInt(getContentResolver(), SCREEN_BRIGHTNESS, brightness);
        }
        return result;
    }

    private int getBrightnessMode() {
        return Settings.System.getInt(getContentResolver(),
                SCREEN_BRIGHTNESS_MODE,
                SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    private int getBrightnessLevel() {
        return Settings.System.getInt(getContentResolver(),
                SCREEN_BRIGHTNESS,
                DEFAULT_BRIGHTNESS);
    }

    @Override
    protected void onUpdateTile(Tile tile) {
        if (getBrightnessMode() == SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            mCurrentState = AUTO;
        } else {
            if (getBrightnessLevel() == BRIGHTNESS_HIGH) {
                mCurrentState = BRIGHT;
            } else {
                mCurrentState = MEDIUM;
            }
        }
        updateState(tile, mCurrentState);
    }

    @Override
    protected void onTileClick(Tile tile) {
        if (tile.getState() == Tile.STATE_ACTIVE) {
            switch (mCurrentState.getSecondaryValue()) {
                case BRIGHTNESS_AUTO:
                    updateState(tile, MEDIUM);
                    break;

                case BRIGHTNESS_MEDIUM:
                    updateState(tile, BRIGHT);
                    break;

                case BRIGHTNESS_HIGH:
                    updateState(tile, AUTO);
                    break;
            }
        }
    }
}
