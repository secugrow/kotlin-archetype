#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
package ${package}.driverutil

import java.util.concurrent.LinkedBlockingQueue

/**
 * Thread-safe pool of Android device serials for parallel Appium test execution.
 *
 * ## How parallel execution works end-to-end
 *
 * Populate the pool at Maven invocation time by passing a comma-separated list of
 * device serials via the system property `-Ddevices`. Device serials are typically
 * resolved dynamically via `adb devices` — see README.md for full invocation examples
 * (bare metal and Docker variants).
 *
 * The pool size naturally caps concurrency: Cucumber spawns as many parallel threads
 * as configured, but each thread blocks in `@Before` until a device becomes available.
 * No manual thread-count tuning is needed — the number of devices drives parallelism.
 *
 * ### Lifecycle per scenario
 *
 * ```
 * @Before  →  DevicePool.acquire()
 *               Blocks until a device serial is available in the queue.
 *               Binds the serial to the current thread via ThreadLocal.
 *
 * createDriver()  →  DevicePool.current()
 *               AppiumAndroidWebDriverFactory reads the thread-local serial
 *               and sets it as the UDID in UiAutomator2Options.
 *
 * @After   →  DevicePool.release(serial)
 *               Returns the serial to the queue (both success and failure paths).
 *               The next waiting thread unblocks immediately.
 * ```
 *
 * ### Example: N devices, M scenarios (N=2, M=3 shown for illustration)
 *
 * Any number of devices can be passed via `-Ddevices`. The pool size equals the number
 * of serials provided, which is the effective parallelism ceiling. The example below
 * uses 2 devices to show the blocking/unblocking behaviour when more scenarios than
 * devices are running concurrently.
 *
 * ```
 * Thread 1 (Scenario A)              Thread 2 (Scenario B)           Thread 3 (Scenario C)
 *   acquire() → "emulator-5554"        acquire() → "R3CN90BCXYZ"       acquire() → blocks...
 *   UDID = emulator-5554               UDID = R3CN90BCXYZ
 *   ... test runs ...                  ... test runs ...
 *   release("emulator-5554")                                            unblocks → "emulator-5554"
 *                                      release("R3CN90BCXYZ")           UDID = emulator-5554
 *                                                                        ... test runs ...
 * ```
 *
 * ### Guards
 *
 * - [isEnabled] returns `false` when `-Ddevices` is absent or empty, so all DevicePool
 *   logic in `Hooks` is skipped entirely for non-Appium runs.
 * - `release` is always called inside a `finally` block in `@After` to guarantee the
 *   device is returned to the pool even when a scenario fails.
 */
object DevicePool {

    private val devices: LinkedBlockingQueue<String> = LinkedBlockingQueue<String>().apply {
        System.getProperty("devices", "")
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { offer(it) }
    }

    // ThreadLocal so each parallel scenario thread holds its own device serial
    private val currentDevice = ThreadLocal<String>()

    fun isEnabled(): Boolean = devices.isNotEmpty()

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