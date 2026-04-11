package com.sam.miniecommerceapi.common.validator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExistsIdValidator implements ConstraintValidator<ExitsId, Long> {
	@PersistenceContext
	EntityManager entityManager;

	Class<?> entityClass;

	@Override
	public void initialize(ExitsId constraintAnnotation) {
		this.entityClass = constraintAnnotation.entity();
	}

	@Override
	public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
		if (id == null) return true;
		String jpql = String.format("SELECT COUNT(e) FROM %s e WHERE e.id = :id", entityClass.getSimpleName());
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("id", id).getSingleResult();
		return count > 0;
	}
}
