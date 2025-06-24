-- Companies
INSERT INTO companies (vat_number, com_name, legal_name, email, web) VALUES
('ESB12345678', 'Empresa Uno S.L.', 'Empresa Uno Sociedad Limitada', 'info@empresa1.com', 'https://empresa1.com');

INSERT INTO companies (vat_number, com_name, legal_name, email, web) VALUES
('ESA87654321', 'Empresa Dos S.L.', 'Empresa Dos Sociedad Limitada', 'info@empresa2.com', 'https://empresa2.com');

UPDATE companies 
SET id_owner_company = id_company 
WHERE vat_number IN ('ESB12345678', 'ESA87654321');

-- Addresses
INSERT INTO addresses (street, st_number, apt, cp, city, state, country, id_company) VALUES
('Calle Mayor', '10', '1ºA', '28013', 'Madrid', 'Madrid', 'España', 1);

INSERT INTO addresses (street, st_number, apt, cp, city, state, country, id_company) VALUES
('Avenida del Sol', '25', '2ºB', '08029', 'Barcelona', 'Cataluña', 'España', 2);

-- Phones
INSERT INTO phones (phone_number, kind, id_company) VALUES
('+34910000001', 'fijo', 1),
('+34600000001', 'móvil', 1);

INSERT INTO phones (phone_number, kind, id_company) VALUES
('+34910000002', 'fijo', 2),
('+34600000002', 'móvil', 2);

-- Contact Persons
INSERT INTO contact_persons (firstname, middlename, lastname, role, email, id_company) VALUES
('Laura', 'Gómez', '', 'Gerente', 'laura@empresa1.com', 1);

INSERT INTO contact_persons (firstname, middlename, lastname, role, email, id_company) VALUES
('Carlos', 'Ruiz', '', 'CEO', 'carlos@empresa2.com', 2);

-- Bank Accounts
INSERT INTO bank_accounts (iban, swift, holder, branch, id_company) VALUES
('ES9121000418450200051332', 'CAIXESBBXXX', 'Empresa Uno S.L.', 'Oficina Central', 1);

INSERT INTO bank_accounts (iban, swift, holder, branch, id_company) VALUES
('ES9820385778983000760236', 'BBVAESMMXXX', 'Empresa Dos S.L.', 'Sucursal Norte', 2);

-- Users
INSERT INTO users (user_name, passwd, email, id_company, id_role, id_plan) VALUES
('admin1',      '$10$F2NpHQBFhVrwbCH0mW3mo.1wJD4eQx.fsneEgXfLlWfOc0Q3O2fWO', 'admin1@example.com',      1, 1, 1),
('accounting1', '$10$chOc95TWlCVF1RCIGFeg7uAwBfOOPcE34w9Z9PR/BINgOZBvSkq1W', 'accounting1@example.com', 1, 2, 1),
('user1',       '$10$n0c11E/Ng/rKClk3sKGI5..AHsvl72WtG4tUP2bjkIAB3O6yKRGUy', 'user1@example.com',       1, 3, 1);

INSERT INTO users (user_name, passwd, email, id_company, id_role, id_plan) VALUES
('admin2',      '$10$SY8QEWZh9Va5nOGtfXBAxu3cdgf/fRoG50EmW4nftOu3L.dFrGRX.', 'admin2@example.com',      2, 1, 1),
('accounting2', '$10$LKjF4sv6XrD7y5l0P5.Bj.iYHKaAWPYtj/3jlMj2n.g.3zwcvYfLG', 'accounting2@example.com', 2, 2, 1),
('user2',       '$10$.0NqH77yhqmna1wIluV9su3HtW7BSJEEfDEqcKzWdnoredrVSRsZC', 'user2@example.com',       2, 3, 1);

-- Customers

-- 5 Customers for Company 1 (Empresa Uno S.L.)
INSERT INTO companies (vat_number, com_name, legal_name, email, web, id_owner_company) VALUES
('ESB11111111', 'Cliente Uno S.A.', 'Cliente Uno Sociedad Anónima', 'info@cliente1.com', 'https://cliente1.com', 1),
('ESB22222222', 'Cliente Dos S.L.', 'Cliente Dos Sociedad Limitada', 'info@cliente2.com', 'https://cliente2.com', 1),
('ESB33333333', 'Cliente Tres S.L.', 'Cliente Tres Sociedad Limitada', 'info@cliente3.com', 'https://cliente3.com', 1),
('ESB44444444', 'Cliente Cuatro S.A.', 'Cliente Cuatro Sociedad Anónima', 'info@cliente4.com', 'https://cliente4.com', 1),
('ESB55555555', 'Cliente Cinco S.L.', 'Cliente Cinco Sociedad Limitada', 'info@cliente5.com', 'https://cliente5.com', 1);

-- Addresses for Customers of Company 1
INSERT INTO addresses (street, st_number, apt, cp, city, state, country, id_company) VALUES
('Calle del Cliente', '1', 'A', '28001', 'Madrid', 'Madrid', 'España', 3),
('Avenida Principal', '10', NULL, '08001', 'Barcelona', 'Cataluña', 'España', 4),
('Plaza Central', '5', '3ºD', '46001', 'Valencia', 'Valencia', 'España', 5),
('Calle Secundaria', '15', '2ºB', '41001', 'Sevilla', 'Andalucía', 'España', 6),
('Paseo del Parque', '20', NULL, '48001', 'Bilbao', 'País Vasco', 'España', 7);

-- Phones for Customers of Company 1
INSERT INTO phones (phone_number, kind, id_company) VALUES
('+34911000011', 'fijo', 3),
('+34600000111', 'móvil', 3),
('+34911000022', 'fijo', 4),
('+34600000222', 'móvil', 4),
('+34911000033', 'fijo', 5),
('+34600000333', 'móvil', 5),
('+34911000044', 'fijo', 6),
('+34600000444', 'móvil', 6),
('+34911000055', 'fijo', 7),
('+34600000555', 'móvil', 7);

-- Contacts for Customers of Company 1
INSERT INTO contact_persons (firstname, middlename, lastname, role, email, id_company) VALUES
('Ana', '', 'García', 'Directora de Compras', 'ana.garcia@cliente1.com', 3),
('Luis', 'Miguel', 'Fernández', 'Gerente', 'luis.fernandez@cliente2.com', 4),
('Marta', '', 'Rodríguez', 'Responsable de Proyectos', 'marta.rodriguez@cliente3.com', 5),
('Javier', '', 'Martínez', 'Director Financiero', 'javier.martinez@cliente4.com', 6),
('Sofía', '', 'López', 'CEO', 'sofia.lopez@cliente5.com', 7);

-- Bank Accounts for Customers of Company 1
INSERT INTO bank_accounts (iban, swift, holder, branch, id_company) VALUES
('ES7621000000000000001111', 'CAIXESBBXXX', 'Cliente Uno S.A.', 'Madrid Centro', 3),
('ES7621000000000000002222', 'BBVAESMMXXX', 'Cliente Dos S.L.', 'Barcelona Plaza', 4),
('ES7621000000000000003333', 'BSCHESMMXXX', 'Cliente Tres S.L.', 'Valencia Norte', 5),
('ES7621000000000000004444', 'SABCESBBXXX', 'Cliente Cuatro S.A.', 'Sevilla Este', 6),
('ES7621000000000000005555', 'BASKES2BXXX', 'Cliente Cinco S.L.', 'Bilbao Deusto', 7);

-- Customers for Company 1
INSERT INTO customers (invoicing_method, duedate, pay_method, default_language, default_vat, default_withholding, europe, enabled, id_company, id_owner_company) VALUES
('Mensual', 30, 'Transferencia', 'es', 0.21, 0.15, 1, 1, 3, 1),
('Trimestral', 60, 'Domiciliación', 'en', 0.21, 0.15, 1, 1, 4, 1),
('Anual', 90, 'Transferencia', 'fr', 0.21, 0.10, 1, 1, 5, 1),
('Bimestral', 45, 'Tarjeta', 'de', 0.21, 0.20, 1, 1, 6, 1),
('Semestral', 30, 'Efectivo', 'it', 0.10, 0.00, 1, 1, 7, 1);

-- 5 Customers for Company 2 (Empresa Dos S.L.)
INSERT INTO companies (vat_number, com_name, legal_name, email, web, id_owner_company) VALUES
('ESA66666666', 'Cliente Seis S.L.', 'Cliente Seis Sociedad Limitada', 'info@cliente6.com', 'https://cliente6.com', 2),
('ESA77777777', 'Cliente Siete S.A.', 'Cliente Siete Sociedad Anónima', 'info@cliente7.com', 'https://cliente7.com', 2),
('ESA88888888', 'Cliente Ocho S.L.', 'Cliente Ocho Sociedad Limitada', 'info@cliente8.com', 'https://cliente8.com', 2),
('ESA99999999', 'Cliente Nueve S.A.', 'Cliente Nueve Sociedad Anónima', 'info@cliente9.com', 'https://cliente9.com', 2),
('ESA10101010', 'Cliente Diez S.L.', 'Cliente Diez Sociedad Limitada', 'info@cliente10.com', 'https://cliente10.com', 2);

-- Addresses for Customers of Company 2
INSERT INTO addresses (street, st_number, apt, cp, city, state, country, id_company) VALUES
('Calle del Mar', '100', '1ºA', '08003', 'Barcelona', 'Cataluña', 'España', 8),
('Avenida de la Libertad', '25', NULL, '28004', 'Madrid', 'Madrid', 'España', 9),
('Paseo de la Habana', '30', '4ºD', '28036', 'Madrid', 'Madrid', 'España', 10),
('Calle Mallorca', '200', NULL, '08008', 'Barcelona', 'Cataluña', 'España', 11),
('Gran Vía', '50', '5ºB', '28013', 'Madrid', 'Madrid', 'España', 12);

-- Phones for Customers of Company 2
INSERT INTO phones (phone_number, kind, id_company) VALUES
('+34912000066', 'fijo', 8),
('+34600000666', 'móvil', 8),
('+34912000077', 'fijo', 9),
('+34600000777', 'móvil', 9),
('+34912000088', 'fijo', 10),
('+34600000888', 'móvil', 10),
('+34912000099', 'fijo', 11),
('+34600000999', 'móvil', 11),
('+34912000101', 'fijo', 12),
('+34600001010', 'móvil', 12);

-- Contacts for Customers of Company 2
INSERT INTO contact_persons (firstname, middlename, lastname, role, email, id_company) VALUES
('David', 'Sánchez', 'Rodriguez', 'Director Comercial', 'david.sanchez@cliente6.com', 8),
('Elena', 'Pérez', '', 'Responsable de Ventas', 'elena.perez@cliente7.com', 9),
('Pablo', 'González', 'Perez', 'CEO', 'pablo.gonzalez@cliente8.com', 10),
('Lucía', 'Díaz', '', 'Directora Financiera', 'lucia.diaz@cliente9.com', 11),
('Raúl', 'Hernández', '', 'Gerente', 'raul.hernandez@cliente10.com', 12);

-- Bank Accounts for Customers of Company 2
INSERT INTO bank_accounts (iban, swift, holder, branch, id_company) VALUES
('ES8521000000000000006666', 'CAIXESBBXXX', 'Cliente Seis S.L.', 'Barcelona Centro', 8),
('ES8521000000000000007777', 'BBVAESMMXXX', 'Cliente Siete S.A.', 'Madrid Norte', 9),
('ES8521000000000000008888', 'BSCHESMMXXX', 'Cliente Ocho S.L.', 'Madrid Sur', 10),
('ES8521000000000000009999', 'SABCESBBXXX', 'Cliente Nueve S.A.', 'Barcelona Diagonal', 11),
('ES8521000000000000010101', 'BASKES2BXXX', 'Cliente Diez S.L.', 'Madrid Castellana', 12);

-- Customers for Company 2
INSERT INTO customers (invoicing_method, duedate, pay_method, default_language, default_vat, default_withholding, europe, enabled, id_company, id_owner_company) VALUES
('Mensual', 15, 'Transferencia', 'es', 0.21, 0.15, 1, 1, 8, 2),
('Bimestral', 30, 'Domiciliación', 'en', 0.21, 0.10, 1, 1, 9, 2),
('Trimestral', 60, 'Tarjeta', 'fr', 0.21, 0.20, 1, 1, 10, 2),
('Semestral', 90, 'Transferencia', 'de', 0.10, 0.00, 1, 1, 11, 2),
('Anual', 30, 'Efectivo', 'it', 0.21, 0.15, 1, 1, 12, 2);

-- Schemes for Company 1
-- Price Schemes for Customers (id_companies 3 to 12)

-- Customer 1 (Company 3, Owner 1)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Basic Spanish-English', 0.10, 'word', 'totalWords', 'es', 'en', 3, 1);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('General translation', 0, 1),
('Legal documents', 5, 1);

-- Customer 2 (Company 4, Owner 1)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Technical Translations', 0.12, 'word', 'totalWords', 'en', 'es', 4, 1);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Manuals', 0, 2),
('Instructions', 3, 2),
('Datasheets', 2, 2);

-- Customer 3 (Company 5, Owner 1)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Basic French', 0.09, 'word', 'totalWords', 'fr', 'es', 5, 1);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Email translation', 0, 3);

-- Customer 4 (Company 6, Owner 1)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('DE-ES Standard', 0.11, 'word', 'totalWords', 'de', 'es', 6, 1);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Technical content', 4, 4),
('Legal agreements', 2, 4);

-- Customer 5 (Company 7, Owner 1)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Italian Pack', 0.10, 'word', 'totalWords', 'it', 'es', 7, 1);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Commercial', 5, 5);

-- Customer 6 (Company 8, Owner 2)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Monthly Spanish', 0.08, 'word', 'totalWords', 'es', 'en', 8, 2);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Blog posts', 0, 6),
('Product descriptions', 2, 6);

-- Customer 7 (Company 9, Owner 2)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('EN to DE', 0.13, 'word', 'totalWords', 'en', 'de', 9, 2);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Legal translation', 0, 7);

-- Customer 8 (Company 10, Owner 2)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('FR-EN Services', 0.12, 'word', 'totalWords', 'fr', 'en', 10, 2);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Contracts', 2, 8),
('Business letters', 1, 8),
('Internal docs', 3, 8);

-- Customer 9 (Company 11, Owner 2)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Multilingual ES', 0.09, 'word', 'totalWords', 'es', 'fr', 11, 2);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Marketing material', 5, 9);

-- Customer 10 (Company 12, Owner 2)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Special DE-FR', 0.14, 'word', 'totalWords', 'de', 'fr', 12, 2);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Technical sheets', 4, 10),
('Product labels', 2, 10);

-- Providers

-- Providers for Company 1
-- Companies
INSERT INTO companies (vat_number, com_name, legal_name, email, web, id_owner_company) VALUES
('ESPV1111111', 'Proveedor Uno S.L.', 'Proveedor Uno Sociedad Limitada', 'info@proveedor1.com', 'https://proveedor1.com', 1),
('ESPV2222222', 'Proveedor Dos S.A.', 'Proveedor Dos Sociedad Anónima', 'info@proveedor2.com', 'https://proveedor2.com', 1),
('ESPV3333333', 'Proveedor Tres S.L.', 'Proveedor Tres Sociedad Limitada', 'info@proveedor3.com', 'https://proveedor3.com', 1);

-- Addresses
INSERT INTO addresses (street, st_number, apt, cp, city, state, country, id_company) VALUES
('Calle Servicios', '101', NULL, '28010', 'Madrid', 'Madrid', 'España', 13),
('Avda. Proveedores', '22', '2ºB', '08010', 'Barcelona', 'Cataluña', 'España', 14),
('Camino Industrial', '7', NULL, '41010', 'Sevilla', 'Andalucía', 'España', 15);

-- Phones
INSERT INTO phones (phone_number, kind, id_company) VALUES
('+34913000013', 'fijo', 13),
('+34610000113', 'móvil', 13),
('+34913000014', 'fijo', 14),
('+34610000114', 'móvil', 14),
('+34913000015', 'fijo', 15),
('+34610000115', 'móvil', 15);

-- Contacts
INSERT INTO contact_persons (firstname, middlename, lastname, role, email, id_company) VALUES
('Pedro', 'López', 'Hernandez', 'Comercial', 'pedro.lopez@proveedor1.com', 13),
('Sara', 'Martín', '', 'Gestora', 'sara.martin@proveedor2.com', 14),
('Alberto', 'Gómez', 'Serrano', 'Jefe de Proyectos', 'alberto.gomez@proveedor3.com', 15);

-- Bank Accounts
INSERT INTO bank_accounts (iban, swift, holder, branch, id_company) VALUES
('ES8821000000000000011111', 'CAIXESBBXXX', 'Proveedor Uno S.L.', 'Madrid Central', 13),
('ES8821000000000000022222', 'BBVAESMMXXX', 'Proveedor Dos S.A.', 'Barcelona Centro', 14),
('ES8821000000000000033333', 'BSCHESMMXXX', 'Proveedor Tres S.L.', 'Sevilla Industrial', 15);

-- Providers
INSERT INTO providers (default_language, default_vat, default_withholding, europe, enabled, id_company, id_owner_company) VALUES
('es', 0.21, 0.15, 1, 1, 13, 1),
('es', 0.21, 0.10, 1, 1, 14, 1),
('en', 0.21, 0.00, 1, 1, 15, 1);

-- Providers for Company 2
-- Companies
INSERT INTO companies (vat_number, com_name, legal_name, email, web, id_owner_company) VALUES
('ESPV4444444', 'Proveedor Cuatro S.A.', 'Proveedor Cuatro Sociedad Anónima', 'info@proveedor4.com', 'https://proveedor4.com', 2),
('ESPV5555555', 'Proveedor Cinco S.L.', 'Proveedor Cinco Sociedad Limitada', 'info@proveedor5.com', 'https://proveedor5.com', 2),
('ESPV6666666', 'Proveedor Seis S.A.', 'Proveedor Seis Sociedad Anónima', 'info@proveedor6.com', 'https://proveedor6.com', 2);

-- Addresses
INSERT INTO addresses (street, st_number, apt, cp, city, state, country, id_company) VALUES
('Avda. del Proveedor', '50', '4ºC', '03003', 'Alicante', 'Comunidad Valenciana', 'España', 16),
('Calle Mayoristas', '18', NULL, '29001', 'Málaga', 'Andalucía', 'España', 17),
('Paseo del Sur', '77', NULL, '35001', 'Las Palmas', 'Canarias', 'España', 18);

-- Phones
INSERT INTO phones (phone_number, kind, id_company) VALUES
('+34914000016', 'fijo', 16),
('+34620000116', 'móvil', 16),
('+34914000017', 'fijo', 17),
('+34620000117', 'móvil', 17),
('+34914000018', 'fijo', 18),
('+34620000118', 'móvil', 18);

-- Contacts
INSERT INTO contact_persons (firstname, middlename, lastname, role, email, id_company) VALUES
('Marina', 'Navarro', 'Diaz', 'Gestora', 'marina.navarro@proveedor4.com', 16),
('Fernando', 'Ortega', '', 'Jefe Técnico', 'fernando.ortega@proveedor5.com', 17),
('Isabel', 'Ramírez', '', 'Responsable Comercial', 'isabel.ramirez@proveedor6.com', 18);

-- Bank Accounts
INSERT INTO bank_accounts (iban, swift, holder, branch, id_company) VALUES
('ES8921000000000000044444', 'CAIXESBBXXX', 'Proveedor Cuatro S.A.', 'Alicante Centro', 16),
('ES8921000000000000055555', 'BBVAESMMXXX', 'Proveedor Cinco S.L.', 'Málaga Puerto', 17),
('ES8921000000000000066666', 'BSCHESMMXXX', 'Proveedor Seis S.A.', 'Las Palmas Triana', 18);

-- Providers
INSERT INTO providers (default_language, default_vat, default_withholding, duedate, europe, enabled, id_company, id_owner_company) VALUES
('es', 0.21, 0.10, 30, 1, 1, 16, 2),
('en', 0.21, 0.15, 45, 1, 1, 17, 2),
('de', 0.10, 0.00, 60, 1, 1, 18, 2);

-- Price Schemes for Providers (Companies 13 to 18)
-- Provider 1 (Company 13, Owner 1)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Translation ES>EN', 0.07, 'word', 'totalWords', 'es', 'en', 13, 1);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Urgent jobs', 10, 11),
('Standard delivery', 0, 11);

-- Provider 2 (Company 14, Owner 1)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Proofreading EN', 0.05, 'word', 'totalWords', 'en', 'en', 14, 1);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Quick review', 0, 12),
('Detailed check', 5, 12),
('With comments', 3, 12);

-- Provider 3 (Company 15, Owner 1)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('FR>ES Technical', 0.09, 'word', 'totalWords', 'fr', 'es', 15, 1);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Manuals', 2, 13);

-- Provider 4 (Company 16, Owner 2)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('DE>EN Services', 0.11, 'word', 'totalWords', 'de', 'en', 16, 2);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Contracts', 4, 14),
('Reports', 0, 14);

-- Provider 5 (Company 17, Owner 2)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Review IT>EN', 0.06, 'word', 'totalWords', 'it', 'en', 17, 2);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Fast review', 1, 15);

-- Provider 6 (Company 18, Owner 2)
INSERT INTO schemes (scheme_name, price, units, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('ES>FR Marketing', 0.08, 'word', 'totalWords', 'es', 'fr', 18, 2);

INSERT INTO scheme_lines (descrip, discount, id_scheme) VALUES
('Email copy', 0, 16),
('Landing pages', 2, 16),
('Ad texts', 3, 16);
-- **********************
-- Invoices
-- **********************

-- 1. Insert exchange rates
INSERT INTO change_rates (currency1, currency2, rate) VALUES
('€', '€', 1.00),
('€', '$', 1.12),
('€', '£', 0.88);

-- Invoice 1
INSERT INTO documents (doc_number, doc_date, doc_type, status, language, vat_rate, withholding, total_net, total_vat, total_gross, total_withholding, total_to_pay, currency, note_delivery, note_payment, deadline, id_company, id_change_rate, id_bank_account, id_document_parent, id_owner_company) VALUES
('FAC-2025-001', '2025-02-15', 'INV_CUST', 'PENDING', 'es', 0.21, 0.15, 62.70, 13.17, 75.86, 9.40, 66.46, '€', NULL, 'Pago por transferencia a 30 días', '2025-03-17', 3, 1, 1, NULL, 1);

-- Orders linked to FAC-2025-001
INSERT INTO orders (descrip, date_order, price_per_unit, units, total, billed, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Pedido 1', '2025-02-15', 0.08, 'línea', 25.37, TRUE, 'Industry', 'es', 'fr', 3, 1),
('Pedido 2', '2025-02-15', 0.12, 'línea', 17.28, TRUE, 'Medical', 'en', 'fr', 3, 1),
('Pedido 3', '2025-02-15', 0.09, 'palabra', 20.04, TRUE, 'Legal', 'it', 'fr', 3, 1);

-- Order Items
INSERT INTO items (descrip, qty, discount, total, id_order) VALUES
('Línea Pedido 1', 341, 0.07, 25.37, 1),
('Línea Pedido 2', 150, 0.04, 17.28, 2),
('Línea Pedido 3', 256, 0.13, 20.04, 3);

-- Linking Orders to Document FAC-2025-001
INSERT INTO document_orders (id_document, id_order) VALUES
(1, 1),
(1, 2),
(1, 3);

-- Invoice 2
INSERT INTO documents (doc_number, doc_date, doc_type, status, language, vat_rate, withholding, total_net, total_vat, total_gross, total_withholding, total_to_pay, currency, note_delivery, note_payment, deadline, id_company, id_change_rate, id_bank_account, id_document_parent, id_owner_company) VALUES
('FAC-2025-002', '2025-03-05', 'INV_CUST', 'PENDING', 'es', 0.21, 0.15, 38.5, 8.09, 46.59, 5.78, 40.81, '€', NULL, 'Transferencia 30 días', '2025-04-05', 3, 1, 1, NULL, 1);

-- Orders
INSERT INTO orders (descrip, date_order, price_per_unit, units, total, billed, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Pedido 4', '2025-03-01', 0.10, 'línea', 25.00, TRUE, 'Finance', 'es', 'en', 3, 1),
('Pedido 5', '2025-03-01', 0.15, 'palabra', 15.00, TRUE, 'Legal', 'fr', 'en', 3, 1);

-- Items
INSERT INTO items (descrip, qty, discount, total, id_order) VALUES
('Línea Pedido 4', 250, 0.00, 25.00, 4),
('Línea Pedido 5', 100, 0.10, 13.50, 5);

-- DocumentOrders
INSERT INTO document_orders (id_document, id_order) VALUES
(2, 4),
(2, 5);

-- Invoice 3
INSERT INTO documents (doc_number, doc_date, doc_type, status, language, vat_rate, withholding, total_net, total_vat, total_gross, total_withholding, total_to_pay, currency, note_delivery, note_payment, deadline, id_company, id_change_rate, id_bank_account, id_document_parent, id_owner_company) VALUES
('FAC-2025-003', '2025-04-01', 'INV_CUST', 'PAID', 'es', 0.21, 0.15, 60.00, 12.60, 72.60, 9.00, 63.60, '€', NULL, 'Pago realizado', '2025-05-01', 3, 1, 1, NULL, 1);

-- Orders
INSERT INTO orders (descrip, date_order, price_per_unit, units, total, billed, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Pedido 6', '2025-03-25', 0.20, 'línea', 60.00, TRUE, 'Marketing', 'es', 'de', 3, 1);

-- Items
INSERT INTO items (descrip, qty, discount, total, id_order) VALUES
('Línea Pedido 6', 300, 0.00, 60.00, 6);

-- DocumentOrders
INSERT INTO document_orders (id_document, id_order) VALUES
(3, 6);

-- Invoice 4
INSERT INTO documents (doc_number, doc_date, doc_type, status, language, vat_rate, withholding, total_net, total_vat, total_gross, total_withholding, total_to_pay, currency, note_delivery, note_payment, deadline, id_company, id_change_rate, id_bank_account, id_document_parent, id_owner_company) VALUES
('FAC-2025-004', '2025-05-10', 'INV_CUST', 'PENDING', 'es', 0.21, 0.15, 50.65, 10.64, 61.29, 7.60, 53.69, '€', NULL, 'Pago 15 días', '2025-05-25', 3, 1, 1, NULL, 1);

-- Orders
INSERT INTO orders (descrip, date_order, price_per_unit, units, total, billed, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Pedido 7', '2025-05-08', 0.25, 'palabra', 25.00, TRUE, 'Medical', 'it', 'es', 3, 1),
('Pedido 8', '2025-05-08', 0.18, 'palabra', 25.65, TRUE, 'Tech', 'en', 'es', 3, 1);

-- Items
INSERT INTO items (descrip, qty, discount, total, id_order) VALUES
('Línea Pedido 7', 100, 0.00, 25.00, 7),
('Línea Pedido 8', 150, 0.05, 25.65, 8);

-- DocumentOrders
INSERT INTO document_orders (id_document, id_order) VALUES
(4, 7),
(4, 8);


-- **********************
-- Quotes
-- **********************

-- QUOTE 1 - PENDING
INSERT INTO documents (doc_number, doc_date, doc_type, status, language, vat_rate, withholding, total_net, total_vat, total_gross, total_withholding, total_to_pay, currency, note_delivery, note_payment, deadline, id_company, id_change_rate, id_bank_account, id_document_parent, id_owner_company) VALUES
('PRE-2025-001', '2025-06-01', 'QUOTE', 'PENDING', 'es', 0.21, 0.15, 40.00, 8.40, 48.40, 6.00, 42.40, '€', NULL, 'Condiciones a confirmar', '2025-06-15', 4, 1, 1, NULL, 1);

-- Orders
INSERT INTO orders (descrip, date_order, price_per_unit, units, total, billed, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Presupuesto Pedido 1', '2025-06-01', 0.10, 'línea', 40.00, FALSE, 'Marketing', 'es', 'en', 4, 1);

-- Items
INSERT INTO items (descrip, qty, discount, total, id_order) VALUES
('Línea Presupuesto 1', 400, 0.00, 40.00, 9);

-- DocumentOrders
INSERT INTO document_orders (id_document, id_order) VALUES
(5, 9);

-- QUOTE 2 - ACCEPTED
INSERT INTO documents (doc_number, doc_date, doc_type, status, language, vat_rate, withholding, total_net, total_vat, total_gross, total_withholding, total_to_pay, currency, note_delivery, note_payment, deadline, id_company, id_change_rate, id_bank_account, id_document_parent, id_owner_company) VALUES
('PRE-2025-002', '2025-06-03', 'QUOTE', 'ACCEPTED', 'es', 0.21, 0.10, 60.00, 12.60, 72.60, 6.00, 66.60, '€', NULL, 'Aceptado por cliente', '2025-06-20', 4, 1, 1, NULL, 1);

-- Orders
INSERT INTO orders (descrip, date_order, price_per_unit, units, total, billed, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Presupuesto Pedido 2', '2025-06-03', 0.15, 'palabra', 60.00, FALSE, 'Legal', 'fr', 'de', 4, 1);

-- Items
INSERT INTO items (descrip, qty, discount, total, id_order) VALUES
('Línea Presupuesto 2', 400, 0.00, 60.00, 10);

-- DocumentOrders
INSERT INTO document_orders (id_document, id_order) VALUES
(6, 10);

-- QUOTE 3 - REJECTED
INSERT INTO documents (doc_number, doc_date, doc_type, status, language, vat_rate, withholding, total_net, total_vat, total_gross, total_withholding, total_to_pay, currency, note_delivery, note_payment, deadline, id_company, id_change_rate, id_bank_account, id_document_parent, id_owner_company) VALUES
('PRE-2025-003', '2025-06-05', 'QUOTE', 'REJECTED', 'es', 0.21, 0.15, 30.00, 6.30, 36.30, 4.50, 31.80, '€', NULL, 'Rechazado por cliente', '2025-06-10', 4, 1, 1, NULL, 1);

-- Orders
INSERT INTO orders (descrip, date_order, price_per_unit, units, total, billed, field_name, source_language, target_language, id_company, id_owner_company) VALUES
('Presupuesto Pedido 3', '2025-06-05', 0.20, 'línea', 30.00, FALSE, 'Technical', 'en', 'es', 4, 1);

-- Items
INSERT INTO items (descrip, qty, discount, total, id_order) VALUES
('Línea Presupuesto 3', 150, 0.00, 30.00, 11);

-- DocumentOrders
INSERT INTO document_orders (id_document, id_order) VALUES
(7, 11);
