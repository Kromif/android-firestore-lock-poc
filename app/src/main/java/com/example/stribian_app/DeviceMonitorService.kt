package com.example.stribian_app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore

class DeviceMonitorService : Service() {

    private val TAG = "StribianService"
    private val CHANNEL_ID = "StribianForegroundChannel"
    private val db = FirebaseFirestore.getInstance()

    // TODO: Obtener el IMEI real o un ID de prueba estable para el emulador
    private val deviceImei = "358765091234567"
    //private lateinit var deviceImei: String

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        // Inicio del servicio en primer plano con notificación persistente
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("StribianApp Activa")
            .setContentText("Monitoreo de seguridad y financiamiento en segundo plano")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .build()

        startForeground(1, notification)
        // Escucha en tiempo real de Firestore
        startFirestoreListener()
    }

    private fun startFirestoreListener() {
        // Ruta en Firestore: devices/{imei}
        db.collection("devices").document(deviceImei)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error escuchando cambios en Firestore: ", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val status = snapshot.getString("status") ?: "ACTIVE"
                    Log.i(TAG, "¡Actualización recibida desde la nube! Status: $status")

                    // Evaluamos el ciclo de vida del crédito según el estado recibido
                    when (status.uppercase()) {
                        "ACTIVE" -> {
                            Log.i(TAG, "Dispositivo al corriente. Operación normal.")
                            // TODO: Asegurar desbloqueo si estuviera bloqueado
                            val closeIntent = Intent(this, LockScreenActivity::class.java).apply {
                                action = "ACTION_CLOSE"
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            }
                            startActivity(closeIntent)
                        }
                        "BLOCKED" -> {
                            Log.w(TAG, "¡ALERTA! Crédito vencido. Ejecutando bloqueo.")
                            lockDeviceNow()
                        }
                        "FINISHED" -> {
                            Log.i(TAG, "¡Crédito liquidado! El dispositivo ha completado su pago.")
                            releaseDeviceCompletely()
                        }
                        "GRACE_PERIOD" -> {
                            Log.i(TAG, "Dispositivo en período de gracia. Permitiendo uso temporal.")
                        }
                        else -> {
                            Log.w(TAG, "Estado desconocido recibido: $status")
                        }
                    }
                } else {
                    Log.w(TAG, "El documento del dispositivo no existe en Firestore.")
                }
            }
    }

    private fun lockDeviceNow() {
        Log.w(TAG, "¡ALERTA! El servidor ordenó el bloqueo del dispositivo.")

        try {
            val dpm = getSystemService(DEVICE_POLICY_SERVICE) as android.app.admin.DevicePolicyManager
            //val adminName = android.content.ComponentName(this, AdminReceiver::class.java)
            if (dpm.isDeviceOwnerApp(packageName)) {
                dpm.lockNow()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al invocar lockNow(): ", e)
        }

        // 2. Actividad de Bloqueo a pantalla completa
        val intent = Intent(this, LockScreenActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }

    private fun releaseDeviceCompletely() {
        Log.i(TAG, "Liberando dispositivo de las restricciones de financiamiento.")
        // Opcional: Remover Device Owner o desinstalar control al liquidar
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Asegura que el servicio se reinicie si el sistema llega a cerrarlo por memoria
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Canal de Monitoreo Stribian",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}