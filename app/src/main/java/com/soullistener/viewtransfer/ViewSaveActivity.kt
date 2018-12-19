package com.soullistener.viewtransfer

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_viewsave.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class ViewSaveActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    var permission = false

    var perms = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewsave)

        if (EasyPermissions.hasPermissions(this, perms.toString())) {

            // 已经申请过权限，做想做的事
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(
                this, perms[0], 0,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        textview.setOnClickListener {

            if (!permission){
                Toast.makeText(this@ViewSaveActivity,"未获取权限",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val file = File(Environment.getExternalStorageDirectory(),"viewsave.txt")

            val viewroot = findViewById<DragView>(R.id.ll_viewsave)

            val viewList = ViewTransUtils.getAllChildViews(viewroot)
            val list = ViewTransUtils.viewTrans2Bean(viewList)
            val isSuccessed = ViewTransUtils.bean2File(file,list)

            if (isSuccessed) {
                val intent = Intent(this@ViewSaveActivity, ViewRestoreAvtivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "写入文件失败", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        permission = true
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        permission = false
    }


}

