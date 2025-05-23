-- ESTO ES snake_case HECHO EXPLICITAMENTE PARA POSTGRESQL
-- Roles
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_USER')
ON CONFLICT (name) DO NOTHING;  -- Conflicto en la columna única

-- Módulos
INSERT INTO modules (name, imagen) VALUES 
('Admin', 'imagen'), 
('Usuarios', 'imagen') 
ON CONFLICT (name) DO NOTHING;

-- Submódulos
INSERT INTO submodules (name, id_module) VALUES
('Admin', 1),
('Usuarios', 2)
ON CONFLICT (name) DO NOTHING;  -- Asumiendo que esta combinación debe ser única

-- Tipos
INSERT INTO tipos (name) VALUES ('Perfume') ON CONFLICT (name) DO NOTHING;
INSERT INTO tipos (name) VALUES ('Agua de perfume') ON CONFLICT (name) DO NOTHING;
INSERT INTO tipos (name) VALUES ('Agua de fragancia') ON CONFLICT (name) DO NOTHING;
INSERT INTO tipos (name) VALUES ('Agua de colonia') ON CONFLICT (name) DO NOTHING;

-- Marcas
INSERT INTO brands (name) VALUES ('Tom Ford') ON CONFLICT (name) DO NOTHING;
INSERT INTO brands (name) VALUES ('Guerlain') ON CONFLICT (name) DO NOTHING;
INSERT INTO brands (name) VALUES ('Hermes') ON CONFLICT (name) DO NOTHING;

-- Perfumes
INSERT INTO perfumes (name, brand_id, price, volume, season, description, fecha, tipos_id)
VALUES ('Black Orchid', 1, 150.0, 100.0, 'Invierno', 'Morado', 2006, 2) ON CONFLICT (name) DO NOTHING;

INSERT INTO perfumes (name, brand_id, price, volume, season, description, fecha, tipos_id)
VALUES ('Habit Rouge Parfum', 2, 90.0, 100.0, 'Invierno', 'Alcohol', 2024, 1) ON CONFLICT (name) DO NOTHING;

INSERT INTO perfumes (name, brand_id, price, volume, season, description, fecha, tipos_id)
VALUES ('Terre D''hermes', 3, 90.0, 100.0, 'Verano', 'Marte', 2006, 4) ON CONFLICT (name) DO NOTHING;