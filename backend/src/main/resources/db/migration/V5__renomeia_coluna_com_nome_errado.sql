-- O nome da coluna tinha sido escrito em camelCase antes, isso corrige renomeando para snake_case
ALTER TABLE perfil
RENAME COLUMN areasDeInteresse TO areas_de_interesse;