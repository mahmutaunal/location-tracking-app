package com.mahmutalperenunal.locationtracking

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmutalperenunal.locationtracking.databinding.ActivityLocationTrackingListBinding

class LocationTrackingListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationTrackingListBinding

    //private lateinit var mainViewModelUsers: MainViewModelUsers

    //private val studentAdapter by lazy { LocationTrackingAdapter(applicationContext) }

    /*private lateinit var sharedPreferencesUserGroup: SharedPreferences
    private lateinit var sharedPreferencesAuthToken: SharedPreferences

    private var userToken: String = ""*/

    private lateinit var userIds: ArrayList<Int>
    private lateinit var userNames: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationTrackingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup toolbar
        binding.locationTrackingListToolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(binding.locationTrackingListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        /*sharedPreferencesUserGroup = getSharedPreferences("userGroup", Context.MODE_PRIVATE)
        sharedPreferencesAuthToken = getSharedPreferences("authToken", MODE_PRIVATE)

        userToken = sharedPreferencesAuthToken.getString("token", null).toString()*/


        userIds = arrayListOf()
        userNames = arrayListOf()


        checkConnection()

        //listStudentData()

        //onCLickProcess()


        //refresh page
        binding.locationTrackingListSwipeRefreshLayout.setOnRefreshListener {
            refreshListStudentPage()
            binding.locationTrackingListSwipeRefreshLayout.isRefreshing = false
        }
    }


    /**
     * searchView
     */
    /*@Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val menuItem = menu?.findItem(R.id.searchId)
        val searchView: SearchView = MenuItemCompat.getActionView(menuItem) as SearchView
        searchView.isIconified = true

        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.queryHint = "User Search"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                studentAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                studentAdapter.filter.filter(newText)
                return true
            }

        })


        searchView.setOnCloseListener {
            false
        }

        return true
    }*/


    /**
     * check network connection
     */
    private fun checkConnection() {

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            if (!isConnected) {
                AlertDialog.Builder(this, R.style.CustomAlertDialog)
                    .setTitle("İnternet Bağlantısı Yok")
                    .setMessage("Lütfen internet bağlantınızı kontrol edin!")
                    .setIcon(R.drawable.without_internet)
                    .setNegativeButton("Tamam") { dialog, _ ->
                        checkConnection()
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }

    }


    /**
     * get user name
     */
    /*private fun listUserData() {

        setupStudentRecyclerview()

        val repository = RepositoryUsers()
        val mainViewModelFactory = MainViewModelFactoryUsers(repository)
        mainViewModelUsers = ViewModelProvider(this, mainViewModelFactory)[MainViewModelUsers::class.java]
        mainViewModelUsers.getUsers("Token $userToken", "Ogrenci")
        mainViewModelUsers.getUsersRepository.observe(this) { response ->
            if (response.isSuccessful) {
                binding.locationTrackingListProgressBar.visibility = View.GONE
                binding.locationTrackingListDeniedUserImageView.visibility = View.GONE
                binding.locationTrackingListDeniedUserTextView.visibility = View.GONE
                response.body()?.let { studentAdapter.setData(it) }
                val size = response.body()!!.size - 1
                userNames.clear()
                userIds.clear()
                response.body()?.let {
                    for (item in 0..size) {
                        userNames.add(response.body()!![item].name + " " + response.body()!![item].surname)
                        userIds.add(response.body()!![item].id)
                    }
                }
            } else {
                Toast.makeText(applicationContext, "İşlem Başarısız!", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.locationTrackingListDeniedUserImageView.visibility = View.VISIBLE
                binding.locationTrackingListDeniedUserTextView.visibility = View.VISIBLE
                Log.e("Grade Get Student Error", response.code().toString())
            }
        }
    }*/


    /*private fun onCLickProcess() {
        userAdapter.setOnItemClickListener(object : LocationTrackingAdapter.OnItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun onItemClick(position: Int) {
                val intent = Intent(applicationContext, LocationTrackingInfoActivity::class.java)
                intent.putExtra("Student ID", userIds[position])
                intent.putExtra("Student Names", userNames[position])
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
    }*/


    /*private fun setupStudentRecyclerview() {
        binding.locationTrackingListRecyclerView.adapter = studentAdapter
        binding.locationTrackingListRecyclerView.layoutManager = LinearLayoutManager(this)
    }*/


    private fun refreshListStudentPage() {
        val intent = Intent(applicationContext, LocationTrackingListActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}