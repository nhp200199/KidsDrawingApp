package eu.tutorials.kidsdrawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var scope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        drawingView.setSizeForBrush(10f)

        val ibBrushSize = findViewById<ImageButton>(R.id.ib_brush_size);
        ibBrushSize.setOnClickListener {
            showBrushSizeDialog()
        }

        scope = CoroutineScope(Dispatchers.Main.immediate)

        findViewById<ImageButton>(R.id.ib_brush_red).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_black).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_green).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_blue).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_cyan).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_purple).setOnClickListener(this::paintClicked)
        findViewById<ImageButton>(R.id.ib_brush_yellow).setOnClickListener(this::paintClicked)

        findViewById<ImageButton>(R.id.ib_undo).setOnClickListener {
            drawingView.undoLastPath()
        }

        findViewById<ImageButton>(R.id.ib_save).setOnClickListener {
            scope.launch {
                val saveBitmapFile =
                    saveBitmapFile(getBitmapFromView(findViewById<FrameLayout>(R.id.fl_drawing_view_container)))
                Toast.makeText(this@MainActivity, saveBitmapFile, Toast.LENGTH_SHORT).show()
            }
        }

        val backgroundImagePickLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                findViewById<ImageView>(R.id.iv_drawing_view_img_bg).setImageURI(it.data?.data)
            }
        }

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            var allGranted = true;
            results.entries.forEach {
                val permission = it.key
                val permissionGranted = it.value

                if (permissionGranted) {
                    Toast.makeText(this, "user granted $permission}", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "user denied $permission}", Toast.LENGTH_SHORT).show()
                    allGranted = false
                }
            }
            if (allGranted) {
                backgroundImagePickLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            }
            else {
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
            }
//            if (results) {
//                Toast.makeText(this, "user granted", Toast.LENGTH_SHORT).show()
//                backgroundImagePickLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
//            }
//            else {
//                Toast.makeText(this, "user denied", Toast.LENGTH_SHORT).show()
//            }
        }

        findViewById<ImageButton>(R.id.ib_bg_image_picker).setOnClickListener {
            requestPermissionLauncher.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ))
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

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)

        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        }
        else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)

        return returnedBitmap
    }

    private suspend fun saveBitmapFile(bitmap: Bitmap?): String {
        return withContext(Dispatchers.IO) {
            if (bitmap != null) {
                try {
                    val bytesStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytesStream)

                    val f = File(getExternalFilesDir(null)?.absolutePath + File.separator + "KidDrawingApp_" + System.currentTimeMillis() / 1000 + ".png")
                    val fo = FileOutputStream(f)
                    fo.write(bytesStream.toByteArray())
                    fo.close()
//                    Toast.makeText(this@MainActivity, f.absolutePath, Toast.LENGTH_SHORT).show()
                    f.absolutePath
                }
                catch (ex: Exception) {
//                    Toast.makeText(this@MainActivity, "something went wrong when save file", Toast.LENGTH_SHORT).show()
                    "exception"
                }
            }
            else "bitmap == null"
        }
    }

    fun paintClicked(v: View) {
        drawingView.setColorForBrush(v.tag as String)
    }
}