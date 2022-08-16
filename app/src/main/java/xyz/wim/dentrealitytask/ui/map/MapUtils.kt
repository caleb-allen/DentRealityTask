package xyz.wim.dentrealitytask.ui.map

import timber.log.Timber

class MapUtils {
    companion object {
        /**
         * Convert from lat/long coordinates to the
         * percent across the map image
         */
        fun coordToPercent(x: Float, y: Float): Pair<Float, Float>{
            val pctX = ((x + 10f) * 1.6949f) / 100f
            val pctY = ((y - 35f) * 2.777f) / 100f

            Timber.d("Coords: \n\tcoord x, y = $x, $y\n\t" +
                    "Pct x, y = $pctX, $pctY")
            return (pctX to pctY)
        }
    }
}