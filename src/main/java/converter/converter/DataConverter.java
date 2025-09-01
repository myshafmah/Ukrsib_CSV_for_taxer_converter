package converter.converter;

import java.util.List;

public interface DataConverter<S, T> {
    T convert(S source, String additionalInfo);
    List<T> convertList(List<S> sourceList, String additionalInfo);
}
