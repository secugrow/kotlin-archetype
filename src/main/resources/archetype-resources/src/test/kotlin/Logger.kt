import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <R : Any> R.logger(): Lazy<Logger> = lazy { LoggerFactory.getLogger(getClassName(this::class.java)) }

private fun <T : Any> getClassName(clazz: Class<T>): String = clazz.name.replace("""\$.*$""", "")