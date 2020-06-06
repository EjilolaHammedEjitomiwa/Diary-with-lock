package com.geodeveloper.mydiary

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.geodeveloper.mydiary.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialogue.view.*
import kotlinx.android.synthetic.main.note_design_ticket.view.*

class MainActivity : AppCompatActivity() {
    private val noteList = ArrayList<noteModel>()
    private var adapter: noteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //onclick listener for fab icon which open the addNote class
        fab.setOnClickListener {
            val intent = Intent(this, AddNotes::class.java)
            startActivity(intent)
        }
        //support for the custom toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //always load from the database
        loadFromDataBase("%")
    }


    override fun onResume() {
        super.onResume()
        loadFromDataBase("%")
    }

    override fun onStart() {
        super.onStart()
        loadFromDataBase("%")
    }

    //Thid function read data from the database and load it on the main page
    private fun loadFromDataBase(title: String) {
        //instance of the dbManager class
        val dbManager = DbManager(this)
        //select the columns
        val projections = arrayOf("ID", "title", "description", "date")
        //selection arguments
        val selectionArgs = arrayOf(title)
        //to query the database
        val cursor = dbManager.query(projections, "title like ?", selectionArgs, "ID")

        noteList.clear()

        //start reading from the last to first
        if (cursor.moveToLast()) {
            do {
                //fetch data from the column ID,title,dexcription and date and add to the notelist ArrayList
                val Id = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("title"))
                val Description = cursor.getString(cursor.getColumnIndex("description"))
                val date = cursor.getString(cursor.getColumnIndex("date"))

                noteList.add(noteModel(Id, Title, Description, date))
            } while (cursor.moveToPrevious())
        }
        //Adapter for the arrayList
        adapter = noteAdapter(this, noteList)
        list_view.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }
    //override funtion to create the option menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        //SearchView details
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.queryHint = "Search by title"
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadFromDataBase("%$query%")
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadFromDataBase("%")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
    //override funtion to listen to any menu item select
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_about -> {
                val i = Intent(this, About::class.java)
                startActivity(i)
            }
            R.id.menu_moreapps -> {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://play.google.com/store/apps/developer?id=Ejilola+Hammed+Ejitomiwa")
                startActivity(openURL)
            }
            R.id.menu_rate -> {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://play.google.com/store/apps/developer?id=Ejilola+Hammed+Ejitomiwa")
                startActivity(openURL)
            }
            R.id.menu_reset ->{
                val i = Intent(this,OldPassword::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //adapter class for the custom listView
    inner class noteAdapter(val context: Context, val noteList: ArrayList<noteModel>) :BaseAdapter() {

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val note = noteList[p0]
            //to inflate the note_design_layout_ticket
            val view = LayoutInflater.from(context).inflate(R.layout.note_design_ticket, null)
            //set the data
            view.title_textView.text = note.title
            view.content_textView.text = note.desc
            view.date_textView.text = note.date

            //onclick listener for the delete icon
            view.icon_del.setOnClickListener {
                //custom dialogue details
                val mDialogueView = LayoutInflater.from(context).inflate(R.layout.custom_dialogue, null)
                val mBuilder = AlertDialog.Builder(context).setView(mDialogueView)
                val mAlertDualogue = mBuilder.show()
                //onclick listener for the dialogue ok button
                mDialogueView.dialogue_ok.setOnClickListener {
                    //This initialize the database mananger class
                    val dbManager = DbManager(context)
                    //The selection is an array of ID of the selected view
                    val selections = arrayOf(note.id.toString())
                    //This select like the colum ID
                    dbManager.delete("id=?", selections)

                    //This reload the view
                    loadFromDataBase("%")
                    mAlertDualogue.dismiss()
                    Toast.makeText(context, "Note deleted", Toast.LENGTH_LONG).show()
                }
                //dismiss dialogue box on cancel
                mDialogueView.dialogue_cancel.setOnClickListener {
                    mAlertDualogue.dismiss()
                }


            }
            //onclick on the note send intent extras to addNote activity
            view.design_linearLayout.setOnClickListener {
                val i = Intent(context, AddNotes::class.java)
                i.putExtra("noteId", note.id)
                i.putExtra("noteTitle", note.title)
                i.putExtra("noteDescription", note.desc)
                startActivity(i)
            }
            return view
        }

        override fun getItem(p0: Int): Any {
            return noteList[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return noteList.size
        }
    }
}
