package org.booking.utils;

import java.util.Collections;
import java.util.List;

public class ListPaginator {

    public static List<?> getPageList(List<?> sourceList, int pageSize, int pageNum) {

        if(pageSize <= 0 || pageNum <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (pageNum - 1) * pageSize;
        if(sourceList == null || sourceList.size() <= fromIndex){
            return Collections.emptyList();
        }

        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

}
