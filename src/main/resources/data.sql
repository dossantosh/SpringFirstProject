-- Roles
INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_ADMIN')
ON CONFLICT (name) DO NOTHING;

-- Módulos
INSERT INTO modules (name, image) VALUES 
('All', 'image'), 
('Users', 'image'), 
('Perfumes', 'image') 
ON CONFLICT (name) DO NOTHING;

-- Submódulos
INSERT INTO submodules (name, id_module) VALUES
('ReadAll', 1),
('WriteAll', 1),
('ReadUsers', 2),
('WriteUsers', 2),
('ReadPerfumes', 3),
('WritePerfumes', 3)
ON CONFLICT (name, id_module) DO NOTHING;  -- if (name, id_module) is unique

-- Types
INSERT INTO types (name) VALUES 
('Perfume'),
('Agua de perfume'),
('Agua de fragancia'),
('Agua de colonia')
ON CONFLICT (name) DO NOTHING;

-- Brands
INSERT INTO brands (name) VALUES 
('Tom Ford'),
('Guerlain'),
('Hermes')
ON CONFLICT (name) DO NOTHING;

-- Perfumes
INSERT INTO perfumes (name, brand_id, price, volume, season, description, fecha, image, tipos_id, version)
VALUES 
('Black Orchid', 1, 150.0, 100.0, 'Invierno', 'Morado', 2006, 'tom_ford_black_orchid_edp', 2, 0),
('Habit Rouge Parfum', 2, 90.0, 100.0, 'Invierno', 'Alcohol', 2024, 'guerlain_habit_rouge_parfum',  1, 0),
('Terre D''hermes', 3, 90.0, 100.0, 'Verano', 'Marte', 2006, 'hermes_terre_de_hermes_parfum', 4, 0)
ON CONFLICT (name) DO NOTHING;
