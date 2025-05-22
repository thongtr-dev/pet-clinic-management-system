-- Users table for authentication
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL
);

-- Owners table
CREATE TABLE owners (
  id INT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  contact VARCHAR(20),
  address VARCHAR(200)
);

-- Pets table (linked to owners)
CREATE TABLE pets (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  species VARCHAR(50),
  breed VARCHAR(50),
  age INT,
  medical_history TEXT,
  owner_id INT,
  FOREIGN KEY (owner_id) REFERENCES owners(id)
);

-- Appointments table
CREATE TABLE appointments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  pet_id INT,
  vet_name VARCHAR(100),
  appointment_date DATETIME,
  notes TEXT,
  FOREIGN KEY (pet_id) REFERENCES pets(id)
);

-- Medical records table
CREATE TABLE medical_records (
  id INT PRIMARY KEY AUTO_INCREMENT,
  pet_id INT,
  vaccination_date DATE,
  treatment TEXT,
  diagnosis TEXT,
  FOREIGN KEY (pet_id) REFERENCES pets(id)
);