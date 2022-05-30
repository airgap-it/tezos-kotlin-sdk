package it.airgap.tezos.core.internal.delegate

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@InternalTezosSdkApi
public class Default<V : Any>(initValue: V? = null, lock: Any? = null, private val defaultValue: () -> V) : ReadWriteProperty<Any?, V> {
    @Volatile private var value: V? = initValue
    private val lock = lock ?: this

    override fun getValue(thisRef: Any?, property: KProperty<*>): V = synchronized(lock) {
        value ?: defaultValue().also { setValue(thisRef, property, it) }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V): Unit = synchronized(lock) {
        this.value = value
    }
}

@InternalTezosSdkApi
public fun <V : Any> default(initValue: V? = null, lock: Any? = null, defaultValue: () -> V): Default<V> = Default(initValue, lock, defaultValue)
