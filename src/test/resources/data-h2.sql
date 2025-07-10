-- Roles
MERGE INTO roles (name) KEY(name) VALUES ('ROLE_USER');
MERGE INTO roles (name) KEY(name) VALUES ('ROLE_ADMIN');

-- Módulos
MERGE INTO modules (name, image) KEY(name) VALUES ('All', 'image');
MERGE INTO modules (name, image) KEY(name) VALUES ('Users', 'image');
MERGE INTO modules (name, image) KEY(name) VALUES ('Perfumes', 'image');

-- Submódulos
MERGE INTO submodules (name, id_module) KEY(name, id_module) VALUES ('ReadAll', 1);
MERGE INTO submodules (name, id_module) KEY(name, id_module) VALUES ('WriteAll', 1);
MERGE INTO submodules (name, id_module) KEY(name, id_module) VALUES ('ReadUsers', 2);
MERGE INTO submodules (name, id_module) KEY(name, id_module) VALUES ('WriteUsers', 2);
MERGE INTO submodules (name, id_module) KEY(name, id_module) VALUES ('ReadPerfumes', 3);
MERGE INTO submodules (name, id_module) KEY(name, id_module) VALUES ('WritePerfumes', 3);

-- Types
MERGE INTO types (name) KEY(name) VALUES ('Perfume');
MERGE INTO types (name) KEY(name) VALUES ('Agua de perfume');
MERGE INTO types (name) KEY(name) VALUES ('Agua de fragancia');
MERGE INTO types (name) KEY(name) VALUES ('Agua de colonia');

-- Brands
MERGE INTO brands (name) KEY(name) VALUES ('Tom Ford');
MERGE INTO brands (name) KEY(name) VALUES ('Guerlain');
MERGE INTO brands (name) KEY(name) VALUES ('Hermes');

-- Perfumes
MERGE INTO perfumes (name, brand_id, price, volume, season, description, fecha, image, tipos_id, version) KEY(name)
VALUES ('Black Orchid', 1, 150.0, 100.0, 'Invierno', 'Morado', 2006, 'tom_ford_black_orchid_edp', 2, 0);

MERGE INTO perfumes (name, brand_id, price, volume, season, description, fecha, image, tipos_id, version) KEY(name)
VALUES ('Habit Rouge Parfum', 2, 90.0, 100.0, 'Invierno', 'Alcohol', 2024, 'guerlain_habit_rouge_parfum',  1, 0);

MERGE INTO perfumes (name, brand_id, price, volume, season, description, fecha, image, tipos_id, version) KEY(name)
VALUES ('Terre d''Hermes', 3, 90.0, 100.0, 'Verano', 'Marte', 2006, 'hermes_terre_de_hermes_parfum', 4, 0);
