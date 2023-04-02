package org.hyperskill.secretdiary

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class Note(){
    var date: String = ""
    var txt: String =""
}

fun printNote(noteList: ArrayDeque<Note>): String{
    var res = ""
    for (el in noteList){
        res += el.date
        res += "\n"
        res += el.txt
        res += "\n"
        res += "\n"
    }
    return res
}

fun fromSp(str: String):ArrayDeque<Note>{
    var list = str.split("\n\n")
    var res =  ArrayDeque<Note>()
    for (el in list) {
        var myNote = Note()
        var list2 = el.split("\n")
        myNote.date = list2[0].toString()
        myNote.txt = list2[1].toString()
        res.add(myNote)
    }
    return res
}





class MainActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //var sharedPreferences: SharedPreferences = getPreferences(Context.MODE_PRIVATE)

        var noteList = ArrayDeque<Note>()

        val btnSave: Button = findViewById(R.id.btnSave)
        val tvDiary: TextView = findViewById(R.id.tvDiary)
        val etNewWriting: EditText = findViewById(R.id.etNewWriting)
        val btnundo: Button = findViewById(R.id.btnUndo)

        //var sp = getSharedPreferences("mySPNote", Context.MODE_PRIVATE)
        var sp = getSharedPreferences("PREF_DIARY", Context.MODE_PRIVATE)
        val editor = sp.edit()

        var txtfromSp = sp.getString("KEY_DIARY_TEXT", "" ).toString()
       // Toast.makeText(this, txtfromSp, Toast.LENGTH_SHORT).show()
        if (txtfromSp != "") {
            noteList = fromSp(txtfromSp)
            tvDiary.text = printNote(noteList).trim()
        }



        btnSave.setOnClickListener {
            var txt = etNewWriting.text
            val regex = Regex("^\\s*$")
            if (txt.toString() == "" || regex.matches(txt.toString())) {
                Toast.makeText(this, "Empty or blank input cannot be saved", Toast.LENGTH_SHORT).show()
                etNewWriting.setText("")
            } else {
                var time = Clock.System.now().toEpochMilliseconds()
                var t = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(time)
                    .toString()

                var not = Note()
                not.date = t
                not.txt = txt.toString()
                noteList.addFirst(not)
                tvDiary.text = printNote(noteList).trim()
                etNewWriting.setText("")

                var txtForSp = tvDiary.text.toString()

                editor.putString("KEY_DIARY_TEXT", txtForSp)

                editor.commit()
                editor.apply()
            }


        }

        val dialogUndo = AlertDialog.Builder(this)
            .setTitle("Remove last note")
            .setMessage("Do you really want to remove the last writing? This operation cannot be undone!")
            .setPositiveButton("YES") { _, _ ->
                if (noteList.size != 0) {
                    noteList.removeFirst()
                    tvDiary.text = printNote(noteList).trim()
                    var txtForSp = tvDiary.text.toString()
                    editor.putString("listNote", txtForSp)
                    editor.commit()
                    editor.apply()
                }

            }
            .setNegativeButton("NO"){_, _ ->
                etNewWriting.setText("")

            }
            .create()


        btnundo.setOnClickListener {
            dialogUndo.show()

        }






    }

}