package eu.tutorials.kidsdrawingapp

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

        findViewById<ImageButton>(R.id.ib_brush_red).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_black).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_green).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_blue).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_cyan).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_purple).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_yellow).setOnClickListener(this::paintClicked)

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "user granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "user denied", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageButton>(R.id.ib_bg_image_picker).setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    fun paintClicked(v: View) {
        drawingView.setColorForBrush(v.tag as String)
    }
}