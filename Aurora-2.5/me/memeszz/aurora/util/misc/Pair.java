
package me.memeszz.aurora.util.misc;

public class Pair<T, S>
{
    T key;
    S value;
    
    public Pair(final T key, final S value) {
        this.key = key;
        this.value = value;
    }
    
    public T getKey() {
        return this.key;
    }
    
    public S getValue() {
        return this.value;
    }
    
    public void setKey(final T key) {
        this.key = key;
    }
    
    public void setValue(final S value) {
        this.value = value;
    }
}
