package com.yogiw.tubessisfo.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.yogiw.tubessisfo.Fragment.PendingFragment
import com.yogiw.tubessisfo.R
import com.yogiw.tubessisfo.Fragment.ScheduleFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        setupViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)


        supportActionBar?.title = "Jadwal Kerja"
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val bundle = Bundle()
        bundle.putString("id_karyawan", "1")

        val schedule = ScheduleFragment()
        schedule.arguments = bundle

        val pending = PendingFragment()
        pending.arguments = bundle

        adapter.addFragment(schedule, "Schedule")
        adapter.addFragment(pending, "Pending Request")

        viewPager.adapter = adapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = mutableListOf<Fragment>()
        private val mFragmentTitleList = mutableListOf<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList.get(position)
        }
    }
}
