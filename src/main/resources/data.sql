-- ESTO ES snake_case HECHO EXPLICITAMENTE PARA POSTGRESQL
-- Roles
INSERT INTO roles (name_role) VALUES
('ROLE_ADMIN'),
('ROLE_USER')
ON CONFLICT (name_role) DO NOTHING;  -- Conflicto en la columna única

-- Módulos
INSERT INTO modules (name_module, imagen_module) VALUES 
('Admin', 'imagen'), 
('Usuarios', 'imagen') 
ON CONFLICT (name_module) DO NOTHING;

-- Submódulos
INSERT INTO submodules (name_submodule, id_module) VALUES
('Admin', 1),
('Usuarios', 2)
ON CONFLICT (name_submodule) DO NOTHING;  -- Asumiendo que esta combinación debe ser única