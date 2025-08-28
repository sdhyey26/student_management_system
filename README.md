## Student Management System (Java, MySQL)

### Overview
Command-line Student Management System with modules for Students, Teachers, Courses, Fees, Dashboards, Analysis, and Helpdesk. Uses JDBC to connect to MySQL and loads configuration from a .env file.

### Project Structure
- `student_managaement_system/src/com/sms/` — application source
  - `main/` — menu entry modules (no direct `public static void main` here)
  - `controller/`, `service/`, `dao/`, `model/`, `utils/`, `exception/`, `database/`
- `student_managaement_system/src/com/sms/test/Main.java` — CLI entrypoint
- `student_managaement_system/helpdesk_tickets_table.sql` — schema for Helpdesk tickets table
- JARs: `mysql-connector-j-8.0.32 2.jar`, `dotenv-java-3.2.0.jar`

### Requirements
- Java 17+ (compatible JDK)
- MySQL 8.x

### Configuration (.env)
Create `student_managaement_system/.env` with:
```
DB_URL=jdbc:mysql://localhost:3306/sms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```
Notes:
- The application throws if any env var is missing (`DBConfig` uses dotenv).
- Ensure the `sms` database exists: `CREATE DATABASE sms;`

### Database Setup
1) Create the database (if not exists):
```sql
CREATE DATABASE IF NOT EXISTS sms;
```
2) Use your preferred migration approach to create required tables. A ready-made script for the Helpdesk module is provided:
```sql
-- Run from MySQL client
USE sms;
SOURCE student_managaement_system/helpdesk_tickets_table.sql;
```
3) Other modules (students, courses, subjects, fees, teachers, dashboards) expect tables like `students`, `profiles`, `courses`, `student_courses`, `fees`, etc. Ensure these exist before running. Review DAOs under `src/com/sms/dao/` for exact schemas if creating manually.

### Build and Run
This repository does not use Maven/Gradle; compile with `javac` and include the provided JARs on the classpath.

From the repository root:
```bash
# 1) Compile (adjust JAVA_HOME if needed)
javac -cp "student_managaement_system/dotenv-java-3.2.0.jar:student_managaement_system/mysql-connector-j-8.0.32 2.jar" \
  -d student_managaement_system/bin \
  $(find student_managaement_system/src -name "*.java")

# 2) Run the CLI entrypoint
java -cp "student_managaement_system/bin:student_managaement_system/dotenv-java-3.2.0.jar:student_managaement_system/mysql-connector-j-8.0.32 2.jar" \
  com.sms.test.Main
```

Tip: On Windows, replace `:` with `;` in the classpath. If the MySQL connector filename with a space causes issues, rename it and update the classpath.

### Usage
When launched, the main menu appears:
- 1: Student Management
- 2: Teacher Management
- 3: Fees Management
- 4: Course Management
- 5: Dashboard
- 6: Analysis
- 7: Helpdesk

Navigate using numeric choices. Submenus include operations like listing, adding, assigning courses, paying fees, analytics, and helpdesk ticket management.

### Development Notes
- DB connection is established via `DBConnection` using `DBConfig.DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
- Auto-commit is enabled; transactions are not manually managed.
- For schema details, consult DAOs in `src/com/sms/dao/` and models in `src/com/sms/model/`.

### Troubleshooting
- "Required environment variable ... is not set": ensure `.env` exists at `student_managaement_system/.env` and variables are set.
- "Failed to establish database connection": verify MySQL is running and credentials/URL are correct.
- Classpath errors: ensure both JARs are present and classpath separators are correct for your OS.

### License
Add your license information here.

