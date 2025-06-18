-- ******************
-- DELETE DATA
-- ******************
DELETE FROM DocumentOrders;
DELETE FROM Items;
DELETE FROM Orders;
DELETE FROM DocumentDetails;
DELETE FROM Documents;
DELETE FROM SchemeEntities;
DELETE FROM SchemeLines;
DELETE FROM Schemes;
DELETE FROM Providers;
DELETE FROM Customers;
DELETE FROM Users;
DELETE FROM Companies;
DELETE FROM BankAccounts;
DELETE FROM ContactPersons;
DELETE FROM Phones;
DELETE FROM Addresses;
DELETE FROM ChangeRates;
DELETE FROM Entities;


-- ******************
-- TEST DATA CLIENTTRA
-- ******************
-- ENTIDADES COMPANY (idEntity 1 a 2)
-- Entities
INSERT INTO Entities (vatNumber, comName, legalName, email, web) VALUES
('B12345678', 'Compañía Alfa', 'Compañía Alfa SL', 'contacto@alfa.com', 'www.alfa.com'),
('C87654321', 'Beta Traducciones', 'Beta Traducciones SA', 'info@beta.com', 'www.beta.com');

-- Addresses
INSERT INTO Addresses (street, stNumber, apt, cp, city, state, country, idEntity) VALUES
('Calle Mayor', '10', '3B', '28001', 'Madrid', 'Madrid', 'España', 1),
('Avenida Central', '45', NULL, '08002', 'Barcelona', 'Cataluña', 'España', 2);

-- Phones
INSERT INTO Phones (phoneNumber, kind, idEntity) VALUES
('600123456', 'móvil', 1),
('912345678', 'fijo', 2);

-- Contact Persons
INSERT INTO ContactPersons (firstname, middlename, lastname, role, email, idEntity) VALUES
('Luis', 'Fernando', 'Gómez', 'Gerente', 'luis.gomez@alfa.com', 1),
('Ana', 'Martínez', NULL, 'Responsable', 'ana.martinez@beta.com', 2);

-- Bank Accounts
INSERT INTO BankAccounts (iban, swift, holder, branch, idEntity) VALUES
('ES9121000418450200051332', 'BBVAESMMXXX', 'Compañía Alfa SL', 'Madrid Centro', 1),
('ES9820385778983000760236', 'CAIXESBBXXX', 'Beta Traducciones SA', 'Barcelona Diagonal', 2);

-- Companies
INSERT INTO Companies (idEntity) VALUES (1), (2);

-- Users
INSERT INTO Users (userName, passwd, email, idCompany, idRole, idPlan) VALUES
('adminAlfa', 'hashed_password_admin', 'admin@alfa.com', 1, 1, 2),
('accountingBeta', 'hashed_password_acc', 'accounting@beta.com', 2, 2, 1),
('userBeta', 'hashed_password_user', 'user@beta.com', 2, 3, 1);

-- Customer entities (idEntity 3 a 7)
INSERT INTO Entities (vatNumber, comName, legalName, email, web) VALUES
('D12345678', 'Cliente Uno SL', 'Cliente Uno SL', 'contacto@cliente1.com', 'www.cliente1.com'),
('E87654321', 'Cliente Dos SA', 'Cliente Dos SA', 'contacto@cliente2.com', 'www.cliente2.com'),
('F23456789', 'Cliente Tres SL', 'Cliente Tres SL', 'contacto@cliente3.com', 'www.cliente3.com'),
('G98765432', 'Cliente Cuatro SL', 'Cliente Cuatro SL', 'contacto@cliente4.com', 'www.cliente4.com'),
('H12349876', 'Cliente Cinco SA', 'Cliente Cinco SA', 'contacto@cliente5.com', 'www.cliente5.com');

-- Customers
INSERT INTO Customers (invoicingMethod, duedate, payMethod, defaultLanguage, defaultVAT, defaultWithholding, europe, enabled, idEntity) VALUES
('Email', 30, 'Transferencia', 'es', 0.21, 0.15, 1, 1, 3),
('Email', 45, 'Cheque', 'en', 0.21, 0.15, 1, 1, 4),
('Email', 15, 'Paypal', 'es', 0.21, 0.15, 1, 1, 5),
('Postal', 30, 'Transferencia', 'es', 0.21, 0.15, 1, 1, 6),
('Email', 60, 'Transferencia', 'en', 0.21, 0.15, 1, 1, 7);

-- Contacts Customers
INSERT INTO ContactPersons (firstname, middlename, lastname, role, email, idEntity) VALUES
('Laura', 'Pérez', NULL, 'Manager', 'laura.perez@cliente1.com', 3),
('Miguel', 'A.', 'Ruiz', 'Asistente', 'miguel.ruiz@cliente1.com', 3),
('Sofía', 'García', NULL, 'Directora', 'sofia.garcia@cliente2.com', 4),
('Carlos', 'M.', 'López', 'Jefe', 'carlos.lopez@cliente3.com', 5),
('Elena', 'Torres', NULL, 'Asistente', 'elena.torres@cliente3.com', 5),
('David', 'Fernández', NULL, 'Coordinador', 'david.fernandez@cliente3.com', 5),
('Ana', 'Martínez', NULL, 'Manager', 'ana.martinez@cliente4.com', 6),
('Jorge', 'L.', 'Sánchez', 'Director', 'jorge.sanchez@cliente5.com', 7),
('Lucía', 'Reyes', NULL, 'Administrativa', 'lucia.reyes@cliente5.com', 7);

-- Phone Customers
INSERT INTO Phones (phoneNumber, kind, idEntity) VALUES
('600111223', 'móvil', 3),
('910111223', 'fijo', 3),
('600222334', 'móvil', 4),
('600333445', 'móvil', 5),
('910333445', 'fijo', 5),
('912333445', 'fax', 5),
('600444556', 'móvil', 6),
('600555667', 'móvil', 7),
('910555667', 'fijo', 7);

-- Bank Account Customers
INSERT INTO BankAccounts (iban, swift, holder, branch, idEntity) VALUES
('ES0012345678901234567890', 'BBVAESMMXXX', 'Cliente Uno SL', 'Sucursal Madrid', 3),
('ES0023456789012345678901', 'CAIXESBBXXX', 'Cliente Dos SA', 'Sucursal Barcelona', 4),
('ES0034567890123456789012', 'SABRESBBXXX', 'Cliente Tres SL', 'Sucursal Sevilla', 5),
('ES0045678901234567890123', 'BBVAESMMXXX', 'Cliente Cuatro SL', 'Sucursal Valencia', 6),
('ES0056789012345678901234', 'CAIXESBBXXX', 'Cliente Cinco SA', 'Sucursal Bilbao', 7);

-- Provider Entities (idEntity 8 a 12)
INSERT INTO Entities (vatNumber, comName, legalName, email, web) VALUES
('I12345678', 'Proveedor Uno SL', 'Proveedor Uno SL', 'contacto@proveedor1.com', 'www.proveedor1.com'),
('J87654321', 'Proveedor Dos SA', 'Proveedor Dos SA', 'contacto@proveedor2.com', 'www.proveedor2.com'),
('K23456789', 'Proveedor Tres SL', 'Proveedor Tres SL', 'contacto@proveedor3.com', 'www.proveedor3.com'),
('L98765432', 'Proveedor Cuatro SL', 'Proveedor Cuatro SL', 'contacto@proveedor4.com', 'www.proveedor4.com'),
('M12349876', 'Proveedor Cinco SA', 'Proveedor Cinco SA', 'contacto@proveedor5.com', 'www.proveedor5.com');

-- Providers
INSERT INTO Providers (defaultLanguage, defaultVAT, defaultWithholding, europe, enabled, idEntity) VALUES
('es', 0.21, 0.15, 1, 1, 8),
('en', 0.21, 0.15, 1, 1, 9),
('es', 0.21, 0.15, 1, 1, 10),
('en', 0.21, 0.15, 1, 1, 11),
('es', 0.21, 0.15, 1, 1, 12);

-- Contacts Providers
INSERT INTO ContactPersons (firstname, middlename, lastname, role, email, idEntity) VALUES
('Pablo', 'Santos', NULL, 'Director', 'pablo.santos@proveedor1.com', 8),
('María', 'Núñez', NULL, 'Asistente', 'maria.nunez@proveedor1.com', 8),
('Fernando', 'J.', 'Cruz', 'Gerente', 'fernando.cruz@proveedor2.com', 9),
('Isabel', 'Mora', NULL, 'Coordinadora', 'isabel.mora@proveedor3.com', 10),
('Luis', 'Vega', NULL, 'Contable', 'luis.vega@proveedor3.com', 10),
('Elena', 'Ramírez', NULL, 'Directora', 'elena.ramirez@proveedor4.com', 11),
('Javier', 'M.', 'Lopez', 'Manager', 'javier.lopez@proveedor5.com', 12),
('Ana', 'Martín', NULL, 'Administrativa', 'ana.martin@proveedor5.com', 12),
('Carmen', 'Ortiz', NULL, 'Asistente', 'carmen.ortiz@proveedor5.com', 12);

-- Phone Providers
INSERT INTO Phones (phoneNumber, kind, idEntity) VALUES
('611123456', 'móvil', 8),
('911123456', 'fijo', 8),
('611223456', 'móvil', 9),
('611323456', 'móvil', 10),
('911323456', 'fijo', 10),
('912323456', 'fax', 10),
('611423456', 'móvil', 11),
('611523456', 'móvil', 12),
('911523456', 'fijo', 12);

-- Bnk Account Providers
INSERT INTO BankAccounts (iban, swift, holder, branch, idEntity) VALUES
('ES0067890123456789012345', 'BBVAESMMXXX', 'Proveedor Uno SL', 'Sucursal Madrid', 8),
('ES0078901234567890123456', 'CAIXESBBXXX', 'Proveedor Dos SA', 'Sucursal Barcelona', 9),
('ES0089012345678901234567', 'SABRESBBXXX', 'Proveedor Tres SL', 'Sucursal Sevilla', 10),
('ES0090123456789012345678', 'BBVAESMMXXX', 'Proveedor Cuatro SL', 'Sucursal Valencia', 11),
('ES0101234567890123456789', 'CAIXESBBXXX', 'Proveedor Cinco SA', 'Sucursal Bilbao', 12);

-- Scheme Customers (2 per customers, exept for customer 7)
INSERT INTO Schemes (schemeName, price, units, fieldName, sourceLanguage, targetLanguage) VALUES
('Traducción general cliente 3', 0.10, 'palabra', 'totalWords', 'es', 'en'),
('Revisión cliente 3', 0.05, 'palabra', 'totalWords', 'en', 'es'),
('Traducción general cliente 4', 0.11, 'palabra', 'totalWords', 'es', 'en'),
('Revisión cliente 4', 0.06, 'palabra', 'totalWords', 'en', 'es'),
('Traducción general cliente 5', 0.09, 'palabra', 'totalWords', 'es', 'en'),
('Revisión cliente 5', 0.04, 'palabra', 'totalWords', 'en', 'es'),
('Traducción general cliente 6', 0.12, 'palabra', 'totalWords', 'es', 'en'),
('Revisión cliente 6', 0.07, 'palabra', 'totalWords', 'en', 'es');

-- Scheme providers (1 per provider, exept for some providers)
INSERT INTO Schemes (schemeName, price, units, fieldName, sourceLanguage, targetLanguage) VALUES
('Traducción general proveedor 8', 0.10, 'palabra', 'totalWords', 'es', 'en'),
('Traducción general proveedor 9', 0.11, 'palabra', 'totalWords', 'es', 'en'),
('Traducción general proveedor 10', 0.09, 'palabra', 'totalWords', 'es', 'en'),
('Traducción general proveedor 11', 0.12, 'palabra', 'totalWords', 'es', 'en');

-- Relation between Schemes and Entities (SchemeEntity)
INSERT INTO SchemeEntities (idScheme, idEntity) VALUES
(1, 3), (2, 3),
(3, 4), (4, 4),
(5, 5), (6, 5),
(7, 6), (8, 6),
(9, 8),
(10, 9),
(11, 10),
(12, 11);

-- 1. Change Rate
INSERT INTO ChangeRates (currency1, currency2, rate) VALUES
('€', '€', 1.0),
('€', '$', 1.08),
('€', '£', 0.86);

-- 2. Documents for customers (3-7) and providers (8-12)
-- Customers (Invoices and quotes)
INSERT INTO Documents (docNumber, docDate, docType, status, language, vatRate, currency, noteDelivery, notePayment, deadline, idEntity, idChangeRate, idBankAccount) VALUES
-- Customer 3 (2 invoices and 3 quotes)
('FAC-2023-001', '2023-01-15', 'INV_CUST', 'PAID', 'es', 0.21, '€', 'Enviar por email', 'Transferencia en 30 días', '2023-02-14', 3, 1, 1),
('FAC-2023-002', '2023-03-20', 'INV_CUST', 'PENDING', 'es', 0.21, '€', 'Enviar por email', 'Transferencia en 30 días', '2023-04-19', 3, 1, 1),
('PRES-2023-001', '2023-01-05', 'QUOTE', 'ACCEPTED', 'es', 0.21, '€', NULL, NULL, '2023-02-04', 3, 1, NULL),
('PRES-2023-002', '2023-02-10', 'QUOTE', 'REJECTED', 'es', 0.21, '€', NULL, NULL, '2023-03-12', 3, 1, NULL),
('PRES-2023-003', '2023-04-01', 'QUOTE', 'PENDING', 'es', 0.21, '€', NULL, NULL, '2023-04-30', 3, 1, NULL),

-- customer 4 (3 invoices and 2 quotes)
('FAC-2023-003', '2023-02-10', 'INV_CUST', 'PAID', 'en', 0.21, '$', 'Email to accounting', 'Paypal payment', '2023-03-27', 4, 2, 2),
('FAC-2023-004', '2023-03-15', 'INV_CUST', 'PENDING', 'en', 0.21, '$', 'Email to manager', 'Paypal payment', '2023-04-29', 4, 2, 2),
('FAC-2023-005', '2023-05-01', 'INV_CUST', 'PENDING', 'en', 0.21, '€', 'Standard delivery', 'Bank transfer', '2023-06-30', 4, 1, 2),
('PRES-2023-004', '2023-01-20', 'QUOTE', 'ACCEPTED', 'en', 0.21, '$', NULL, NULL, '2023-02-19', 4, 2, NULL),
('PRES-2023-005', '2023-03-01', 'QUOTE', 'ACCEPTED', 'en', 0.21, '€', NULL, NULL, '2023-03-31', 4, 1, NULL),

-- Customer 5 (2 invoices and 3 quotes)
('FAC-2023-006', '2023-01-25', 'INV_CUST', 'PAID', 'es', 0.21, '€', 'Enviar a oficina central', 'Transferencia en 15 días', '2023-02-09', 5, 1, 3),
('FAC-2023-007', '2023-04-10', 'INV_CUST', 'PENDING', 'es', 0.21, '€', 'Enviar a delegación', 'Transferencia en 15 días', '2023-04-25', 5, 1, 3),
('PRES-2023-006', '2023-02-15', 'QUOTE', 'REJECTED', 'es', 0.21, '€', NULL, NULL, '2023-03-02', 5, 1, NULL),
('PRES-2023-007', '2023-03-20', 'QUOTE', 'ACCEPTED', 'es', 0.21, '€', NULL, NULL, '2023-04-04', 5, 1, NULL),
('PRES-2023-008', '2023-05-05', 'QUOTE', 'PENDING', 'es', 0.21, '€', NULL, NULL, '2023-05-20', 5, 1, NULL),

-- Documents for providers (invoices and POs)
-- Provider 8 (3 invoices and 2 POs)
('FAC-PROV-001', '2023-01-10', 'INV_PROV', 'PAID', 'es', 0.21, '€', NULL, 'Pagado por transferencia', '2023-02-09', 8, 1, 4),
('FAC-PROV-002', '2023-02-15', 'INV_PROV', 'PAID', 'es', 0.21, '€', NULL, 'Pagado por transferencia', '2023-03-17', 8, 1, 4),
('FAC-PROV-003', '2023-04-01', 'INV_PROV', 'PENDING', 'es', 0.21, '€', NULL, 'Pendiente de pago', '2023-04-30', 8, 1, 4),
('OC-2023-001', '2023-01-05', 'PO', 'ACCEPTED', 'es', 0.21, '€', 'Urgente', NULL, '2023-01-20', 8, 1, NULL),
('OC-2023-002', '2023-03-12', 'PO', 'ACCEPTED', 'es', 0.21, '€', 'Entrega parcial permitida', NULL, '2023-03-27', 8, 1, NULL),

-- Provider 9 (2 invoices and 3 POs)
('FAC-PROV-004', '2023-02-20', 'INV_PROV', 'PAID', 'en', 0.21, '£', NULL, 'Paid by bank transfer', '2023-03-22', 9, 3, 5),
('FAC-PROV-005', '2023-04-05', 'INV_PROV', 'PENDING', 'en', 0.21, '£', NULL, 'Pending payment', '2023-05-05', 9, 3, 5),
('OC-2023-003', '2023-01-15', 'PO', 'ACCEPTED', 'en', 0.21, '£', 'High priority', NULL, '2023-01-30', 9, 3, NULL),
('OC-2023-004', '2023-02-10', 'PO', 'REJECTED', 'en', 0.21, '£', 'Standard delivery', NULL, '2023-02-25', 9, 3, NULL),
('OC-2023-005', '2023-04-01', 'PO', 'ACCEPTED', 'en', 0.21, '£', 'Check quality before payment', NULL, '2023-04-16', 9, 3, NULL);

-- 3. Insertamos pedidos para cada documento (entre 1 y 5 por documento)
-- Orders for customers
INSERT INTO Orders (descrip, dateOrder, pricePerUnit, units, total, billed, fieldName, sourceLanguage, targetLanguage, idEntity) VALUES
-- Orders for FAC-2023-001 (Customer 3)
('Traducción manual técnico', '2023-01-10', 0.10, 'palabra', 0, 1, 'totalWords', 'es', 'en', 3),
('Revisión informe anual', '2023-01-12', 0.05, 'palabra', 0, 1, 'totalWords', 'en', 'es', 3),

-- Orders for FAC-2023-002 (Customer 3)
('Traducción folletos promocionales', '2023-03-15', 0.10, 'palabra', 0, 1, 'totalWords', 'es', 'en', 3),

-- Orders for PRES-2023-001 (Customer 3)
('Traducción contrato', '2023-01-03', 0.10, 'palabra', 0, 0, 'totalWords', 'es', 'en', 3),
('Revisión presentación', '2023-01-04', 0.05, 'palabra', 0, 0, 'totalWords', 'en', 'es', 3),

-- Orders for FAC-2023-003 (Customer 4)
('Website translation', '2023-02-05', 0.11, 'palabra', 0, 1, 'totalWords', 'es', 'en', 4),
('Product descriptions', '2023-02-07', 0.11, 'palabra', 0, 1, 'totalWords', 'es', 'en', 4),
('Review marketing materials', '2023-02-08', 0.06, 'palabra', 0, 1, 'totalWords', 'en', 'es', 4),

-- Orders for OC-2023-001 (Provider 8)
('Servicio de traducción urgente', '2023-01-03', 0.10, 'palabra', 0, 1, 'totalWords', 'es', 'en', 8),
('Corrección estilo', '2023-01-04', 0.07, 'palabra', 0, 1, 'totalWords', 'es', 'es', 8),

-- Orders for OC-2023-003 (Provider 9)
('Technical translation', '2023-01-12', 0.11, 'palabra', 0, 1, 'totalWords', 'es', 'en', 9),
('Proofreading service', '2023-01-13', 0.08, 'palabra', 0, 1, 'totalWords', 'en', 'en', 9);

-- 4. Relation between orders and documents
SET @first_order_id = 1;

INSERT INTO DocumentOrders (idDocument, idOrders) VALUES
-- FAC-2023-001
(1, @first_order_id), (1, @first_order_id + 1),
-- FAC-2023-002
(2, @first_order_id + 2),
-- PRES-2023-001
(3, @first_order_id + 3), (3, @first_order_id + 4),
-- FAC-2023-003
(4, @first_order_id + 5), (4, @first_order_id + 6), (4, @first_order_id + 7),
-- OC-2023-001
(16, @first_order_id + 8), (16, @first_order_id + 9),
-- OC-2023-003
(18, @first_order_id + 10), (18, @first_order_id + 11);

-- 5. Relation between ítems for each order (between 1 and 5 per order)
-- Items for Order 1 (@first_order_id)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('Capítulos 1-3', 1500, 0.05, 0, @first_order_id),
('Capítulos 4-6', 2000, 0.00, 0, @first_order_id),
('Anexos', 500, 0.10, 0, @first_order_id);

-- Items for Order 2 (@first_order_id + 1)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('Informe completo', 3000, 0.00, 0, @first_order_id + 1);

-- Items for Order 3 (@first_order_id + 2)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('Folletos principal', 2500, 0.00, 0, @first_order_id + 2),
('Folletos secundarios', 1500, 0.05, 0, @first_order_id + 2);

-- Items for Order 4 (@first_order_id + 3)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('DataSheet', 3230, 0.25, 0, @first_order_id + 3);

-- Items for Order 5 (@first_order_id + 4)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('No Match', 2500, 0.00, 0, @first_order_id + 4),
('50-74%', 1500, 0.15, 0, @first_order_id + 4);

-- Items for Order 6 (@first_order_id + 5)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('Homepage', 800, 0.00, 0, @first_order_id + 5),
('Product pages', 1200, 0.05, 0, @first_order_id + 5),
('About us', 500, 0.00, 0, @first_order_id + 5);

-- Items for Order 7 (@first_order_id + 6)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('No Match', 800, 0.00, 0, @first_order_id + 6),
('50-74%', 1200, 0.15, 0, @first_order_id + 6),
('75-84%', 500, 0.30, 0, @first_order_id + 6);

-- Items for Order 8 (@first_order_id + 7)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('No Match', 3000, 0.00, 0, @first_order_id + 7),
('95-99%', 1500, 0.75, 0, @first_order_id + 7);

-- Items for Order 9 (@first_order_id + 8)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('Documento principal', 3400, 0.10, 0, @first_order_id + 8),
('Anexos técnicos', 1900, 0.00, 0, @first_order_id + 8);

-- Items for Order 10 (@first_order_id + 9)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('Revisión', 1900, 0.00, 0, @first_order_id + 9);

-- Items for Order 11 (@first_order_id + 10)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('MTPE', 43000, 0.10, 0, @first_order_id + 10);

-- Items for Order 12 (@first_order_id + 11)
INSERT INTO Items (descrip, qty, discount, total, idOrders) VALUES
('Documento técnico', 12000, 0.00, 0, @first_order_id + 11),
('Anexos técnicos', 15000, 0.10, 0, @first_order_id + 11);

-- 6. Update totals of Items (sintaxis MySQL)
UPDATE Items i
JOIN Orders o ON i.idOrders = o.idOrders
SET i.total = i.qty * o.pricePerUnit * (1 - i.discount)
WHERE i.total = 0;

-- 7. Update totals of Orders (sintaxis MySQL)
UPDATE Orders o
JOIN (
  SELECT idOrders, SUM(total) as sum_total
  FROM Items
  GROUP BY idOrders
) i ON o.idOrders = i.idOrders
SET o.total = i.sum_total
WHERE o.total = 0;

-- 8. Update totals for Documents (sintaxis MySQL)
UPDATE Documents d
JOIN (
  SELECT do.idDocument, SUM(o.total) as sumTotal
  FROM DocumentOrders do
  JOIN Orders o ON do.idOrders = o.idOrders
  GROUP BY do.idDocument
) docTotals ON d.idDocument = docTotals.idDocument
SET 
  d.totalNet = docTotals.sumTotal,
  d.vat = d.vatRate,
  d.totalVat = d.totalNet * d.vatRate,
  d.totalGross = d.totalNet + (d.totalNet * d.vatRate);

-- 9. Insert document details (withholding and total to pay) - sintaxis MySQL
INSERT INTO DocumentDetails (idDocument, withholding, totalWithholding, totalToPay, paid)
SELECT 
  d.idDocument,
  CASE 
    WHEN d.docType = 'INV_CUST' THEN c.defaultWithholding
    WHEN d.docType = 'INV_PROV' THEN 0
    ELSE 0
  END as withholding,
  CASE 
    WHEN d.docType = 'INV_CUST' THEN d.totalNet * c.defaultWithholding
    ELSE 0
  END as totalWithholding,
  CASE
    WHEN d.docType = 'INV_CUST' THEN d.totalGross - (d.totalNet * c.defaultWithholding)
    ELSE d.totalGross
  END as totalToPay,
  CASE 
    WHEN d.status = 'PAID' THEN 1
    ELSE 0
  END as paid
FROM Documents d
LEFT JOIN Customers c ON d.idEntity = c.idEntity
WHERE d.docType IN ('INV_CUST', 'INV_PROV');
