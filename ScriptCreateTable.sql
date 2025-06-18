DROP TABLE IF EXISTS DocumentOrders;
DROP TABLE IF EXISTS Items;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS DocumentDetails;
DROP TABLE IF EXISTS Documents;
DROP TABLE IF EXISTS SchemeEntities;
DROP TABLE IF EXISTS SchemeLines;
DROP TABLE IF EXISTS Schemes;
DROP TABLE IF EXISTS Providers;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Plans;
DROP TABLE IF EXISTS Roles;
DROP TABLE IF EXISTS Companies;
DROP TABLE IF EXISTS BankAccounts;
DROP TABLE IF EXISTS ContactPersons;
DROP TABLE IF EXISTS Phones;
DROP TABLE IF EXISTS Addresses;
DROP TABLE IF EXISTS ChangeRates;
DROP TABLE IF EXISTS Entities;

CREATE TABLE IF NOT EXISTS Entities(
  idEntity	INT(10) 	AUTO_INCREMENT,
  vatNumber	VARCHAR(25) 	NOT NULL UNIQUE,
  comName	VARCHAR(100),
  legalName	VARCHAR(100) 	NOT NULL UNIQUE,
  email		VARCHAR(100),
  web		VARCHAR(100),
  PRIMARY KEY 	(idEntity)
);

CREATE TABLE IF NOT EXISTS Addresses(
  idAddress	INT(10) 	AUTO_INCREMENT,
  street	VARCHAR(100) 	NOT NULL,
  stNumber	VARCHAR(10) 	NOT NULL,
  apt		VARCHAR(100),
  cp		VARCHAR(10) 	NOT NULL,
  city		VARCHAR(40) 	NOT NULL,
  state		VARCHAR(40),
  country	VARCHAR(40) 	NOT NULL,
  idEntity	INT(10),
  PRIMARY KEY(idAddress),
  FOREIGN KEY(idEntity)	REFERENCES Entities(idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Phones(
  phoneNumber	VARCHAR(25) 	NOT NULL UNIQUE,
  kind		VARCHAR(25),
  idEntity	INT(10) 	NOT NULL,
  PRIMARY KEY 	(phoneNumber),
  FOREIGN KEY 	(idEntity) 	REFERENCES Entities(idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ContactPersons(
  idContactPerson	INT(10) 	AUTO_INCREMENT,
  firstname		VARCHAR(40) 	NOT NULL,
  middlename		VARCHAR(40) 	NOT NULL,
  lastname		VARCHAR(40),
  role			VARCHAR(40),
  email			VARCHAR(100),
  idEntity		INT(10) 	NOT NULL,
  PRIMARY KEY		(idContactPerson),
  FOREIGN KEY 		(idEntity) 	REFERENCES Entities(idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS BankAccounts(
  idBankAccount	INT(10) AUTO_INCREMENT,
  iban		VARCHAR(40),
  swift		VARCHAR(15),
  holder	VARCHAR(40),
  branch	VARCHAR(40),
  idEntity	INT(10) 	NOT NULL,
  PRIMARY KEY	(idBankAccount),
  FOREIGN KEY 	(idEntity) 	REFERENCES Entities(idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Companies(
  idCompany	INT(10) 	AUTO_INCREMENT,
  idEntity	INT(10) 	NOT NULL,
  PRIMARY KEY	(idCompany),
  FOREIGN KEY	(idEntity)	REFERENCES Entities (idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Roles(
  idRole 	INT(10)		AUTO_INCREMENT,
  roleName 	VARCHAR(20) 	NOT NULL UNIQUE,
  PRIMARY KEY 	(idRole)
);

CREATE TABLE IF NOT EXISTS Plans(
  idPlan 	INT(10)		AUTO_INCREMENT,
  planName 	VARCHAR(20) 	NOT NULL UNIQUE,
  PRIMARY KEY 	(idPlan)
);

CREATE TABLE IF NOT EXISTS Users(
  idUser	INT(10) 	AUTO_INCREMENT,
  userName	VARCHAR(100) 	NOT NULL UNIQUE,
  passwd	VARCHAR(150) 	NOT NULL,
  email		VARCHAR(100),
  idCompany	INT(10) 	NOT NULL,
  idRole	INT(10) 	NOT NULL,
  idPlan	INT(10) 	NOT NULL,
  PRIMARY KEY (idUser),
  FOREIGN KEY (idCompany) 	REFERENCES Companies(idCompany) ON UPDATE CASCADE,
  FOREIGN KEY (idRole) 		REFERENCES Roles(idRole) ON UPDATE CASCADE,
  FOREIGN KEY (idPlan) 		REFERENCES Plans(idPlan) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Customers(
  idCustomer		INT(10)		AUTO_INCREMENT,
  invoicingMethod	VARCHAR(100),
  duedate 		INT(3),
  payMethod		VARCHAR(40),
  defaultLanguage	VARCHAR(10),
  defaultVAT		DOUBLE 		NOT NULL DEFAULT 0.21,
  defaultWithholding	DOUBLE 		NOT NULL DEFAULT 0.15,
  europe		boolean 	NOT NULL DEFAULT 1,
  enabled		boolean 	NOT NULL DEFAULT 1,
  idEntity		INT(10) 	NOT NULL,
  PRIMARY KEY 		(idCustomer),
  FOREIGN KEY 		(idEntity)	REFERENCES Entities(idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Providers(
  idProvider		INT(10) 	AUTO_INCREMENT,
  defaultLanguage	VARCHAR(10),
  defaultVAT		DOUBLE 		NOT NULL DEFAULT 0.21,
  defaultWithholding	DOUBLE 		NOT NULL DEFAULT 0.15,
  europe		boolean 	NOT NULL DEFAULT 1,
  enabled		boolean 	NOT NULL DEFAULT 1,
  idEntity		INT(10) 	NOT NULL,
  PRIMARY KEY 		(idProvider),
  FOREIGN KEY 		(idEntity)	REFERENCES Entities(idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Schemes(
  idScheme		INT(10) 	AUTO_INCREMENT,
  schemeName		VARCHAR(50)	NOT NULL,
  price			DOUBLE		NOT NULL,
  units			VARCHAR(25),
  fieldName		VARCHAR(15),
  sourceLanguage	VARCHAR(15),
  targetLanguage	VARCHAR(15),
  PRIMARY KEY (idScheme)
);

CREATE TABLE IF NOT EXISTS SchemeLines(
  idSchemeLine	INT(10) 	AUTO_INCREMENT,
  descrip	VARCHAR(255)	NOT NULL,
  discount	DOUBLE		NOT NULL DEFAULT 0,
  idScheme	INT(10) 	NOT NULL,
  PRIMARY KEY 	(idSchemeLine),
  FOREIGN KEY 	(idScheme)	REFERENCES Schemes(idScheme) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS SchemeEntities(
  idEntity	INT(10),
  idScheme	INT(10),
  PRIMARY KEY 	(idEntity, idScheme),
  FOREIGN KEY 	(idScheme) 	REFERENCES Schemes(idScheme) ON UPDATE CASCADE,
  FOREIGN KEY 	(idEntity)	REFERENCES Entities(idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ChangeRates(
  idChangeRate	INT(10) 	AUTO_INCREMENT, 
  currency1	VARCHAR(10) 	NOT NULL DEFAULT '€',
  currency2	VARCHAR(10) 	NOT NULL,
  rate		DOUBLE 		NOT NULL DEFAULT 1,
  PRIMARY KEY	(idChangeRate)
);

CREATE TABLE IF NOT EXISTS Documents(
  idDocument 		INT(10) 	AUTO_INCREMENT,
  docNumber 		VARCHAR(25)	NOT NULL,
  docDate		DATE		NOT NULL,
  docType 		VARCHAR(25)	NOT NULL,	-- ENUM('INV_CUST', 'INV_PROV', 'QUOTE', 'PO')
  status 		VARCHAR(25)	NOT NULL,	-- ENUM('PENDING', 'PAID', 'ACCEPTED', 'REJECTED')
  language 		VARCHAR(25),
  vatRate 		DOUBLE,				
  totalNet		DOUBLE,				
  vat			DOUBLE,
  totalVat		DOUBLE,
  totalGross		DOUBLE,
  currency 		VARCHAR(10)	NOT NULL DEFAULT '€',
  noteDelivery		VARCHAR(100),
  notePayment		VARCHAR(100),
  deadline		DATE,
  idEntity		INT(10),
  idChangeRate 		INT(10)		NOT NULL DEFAULT 1,
  idBankAccount		INT(10),
  idDocumentParent 	INT NULL, 			-- FK to Document (for conversion QUOTE->INV_CUST, PO->INV_PROV)
  PRIMARY KEY (idDocument),
  FOREIGN KEY (idEntity)		REFERENCES Entities(idEntity) ON UPDATE CASCADE,
  FOREIGN KEY (idChangeRate)		REFERENCES ChangeRates(idChangeRate) ON UPDATE CASCADE,
  FOREIGN KEY (idBankAccount)		REFERENCES BankAccounts(idBankAccount) ON UPDATE CASCADE,
  FOREIGN KEY (idDocumentParent)	REFERENCES Documents(idDocument) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS DocumentDetails (
  idDocument 	 	INT(10),
  withholding 		DOUBLE,
  totalWithholding	DOUBLE,
  totalToPay		DOUBLE,
  paid 			BOOLEAN NOT NULL DEFAULT 0,
  PRIMARY KEY (idDocument),
  FOREIGN KEY (idDocument) 	REFERENCES Documents(idDocument) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Orders(
  idOrders		INT(10) 	AUTO_INCREMENT,
  descrip		VARCHAR(100)	NOT NULL,
  dateOrder		DATE		NOT NULL,
  pricePerUnit		DOUBLE		NOT NULL,
  units			VARCHAR(15),
  total			DOUBLE		NOT NULL,
  billed		BOOLEAN		NOT NULL DEFAULT 0,
  fieldName		VARCHAR(25),
  sourceLanguage	VARCHAR(25),
  targetLanguage	VARCHAR(25),		
  idEntity		INT(10),
  PRIMARY KEY		(idOrders),
  FOREIGN KEY 		(idEntity) 	REFERENCES Entities(idEntity) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS DocumentOrders(
  idDocument	INT(10),
  idOrders	INT(10),
  PRIMARY KEY	(idDocument, idOrders),
  FOREIGN KEY	(idDocument) 		REFERENCES Documents(idDocument) ON UPDATE CASCADE,
  FOREIGN KEY	(idOrders)		REFERENCES Orders(idOrders) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Items(
  idItem	INT(10) 	AUTO_INCREMENT, 
  descrip	VARCHAR(255)	NOT NULL,
  qty		DOUBLE		NOT NULL,
  discount	DOUBLE		NOT NULL DEFAULT 0,
  total		DOUBLE		NOT NULL,
  idOrders	INT(10)		NOT NULL,
  PRIMARY KEY	(idItem),
  FOREIGN KEY	(idOrders)	REFERENCES Orders(idOrders) ON UPDATE CASCADE
);

INSERT INTO Roles (roleName) VALUES ('ADMIN'), ('ACCOUNTING'), ('USER');
INSERT INTO Plans (planName) VALUES ('FREEMIUM'), ('PREMIUM');