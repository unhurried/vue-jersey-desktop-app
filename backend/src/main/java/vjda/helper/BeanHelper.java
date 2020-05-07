package vjda.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/** A helper class that makes creating and copying beans easy. */
@Component
public class BeanHelper {
	public <S, T> Iterable<T> createAndCopyIterable(Iterable<S> src, Class<T> clazz) {
		return createAndCopyIterable(src, clazz, null);
	}

	public <S, T> Iterable<T> createAndCopyIterable(Iterable<S> src, Class<T> clazz, BiConsumer<S, T> function) {
		List<T> target = new ArrayList<>();
		for(S s : src) {
			target.add(createAndCopy(s, clazz, function));
		}
		return target;
	}

	public <T> T createAndCopy(Object src, Class<T> clazz)  {
		return createAndCopy(src, clazz, null);
	}

	public <S, T> T createAndCopy(S src, Class<T> clazz, BiConsumer<S, T> function)  {
		T target = BeanUtils.instantiateClass(clazz);
		BeanUtils.copyProperties(src, target);
		if(function != null) {
			function.accept(src, target);
		}
		return target;
	}

	public void copyProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target);
	}
}