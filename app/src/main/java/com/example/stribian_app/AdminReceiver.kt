package com.example.stribian_app

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AdminReceiver : DeviceAdminReceiver() {

    private val TAG = "AdminReceiverTag"
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Toast.makeText(context, "StribianApp: Administrador habilitado", Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Toast.makeText(context, "StribianApp: Administrador deshabilitado", Toast.LENGTH_SHORT).show()
    }
/*
    // Este método se dispara automáticamente cuando el aprovisionamiento (QR o ADB) finaliza con éxito
    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
        super.onProfileProvisioningComplete(context, intent)
        Log.i(TAG, "¡Aprovisionamiento completado con éxito!")

        try {
            // Android entrega los extras del QR o ADB directamente en el Intent del sistema
            // Usamos EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE
            val extras = intent.getParcelableExtra<android.os.PersistableBundle>(
                DevicePolicyManager.EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE
            )

            if (extras != null) {
                // Buscamos la llave 'device_imei' que enviamos desde el QR o la terminal
                val imei = extras.getString("device_imei")

                if (!imei.isNullOrEmpty()) {
                    Log.i(TAG, "IMEI capturado exitosamente desde el Bundle: $imei")

                    // Lo guardamos de forma persistente en SharedPreferences locales
                    val sharedPreferences = context.getSharedPreferences("StribianPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("KEY_DEVICE_IMEI", imei).apply()

                    Toast.makeText(context, "Dispositivo vinculado correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Log.w(TAG, "El Bundle llegó pero la clave 'device_imei' está vacía.")
                }
            } else {
                Log.w(TAG, "No se encontraron extras en el aprovisionamiento (Bundle vacío).")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al procesar los extras de aprovisionamiento: ${e.message}", e)
        }
    }
     // Este metodo se elimina si si se requiere junto con el de arriba para pruebas rápidas
    companion object {
        fun getComponentName(context: Context): android.content.ComponentName {
            return android.content.ComponentName(context, AdminReceiver::class.java)
        }
    }
    */
}