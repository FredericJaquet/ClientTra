-- Entity
CREATE INDEX idx_entity_vatNumber ON Entities(vatNumber);
CREATE INDEX idx_entity_legalName ON Entities(legalName);
CREATE INDEX idx_entity_comName ON Entities(comName);


-- Address
CREATE INDEX idx_address_idEntity ON Addresses(idEntity);
CREATE INDEX idx_address_city_country ON Addresses(city, country);

-- Phone
CREATE INDEX idx_phone_idEntity ON Phones(idEntity);

-- ContactPerson
CREATE INDEX idx_contact_entity ON ContactPersons(idEntity);

-- BankAccount
CREATE INDEX idx_bank_idEntity ON BankAccounts(idEntity);

-- Company
CREATE UNIQUE INDEX idx_company_entity ON Companies(idEntity);

-- User
CREATE INDEX idx_user_idCompany ON Users(idCompany);
CREATE INDEX idx_user_userName ON Users(userName);

-- Customer / Provider
CREATE INDEX idx_customer_entity ON Customers(idEntity);
CREATE INDEX idx_provider_entity ON Providers(idEntity);
CREATE INDEX idx_customer_enabled ON Customers(enabled);
CREATE INDEX idx_provider_enabled ON Providers(enabled);

-- SchemeEntity
CREATE INDEX idx_schemeentity_scheme ON SchemeEntities(idScheme);

-- Document
CREATE INDEX idx_document_type_status ON Documents(docType, status);
CREATE INDEX idx_document_entity ON Documents(idEntity);
CREATE INDEX idx_document_date ON Documents(docDate);
CREATE INDEX idx_document_parent ON Documents(idDocumentParent);

-- Orders
CREATE INDEX idx_orders_entity ON Orders(idEntity);
CREATE INDEX idx_orders_billed ON Orders(billed);
CREATE INDEX idx_orders_date ON Orders(dateOrder);

-- DocumentOrders
CREATE INDEX idx_docorders_orders ON DocumentOrders(idOrders);

-- Item
CREATE INDEX idx_item_order ON Items(idOrders);
