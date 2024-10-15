-- Insérer les pays
INSERT INTO countries (code, name) VALUES
('FR', 'France'),
('BE', 'Belgique'),
('DE', 'Germany'),
('LU', 'Luxembourg'),
('MA', 'Maroc'),
('TN', 'Tunisia'),
('PT', 'Portugal');

-- Insérer les provinces pour chaque pays
-- France
INSERT INTO provinces (code, name, country_id) VALUES
('IDF', 'Île-de-France', (SELECT id FROM countries WHERE code = 'FR')),
('NAQ', 'Nouvelle-Aquitaine', (SELECT id FROM countries WHERE code = 'FR')),
('OCC', 'Occitanie', (SELECT id FROM countries WHERE code = 'FR')),
('PAC', 'Provence-Alpes-Côte d\'Azur', (SELECT id FROM countries WHERE code = 'FR'));

-- Belgique
INSERT INTO provinces (code, name, country_id) VALUES
('BRU', 'Bruxelles-Capitale', (SELECT id FROM countries WHERE code = 'BE')),
('WAL', 'Wallonie', (SELECT id FROM countries WHERE code = 'BE')),
('FLA', 'Flandre', (SELECT id FROM countries WHERE code = 'BE'));

-- Germany
INSERT INTO provinces (code, name, country_id) VALUES
('BY', 'Bayern', (SELECT id FROM countries WHERE code = 'DE')),
('BE', 'Berlin', (SELECT id FROM countries WHERE code = 'DE')),
('BW', 'Baden-Württemberg', (SELECT id FROM countries WHERE code = 'DE'));

-- Luxembourg
INSERT INTO provinces (code, name, country_id) VALUES
('LUX', 'Luxembourg District', (SELECT id FROM countries WHERE code = 'LU'));

-- Maroc
INSERT INTO provinces (code, name, country_id) VALUES
('CAS', 'Casablanca', (SELECT id FROM countries WHERE code = 'MA')),
('RAB', 'Rabat', (SELECT id FROM countries WHERE code = 'MA')),
('MAR', 'Marrakech', (SELECT id FROM countries WHERE code = 'MA'));

-- Tunisie
INSERT INTO provinces (code, name, country_id) VALUES
('TUN', 'Tunis', (SELECT id FROM countries WHERE code = 'TN')),
('SFAX', 'Sfax', (SELECT id FROM countries WHERE code = 'TN')),
('SOUS', 'Sousse', (SELECT id FROM countries WHERE code = 'TN'));

-- Portugal
INSERT INTO provinces (code, name, country_id) VALUES
('LIS', 'Lisboa', (SELECT id FROM countries WHERE code = 'PT')),
('POR', 'Porto', (SELECT id FROM countries WHERE code = 'PT')),
('ALG', 'Algarve', (SELECT id FROM countries WHERE code = 'PT'));
