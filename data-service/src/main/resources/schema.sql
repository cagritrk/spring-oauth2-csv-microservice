CREATE TABLE customers (
    index INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    company VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),
    phone1 VARCHAR(255),
    phone2 VARCHAR(255),
    email VARCHAR(255),
    subscription_date DATE,
    website VARCHAR(255)
);