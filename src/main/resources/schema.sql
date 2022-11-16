
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    password VARCHAR(64) NOT NULL,

    CONSTRAINT uq_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS messages (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    owner BIGINT NOT NULL,
    message VARCHAR(1024) NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (owner) REFERENCES users(id)
);
