package ${package}.driverutil

import ${package}.driverutil.strategy.DriverContext
import logger

/**
 * Singleton object responsible for managing WebDriverSession instances.
 *
 * This store maintains a mapping between session names and WebDriverSession objects,
 * allowing for reuse of WebDriver instances across the test execution. It implements
 * a simple cache mechanism to avoid creating multiple WebDriver instances for the same
 * test scenario, which improves performance and resource utilization.
 */
object WebDriverSessionStore {

    /**
     * Logger for this class.
     */
    private val log by logger()

    /**
     * Internal storage for WebDriverSession instances, keyed by session name.
     */
    private val store = HashMap<String, WebDriverSession>()

    /**
     * Retrieves an existing WebDriverSession or creates a new one with a default DriverContext.
     *
     * This method is thread-safe to prevent race conditions when multiple threads
     * attempt to access or create sessions simultaneously.
     *
     * @param sessionName A unique identifier for the session
     * @return The WebDriverSession associated with the given name
     */
    @Synchronized
    fun get(sessionName: String): WebDriverSession {
        return get(sessionName, DriverContext())
    }

    /**
     * Retrieves an existing WebDriverSession or creates a new one with the specified DriverContext.
     *
     * This method is thread-safe to prevent race conditions when multiple threads
     * attempt to access or create sessions simultaneously.
     *
     * @param sessionName A unique identifier for the session
     * @param driverContext The DriverContext to use when creating a new WebDriverSession
     * @return The WebDriverSession associated with the given name
     */
    @Synchronized
    fun get(sessionName: String, driverContext: DriverContext): WebDriverSession {
        if (!store.containsKey(sessionName)) {
            //TODO log a warning or error if Appium is used, only one session on mobile phones!
            store[sessionName] = WebDriverSession(sessionName, driverContext)
        }
        return store[sessionName]!!
    }

    /**
     * Removes a WebDriverSession from the store and closes it properly.
     *
     * This method ensures that WebDriver resources are properly released when a session
     * is no longer needed, preventing memory leaks and orphaned browser instances.
     * It uses the session's close() method which utilizes the factory's cleanup() method
     * for proper resource management.
     *
     * @param sessionName The identifier of the session to remove
     */
    fun remove(sessionName: String) {
        log.info("Removing WebDriver session: $sessionName")
        val session = store.remove(sessionName)
        session?.close()
    }

    /**
     * Closes all WebDriver sessions and clears the store.
     *
     * This method should be called at the end of test execution to ensure all browser
     * instances are properly closed and resources are released. It uses each session's
     * close() method which utilizes the factory's cleanup() method for proper resource management.
     */
    fun quitAll() {
        log.info("Closing all WebDriver sessions")
        val keys = store.keys.toList() // Create a copy to avoid concurrent modification
        keys.forEach { sessionName ->
            log.info("Closing session: $sessionName")
            store[sessionName]?.close()
        }
        store.clear()
    }

    /**
     * Retrieves a WebDriverSession if it exists, without creating a new one.
     *
     * Unlike the [get] methods, this method does not create a new session if one
     * doesn't exist with the given name.
     *
     * @param sessionName The identifier of the session to retrieve
     * @return The WebDriverSession if it exists, or null if no session exists with the given name
     */
    fun getIfExists(sessionName: String): WebDriverSession? {
        if (store.containsKey(sessionName)) {
            return store[sessionName]
        }
        return null
    }
}
