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
