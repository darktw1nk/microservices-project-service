CREATE TABLE projects (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    companyId INTEGER NOT NULL,
    status VARCHAR(255) NOT NULL,
    progress INTEGER NOT NULL,
    designPoints INTEGER NOT NULL,
    programmingPoints INTEGER NOT NULL,
    marketingPoints INTEGER NOT NULL,
    revenue INTEGER NOT NULL
);