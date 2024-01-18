package ttps.java.entregable6_v2.helpers.Pagination;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

public class PaginationUtils<T> {

    public Map<String, Object> createPaginationResponse(Page<T> paginatedData) {
        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", paginatedData.getTotalElements());
        response.put("totalPages", paginatedData.getTotalPages());
        response.put("currentPage", paginatedData.getNumber() + 1);
        return response;
    }
}
