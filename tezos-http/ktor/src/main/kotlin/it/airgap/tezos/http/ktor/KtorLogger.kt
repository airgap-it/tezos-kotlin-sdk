package it.airgap.tezos.http.ktor

public abstract class KtorLogger(public val level: LogLevel = LogLevel.All) {
    public open fun log(message: String) {
        println(message)
    }

    public enum class LogLevel {
        All,
        Headers,
        Body,
        Info,
        None,
    }
}