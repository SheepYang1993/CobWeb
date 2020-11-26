package me.sheepyang.cobweb

import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import me.sheepyang.cobwebview.CobWebView

class MainActivity : AppCompatActivity(), OnTouchListener, OnSeekBarChangeListener {
    private var mDrawerLayout: DrawerLayout? = null
    private var mFlDrawer: LinearLayout? = null
    private var mCobWebView: CobWebView? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mToolbar: Toolbar? = null
    private var mSeekbar1: SeekBar? = null
    private var mSeekbar2: SeekBar? = null
    private var mSeekbar3: SeekBar? = null
    private var mSeekbar4: SeekBar? = null
    private var mTextView1: TextView? = null
    private var mTextView2: TextView? = null
    private var mTextView3: TextView? = null
    private var mTextView4: TextView? = null
    private var mTextView5: TextView? = null
    private var mTextView6: TextView? = null
    private var mTextView7: TextView? = null
    private var mTextView8: TextView? = null
    private var mSeekbar5: SeekBar? = null
    private var mSeekbar6: SeekBar? = null
    private var mSeekbar7: SeekBar? = null
    private var mSeekbar8: SeekBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
        mSeekbar1!!.progress = 49
        mSeekbar2!!.progress = 4
        mSeekbar3!!.progress = 250
        mSeekbar4!!.progress = 150
        mSeekbar5!!.progress = 15
        mSeekbar6!!.progress = 15
        mSeekbar7!!.progress = 15
        mSeekbar8!!.progress = 50
    }

    private fun initView() {
        mTextView1 = findViewById<View>(R.id.textview1) as TextView
        mTextView2 = findViewById<View>(R.id.textview2) as TextView
        mTextView3 = findViewById<View>(R.id.textview3) as TextView
        mTextView4 = findViewById<View>(R.id.textview4) as TextView
        mTextView5 = findViewById<View>(R.id.textview5) as TextView
        mTextView6 = findViewById<View>(R.id.textview6) as TextView
        mTextView7 = findViewById<View>(R.id.textview7) as TextView
        mTextView8 = findViewById<View>(R.id.textview8) as TextView
        mSeekbar1 = findViewById<View>(R.id.seekbar1) as SeekBar
        mSeekbar2 = findViewById<View>(R.id.seekbar2) as SeekBar
        mSeekbar3 = findViewById<View>(R.id.seekbar3) as SeekBar
        mSeekbar4 = findViewById<View>(R.id.seekbar4) as SeekBar
        mSeekbar5 = findViewById<View>(R.id.seekbar5) as SeekBar
        mSeekbar6 = findViewById<View>(R.id.seekbar6) as SeekBar
        mSeekbar7 = findViewById<View>(R.id.seekbar7) as SeekBar
        mSeekbar8 = findViewById<View>(R.id.seekbar8) as SeekBar
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        mCobWebView = findViewById<View>(R.id.cob_web_view) as CobWebView
        mFlDrawer = findViewById<View>(R.id.left_drawer) as LinearLayout
        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close)
        mDrawerToggle!!.syncState()
    }

    private fun initListener() {
        mToolbar!!.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item1 -> {
                    mCobWebView!!.restart()
                    return@OnMenuItemClickListener true
                }
            }
            false
        })
        mSeekbar1!!.setOnTouchListener(this)
        mSeekbar2!!.setOnTouchListener(this)
        mSeekbar3!!.setOnTouchListener(this)
        mSeekbar4!!.setOnTouchListener(this)
        mSeekbar5!!.setOnTouchListener(this)
        mSeekbar6!!.setOnTouchListener(this)
        mSeekbar7!!.setOnTouchListener(this)
        mSeekbar8!!.setOnTouchListener(this)
        mSeekbar1!!.setOnSeekBarChangeListener(this)
        mSeekbar2!!.setOnSeekBarChangeListener(this)
        mSeekbar3!!.setOnSeekBarChangeListener(this)
        mSeekbar4!!.setOnSeekBarChangeListener(this)
        mSeekbar5!!.setOnSeekBarChangeListener(this)
        mSeekbar6!!.setOnSeekBarChangeListener(this)
        mSeekbar7!!.setOnSeekBarChangeListener(this)
        mSeekbar8!!.setOnSeekBarChangeListener(this)
        mDrawerLayout!!.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                mDrawerToggle!!.onDrawerSlide(drawerView, slideOffset)
            }

            override fun onDrawerOpened(drawerView: View) {
                mDrawerToggle!!.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
                mDrawerToggle!!.onDrawerClosed(drawerView)
            }

            override fun onDrawerStateChanged(newState: Int) {
                mCobWebView!!.clearTouchPoint()
                mDrawerToggle!!.onDrawerStateChanged(newState)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu) //加载menu文件到布局
        return true
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (v is SeekBar) mDrawerLayout!!.requestDisallowInterceptTouchEvent(true)
        return false
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.seekbar1 -> {
                mCobWebView!!.pointNum = progress + 1
                mTextView1!!.text = String.format("小点数量： %d", mCobWebView!!.pointNum)
            }
            R.id.seekbar2 -> {
                mCobWebView!!.acceleration = progress + 3
                mTextView2!!.text = String.format("加速度： %d", mCobWebView!!.acceleration)
            }
            R.id.seekbar3 -> {
                mCobWebView!!.maxDistance = progress + 20
                mTextView3!!.text = String.format("最大连线距离： %d", mCobWebView!!.maxDistance)
            }
            R.id.seekbar4 -> {
                mCobWebView!!.lineAlpha = progress
                mTextView4!!.text = String.format("连线透明度： %d", mCobWebView!!.lineAlpha)
            }
            R.id.seekbar5 -> {
                mCobWebView!!.lineWidth = progress
                mTextView5!!.text = String.format("连线粗细： %d", mCobWebView!!.lineWidth)
            }
            R.id.seekbar6 -> {
                mCobWebView!!.pointRadius = progress
                mTextView6!!.text = String.format("小点半径： %d", mCobWebView!!.pointRadius)
            }
            R.id.seekbar7 -> {
                mCobWebView!!.touchPointRadius = progress
                mTextView7!!.text = String.format("触摸点半径： %d", mCobWebView!!.touchPointRadius)
            }
            R.id.seekbar8 -> {
                mCobWebView!!.pullBackRange = progress
                mTextView8!!.text = String.format("引力强度： %d", mCobWebView!!.pullBackRange)
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}