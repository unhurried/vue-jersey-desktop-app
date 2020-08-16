package backend.helper

import org.springframework.beans.BeanUtils
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.BiConsumer

/** A helper class that makes creating and copying beans easy.  */
@Component
class BeanHelper {
    fun <S, T> createAndCopyIterable(src: Iterable<S>, clazz: Class<T>): Iterable<T> {
        return createAndCopyIterable(src, clazz, null)
    }

    fun <S, T> createAndCopyIterable(src: Iterable<S>, clazz: Class<T>, function: BiConsumer<S, T>?): Iterable<T> {
        val target: MutableList<T> = ArrayList()
        for (s in src) {
            target.add(createAndCopy(s, clazz, function))
        }
        return target
    }

    fun <T> createAndCopy(src: Any, clazz: Class<T>): T {
        return createAndCopy(src, clazz, null)
    }

    fun <S, T> createAndCopy(src: S, clazz: Class<T>, function: BiConsumer<S, T>?): T {
        val target = BeanUtils.instantiateClass(clazz)
        BeanUtils.copyProperties(src!!, target!!)
        function?.accept(src, target)
        return target
    }

    fun copyProperties(source: Any, target: Any) {
        BeanUtils.copyProperties(source, target)
    }
}