package eu.tutorials.kidsdrawingapp

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        drawingView.setSizeForBrush(10f)

        val ibBrushSize = findViewById<ImageButton>(R.id.ib_brush_size);
        ibBrushSize.setOnClickListener {
            showBrushSizeDialog()
        }
    }

    private fun showBrushSizeDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_brush_size)
        dialog.findViewById<ImageButton>(R.id.ib_small_brush).setOnClickListener {
            drawingView.setSizeForBrush(10f)
            dialog.dismiss()
        }
        dialog.findViewById<ImageButton>(R.id.ib_medium_brush).setOnClickListener {
            drawingView.setSizeForBrush(20f)
            dialog.dismiss()
        }
        dialog.findViewById<ImageButton>(R.id.ib_large_brush).setOnClickListener {
            drawingView.setSizeForBrush(30f)
            dialog.dismiss()
        }

        dialog.show()
    }

}