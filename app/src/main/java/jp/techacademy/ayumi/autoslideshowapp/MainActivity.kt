package jp.techacademy.ayumi.autoslideshowapp

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.database.Cursor
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100

        //タイマー
    private val TIMER_SEC = 2000

    private var mTimer: Timer? = null
    private val mHandler = Handler()
    private var mCursor: Cursor? = null

    private var runflg = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
               mCursor = getCursor()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            mCursor = getCursor()
        }
    }

    //再生停止ボタン
    buttonRun.setOnClickListener
    {
        if (runflg) {
            mTimer?.cancel()
            mTimer = Timer()
            mTimer!!.Schdule(object : TimerTask) {
                override fun run() {
                    mHandler.post {
                        if (mCursor != null)
                            showImage()
                        //最後まで来たら最初から
                        if (!mCursor!!.moveToNext()) mCursor!!.moveToFirst()
                    }
                }
            }
        }, TIMER_SEC.toLong(),TIMER_SEC.toLong())
    }
}
    runflg = !runflg
    setBtnEnable()
}

//戻るボタン
buttonBack.setOnclickListener {
    it:View!
    if (mCursor != null) {
        if (!mCursor!!.moveToprevious()) mCursor!!.moveToLast()
        showImage()
    }
}

//進ボタン
buttonNext.setOnclickListener {
    it:view!
    if (mCursor != null) {
        if (!mCursor!!.moveToPrevious()) mCursor!!.moveToLast()
        showImage()
    }
}
    setBtnEnable()
}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目（null = 全項目）
            null, // フィルタ条件（null = フィルタなし）
            null, // フィルタ用パラメータ
            null // ソート (nullソートなし）
        )

        return if (cursor!!.moveToFirst()) {
            cursor
        } else{
            cursor.close()
            null
       }
}

//immageの表示
private fun showImage()
    if(mCusor != null){
        val  fieldIndex = mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
        val id = mCursor!!.getLong(fieldIndex)
        val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id)
        imageView.setImageURL(imageUri)
    }
}
        //ボタンの表示有効有効無効を切り替える
private fun setBtnEnable() {
            buttonRun.text = if (runflg) "停止" else "再生"
            buttonBack.isEnabled = !runflg
            buttonNext.isEnabled = !runflg
        }
}

