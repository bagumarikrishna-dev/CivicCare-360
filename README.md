# Smart Civic 360 — Advanced Civic Issue Reporting System

A Java full-stack student project using Java 17+, Spring Boot 3.3.5, Spring Security/JWT, Spring Data JPA, MySQL 8+, React 18 and Axios.

## Advanced features
- Citizen registration/login with BCrypt password hashing and JWT API authentication.
- Create civic complaints with category, priority, description, address and optional image upload (max 5 MB).
- Optional browser GPS capture; saved coordinates open in Google Maps. GPS permission is optional and the user can type the address instead.
- Citizen dashboard to search/filter personal reports and view status/resolution notes.
- Admin dashboard with status counts, category distribution, unresolved priority overview, search/filter and CSV export.
- Admin status updates with resolution notes.
- Role-based restriction: only an ADMIN can see all reports, statistics and update status.

## Requirements
- JDK 17 or newer.
- Apache Maven 3.8+ (`mvn -version`).
- Node.js 18+ and npm.
- MySQL 8+.
- VS Code or IntelliJ IDEA.

## Before you start
Extract this folder separately from the original project. Stop the old Spring Boot app before starting this version, because both use port 8080. Keep the backend terminal and frontend terminal open while testing.

## 1. Create the database
In MySQL Workbench or MySQL command line, run:
```sql
CREATE DATABASE civic_db;
```
If you already created it, skip this step.

## 2. Configure MySQL
Open `backend/src/main/resources/application.properties`. Set `spring.datasource.username` and `spring.datasource.password` to your own MySQL credentials. Do not share your password in chat or commit it to a public repository.

The app uses `spring.jpa.hibernate.ddl-auto=update`, so the optional latitude/longitude columns are added automatically when the backend starts. Back up any important database before changing schema. The sample properties use MySQL username `root` and password `root`; replace the password with your actual local MySQL password. Do not send your password in chat. The JWT secret is a local demo value; replace it before any deployment.

## 3. Run the backend
Open a terminal in the `backend` folder and run:
```powershell
mvn spring-boot:run
```
Backend API: `http://localhost:8080/api`
Keep this terminal open. If port 8080 is occupied, stop the old app instance or change `server.port` in `application.properties` and update the API base URL in `frontend/src/api.js`.

## 4. Run the frontend
Open a second terminal in the `frontend` folder and run:
```powershell
npm install
npm start
```
Frontend: `http://localhost:3000`
Keep both terminals running.

## 5. Make an admin account
1. Register through the UI with the email you want to use for admin.
2. In MySQL, run the following after replacing the email with your registered email:
```sql
USE civic_db;
UPDATE users SET role='ADMIN' WHERE email='admin@example.com';
```
3. Log out and log in again so the new JWT contains the ADMIN role. The Admin dashboard link should appear.

## Demo checklist
1. Register a citizen and log in.
2. Submit a pothole or garbage report; try the GPS button if the browser grants location permission.
3. Open My Reports and search/filter the submitted report.
4. Promote a second account to ADMIN, then log in as that account.
5. Check the admin analytics, export CSV, and update a report status with resolution notes.
6. Return to the citizen account and verify the status update.

## Notes and limitations
- GPS works only when browser permission is granted; localhost is treated as a secure context by modern browsers. Manual location entry remains available.
- The generated JWT secret in `application.properties` is for local demonstration only. Replace it with a long random secret before any deployment.
- Uploaded images are stored locally in the `uploads` folder.
- This is an educational demo, not a production municipal system. Production deployment should add HTTPS, rate limiting, email verification, audit logs, malware scanning, managed file storage, backups, monitoring and privacy review.
- npm may print deprecation/audit warnings because the starter uses Create React App dependencies. Avoid `npm audit fix --force` unless you are ready to test dependency upgrades.
