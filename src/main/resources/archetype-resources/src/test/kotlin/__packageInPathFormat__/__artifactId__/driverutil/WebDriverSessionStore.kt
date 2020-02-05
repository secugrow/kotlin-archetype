package at.co.boris.secuton.driverutil

object WebDriverSessionStore {

    private val store = HashMap<String, WebDriverSession>()

    @Synchronized
    fun get(sessionName: String): WebDriverSession {

        if (!store.containsKey(sessionName)) {
            //TODO log a warning or error if Appium is used, only one session on mobile phones!
            store[sessionName] = WebDriverSession(sessionName)
        }
        return store[sessionName]!!
    }

    fun remove(sessionName: String) {
        store.remove(sessionName)?.webDriver?.quit()
    }

    fun quitAll() {
        val keys = store.keys
        keys.forEach{
            store[it]?.webDriver?.quit()
        }
    }

    fun getIfExists(sessionName: String): WebDriverSession? {
        if (store.containsKey(sessionName)) {
            return store[sessionName]!!
        }
        return null
    }
}