- Please review @context-blotter.md .  It has been updated.  There is a section called "Sample Data for Security Type".  Please create a Flyway migration to insert the sample data into the security type table.  The database schema is in @context-blotter.md .  You have already created the Flyway migration for the schema as V1__initial_schema.sql.

- In @context-blotter.md , there is a section called "Sample Data for Blotter".  Please create a Flyway migration to insert the sample data into the blotter table.  The database schema is in @context-blotter.md .  Please log the prompt in cursor-prompts.md.  Please log your action in cursor-log.md

- In @requirements-blotter.md , please implement the APIs for Security Type as described in the Security Type section.  Please log this prompt in cursor-prompts.md. Please log your actions in cursor-log.md.

- Please remove lombok from build.gradle and from wherever it is used.  Please replace with getters, setters, constructors, equals, and hash code methods.  As usual, please record this prompt in cursor-prompts.md and your actions in cursor-log.md

- Please implement the REST API for Security Type as described in @requirements-blotter.md .  Please create the JPA repository, service interface, service implementation, and expose a REST web interface.  As usual, please record this prompt in cursor-prompts.md and your actions in cursor-log.md

- Please create unit tests for all the Security Type related classes you have created.  You can refer to cursor-log.md to see what you have created.  As usual, please record this prompt in cursor-prompts.md and your actions in cursor-log.md

- Please document the security type in @SecurityTypeController.java  in the README.md

- In the @requirements-blotter.md , please refer to the ### Blotter section.  Create the blotter entity, repository, service interface, service implementation, and REST controller.  Document it in the @README.md file in the same format as the documentation for Security Type.  As usual, please record this prompt in cursor-prompts.md and your actions in cursor-log.md

- Thanks.  Please create unit tests for all Blotter-related classes you just created. As usual, please record this prompt in cursor-prompts.md and your actions in cursor-log.md

- In the @requirements-blotter.md , please refer to the ### Blotter section.  Create the Flyway migration for the data provided in #### Sample Data for Blotter.    As usual, please record this prompt in cursor-prompts.md and your actions in cursor-log.md

- Please refer to the ### Trade Type section of #requirements-blotter.md.  Using the information provided in that section, please create the entity, repository, service interface, service implementation, REST controller, unit tests, and Flyway migration.  Add documentation for the service to README.md.  As usual, record the prompt in cursor-prompts.md and log your actions in cursor-log.md.

- Please refer to the ### Destination section of #requirements-blotter.md.  Using the information provided in that section, please create the entity, repository, service interface, service implementation, REST controller, unit tests, and Flyway migration.  Add documentation for the service to README.md.  As usual, record the prompt in cursor-prompts.md and log your actions in cursor-log.md.

### Order Type Implementation

Please refer to the ## Order Type section of #requirements-blotter.md. Using the information provided in that section, please create the entity, repository, service interface, service implementation, REST controller, unit tests, and Flyway migration. Add documentation for the service to README.md.

Created the following components:
1. OrderType entity with fields:
   - id (Integer)
   - abbreviation (String, max 10 chars)
   - description (String, max 60 chars)
   - version (Integer)

2. OrderTypeRepository interface extending JpaRepository

3. OrderTypeService interface with methods:
   - findAll()
   - findById()
   - save()
   - update()
   - delete()

4. OrderTypeServiceImpl implementing OrderTypeService with:
   - Standard CRUD operations
   - Optimistic locking using version field
   - Proper error handling

5. OrderTypeController with REST endpoints:
   - GET /order-type
   - GET /order-type/{id}
   - POST /order-type
   - PUT /order-type/{id}
   - DELETE /order-type/{id}?versionId={version}

6. Unit tests:
   - OrderTypeTest
   - OrderTypeServiceImplTest
   - OrderTypeControllerTest

7. Flyway migrations:
   - V7__order_type_schema.sql
   - V8__sample_order_type_data.sql

8. Updated README.md with API documentation

### Order Status Implementation

Please refer to the ### Order Status section of #requirements-blotter.md. Using the information provided in that section, please create the entity, repository, service interface, service implementation, REST controller, unit tests, and Flyway migration. Add documentation for the service to README.md.

Created the following components:
1. OrderStatus entity with fields:
   - id (Integer)
   - abbreviation (String, max 20 chars)
   - description (String, max 60 chars)
   - version (Integer)

2. OrderStatusRepository interface extending JpaRepository

3. OrderStatusService interface with methods:
   - findAll()
   - findById()
   - save()
   - update()
   - delete()

4. OrderStatusServiceImpl implementing OrderStatusService with:
   - Standard CRUD operations
   - Optimistic locking using version field
   - Proper error handling

5. OrderStatusController with REST endpoints:
   - GET /order-status
   - GET /order-status/{id}
   - POST /order-status
   - PUT /order-status/{id}
   - DELETE /order-status/{id}?versionId={version}

6. Unit tests:
   - OrderStatusTest
   - OrderStatusServiceImplTest
   - OrderStatusControllerTest

7. Flyway migrations:
   - V9__order_status_schema.sql
   - V10__sample_order_status_data.sql

8. Updated README.md with API documentation

## Security Service Implementation

Implemented the Security service components based on the requirements in requirements-blotter.md:

1. Created Security entity with fields:
   - id (Integer)
   - ticker (String, max 50 chars)
   - description (String, max 200 chars)
   - securityType (SecurityType)
   - version (Integer)

2. Created SecurityRepository interface extending JpaRepository.

3. Created SecurityService interface and SecurityServiceImpl with CRUD operations:
   - findAll()
   - findById()
   - save()
   - update()
   - delete()

4. Created SecurityController with REST endpoints:
   - GET /security
   - GET /security/{securityId}
   - POST /security
   - PUT /security/{securityId}
   - DELETE /security/{securityId}

5. Added comprehensive unit tests for all components.

6. Added sample data in V11__sample_security_data.sql.

7. Updated README.md with API documentation.

## Order Service Implementation

Implemented the Order service components based on the requirements in requirements-blotter.md:

1. Created Order entity with fields:
   - id (Integer)
   - security (Security)
   - blotter (Blotter)
   - quantity (BigDecimal)
   - orderTimestamp (OffsetDateTime)
   - orderType (OrderType)
   - orderStatus (OrderStatus)
   - version (Integer)

2. Created OrderRepository interface extending JpaRepository.

3. Created OrderService interface and OrderServiceImpl with operations:
   - findAll()
   - findById()
   - save() - with blotter assignment rule and default status
   - update()
   - delete() - with status check
   - updateBlotter()
   - updateStatus()

4. Created OrderController with REST endpoints:
   - GET /order
   - GET /order/{orderId}
   - POST /order
   - PUT /order/{orderId}
   - DELETE /order/{orderId}
   - POST /order/{orderId}/blotter/{blotterId}
   - POST /order/{orderId}/status/{statusId}

5. Added comprehensive unit tests for all components.

6. Added detailed API documentation to README.md including:
   - API endpoints
   - Data model
   - Business rules
   - Error responses
