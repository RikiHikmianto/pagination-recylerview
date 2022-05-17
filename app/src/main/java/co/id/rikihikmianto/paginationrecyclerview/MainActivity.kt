package co.id.rikihikmianto.paginationrecyclerview

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.id.rikihikmianto.paginationrecyclerview.databinding.ActivityMainBinding
import co.id.rikihikmianto.paginationrecyclerview.model.DataItem
import co.id.rikihikmianto.paginationrecyclerview.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var userList: MutableList<DataItem?>? = ArrayList()
    private var page = 1
    private var totalPage = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)
        binding.swipeRefresh.setOnRefreshListener(this)
        setupRecylerView()
        getuser(false)

        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val itemCount = layoutManager.itemCount
                val visibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if (dy > 0) {
                    if (!isLoading && page < totalPage) {
                        if (itemCount + visibleItem >= total) {
                            page++
                            getuser(false)
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun getuser(isOnRefresh: Boolean) {
        Handler().postDelayed({ // delete for this
            isLoading = true
            if (!isOnRefresh) binding.progressBar.visibility = View.VISIBLE
            val hashMap = HashMap<String, String>()
            hashMap["page"] = page.toString()
            ApiConfig.instance.getUsers(hashMap).enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    totalPage = response.body()?.totalPages!!
                    val listUserResponse = response.body()?.data
                    if (listUserResponse != null) {
                        adapter.addList(listUserResponse)
                    }
                    if (page == totalPage) {
                        binding.progressBar.visibility = View.GONE
                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    isLoading = false
                    binding.swipeRefresh.isRefreshing = false
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "onFailure: ${t.message}")
                }

            })
        }, 3000)
    }

    private fun setupRecylerView() {
        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = layoutManager
            adapter = UserAdapter(userList)
            rvUsers.adapter = adapter
        }
    }

    override fun onRefresh() {
        adapter.clear()
        page = 1
        getuser(true)
    }
}