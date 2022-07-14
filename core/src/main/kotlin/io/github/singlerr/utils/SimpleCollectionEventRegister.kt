package io.github.singlerr.utils

import io.github.singlerr.Environment
import org.gradle.api.Action
import org.gradle.api.internal.CollectionCallbackActionDecorator
import org.gradle.api.internal.collections.CollectionEventRegister
import org.gradle.api.internal.collections.CollectionFilter
import org.gradle.internal.Cast
import org.gradle.internal.ImmutableActionSet


class SimpleCollectionEventRegister<T : Environment> : CollectionEventRegister<T> {

    private var addActions = ImmutableActionSet.empty<T>()
    private var removeActions = ImmutableActionSet.empty<T>()

    private val subscribedTypes = java.util.HashSet<Class<*>>()
    override fun isSubscribed(type: Class<*>?): Boolean {
        for (subscribedType in subscribedTypes)
            if (subscribedType.isAssignableFrom(type))
                return true
        return false
    }

    /**
     * Returns a snapshot of the *current* set of actions to run when an element is added.
     */
    override fun getAddActions(): ImmutableActionSet<T> {
        return addActions
    }

    override fun getDecorator(): CollectionCallbackActionDecorator? {
        return null
    }

    override fun <S : T> filtered(filter: CollectionFilter<S>): CollectionEventRegister<S> {
        return FilteredEventRegister(filter, this as FilteredEventRegister<S>)
    }

    private fun subscribe(type: Class<out T>) {
        subscribedTypes.add(type)
    }

    override fun registerRemoveAction(type: Class<out T>, removeAction: Action<in T>) {
        subscribe(type)
        removeActions = removeActions.add(removeAction)

    }

    override fun registerLazyAddAction(addAction: Action<in T>): Action<in T> {
        addActions = addActions.add(addAction)
        return addAction
    }

    override fun registerEagerAddAction(type: Class<out T>?, addAction: Action<in T>?): Action<in T> {
        subscribe(type!!)
        addActions = addActions.add(addAction)
        return addAction!!
    }

    override fun fireObjectRemoved(element: T) {
        removeActions.execute(element)
    }

    override fun fireObjectAdded(element: T) {
        addActions.execute(element)
    }

    private class FilteredEventRegister<T>(
        private val filter: CollectionFilter<T>,
        delegate: CollectionEventRegister<T>
    ) :
        CollectionEventRegister<T> {
        private val delegate: CollectionEventRegister<T>

        init {
            this.delegate = Cast.uncheckedCast(delegate)!!
        }

        override fun getDecorator(): CollectionCallbackActionDecorator {
            return delegate.decorator
        }

        override fun getAddActions(): ImmutableActionSet<T> {
            throw UnsupportedOperationException()
        }

        override fun fireObjectAdded(element: T) {
            throw UnsupportedOperationException()
        }

        override fun fireObjectRemoved(element: T) {
            throw UnsupportedOperationException()
        }

        override fun isSubscribed(type: Class<*>?): Boolean {
            throw UnsupportedOperationException()
        }

        override fun registerEagerAddAction(type: Class<out T>, addAction: Action<in T>): Action<in T> {
            return delegate.registerEagerAddAction(type, filter.filtered(addAction))
        }

        override fun registerLazyAddAction(addAction: Action<in T>): Action<in T> {
            return delegate.registerLazyAddAction(filter.filtered(addAction))
        }

        override fun registerRemoveAction(type: Class<out T>, removeAction: Action<in T>) {
            delegate.registerRemoveAction(type, filter.filtered(removeAction))
        }

        override fun <N : T> filtered(filter: CollectionFilter<N>): CollectionEventRegister<N> {
            return FilteredEventRegister(filter, this as FilteredEventRegister<N>)
        }
    }
}