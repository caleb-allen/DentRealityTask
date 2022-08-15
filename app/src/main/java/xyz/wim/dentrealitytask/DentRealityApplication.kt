package xyz.wim.dentrealitytask

import android.app.Application
import android.content.res.Resources
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import timber.log.Timber
import xyz.wim.dentrealitytask.data.CountryRepository
import xyz.wim.dentrealitytask.data.LocalCountryRepository
import xyz.wim.dentrealitytask.ui.main.MainViewModel
import xyz.wim.dentrealitytask.ui.map.MapViewModel

class DentRealityApplication : Application() {
    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val appModule = module {

//            single<HelloRepository> { HelloRepositoryImpl() }

//            factory<Resources>{ androidContext()}
            // MyViewModel ViewModel
            viewModel { MainViewModel() }
            viewModel { MapViewModel(get()) }
            single<CountryRepository> { LocalCountryRepository(get(), get()) }
            single<Moshi> { Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build() }
        }

        startKoin {
            androidLogger()
            androidContext(this@DentRealityApplication)
            modules(appModule)
        }
    }
}