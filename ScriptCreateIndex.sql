-- Entities
CREATE INDEX idx_entity_vatNumber ON Entities(vatNumber);
CREATE INDEX idx_entity_legalName ON Entities(legalName);
CREATE INDEX idx_entity_comName ON Entities(comName);
CREATE INDEX idx_entity_owner ON Entities(idOwnerEntity);

-- Addresses
CREATE INDEX idx_address_idEntity ON Addresses(idEntity);

-- Phones
CREATE INDEX idx_phone_idEntity ON Phones(idEntity);

-- ContactPersons
CREATE INDEX idx_contact_entity ON ContactPersons(idEntity);

-- BankAccounts
CREATE INDEX idx_bank_idEntity ON BankAccounts(idEntity);

-- Users
CREATE INDEX idx_user_entity ON Users(idEntity);
CREATE INDEX idx_user_userName ON Users(userName);

-- Customers / Providers
CREATE INDEX idx_customer_entity ON Customers(idEntity);
CREATE INDEX idx_customer_owner ON Customers(idOwnerEntity);
CREATE INDEX idx_customer_enabled ON Customers(enabled);
CREATE INDEX idx_provider_entity ON Providers(idEntity);
CREATE INDEX idx_provider_owner ON Providers(idOwnerEntity);
CREATE INDEX idx_provider_enabled ON Providers(enabled);

-- Schemes
CREATE INDEX idx_scheme_owner ON Schemes(idOwnerEntity);

-- Documents
CREATE INDEX idx_document_type_status ON Documents(docType, status);
CREATE INDEX idx_document_entity ON Documents(idEntity);
CREATE INDEX idx_document_date ON Documents(docDate);
CREATE INDEX idx_document_parent ON Documents(idDocumentParent);
CREATE INDEX idx_document_owner ON Documents(idOwnerEntity);

-- Orders
CREATE INDEX idx_orders_entity ON Orders(idEntity);
CREATE INDEX idx_orders_owner ON Orders(idOwnerEntity);
CREATE INDEX idx_orders_billed ON Orders(billed);
CREATE INDEX idx_orders_date ON Orders(dateOrder);

-- DocumentOrders
CREATE INDEX idx_docorders_orders ON DocumentOrders(idOrders);

-- Items
CREATE INDEX idx_item_order ON Items(idOrders);
