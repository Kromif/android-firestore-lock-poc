package com.example.stribian_app

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class LockScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setShowWhenLocked(true)
        setTurnScreenOn(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        try {
            val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val adminComponent = ComponentName(this, AdminReceiver::class.java)
            val isOwner = dpm.isDeviceOwnerApp(packageName)
            Log.w("StribianOwnerCheck", "¿Soy Device Owner?: $isOwner")
            startLockTask()
        } catch (e: Exception) {
            // Si por alguna razón no es Device Owner en este entorno, evitamos que crashee
        }

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFB71C1C)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🔒", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "CreditoCel", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "¡ATENCIÓN: CRÉDITO VENCIDO!", color = Color.Yellow, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Su dispositivo se encuentra bloqueado temporalmente debido a un retraso en su esquema de financiamiento. Para reactivar el equipo, realice su pago y comuníquese con su distribuidor.", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 8.dp))
                    Spacer(modifier = Modifier.height(40.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Soporte y Ventas", fontWeight = FontWeight.Bold, color = Color.DarkGray, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            //Text(text = "Tel: 55-XXXX-XXXX", color = Color.Black, fontSize = 14.sp)
                            Text(text = "email: correo@mail.com", color = Color.Black, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

    // Método clave: Si el servicio vuelve a lanzar la actividad con un comando de cierre o si detecta el estado ACTIVE, se destruye sola.
    /*
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == "ACTION_CLOSE") {
            try {
                stopLockTask()
            } catch (e: Exception) {}
            finish()
        }
    }*/

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == "ACTION_CLOSE") {
            releaseAndFinish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Asegurarnos de limpiar el task lock si la actividad se destruye por otro medio
        try {
            stopLockTask()
        } catch (e: Exception) {}
    }

    private fun releaseAndFinish() {
        try {
            // Liberamos el anclaje de tareas antes de cerrar la actividad
            stopLockTask()
        } catch (e: Exception) {}
        finish()
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Bloqueado: No se permite salir con el botón atrás
    }
}