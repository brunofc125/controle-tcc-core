package com.controletcc.dto.options.base;

import com.controletcc.dto.enums.OrderByDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        if (this.orderByDirection != null && this.orderByField != null && !this.orderByField.isBlank()) {
            return PageRequest.of(this.page.intValue(), this.pageSize.intValue(), this.orderByDirection.getDirection(), this.orderByField);
        }

        return PageRequest.of(this.page.intValue(), this.pageSize.intValue());
    }
}


