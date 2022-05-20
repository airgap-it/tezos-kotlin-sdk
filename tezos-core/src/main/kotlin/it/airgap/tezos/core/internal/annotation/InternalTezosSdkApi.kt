package it.airgap.tezos.core.internal.annotation

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This is an internal it.airgap.tezos API that should not be used outside of it.airgap.tezos."
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS, AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR)
public annotation class InternalTezosSdkApi
