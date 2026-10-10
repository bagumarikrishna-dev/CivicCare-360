CREATE DATABASE IF NOT EXISTS civic_db;
USE civic_db;
-- Spring Boot JPA creates/updates the users and complaints tables at startup.
-- After registering an account in the app, promote that account to admin with:
-- UPDATE users SET role='ADMIN' WHERE email='admin@example.com';
