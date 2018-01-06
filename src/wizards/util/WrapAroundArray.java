package wizards.util;

/**
 * An array wrapper
 * User: mbs207
 * Date: 4/22/11
 * Time: 2:43 PM
 */
public class WrapAroundArray<T> {
    T[] array;
    int i;
    public WrapAroundArray(T[] array){
        if(array.length==0) throw new IllegalArgumentException("length of array must be greater than zero");
        this.array = array;
        i = array.length;
    }
    public T next(){
        i++;
        i = i>=array.length?0:i;

        T value = array[i];

        return value;
    }
}
