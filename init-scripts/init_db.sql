
CREATE TABLE IF NOT EXISTS users (
  userId SERIAL PRIMARY KEY,
  userName VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,  -- Should be hashed
  email VARCHAR(255) UNIQUE NOT NULL,
  bBucksBalance DECIMAL(10, 2) DEFAULT 0,
  creationDate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  lastLogin TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS bookInfo (
  bookId SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  isbn INT UNIQUE,
  publishDate DATE,
  author VARCHAR(255),
  genre VARCHAR(255),
  bookStatus VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS bookListings (
  listingId SERIAL PRIMARY KEY,
  userId INT NOT NULL REFERENCES users(userId),
  price DECIMAL(10, 2) NOT NULL,
  bookId INT NOT NULL REFERENCES bookInfo(bookId),
  listingStatus VARCHAR(50),
  listDate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookTransactions (
  transactionId SERIAL PRIMARY KEY,
  buyerId INT REFERENCES users(userId),
  sellerId INT REFERENCES users(userId),
  transactionDate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  listingId INT NOT NULL REFERENCES bookListings(listingId),
  transactionPrice DECIMAL(10, 2) NOT NULL,
  transactionStatus VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS bookSearches (
  searchId SERIAL PRIMARY KEY,
  userId INT REFERENCES users(userId),
  searchQuery TEXT,
  searchDate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS store (
  storeId SERIAL PRIMARY KEY,
  item VARCHAR(255) NOT NULL,
  itemPrice DECIMAL(10, 2) NOT NULL,
  specialOffer TEXT,
  itemDescription TEXT
);

CREATE TABLE IF NOT EXISTS pointTransactions (
  bbTransactionId SERIAL PRIMARY KEY,
  userId INT REFERENCES users(userId),
  transactionType VARCHAR(50),
  amount DECIMAL(10, 2) NOT NULL,
  transactionDate TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  currentBalance DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS bookTags (
  tagId SERIAL PRIMARY KEY,
  tagName VARCHAR(255) NOT NULL,
  bookId INT REFERENCES bookInfo(bookId)
);
