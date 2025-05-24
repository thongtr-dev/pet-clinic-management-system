DROP TABLE IF EXISTS medical_records;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS owners;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL
);

CREATE TABLE owners (
  id INT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(20) UNIQUE,
  phone VARCHAR(15) UNIQUE NOT NULL,
  address VARCHAR(200)
);

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

CREATE TABLE appointments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  pet_id INT,
  vet_name VARCHAR(100),
  appointment_date DATETIME,
  notes TEXT,
  FOREIGN KEY (pet_id) REFERENCES pets(id)
);

CREATE TABLE medical_records (
  id INT PRIMARY KEY AUTO_INCREMENT,
  pet_id INT,
  vaccination_date DATE,
  treatment TEXT,
  diagnosis TEXT,
  FOREIGN KEY (pet_id) REFERENCES pets(id)
);

-- Add some sample data
INSERT INTO users (username, password) VALUES 
    ('admin', 'admin123'),
    ('bac_si', 'doctor123');

INSERT INTO owners (full_name, email, phone, address) VALUES
    ('Nguyễn Văn An', 'an@example.com', '090-123-4567', '123 Lê Lợi, Hà Nội'),
    ('Trần Thị Mai', 'mai@example.com', '091-234-5678', '45 Nguyễn Huệ, TP.HCM');

INSERT INTO pets (name, species, breed, age, medical_history, owner_id) VALUES
    ('Milo', 'Dog', 'Phú Quốc', 5, 'Tiêm phòng đầy đủ hàng năm', 1),
    ('Mèo', 'Cat', 'Mèo Xiêm', 3, 'Đã triệt sản năm 2023', 1),
    ('Bông', 'Dog', 'Golden Retriever', 2, 'Không có vấn đề sức khỏe đáng kể', 2);

INSERT INTO appointments (pet_id, vet_name, appointment_date, notes) VALUES
    (1, 'BS. Trần Văn Nam', '2023-12-15 10:30:00', 'Khám sức khỏe định kỳ'),
    (2, 'BS. Lê Thị Hương', '2023-12-16 14:00:00', 'Tiêm nhắc vaccine'),
    (3, 'BS. Trần Văn Nam', '2023-12-17 09:15:00', 'Kiểm tra kích ứng da');

INSERT INTO medical_records (pet_id, vaccination_date, treatment, diagnosis) VALUES
    (1, '2023-06-10', 'Vaccine dại, vaccine Distemper', 'Khỏe mạnh, không phát hiện vấn đề'),
    (2, '2023-07-20', 'Vaccine FVRCP', 'Vấn đề răng nhẹ, đề xuất làm sạch lần tới'),
    (3, '2023-09-05', 'Vaccine Bordetella, phòng ngừa ký sinh trùng', 'Chó con khỏe mạnh, phát triển bình thường');