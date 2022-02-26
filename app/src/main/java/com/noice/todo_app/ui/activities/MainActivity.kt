package com.noice.todo_app.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.noice.todo_app.R
import com.noice.todo_app.databinding.ActivityMainBinding
import com.noice.todo_app.ui.fragments.main_fragments.EditFragment
import com.noice.todo_app.ui.fragments.main_fragments.ListsRVAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bind: ActivityMainBinding
    private lateinit var actionbarDrawerToggle:ActionBarDrawerToggle
    private val bottomSheetEditFragment = EditFragment.newInstance()
    private var DummyTestListforListsinNavView = mutableListOf(
        Pair(1,"list 1"),
        Pair(2,"list 2"),
        Pair(3,"list 3")
    )
    private val listAdapter = ListsRVAdapter(DummyTestListforListsinNavView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        configureToolbar()
        configureNavigationView()
        configureFab()

    }

    private fun configureFab() {
        bind.fab.setOnClickListener {
            bottomSheetEditFragment.show(supportFragmentManager,"Edit Fragment")
        }
    }

    private fun configureNavigationView() {
        bind.usernameTv.text = "DANIEL"
        bind.navListsRv.layoutManager = LinearLayoutManager(this)
            //object : LinearLayoutManager(this){override fun canScrollVertically(): Boolean { return false }}

        bind.navListsRv.itemAnimator = null
        bind.navListsRv.adapter = listAdapter


        bind.navAddListEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                DummyTestListforListsinNavView.add(Pair(DummyTestListforListsinNavView.size,bind.navAddListEt.text.toString()))

                listAdapter.notifyItemInserted(DummyTestListforListsinNavView.size-1)

                val bottom: Int = bind.navListsRv.adapter?.itemCount?.minus(1) ?: 0
                bind.navListsRv.smoothScrollToPosition(bottom)

                bind.navAddListEt.setText("")
                return@setOnEditorActionListener true
            }

            false
        }
        bind.navAddListTil.setEndIconOnClickListener {
            DummyTestListforListsinNavView.add(Pair(DummyTestListforListsinNavView.size,bind.navAddListEt.text.toString()))

            listAdapter.notifyItemInserted(DummyTestListforListsinNavView.size-1)
            val bottom: Int = bind.navListsRv.adapter?.itemCount?.minus(1) ?: 0
            bind.navListsRv.smoothScrollToPosition(bottom)

            bind.navAddListEt.setText("")
        }


    }

    private fun configureToolbar() {
        setSupportActionBar(bind.toolbar)

        actionbarDrawerToggle = ActionBarDrawerToggle(this,bind.drawerLayout,R.string.nav_open,R.string.nav_close)
        bind.drawerLayout.addDrawerListener(actionbarDrawerToggle)
        actionbarDrawerToggle.syncState()

        supportActionBar?.title =""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_options_menu,menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(actionbarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}