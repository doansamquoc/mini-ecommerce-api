package com.sam.miniecommerceapi.common.util;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

public class SortUtils {
	public static List<Sort.Order> normalizeSort(String[] sorts) {
		List<Sort.Order> orders = new ArrayList<>();
		if (sorts == null) return orders;

		for (String sort : sorts) {
			String[] _sort = sort.split(",");
			String property = _sort[0];
			Sort.Direction direction = (_sort.length > 1 && _sort[1].equalsIgnoreCase("desc") ? DESC : ASC);
			orders.add(new Sort.Order(direction, property));
		}

		return orders;
	}
}
