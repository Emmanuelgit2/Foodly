package com.example.saborapp

import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

private val FOODLY_API: String by lazy {
    val isEmulator = Build.FINGERPRINT.startsWith("generic") ||
        Build.FINGERPRINT.contains("emulator", ignoreCase = true) ||
        Build.MODEL.contains("Emulator", ignoreCase = true) ||
        Build.HARDWARE.contains("ranchu", ignoreCase = true) ||
        Build.PRODUCT.contains("sdk", ignoreCase = true)

    if (isEmulator) "http://10.0.2.2:3000" else "http://127.0.0.1:3000"
}
private const val GOOGLE_WEB_CLIENT_ID =
    "46704747268-c0vgolsbqbhh10b89en9cug1jc34utpe.apps.googleusercontent.com"

suspend fun obtenerGoogleIdToken(context: Context): String {
    // El botón usa directamente el flujo de selección de cuenta de Google.
    // Evita primero intentar el selector de credenciales guardadas y esperar
    // a que falle antes de mostrar la interfaz de inicio de sesión.
    val option = GetSignInWithGoogleOption.Builder(GOOGLE_WEB_CLIENT_ID).build()
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(option)
        .build()

    val result = try {
        CredentialManager.create(context).getCredential(context, request)
    } catch (error: NoCredentialException) {
        throw IOException(
            "Google Play no encontró una cuenta en este dispositivo. Añade tu cuenta en Ajustes > Contraseñas y cuentas y actualiza Google Play services.",
            error,
        )
    }
    val credential = result.credential

    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }

    throw IOException("Google no devolvió una credencial compatible.")
}

suspend fun obtenerTurnstileSiteKey(): String = withContext(Dispatchers.IO) {
    val json = solicitudHttp("$FOODLY_API/auth/config")
    val siteKey = JSONObject(json).optString("turnstileSiteKey")
    if (siteKey.isBlank() || siteKey == "null") {
        throw IOException("El backend no tiene TURNSTILE_SITE_KEY configurada.")
    }
    siteKey
}

suspend fun iniciarSesionGoogle(credential: String, turnstileToken: String): JSONObject =
    withContext(Dispatchers.IO) {
        val body = JSONObject()
            .put("credential", credential)
            .put("turnstileToken", turnstileToken)
            .toString()
        JSONObject(solicitudHttp("$FOODLY_API/auth/google", "POST", body))
    }

private fun solicitudHttp(
    url: String,
    method: String = "GET",
    body: String? = null,
): String {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = method
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    connection.setRequestProperty("Accept", "application/json")

    if (body != null) {
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        connection.outputStream.bufferedWriter(Charsets.UTF_8).use { it.write(body) }
    }

    val status = connection.responseCode
    val stream = if (status in 200..299) connection.inputStream else connection.errorStream
    val response = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
    connection.disconnect()

    if (status !in 200..299) {
        val message = runCatching { JSONObject(response).optString("message") }
            .getOrNull()
            .orEmpty()
        throw IOException(message.ifBlank { "El servidor respondió con error ($status)." })
    }
    return response
}

@Composable
fun TurnstileDialog(
    siteKey: String,
    onToken: (String) -> Unit,
    onClose: () -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Verifica que eres una persona") },
        text = {
            Column {
                Text("Completa el CAPTCHA para continuar con Google.")
                AndroidView(
                    modifier = Modifier.fillMaxWidth().height(180.dp).padding(top = 8.dp),
                    factory = { viewContext ->
                        val safeSiteKey = siteKey.filter { it.isLetterOrDigit() || it == '-' || it == '_' }
                        WebView(viewContext).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            CookieManager.getInstance().setAcceptCookie(true)
                            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                            webViewClient = WebViewClient()
                            addJavascriptInterface(
                                TurnstileBridge(context, onToken),
                                "FoodlyCaptcha",
                            )
                            val html = """
                                <!doctype html><html lang="es"><head>
                                <meta name="viewport" content="width=device-width, initial-scale=1">
                                <script src="https://challenges.cloudflare.com/turnstile/v0/api.js?render=explicit" async defer></script>
                                </head><body style="font-family:sans-serif;margin:8px">
                                <div id="captcha"></div><div id="captcha-error" style="color:#b00020"></div>
                                <script>
                                  function renderCaptcha() {
                                    if (!window.turnstile) { setTimeout(renderCaptcha, 100); return; }
                                    turnstile.render('#captcha', {
                                      sitekey: '$safeSiteKey',
                                      callback: function(token) { FoodlyCaptcha.onToken(token); },
                                      'error-callback': function() { document.getElementById('captcha-error').textContent = 'No se pudo cargar CAPTCHA. Revisa la conexión.'; },
                                      'expired-callback': function() { document.getElementById('captcha-error').textContent = 'La verificación venció. Cierra y vuelve a intentarlo.'; }
                                    });
                                  }
                                  renderCaptcha();
                                </script></body></html>
                            """.trimIndent()
                            loadDataWithBaseURL(
                                "https://localhost/",
                                html,
                                "text/html",
                                "UTF-8",
                                null,
                            )
                        }
                    },
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onClose) { Text("Cancelar") }
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    )
}

private class TurnstileBridge(
    context: Context,
    private val onToken: (String) -> Unit,
) {
    private val mainHandler = android.os.Handler(context.mainLooper)

    @JavascriptInterface
    fun onToken(token: String) {
        if (token.isNotBlank() && token.length <= 2048) {
            mainHandler.post { onToken.invoke(token) }
        }
    }
}
