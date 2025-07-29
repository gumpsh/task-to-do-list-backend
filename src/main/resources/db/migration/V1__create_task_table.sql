CREATE TABLE task (
        id serial PRIMARY KEY UNIQUE,
        name VARCHAR(40) NOT NULL,
        description VARCHAR(265),
        status VARCHAR(40) NOT NULL,
        createdDate TIMESTAMP
);
