plugins {
    // plugin principal de la aplicacion android
    alias(libs.plugins.android.application) apply false

    // plugin de google services para conectar firebase correctamente
    id("com.google.gms.google-services") version "4.4.1" apply false
}