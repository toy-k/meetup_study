-- CREATE USER ${INIT_SQL_USER}@'localhost' IDENTIFIED BY ${PASSWORD};
-- CREATE USER ${INIT_SQL_USER}@'%' IDENTIFIED BY ${PASSWORD};
--
-- GRANT ALL PRIVILEGES ON *.* TO
-- ${INIT_SQL_USER}@'localhost';
-- GRANT ALL PRIVILEGES ON *.* TO
-- ${INIT_SQL_USER}@'%';

CREATE DATABASE meetup_study_DB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

grant all privileges on meetup_study_DB.* to 'user'@'%' identified by '1234';

grant replication slave on *.* to 'user'@'%';

flush privileges;

CREATE DATABASE meetup_slave_DB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;