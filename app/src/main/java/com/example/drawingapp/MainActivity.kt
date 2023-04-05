package com.example.drawingapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get


class MainActivity : AppCompatActivity() {

    private var drawingView:DrawingView?=null
    private var imgButtonCurrentPaint:ImageButton?=null
    val openGallaryLauncher:ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result->
                if(result.resultCode== RESULT_OK && result.data!=null){
                    val ImageBackGround:ImageView=findViewById(R.id.iv_background)
                    ImageBackGround.setImageURI(result.data?.data)
                }
            }

     val requsetPermission:ActivityResultLauncher<Array<String>> =
             registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
                 permissions->
                 permissions.entries.forEach{
                     val permissionName=it.key
                     val isGranted=it.value
                     if(isGranted){
                        Toast.makeText(this@MainActivity,"Permission granted now you can read the storage files."
                            ,Toast.LENGTH_LONG).show()

                         val pickIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                         openGallaryLauncher.launch(pickIntent)
                     }else{
                         if(permissionName==android.Manifest.permission.READ_EXTERNAL_STORAGE){
                             Toast.makeText(this@MainActivity,"Oops You Just denied the Permission ."
                                 ,Toast.LENGTH_LONG).show()
                         }
                     }
                 }
             }

    private fun requsetStoragePermission(){
         if(ActivityCompat.shouldShowRequestPermissionRationale(
                 this,
                 android.Manifest.permission.READ_EXTERNAL_STORAGE
         )){
             showRationalDialog("kids Drawing App","Kids Drawing App"+"" +
                     "need to Access Your External Storage")
         }else{
             requsetPermission.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
         }
    }

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

        val ibUndo: ImageButton =findViewById(R.id.ib_undo)
        ibUndo.setOnClickListener {
                 drawingView?.onClickUndo()
        }

        val ibGallery:ImageButton=findViewById(R.id.ib_gallery)
        ibGallery.setOnClickListener {
           requsetStoragePermission()
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
       if(view !== imgButtonCurrentPaint){
           val imageButton = view as ImageButton
           val colorTag=imageButton.tag.toString()
           drawingView?.setColor(colorTag)
           imgButtonCurrentPaint!!.setImageDrawable(
               ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
           )

           imgButtonCurrentPaint=view
       }
    }
    private fun showRationalDialog(
        title:String,
        message:String,
    ){
        val builder: AlertDialog.Builder= AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
        builder.create().show()
    }
}