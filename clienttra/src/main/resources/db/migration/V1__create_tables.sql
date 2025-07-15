DROP TABLE IF EXISTS document_orders;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS document_details;
DROP TABLE IF EXISTS documents;
DROP TABLE IF EXISTS scheme_lines;
DROP TABLE IF EXISTS Schemes;
DROP TABLE IF EXISTS providers;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS plans;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS bank_accounts;
DROP TABLE IF EXISTS contact_persons;
DROP TABLE IF EXISTS phones;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS change_rates;
DROP TABLE IF EXISTS companies;

CREATE TABLE IF NOT EXISTS companies (
  id_company           INT AUTO_INCREMENT PRIMARY KEY,
  vat_number           VARCHAR(25),
  com_name             VARCHAR(100),
  legal_name           VARCHAR(100),
  email                VARCHAR(100),
  web                  VARCHAR(100),
  logo_path            VARCHAR(255),
  id_owner_company     INT,
  CONSTRAINT uq_companies_vat_owner	 UNIQUE(vat_number, id_owner_company),
  CONSTRAINT uq_companies__legal_owner	 UNIQUE(legal_name, id_owner_company),
  FOREIGN KEY (id_owner_company) REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS addresses (
  id_address           INT AUTO_INCREMENT PRIMARY KEY,
  street               VARCHAR(100) NOT NULL,
  st_number            VARCHAR(10) NOT NULL,
  apt                  VARCHAR(100),
  cp                   VARCHAR(10) NOT NULL,
  city                 VARCHAR(40) NOT NULL,
  state                VARCHAR(40),
  country              VARCHAR(40) NOT NULL,
  id_company           INT,
  FOREIGN KEY (id_company) REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE phones (
  id_phone 		INT 		AUTO_INCREMENT PRIMARY KEY,
  phone_number 		VARCHAR(25) 	NOT NULL,
  kind 			VARCHAR(25),
  id_company 		INT 		NOT NULL,
  FOREIGN KEY (id_company) 		REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS contact_persons (
  id_contact_person    INT AUTO_INCREMENT PRIMARY KEY,
  firstname            VARCHAR(40) NOT NULL,
  middlename           VARCHAR(40) NOT NULL,
  lastname             VARCHAR(40),
  role                 VARCHAR(40),
  email                VARCHAR(100),
  id_company           INT NOT NULL,
  FOREIGN KEY (id_company) REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bank_accounts (
  id_bank_account      INT AUTO_INCREMENT PRIMARY KEY,
  iban                 VARCHAR(34) NOT NULL,
  swift                VARCHAR(11),
  holder               VARCHAR(40),
  branch               VARCHAR(40),
  id_company           INT NOT NULL,
  FOREIGN KEY (id_company) REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS roles (
  id_role              INT AUTO_INCREMENT PRIMARY KEY,
  role_name            VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS plans (
  id_plan              INT AUTO_INCREMENT PRIMARY KEY,
  plan_name            VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
  id_user              INT AUTO_INCREMENT PRIMARY KEY,
  user_name            VARCHAR(100) NOT NULL UNIQUE,
  passwd               VARCHAR(255) NOT NULL,
  email                VARCHAR(100),
  preferred_language   VARCHAR(10) DEFAULT 'es',
  preferred_theme      VARCHAR(20) DEFAULT 'red',
  dark_mode	       BOOLEAN DEFAULT FALSE,
  enabled	       BOOLEAN NOT NULL DEFAULT TRUE,
  id_company           INT NOT NULL,
  id_role              INT NOT NULL,
  id_plan              INT NOT NULL,
  FOREIGN KEY (id_company) 	REFERENCES companies(id_company) ON UPDATE CASCADE,
  FOREIGN KEY (id_role) 	REFERENCES roles(id_role) ON UPDATE CASCADE,
  FOREIGN KEY (id_plan) 	REFERENCES plans(id_plan) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS customers (
  id_customer          INT 		AUTO_INCREMENT PRIMARY KEY,
  invoicing_method     VARCHAR(100),
  duedate              INT,
  pay_method           VARCHAR(40),
  default_language     VARCHAR(10),
  default_vat          DOUBLE 		NOT NULL DEFAULT 0.21,
  default_withholding  DOUBLE 		NOT NULL DEFAULT 0.15,
  europe               BOOLEAN 		NOT NULL DEFAULT 1,
  enabled              BOOLEAN		NOT NULL DEFAULT 1,
  id_company           INT 		NOT NULL,
  id_owner_company     INT 		NOT NULL,
  FOREIGN KEY (id_company) 		REFERENCES companies(id_company) ON UPDATE CASCADE,
  FOREIGN KEY (id_owner_company) 	REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS providers (
  id_provider          INT		AUTO_INCREMENT PRIMARY KEY,
  default_language     VARCHAR(10),
  default_vat          DOUBLE 		NOT NULL DEFAULT 0.21,
  default_withholding  DOUBLE 		NOT NULL DEFAULT 0.15,
  duedate              INT,
  europe               BOOLEAN 		NOT NULL DEFAULT 1,
  enabled              BOOLEAN 		NOT NULL DEFAULT 1,
  id_company           INT 		NOT NULL,
  id_owner_company     INT 		NOT NULL,
  FOREIGN KEY (id_company) REFERENCES companies(id_company) ON UPDATE CASCADE,
  FOREIGN KEY (id_owner_company) REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS schemes (
  id_scheme            INT AUTO_INCREMENT PRIMARY KEY,
  scheme_name          VARCHAR(50) NOT NULL,
  price                DOUBLE NOT NULL,
  units                VARCHAR(25),
  field_name           VARCHAR(15),
  source_language      VARCHAR(15),
  target_language      VARCHAR(15),
  id_company           INT NOT NULL,
  id_owner_company     INT NOT NULL,
  FOREIGN KEY (id_company) REFERENCES companies(id_company) ON UPDATE CASCADE,
  FOREIGN KEY (id_owner_company) REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS scheme_lines (
  id_scheme_line       INT 		AUTO_INCREMENT PRIMARY KEY,
  descrip              VARCHAR(255) 	NOT NULL,
  discount             DOUBLE 		NOT NULL DEFAULT 0,
  id_scheme            INT 		NOT NULL,
  FOREIGN KEY (id_scheme) 		REFERENCES schemes(id_scheme) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS change_rates (
  id_change_rate       INT AUTO_INCREMENT PRIMARY KEY,
  currency1            VARCHAR(10) NOT NULL,
  currency2            VARCHAR(10) NOT NULL,
  rate                 DECIMAL(10,6) NOT NULL DEFAULT 1.000000,
  date                 DATE NOT NULL,
  id_owner_company     INT NOT NULL,

  FOREIGN KEY (id_owner_company)	REFERENCES companies(id_company) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS documents (
  id_document          INT AUTO_INCREMENT PRIMARY KEY,
  doc_number           VARCHAR(25) NOT NULL,
  doc_date             DATE NOT NULL,
  doc_type             VARCHAR(25) NOT NULL,
  status               VARCHAR(25) NOT NULL,
  language             VARCHAR(25),
  vat_rate             DOUBLE NOT NULL DEFAULT 0,
  withholding          DOUBLE NOT NULL DEFAULT 0,
  total_net            DOUBLE NOT NULL DEFAULT 0,
  total_vat            DOUBLE NOT NULL DEFAULT 0,
  total_gross          DOUBLE NOT NULL DEFAULT 0,
  total_withholding    DOUBLE NOT NULL DEFAULT 0,
  total_to_pay         DOUBLE NOT NULL DEFAULT 0,
  currency             VARCHAR(10) NOT NULL DEFAULT '€',
  note_delivery        VARCHAR(100),
  note_payment         VARCHAR(255),
  note_comment	       VARCHAR(1000),
  deadline             DATE,
  id_company           INT,
  id_change_rate       INT NOT NULL DEFAULT 1,
  id_bank_account      INT,
  id_document_parent   INT,
  id_owner_company     INT NOT NULL,
  FOREIGN KEY (id_company) REFERENCES companies(id_company) ON UPDATE CASCADE,
  FOREIGN KEY (id_change_rate) REFERENCES change_rates(id_change_rate) ON UPDATE CASCADE,
  FOREIGN KEY (id_bank_account) REFERENCES bank_accounts(id_bank_account) ON UPDATE CASCADE,
  FOREIGN KEY (id_document_parent) REFERENCES documents(id_document) ON UPDATE CASCADE,
  FOREIGN KEY (id_owner_company) REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
  id_order             INT 		AUTO_INCREMENT PRIMARY KEY,
  descrip              VARCHAR(100) 	NOT NULL,
  date_order           DATE 		NOT NULL,
  price_per_unit       DOUBLE 		NOT NULL,
  units                VARCHAR(15),
  total                DOUBLE 		NOT NULL DEFAULT 0,
  billed               BOOLEAN 		NOT NULL DEFAULT 0,
  field_name           VARCHAR(25),
  source_language      VARCHAR(25),
  target_language      VARCHAR(25),
  id_company           INT,
  id_owner_company     INT 		NOT NULL,
  FOREIGN KEY (id_company) 		REFERENCES companies(id_company) ON UPDATE CASCADE,
  FOREIGN KEY (id_owner_company) 	REFERENCES companies(id_company) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS document_orders (
  id_document   INT,
  id_order	INT,
  PRIMARY KEY (id_document, id_order),
  FOREIGN KEY (id_document) REFERENCES documents(id_document) ON UPDATE CASCADE,
  FOREIGN KEY (id_order) REFERENCES orders(id_order) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
  id_item              INT AUTO_INCREMENT PRIMARY KEY, 
  descrip              VARCHAR(255) NOT NULL,
  qty                  DOUBLE NOT NULL,
  discount             DOUBLE NOT NULL DEFAULT 0,
  total                DOUBLE NOT NULL DEFAULT 0,
  id_order             INT NOT NULL,
  FOREIGN KEY (id_order) REFERENCES orders(id_order) ON UPDATE CASCADE
);

-- Datos iniciales
INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN'), ('ROLE_ACCOUNTING'), ('ROLE_USER');
INSERT INTO plans (plan_name) VALUES ('FREEMIUM'), ('PREMIUM'), ('TEST');
INSERT INTO change_rates value ('€','€',1,'2025-01-01',1);
