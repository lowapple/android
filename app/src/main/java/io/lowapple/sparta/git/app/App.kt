package io.lowapple.sparta.git.app

import android.annotation.SuppressLint
import android.app.Application
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@SuppressLint("Registered")
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        // Koin 시작
        startKoin {
            this.androidContext(applicationContext)
            this.modules(appModule)
        }
    }
}