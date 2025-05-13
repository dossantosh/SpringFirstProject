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