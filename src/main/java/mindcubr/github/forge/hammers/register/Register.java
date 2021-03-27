package mindcubr.github.forge.hammers.register;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * A synchronized data structure, that acts like an non-removable data structure
 * {@link ArrayList} with only defined elements contained and only added when absent.
 * <p>The register provides an {@link ArrayList} as is therefore accessing to
 * {@link java.util.RandomAccess}.
 *
 * @author mindcubr
 * @since 1.0.0-0.1
 */
public class Register<E> {

    @Nonnull
    private final ArrayList<E> list;

    protected Register(@Nonnull ArrayList<E> list) {
        this.list = list;
    }

    /**
     * Registers the input {@code element} if it is not already
     * registered and returns whether the insertion of the {@code element}
     * happened.
     * <p>If the {@code element} is undefined, <em>False</em> is returned.
     * No side effects occur.
     * <p>This operation is synchronized with the register itself.
     *
     * @param element the element
     */
    public boolean register(@Nonnull E element) {
        Validate.notNull(element);
        if (isRegistered(element))
            return false;

        synchronized (list) {
            return list.add(element);
        }
    }

    /**
     * Returns whether the input {@code element} is already registered.
     * <p>If the {@code element} is undefined, <em>False</em> is returned.
     * No side effects occur.
     * <p>This operation is synchronized with the register itself.
     *
     * @param element the element to check for within the register
     * @return whether the {@code element} is contained in this register.
     * @implNote This operation is not overrideable.
     */
    public final boolean isRegistered(E element) {
        //Don't pass illegal arguments
        if (element == null)
            return false;

        synchronized (list) {
            return list.contains(element);
        }
    }

    /**
     * Returns this registers size of registered elements.
     *
     * @return the length of registrations of elements
     */
    public final int size() {
        return list.size();
    }

    /**
     * Returns the registered element at {@code index} position.
     * <p>The time complexity for this invocation is equal to <code>O(1)</code>.
     *
     * @param index the index position where to locate the
     *              returning element.
     * @return the possible element instance at the given {@code index}
     * position.
     * @throws IndexOutOfBoundsException if the {@code index} is out of bounds,
     * in relation to this {@link #size()}.
     * @see #size()
     */
    public E get(int index) {
        synchronized (list) {
            Validate.validIndex(list, index);
            return list.get(index);
        }
    }

    /**
     * Returns this <em>mutable</em> {@link #list register list}, that possibly
     * contains already registered {@link #register(Object) elements}.
     *
     * @return the <em>mutable</em> list of past registrations.
     * @apiNote Be careful with this method, as it might can corrupt
     * when using at multiple thread levels!
     * @see #register(Object)
     */
    @Nonnull
    public synchronized ArrayList<E> getRegistered() {
        return list;
    }

    /**
     * Returns the parallel stream of this register.
     *
     * @return the parallel stream.
     */
    public Stream<E> stream() {
        synchronized (list) {
            return list.parallelStream();
        }
    }

    /**
     * Returns a mutable iterator instance of this register.
     *
     * @return a new iterator instance of this register.
     * @see Iterator
     */
    @Nonnull
    public Iterator<E> iterator() {
        synchronized (list) {
            return list.iterator();
        }
    }

    /**
     * Creates an empty {@link Register} instance.
     *
     * @param <T> the type of the register.
     * @return the new register instance.
     */
    public static <T> Register<T> emptyRegister() {
        return new Register<>(Lists.newArrayList());
    }

    /**
     * Creates an {@link Register} instance with given {@code list}
     * as constructive argument.
     *
     * @param <T> the type of the register and therefore its elements
     * @return the new register instance.
     */
    public static <T> Register<T> registerOf(@Nonnull ArrayList<T> list) {
        Validate.notNull(list);
        return new Register<>(list);
    }

}
