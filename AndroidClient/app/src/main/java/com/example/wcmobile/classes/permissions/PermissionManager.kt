package com.example.wcmobile.classes.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

class PermissionManager(private val activity: ComponentActivity) {

    // Callback para manejar el resultado de los permisos
    private var onPermissionsResult: ((Boolean, List<String>) -> Unit)? = null

    // Registrar el launcher para solicitar múltiples permisos
    private val multiplePermissionsRequest = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val grantedPermissions = permissions.filterValues { it }.keys.toList()
        val deniedPermissions = permissions.filterValues { !it }.keys.toList()

        // Llama al callback con el resultado
        onPermissionsResult?.invoke(deniedPermissions.isEmpty(), deniedPermissions)
    }

    /**
     * Solicita los permisos especificados.
     * @param permissions Lista de permisos que se desean solicitar.
     * @param callback Callback que recibe true si todos los permisos fueron concedidos, false en caso contrario.
     */
    fun requestPermissions(permissions: Array<String>, callback: (Boolean, List<String>) -> Unit) {
        onPermissionsResult = callback

        // Verifica qué permisos ya fueron concedidos
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_DENIED
        }

        if (permissionsToRequest.isNotEmpty()) {
            // Lanza la solicitud de permisos
            multiplePermissionsRequest.launch(permissionsToRequest.toTypedArray())
        } else {
            // Si ya están todos concedidos, invoca el callback directamente
            callback(true, emptyList())
        }
    }

    /**
     * Solicita solo el permiso de la cámara.
     */
    fun requestCameraPermission(callback: (Boolean) -> Unit) {
        requestPermissions(arrayOf(Manifest.permission.CAMERA)) { allGranted, _ ->
            callback(allGranted)
        }
    }

    /**
     * Solicita solo el permiso de audio.
     */
    fun requestAudioPermission(callback: (Boolean) -> Unit) {
        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO)) { allGranted, _ ->
            callback(allGranted)
        }
    }

    /**
     * Solicita solo el permiso de internet.
     */
    fun requestInternetPermission(callback: (Boolean) -> Unit) {
        requestPermissions(arrayOf(Manifest.permission.INTERNET)) { allGranted, _ ->
            callback(allGranted)
        }
    }
}
