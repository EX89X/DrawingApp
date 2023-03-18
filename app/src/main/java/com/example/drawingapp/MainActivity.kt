package com.example.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    private var drawingView:DrawingView?=null
    private var imgButtonCurrentPaint:ImageButton?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView=findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())

        val linearLayoutPaintColor=findViewById<LinearLayout>(R.id.ll_paint_colors)

        imgButtonCurrentPaint=linearLayoutPaintColor[2]as ImageButton

        imgButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
        )

        val ib_brush: ImageButton =findViewById(R.id.ib_brush)
        ib_brush.setOnClickListener {
            showBrushSizeChooserDialog()

        }
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog= Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size")

        val smallBtn:ImageButton=brushDialog.findViewById(R.id.small_brush)
        smallBtn.setOnClickListener {
            drawingView!!.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()


    val mediumBtn:ImageButton=brushDialog.findViewById(R.id.medium_brush)
        mediumBtn.setOnClickListener {
        drawingView!!.setSizeForBrush(20.toFloat())
        brushDialog.dismiss()
    }
    brushDialog.show()


        val largeBtn:ImageButton=brushDialog.findViewById(R.id.large_brush)
        largeBtn.setOnClickListener {
            drawingView!!.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
}

    fun paintClicked(view: View){
       if(view !==  imgButtonCurrentPaint){
           val imageButton = view as ImageButton
           val colorTag=imageButton.tag.toString()
           drawingView?.setColor(colorTag)
           imgButtonCurrentPaint!!.setImageDrawable(
               ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
           )

           imgButtonCurrentPaint=view
       }
    }
}