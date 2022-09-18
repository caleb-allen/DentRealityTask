package xyz.wim.dentrealitytask

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import timber.log.Timber
import xyz.wim.dentrealitytask.data.CountryRepository
import xyz.wim.dentrealitytask.data.HomeManager
import xyz.wim.dentrealitytask.data.LocalCountryRepository
import xyz.wim.dentrealitytask.ui.details.DetailsViewModel
import xyz.wim.dentrealitytask.ui.main.MainViewModel
import xyz.wim.dentrealitytask.ui.map.MapViewModel

class DentRealityApplication : Application() {
    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val appModule = module {

            viewModel { MainViewModel() }
            viewModel { MapViewModel(get()) }
            viewModel { DetailsViewModel(get(), get(), get()) }
            single<HomeManager> { HomeManager(get(), get()) }
            single<CountryRepository> { LocalCountryRepository(get(), get()) }
            single<Moshi> {
                Moshi.Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
            }
        }

        startKoin {
            androidLogger()
            androidContext(this@DentRealityApplication)
            modules(appModule)
        }
    }
}