#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.driverutil

abstract class AppiumNativeAppCommonWebDriverFactory: RemoteWebDriverFactory() {
    protected fun getMobileDeviceId(): String {
        val id = System.getProperty("device.id", "device.id is not set!")
        println("Using following device.id : $id")
        return id
    }

    protected fun getDeviceName(): String {
        return System.getProperty("device.name", "device.name is not set!")
    }

    protected fun getPlatformVersion(): String {
        return System.getProperty("device.platformversion", "device.platformversion not set!")
    }


}