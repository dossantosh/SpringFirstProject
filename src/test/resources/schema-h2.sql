-- Roles
CREATE TABLE roles (
    id_role SERIAL PRIMARY KEY,
    name VARCHAR(20) UNIQUE NOT NULL
);

-- Modules
CREATE TABLE modules (
    id_module SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    image VARCHAR(50)
);

-- Submodules
CREATE TABLE submodules (
    id_submodule SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    id_module INT NOT NULL,
    CONSTRAINT fk_module FOREIGN KEY (id_module) REFERENCES modules(id_module),
    CONSTRAINT unique_name_module UNIQUE (name, id_module)
);

-- Users
CREATE TABLE users (
    id_user SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    email VARCHAR(50) UNIQUE,
    enabled BOOLEAN,
    password VARCHAR(100)
);

-- Join tables ManyToMany

CREATE TABLE users_roles (
    id_user INT NOT NULL,
    id_role INT NOT NULL,
    PRIMARY KEY (id_user, id_role),
    CONSTRAINT fk_users_roles_user FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    CONSTRAINT fk_users_roles_role FOREIGN KEY (id_role) REFERENCES roles(id_role) ON DELETE CASCADE
);

CREATE TABLE users_modules (
    id_user INT NOT NULL,
    id_module INT NOT NULL,
    PRIMARY KEY (id_user, id_module),
    CONSTRAINT fk_users_modules_user FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    CONSTRAINT fk_users_modules_module FOREIGN KEY (id_module) REFERENCES modules(id_module) ON DELETE CASCADE
);

CREATE TABLE users_submodules (
    id_user INT NOT NULL,
    id_submodule INT NOT NULL,
    PRIMARY KEY (id_user, id_submodule),
    CONSTRAINT fk_users_submodules_user FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    CONSTRAINT fk_users_submodules_submodule FOREIGN KEY (id_submodule) REFERENCES submodules(id_submodule) ON DELETE CASCADE
);

-- Types
CREATE TABLE types (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- Brands
CREATE TABLE brands (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- Perfumes
CREATE TABLE perfumes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    brand_id INT,
    tipos_id INT,
    price FLOAT CHECK (price >= 10.0 AND price <= 1000.0),
    volume FLOAT CHECK (volume >= 1.0 AND volume <= 1000.0),
    season VARCHAR(50),
    description VARCHAR(150),
    fecha INT CHECK (fecha >= 1900 AND fecha <= 2100),
    image VARCHAR(150),
    version INT,
    CONSTRAINT fk_perfumes_brand FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE SET NULL,
    CONSTRAINT fk_perfumes_tipo FOREIGN KEY (tipos_id) REFERENCES types(id) ON DELETE SET NULL
);
