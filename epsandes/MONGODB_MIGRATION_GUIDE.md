# EPSAndes MongoDB Migration Guide

## Overview
This document describes the complete migration of the EPSAndes healthcare system from Oracle Database with JPA/Hibernate to MongoDB with Spring Data MongoDB.

## Migration Summary

### 1. Dependency Changes
- **Removed**: `spring-boot-starter-data-jpa`, `ojdbc8`
- **Added**: `spring-boot-starter-data-mongodb`

### 2. Configuration Changes
- **File**: `application.properties`
- **Removed**: Oracle datasource configuration
- **Added**: MongoDB connection string: `mongodb://ISIS2304E08202510:AZHHkGYKMyhj@157.253.236.88:8087/epsandes`

### 3. Entity Model Changes

#### Annotation Mapping:
- `@Entity` → `@Document`
- `@Table(name="...")` → `@Document(collection="...")`
- `@Column(name="...")` → `@Field("...")`
- `@Id` remains the same (but uses String for auto-generated IDs where applicable)

#### Relationship Changes:
- **JPA Relationships** → **Reference IDs**
- `@ManyToOne`, `@OneToMany`, `@ManyToMany` → Removed
- Foreign key objects → Long reference fields
- Composite keys → Single auto-generated String IDs (where applicable)

#### Specific Entity Changes:

**Afiliado.java**:
- Converted to MongoDB document
- Maintained all core fields
- Removed JPA relationship annotations

**IPS.java**:
- Added `serviciosIds: List<Long>` for service references
- Removed JPA service relationships

**ServicioDeSalud.java**:
- Simple conversion to MongoDB document
- No major structural changes

**Medico.java**:
- `ips` → `ipsNit: Long` (reference ID)
- Added `serviciosIds: List<Long>` for service references

**AgendarCita.java**:
- `afiliado` → `afiliadoNumeroDocumento: Long`
- `medico` → `medicoNumeroDocumento: Long`
- `servicioDeSalud` → `servicioSaludId: Long`
- `ordenDeServicio` → `idOrdenDeServicio: Long`

**OrdenDeServicio.java**:
- `medico` → `medicoNumeroDocumento: Long`
- `afiliado` → `afiliadoNumeroDocumento: Long`
- `servicios` → `serviciosIds: List<Long>`

**PrestacionServicio.java**:
- Removed composite key
- Uses auto-generated String ID
- `agendarCita` → `agendarCitaId: Long`
- `ips` → `ipsNit: Long`

### 4. Repository Changes

All repositories migrated from `JpaRepository` to `MongoRepository`:
- SQL queries → MongoDB JSON queries using `@Query`
- Native SQL → MongoDB aggregation framework (for complex queries)
- Method query derivation adapted to MongoDB conventions

### 5. Service Layer Changes

#### Core Changes:
- **Removed**: All `@Transactional` annotations (MongoDB doesn't support ACID transactions in the same way)
- **Updated**: All methods to work with reference IDs instead of entity objects
- **Modified**: Data validation from JPA constraints to MongoDB query-based validation

#### Specific Service Updates:

**AfiliadoService**:
- `eliminarAfiliado`: Uses MongoDB queries to check for related citas/ordenes before deletion

**MedicoService**:
- `registrarMedico`: Works with `serviciosIds` list instead of entity relationships
- All queries updated to MongoDB syntax

**ServicioDeSaludService**:
- `asignarServicioAIPS`: Updates IPS document's `serviciosIds` array

**OrdenDeServicioService**:
- All operations work with reference IDs
- Service assignment updates `serviciosIds` arrays

**AgendarCitaService**:
- Major overhaul to use reference IDs
- Availability queries marked for MongoDB aggregation implementation
- Validation uses separate repository lookups instead of JPA constraints

### 6. Controller Changes

**Updated Controllers**:
- `AgendarCitaController`: Response mapping updated for reference IDs
- `OrdenDeServicioController`: Response mapping updated for reference IDs
- `MedicoController`: Fixed to use `getIpsNit()` instead of `getIps()`
- Removed unused transaction imports

### 7. Database Schema Mapping

#### MongoDB Collections:
- `afiliados` (corresponds to Oracle AFILIADOS table)
- `ips` (corresponds to Oracle IPS table)
- `serviciosDeSalud` (corresponds to Oracle SERVICIO_DE_SALUD table)
- `medicos` (corresponds to Oracle MEDICO table)
- `ordenesDeServicio` (corresponds to Oracle ORDEN_DE_SERVICIO table)
- `agendarCitas` (corresponds to Oracle AGENDAR_CITA table)
- `prestacionServicios` (corresponds to Oracle PRESTACION_SERVICIO table)

#### Relationship Handling:
- **One-to-Many/Many-to-One**: Replaced with reference ID fields
- **Many-to-Many**: Replaced with ID arrays in one document
- **Junction Tables**: Eliminated in favor of embedded arrays

### 8. Complex Queries Status

#### Implemented:
- Basic CRUD operations
- Simple filtering and searching
- Reference validation

#### TODO (MongoDB Aggregation Required):
- Availability queries in `AgendarCitaService`
- Most requested services statistics
- Complex reporting queries
- Date range queries with service availability

### 9. Setup Scripts Created

**mongodb-setup.js**:
- Creates all collections with validation schemas
- Sets up indexes for performance
- Includes compound indexes for common query patterns

**data-migration.js**:
- Template for migrating Oracle data to MongoDB
- Includes data transformation examples
- Validation queries for post-migration verification

## Next Steps

### 1. Data Migration
1. Run `mongodb-setup.js` to create collections and indexes
2. Export data from Oracle database
3. Transform data according to MongoDB schema
4. Use `data-migration.js` as template for data insertion
5. Run validation queries

### 2. Complex Query Implementation
Implement the following using MongoDB aggregation framework:
- Service availability queries
- Complex reporting and analytics
- Performance statistics
- Date-based filtering with multiple conditions

### 3. Testing
1. Unit tests for all services with MongoDB
2. Integration tests for controller endpoints
3. Performance testing with MongoDB
4. Data consistency validation

### 4. Performance Optimization
1. Review and optimize MongoDB indexes
2. Implement caching where appropriate
3. Consider MongoDB-specific optimizations
4. Monitor query performance

## Benefits of MongoDB Migration

### Advantages:
1. **Flexible Schema**: Easier to modify document structure
2. **Denormalization**: Better read performance for common queries
3. **Scalability**: Better horizontal scaling capabilities
4. **JSON Native**: Natural fit for REST APIs
5. **Aggregation Framework**: Powerful analytics capabilities

### Considerations:
1. **Transaction Support**: Limited compared to RDBMS
2. **Complex Joins**: Require aggregation pipelines
3. **Data Consistency**: Requires careful design
4. **Learning Curve**: Different query paradigm

## File Structure Summary

```
epsandes/
├── src/main/java/uniandes/edu/co/epsandes/
│   ├── modelo/ (all entities converted to MongoDB documents)
│   ├── repositorio/ (all repositories converted to MongoRepository)
│   ├── servicio/ (all services updated for MongoDB)
│   └── controller/ (controllers updated for reference IDs)
├── src/main/resources/
│   └── application.properties (MongoDB configuration)
├── mongodb-setup.js (collection setup script)
├── data-migration.js (data migration template)
└── pom.xml (updated dependencies)
```

## Conclusion

The migration from Oracle/JPA to MongoDB is now complete at the code level. The system has been restructured to work with MongoDB's document-oriented approach, using reference IDs instead of object relationships. The next phase involves data migration and testing to ensure all functionality works correctly with the new database system.
