package br.eng.alvloureiro.heroes.extensions

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import br.eng.alvloureiro.heroes.HeroesApplication
import br.eng.alvloureiro.heroes.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.experimental.CancellationException
import kotlinx.coroutines.experimental.CoroutineScope
import java.text.SimpleDateFormat


val AppCompatActivity.app: HeroesApplication get() = application as HeroesApplication

val Context.app get() = applicationContext as HeroesApplication

val Context.connectivityManager get() = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.isNetworkAvailable(): Boolean {
    val networkInfo = connectivityManager.activeNetworkInfo

    return networkInfo != null && networkInfo.isConnected
}

fun View.show() {
    context?.let {
        startAnimation(AnimationUtils.loadAnimation(it, R.anim.fade_in))
    }

    if (visibility == View.GONE)
        visibility = View.VISIBLE
}

fun View.hide() {
    context?.let {
        startAnimation(AnimationUtils.loadAnimation(it, R.anim.fade_out))
    }

    if (visibility == View.VISIBLE)
        visibility = View.GONE
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.launch(
    options: Bundle? = null, noinline init: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.init()
    if (options != null) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun<reified T: RecyclerView> AppCompatActivity.withListView(noinline init: T.() -> Unit) {
    val listView = findViewById<T>(R.id.heroList)
    listView.init()
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)

}

fun AppCompatImageView.loadHeroImageFromUrl(url: String?) {
    if (url != null) {
        val imageUrlXLarge = "$url.jpg"
        Log.d("Picasso Load Image: ", imageUrlXLarge)
        Picasso.with(context).load(imageUrlXLarge.toUri()).into(this)
    }
}

fun String.toUri(): Uri {
    return Uri.parse(this)
}

inline fun<reified T> AppCompatActivity.getParam(modelName: String): T {
    return intent.extras.get(modelName) as T
}

suspend fun CoroutineScope.tryCatch(
    tryBlock: suspend CoroutineScope.() -> Unit,
    catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
    handleCancellationExceptionManually: Boolean = false) {
    try {
        tryBlock()
    } catch (e: Throwable) {
        if (e !is CancellationException || handleCancellationExceptionManually) {
            catchBlock(e)
        } else {
            throw e
        }
    }
}

suspend fun CoroutineScope.tryCatchFinally(
    tryBlock: suspend CoroutineScope.() -> Unit,
    catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
    finallyBlock: suspend CoroutineScope.() -> Unit,
    handleCancellationExceptionManually: Boolean = false) {

    var caughtThrowable: Throwable? = null

    try {
        tryBlock()
    } catch (e: Throwable) {
        if (e !is CancellationException || handleCancellationExceptionManually) {
            catchBlock(e)
        } else {
            caughtThrowable = e
        }
    } finally {
        if (caughtThrowable is CancellationException && !handleCancellationExceptionManually) {
            throw caughtThrowable
        } else {
            finallyBlock()
        }
    }
}

suspend fun CoroutineScope.tryFinally(
    tryBlock: suspend CoroutineScope.() -> Unit,
    finallyBlock: suspend CoroutineScope.() -> Unit,
    suppressCancellationException: Boolean = false) {

    var caughtThrowable: Throwable? = null

    try {
        tryBlock()
    } catch (e: CancellationException) {
        if (!suppressCancellationException) {
            caughtThrowable = e
        }
    } finally {
        if (caughtThrowable is CancellationException && !suppressCancellationException) {
            throw caughtThrowable
        } else {
            finallyBlock()
        }
    }
}
