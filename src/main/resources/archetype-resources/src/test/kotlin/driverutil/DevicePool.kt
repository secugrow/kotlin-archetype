#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
package ${package}.driverutil

import java.util.concurrent.LinkedBlockingQueue

object DevicePool {

    private val devices: LinkedBlockingQueue<String> = LinkedBlockingQueue<String>().apply {
        System.getProperty("devices", "")
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { offer(it) }
    }

    private val currentDevice = ThreadLocal<String>()

    fun isMultiDevice(): Boolean = devices.isNotEmpty() || currentDevice.get() != null

    fun acquire(): String {
        val serial = devices.take()
        currentDevice.set(serial)
        return serial
    }

    fun release(serial: String) {
        currentDevice.remove()
        devices.offer(serial)
    }

    fun current(): String = currentDevice.get()
        ?: error("No device acquired on this thread. Was DevicePool.acquire() called in @Before?")
}