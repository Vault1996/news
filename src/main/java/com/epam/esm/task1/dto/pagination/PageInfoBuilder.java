package com.epam.esm.task1.dto.pagination;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.epam.esm.task1.exception.ValidationException;

@Component
public class PageInfoBuilder {

	/**
	 * Builds PageInfo object by String representation of its fields.
	 * 
	 * @param offset
	 *            string representation of offset
	 * @param limit
	 *            string representation of limit
	 * @return PageInfo object
	 * @throws ValidationException
	 *             if string is not creatable or isn't positive
	 */
	public PageInfo buildPageInfo(String offset, String limit) {
		if (!NumberUtils.isCreatable(offset)) {
			throw new ValidationException("Offset should be a number");
		}
		int intOffset = Integer.parseInt(offset);
		if (intOffset < 0) {
			throw new ValidationException("Offset should be non-negative");
		}

		if (!NumberUtils.isCreatable(limit)) {
			throw new ValidationException("Limit should be a number");
		}
		int intLimit = Integer.parseInt(limit);
		if (intLimit <= 0) {
			throw new ValidationException("Limit should be positive");
		}
		return new PageInfo(intOffset, intLimit);
	}

}
