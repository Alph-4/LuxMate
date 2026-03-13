package org.julienjnnqin.luxmateapp.core.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Represents a CSS-like drop shadow.
 *
 * Only [radius] (mapped to elevation) and [color] are applied by [dropShadow].
 * The [offset] and [spread] fields are retained for API completeness but are
 * currently not supported by the underlying [Modifier.shadow] implementation.
 */
data class Shadow(
    val radius: Dp = 0.dp,
    val spread: Dp = 0.dp,
    val color: Color = Color.Black,
    val offset: DpOffset = DpOffset.Zero
)

/**
 * Applies a drop shadow to a composable, approximating CSS box-shadow behaviour
 * using the standard [Modifier.shadow] API (compatible with all KMP targets).
 *
 * **Note:** The [Shadow.offset] and [Shadow.spread] parameters are not supported by the
 * underlying [Modifier.shadow] API and are therefore ignored. Only [Shadow.radius] (mapped
 * to elevation) and [Shadow.color] are applied.
 *
 * @param shape  The outline shape of the shadow.
 * @param shadow Shadow parameters (radius and color are applied; offset and spread are ignored).
 */
fun Modifier.dropShadow(
    shape: Shape,
    shadow: Shadow
): Modifier = this.shadow(
    elevation = shadow.radius,
    shape = shape,
    ambientColor = shadow.color,
    spotColor = shadow.color
)
