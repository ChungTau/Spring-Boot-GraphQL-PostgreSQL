package com.chungtau.demo.exception;

public class EntityRuntimeException extends RuntimeException {
    public EntityRuntimeException(String message) {
        super(message);
    }

    public static EntityRuntimeException notFound(String entityName, Object entityId) {
        return new EntityRuntimeException(entityName + " not found for id: " + entityId);
    }

    public static EntityRuntimeException entityIdNotNull(String entityName) {
        return new EntityRuntimeException(entityName + " ID cannot be null");
    }
}