-- Companies
CREATE INDEX idx_Company_vatNumber ON Companies(vatNumber);
CREATE INDEX idx_Company_legalName ON Companies(legalName);
CREATE INDEX idx_Company_comName ON Companies(comName);
CREATE INDEX idx_Company_owner ON Companies(idOwnerCompany);

-- Addresses
CREATE INDEX idx_address_idCompany ON Addresses(idCompany);

-- Phones
CREATE INDEX idx_phone_idCompany ON Phones(idCompany);

-- ContactPersons
CREATE INDEX idx_contact_Company ON ContactPersons(idCompany);

-- BankAccounts
CREATE INDEX idx_bank_idCompany ON BankAccounts(idCompany);

-- Users
CREATE INDEX idx_user_Company ON Users(idCompany);
CREATE INDEX idx_user_userName ON Users(userName);

-- Customers / Providers
CREATE INDEX idx_customer_Company ON Customers(idCompany);
CREATE INDEX idx_customer_owner ON Customers(idOwnerCompany);
CREATE INDEX idx_customer_enabled ON Customers(enabled);
CREATE INDEX idx_provider_Company ON Providers(idCompany);
CREATE INDEX idx_provider_owner ON Providers(idOwnerCompany);
CREATE INDEX idx_provider_enabled ON Providers(enabled);

-- Schemes
CREATE INDEX idx_scheme_owner ON Schemes(idOwnerCompany);

-- Documents
CREATE INDEX idx_document_type_status ON Documents(docType, status);
CREATE INDEX idx_document_Company ON Documents(idCompany);
CREATE INDEX idx_document_date ON Documents(docDate);
CREATE INDEX idx_document_parent ON Documents(idDocumentParent);
CREATE INDEX idx_document_owner ON Documents(idOwnerCompany);

-- Orders
CREATE INDEX idx_orders_Company ON Orders(idCompany);
CREATE INDEX idx_orders_owner ON Orders(idOwnerCompany);
CREATE INDEX idx_orders_billed ON Orders(billed);
CREATE INDEX idx_orders_date ON Orders(dateOrder);

-- DocumentOrders
CREATE INDEX idx_docorders_orders ON DocumentOrders(idOrders);

-- Items
CREATE INDEX idx_item_order ON Items(idOrders);
