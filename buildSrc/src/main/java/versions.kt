@file:Suppress("ClassName")
object versions {
    const val androidx = "1.1.0"
    const val androidxArch = "2.1.0"
    const val constraintLayout = "1.1.3"
    const val fragment = "1.2.0-rc02"
    const val kotlin = "1.3.70"
    const val material = "1.1.0"
    const val navigation = "2.1.0"
    const val room = "2.2.2"
    const val timber = "4.7.1"
    const val appCenterSdk = "3.0.0"
    const val work = "2.2.0"
}

open class Cred (
    val appCenterToken: String = "",
    val appCenterOwnerName: String = "",
    val appCenterCollaborators: List<String> = listOf(),
    val appCenterAppSecret: String = "\"\"",
    val store: String = "/",
    val storePass: String = "",
    val alias: String = "",
    val pass: String = ""
)

object credentials {
    var get: Cred = Cred()

    init {
        val realCreds = "RealCreds"
        try {
            val realCredsClass = Class.forName(realCreds)
            get = realCredsClass.newInstance() as Cred
        } catch (e: Exception) {
            System.err.println("Could not find real credentials, using fake ones")
        }
    }
}