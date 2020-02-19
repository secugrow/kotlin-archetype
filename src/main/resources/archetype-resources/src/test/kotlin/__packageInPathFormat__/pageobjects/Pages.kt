package ${package}.pageobjects

enum class PageUrls(val subUrl: String) {
    HOME("/"),
    SOFTWARETEST("/wiki/Software_testing");

    fun getFullUrl(baseUrl: String): String? {
        return baseUrl + subUrl
    }
}
