package com.example.e03builduiwithlayouteditor2

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val firstnames = arrayOf("Renato", "Rosangela", "Tim", "Bartol", "Jeannette")
    val lastnames = arrayOf("Ksenia", "Metzli", "Asuncion", "Zemfina", "Giang")
    val jobtitles = arrayOf(
        "District Quality Coordinator",
        "International Intranet Representative",
        "District Intranet Administrator",
        "Dynamic Research Manager",
        "Central Infrastructure Consultant"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // show first employee data
        showEmployeeData(0)
    }

    fun numberClicked(view: View?) {
        // get clicked view as a textview and its text as a string
        val text = (view as TextView).text.toString()
        // modify string to int and decrease by one (array's start position 0)
        val int = text.toInt() - 1
        // show selected employee data in UI, call earlier made function
        showEmployeeData(int)
    }

    // function displays employees data in UI
    fun showEmployeeData(index: Int) {
        // find TextView's and ImageView from the UI layout file
        val firstnameTextView = findViewById<TextView>(R.id.firstnameTextView)
        val lastnameTextView = findViewById<TextView>(R.id.lastnameTextView)
        val jobtitleTextView = findViewById<TextView>(R.id.jobtitleTextView)
        val employeeInfoTextView = findViewById<TextView>(R.id.employeeInfoTextView)

        // Update TextView texts
        firstnameTextView.text = firstnames[index]
        lastnameTextView.text = lastnames[index]
        jobtitleTextView.text = jobtitles[index]

        // info is
        employeeInfoTextView.text = getString(R.string.employee_info_text, lastnames[index], firstnames[index], getString(R.string.basic_text))

        // set correct employee image based on index
        val id = when (index) {
            0 -> R.drawable.employee1
            1 -> R.drawable.employee2
            2 -> R.drawable.employee3
            3 -> R.drawable.employee4
            4 -> R.drawable.employee5
            else -> R.drawable.employee1
        }
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(id)
    }
}
