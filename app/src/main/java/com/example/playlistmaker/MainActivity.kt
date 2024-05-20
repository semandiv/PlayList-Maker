package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchButton = findViewById<Button>(R.id.search_button)
        val libraryButton = findViewById<Button>(R.id.library_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        //вариант с анонимным классом
        val searchButtonClickListener: View.OnClickListener = object: View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали кнопку Поиск", Toast.LENGTH_LONG).show()
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        //вариант с лямбдой
        libraryButton.setOnClickListener{
            Toast.makeText(this@MainActivity, "Нажали кнопку Медиатека", Toast.LENGTH_LONG).show()
        }

        //вариант с лямбдой еще раз
        settingsButton.setOnClickListener{
            Toast.makeText(this@MainActivity, "Нажали кнопку Настройки", Toast.LENGTH_LONG).show()
        }
    }
}