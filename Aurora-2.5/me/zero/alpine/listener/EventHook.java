
package me.zero.alpine.listener;

@FunctionalInterface
public interface EventHook<T>
{
    void invoke(final T p0);
}
