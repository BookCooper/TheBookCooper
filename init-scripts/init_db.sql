
CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL PRIMARY KEY,
  user_name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,  -- Should be hashed
  email VARCHAR(255) UNIQUE NOT NULL,
  b_bucks_balance DECIMAL(10, 2) DEFAULT 0,
  creation_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  last_login TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS book_info (
  book_id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  --isbn BIGINT NOT NULL UNIQUE,
  isbn NUMERIC(13, 0) NOT NULL UNIQUE,
  publish_date DATE,
  author VARCHAR(255),
  genre VARCHAR(255),
  book_condition VARCHAR(255),
  price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS book_listings (
  listing_id SERIAL PRIMARY KEY,
  user_id INT NOT NULL REFERENCES users(user_id),
  book_id INT NOT NULL REFERENCES book_info(book_id),
  listing_status VARCHAR(50),
  listing_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS book_transactions (
  transaction_id SERIAL PRIMARY KEY,
  buyer_id INT REFERENCES users(user_id),
  seller_id INT REFERENCES users(user_id),
  transaction_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  listing_id INT NOT NULL REFERENCES book_listings(listing_id),
  transaction_price DECIMAL(10, 2) NOT NULL,
  transaction_status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS book_searches (
  search_id SERIAL PRIMARY KEY,
  user_id INT REFERENCES users(user_id),
  search_query TEXT,
  search_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS store (
  store_id SERIAL PRIMARY KEY,
  item VARCHAR(255) NOT NULL,
  item_price DECIMAL(10, 2) NOT NULL,
  special_offer TEXT,
  item_description TEXT
);

CREATE TABLE IF NOT EXISTS point_transactions (
  bb_transaction_id SERIAL PRIMARY KEY,
  user_id INT REFERENCES users(user_id),
  transaction_type VARCHAR(50),
  amount DECIMAL(10, 2) NOT NULL,
  transaction_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  current_balance DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS book_tags (
  tag_id SERIAL PRIMARY KEY,
  tag_name VARCHAR(255) NOT NULL,
  book_id INT REFERENCES book_info(book_id)
);
