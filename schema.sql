CREATE DATABASE IF NOT EXISTS assignment5;
USE assignment5;

CREATE TABLE IF NOT EXISTS bus_pass (
    pass_id VARCHAR(20) PRIMARY KEY,
    reg_no VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    route_no INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL
);

-- Test-only table for the SQL injection demonstration.
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    pwd VARCHAR(100) NOT NULL
);
INSERT IGNORE INTO users(id, pwd) VALUES ('testuser', 'correct-password');
