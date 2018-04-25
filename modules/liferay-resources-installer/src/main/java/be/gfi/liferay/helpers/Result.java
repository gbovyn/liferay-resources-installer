package be.gfi.liferay.helpers;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Result<T> {
    private List<T> success;
    private List<T> errors;
}
