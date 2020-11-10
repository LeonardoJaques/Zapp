package com.jaques.projetos.zapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jaques.projetos.zapp.R
import com.jaques.projetos.zapp.fragment.ContactsFragment
import com.jaques.projetos.zapp.fragment.TalksFragment
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbarMain)
        toolbar.title = "Zapp"
        setSupportActionBar(toolbar)


        val adapter = FragmentPagerItemAdapter(
            supportFragmentManager, FragmentPagerItems.with(this)
                .add("Conversas", TalksFragment::class.java)
                .add("Contatos", ContactsFragment::class.java)
                .create()
        )

        val viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        viewPager.adapter = adapter

        val viewPagerTab = findViewById<View>(R.id.viewPagerTab) as SmartTabLayout
        viewPagerTab.setViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.out_menu -> {
                logout()
                finish()
            }
            R.id.setting_menu -> openSettings()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun logout(){
        try {
            Firebase.auth.signOut()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun openSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}