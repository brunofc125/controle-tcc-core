package com.controletcc.dto.options.base;

import com.controletcc.dto.enums.OrderByDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseGridOptions {
    private Long page = 1L;
    private Long pageSize;
    private String orderByField;
    private OrderByDirection orderByDirection;

    public Pageable getPageable() {
        if (this.page == null || this.pageSize == null) {
            return null;
        }

        var pageable = PageRequest.of(this.page.intValue(), this.pageSize.intValue());

        if (this.orderByField != null && !this.orderByField.isBlank()) {
            var sort = Sort.by(this.orderByField);
            if (OrderByDirection.ASC.equals(this.orderByDirection)) {
                sort.ascending();
            } else if (OrderByDirection.DESC.equals(this.orderByDirection)) {
                sort.descending();
            }
            pageable.withSort(sort);
        }

        return pageable;
    }
}


