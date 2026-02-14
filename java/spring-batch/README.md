
## Debug the project 

```shell
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"
```

## File Support

The application supports both CSV and Excel files:

- **CSV files**: Place in `src/main/resources/employees.csv`
- **Excel files**: Place in `src/main/resources/employees.xlsx`

To specify which file to use:
```shell
mvn spring-boot:run -Dinput.file=employees.xlsx
```

## Connect to Database

```shell
psql -h localhost -d postgres
```

List all the users 

```shell
\du
```
Result

```txt
                                     List of roles
Role name   |                         Attributes                         | Member of
---------------+------------------------------------------------------------+-----------
airflow       |                                                            | {}
zainabfirdaus | Superuser, Create role, Create DB, Replication, Bypass RLS | {}
```
Debug the project 

```shell
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005"

```

Auto created tables :
```sql
 
BATCH_JOB_INSTANCE

BATCH_JOB_EXECUTION

BATCH_JOB_EXECUTION_PARAMS

BATCH_JOB_EXECUTION_CONTEXT

BATCH_STEP_EXECUTION

BATCH_STEP_EXECUTION_CONTEXT
```