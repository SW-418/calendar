package samwells.io.calendar.mapper;

public interface Mapper<T, U> {
    U map(T toMap);
}
