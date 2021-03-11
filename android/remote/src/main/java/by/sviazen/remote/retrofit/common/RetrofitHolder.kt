package by.sviazen.remote.retrofit.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.*


@Singleton
class RetrofitHolder @Inject constructor(
    @ApplicationContext private val appContext: Context ) {

    val retrofit by lazy {
        val okhttpClient = OkHttpClient
            .Builder()
            // .configForHttps()  // TODO: Unconmment when HTTPS is supported
            .build()

        // TODO: Change to HTTPS when HTTPS is supported
        Retrofit.Builder()
            .baseUrl("http://$HOST/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient)
            .build()
    }


    /** Configures the [OkHttpClient] to accept the Sviazen server certificate. **/
    private fun OkHttpClient.Builder.configForHttps(): OkHttpClient.Builder {
        val certFact = CertificateFactory.getInstance("X.509")
        val certificate = appContext.assets.open("crt/sviazen.crt").use { istream ->
            certFact.generateCertificate(istream)
        } as X509Certificate

        val keystore = KeyStore.getInstance(KeyStore.getDefaultType())
        keystore.load(null, null)
        keystore.setCertificateEntry("sviazen", certificate)

        val trustFact = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustFact.init(keystore)
        val trustMan = trustFact.trustManagers.find { man ->
            man is X509TrustManager
        } as X509TrustManager?
        trustMan ?: throw Error(
            "Crap, there's no appropriate ${TrustManager::class.java.simpleName}" )

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustFact.trustManagers, null)

        val defaultVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
        return this
            .sslSocketFactory(sslContext.socketFactory, trustMan)
            .hostnameVerifier { hostname, session ->
                hostname == HOST ||
                defaultVerifier.verify(hostname, session)
            }
    }


    companion object {
        private const val HOST = "sviazen.herokuapp.com"
    }
}