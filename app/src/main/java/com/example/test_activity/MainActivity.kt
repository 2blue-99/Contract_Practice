package com.example.test_activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var contactPickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 연락처 선택 런처 초기화
        contactPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.e("TAG", "onCreate: ${result.data?.data}")
            if (result.resultCode == Activity.RESULT_OK) {
                val contactUri: Uri? = result.data?.data
                contactUri?.let { uri ->
                    // 연락처 URI에서 데이터를 가져옴
                    val cursor = contentResolver.query(uri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            // 전화번호를 가져오기 위한 인덱스
                            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                            val contactName = it.getString(nameIndex) // 연락처 이름
                            val contactNumber = it.getString(numberIndex) // 연락처 전화번호
                            // 전화번호를 처리하거나 UI에 표시
                            Log.e("TAG", "name : $contactName / num : $contactNumber", )
                        }
                    }
                }
            }
        }

        findViewById<TextView>(R.id.myBtn).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            contactPickerLauncher.launch(intent)
        }
//        findViewById<TextView>(R.id.myText).text = "안녕하세 안녕하세 안녕하세 안녕하세 안녕하세".replace(" ", "\u00A0")
    }
}