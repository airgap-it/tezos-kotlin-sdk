package it.airgap.tezos.core.internal.delegate

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@InternalTezosSdkApi
public class Default<V : Any>(initValue: V? = null, private val defaultValue: () -> V) : ReadWriteProperty<Any?, V> {
    private var value: V? = initValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): V = value ?: defaultValue()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        this.value = value
    }
}

@InternalTezosSdkApi
public fun <V : Any> default(initValue: V? = null, defaultValue: () -> V): Default<V> = Default(initValue, defaultValue)
