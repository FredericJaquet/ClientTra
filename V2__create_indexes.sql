-- Índices adicionales

-- Companies
CREATE UNIQUE INDEX idx_companies_vat_number ON companies(vat_number);
CREATE UNIQUE INDEX idx_companies_legal_name ON companies(legal_name);

-- Users
CREATE UNIQUE INDEX idx_users_user_name ON users(user_name);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_company ON users(id_company);

-- Addresses
CREATE INDEX idx_addresses_company ON addresses(id_company);
CREATE INDEX idx_addresses_city_country ON addresses(city, country);

-- Phones
CREATE INDEX idx_phones_company ON phones(id_company);

-- ContactPersons
CREATE INDEX idx_contact_persons_company ON contact_persons(id_company);

-- BankAccounts
CREATE INDEX idx_bank_accounts_company ON bank_accounts(id_company);

-- Customers
CREATE INDEX idx_customers_company ON customers(id_company);
CREATE INDEX idx_customers_owner_company ON customers(id_owner_company);
CREATE INDEX idx_customers_enabled ON customers(enabled);

-- Providers
CREATE INDEX idx_providers_company ON providers(id_company);
CREATE INDEX idx_providers_owner_company ON providers(id_owner_company);
CREATE INDEX idx_providers_enabled ON providers(enabled);

-- Documents
CREATE INDEX idx_documents_doc_number ON documents(doc_number);
CREATE INDEX idx_documents_company ON documents(id_company);
CREATE INDEX idx_documents_type_status ON documents(doc_type, status);
CREATE INDEX idx_documents_date ON documents(doc_date);
CREATE INDEX idx_documents_owner_company ON documents(id_owner_company);

-- Orders
CREATE INDEX idx_orders_company ON orders(id_company);
CREATE INDEX idx_orders_billed ON orders(billed);
CREATE INDEX idx_orders_owner_company ON orders(id_owner_company);

-- DocumentOrders
CREATE INDEX idx_document_orders_document ON document_orders(id_document);
CREATE INDEX idx_document_orders_order ON document_orders(id_order);

-- Items
CREATE INDEX idx_items_order ON items(id_order);

-- Schemes
CREATE INDEX idx_schemes_company ON schemes(id_company);
CREATE INDEX idx_schemes_owner_company ON schemes(id_owner_company);

-- SchemeLines
CREATE INDEX idx_scheme_lines_scheme ON scheme_lines(id_scheme);

-- ChangeRates
CREATE INDEX idx_change_rates_pair ON change_rates(currency1, currency2);
