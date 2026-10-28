package com.example.poolclock

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent { PoolClock() }
    }
}

// Le 4 lancette sono sfasate di 15 secondi, in senso orario: nero, verde, rosso, giallo
private val hands = listOf(
    0.0 to Color(0xFF111111),   // nero
    15.0 to Color(0xFF1B8A2E),  // verde
    30.0 to Color(0xFFE5231B),  // rosso
    45.0 to Color(0xFFF2B01E)   // giallo
)

@Composable
fun PoolClock() {
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var running by remember { mutableStateOf(true) }

    LaunchedEffect(running) {
        if (running) {
            var last = withFrameNanos { it }
            while (true) {
                val now = withFrameNanos { it }
                elapsedMs += (now - last) / 1_000_000
                last = now
            }
        }
    }

    val textPaint = remember {
        Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            color = android.graphics.Color.BLACK
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { running = !running },       // tocco: pausa / riprendi
                    onLongPress = { elapsedMs = 0L }       // pressione lunga: azzera
                )
            }
    ) {
        val c = center
        val r = min(size.width, size.height) / 2f

        // Quadrante
        drawCircle(Color.White, radius = r, center = c)

        // Tacche
        for (i in 0 until 60) {
            val a = Math.toRadians(i * 6.0 - 90.0)
            val major = i % 5 == 0
            val outer = r * 0.97f
            val inner = if (major) r * 0.86f else r * 0.91f
            drawLine(
                Color.Black,
                Offset(c.x + (cos(a) * inner).toFloat(), c.y + (sin(a) * inner).toFloat()),
                Offset(c.x + (cos(a) * outer).toFloat(), c.y + (sin(a) * outer).toFloat()),
                strokeWidth = if (major) r * 0.035f else r * 0.015f
            )
        }

        // Numeri 5..60
        textPaint.textSize = r * 0.16f
        for (n in 1..12) {
            val a = Math.toRadians(n * 30.0 - 90.0)
            val tr = r * 0.70
            val x = c.x + (cos(a) * tr).toFloat()
            val y = c.y + (sin(a) * tr).toFloat() + textPaint.textSize / 3f
            drawContext.canvas.nativeCanvas.drawText((n * 5).toString(), x, y, textPaint)
        }

        // Lancette
        val seconds = (elapsedMs / 1000.0)
        for ((offset, color) in hands) {
            val a = Math.toRadians(((seconds + offset) % 60.0) * 6.0 - 90.0)
            val end = Offset(
                c.x + (cos(a) * r * 0.82).toFloat(),
                c.y + (sin(a) * r * 0.82).toFloat()
            )
            drawLine(color, c, end, strokeWidth = r * 0.055f, cap = StrokeCap.Round)
        }

        // Perno centrale
        drawCircle(Color.White, radius = r * 0.07f, center = c)
        drawCircle(Color.Black, radius = r * 0.07f, center = c, style = Stroke(width = r * 0.012f))

        // Indicatore di pausa
        if (!running) {
            drawCircle(Color(0x88000000), radius = r * 0.12f, center = Offset(c.x, c.y + r * 0.35f))
        }
    }
}
