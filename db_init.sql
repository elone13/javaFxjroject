-- Création de la base de données
CREATE DATABASE IF NOT EXISTS eventdb;
USE eventdb;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYE') NOT NULL
);

-- Table des clients
CREATE TABLE IF NOT EXISTS clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- Table des événements
CREATE TABLE IF NOT EXISTS events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    event_date DATE NOT NULL,
    client_id INT,
    provider VARCHAR(100),
    status VARCHAR(50),
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- Données de test pour les utilisateurs
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('employe', 'employe123', 'EMPLOYE');

-- Données de test pour les clients
INSERT INTO clients (name, email, phone) VALUES
('Client A', 'a@mail.com', '0600000000'),
('Client B', 'b@mail.com', '0611111111');

-- Données de test pour les événements
INSERT INTO events (title, event_date, client_id, provider, status) VALUES
('Mariage Dupont', '2024-07-01', 1, 'Prestataire X', 'Prévu'),
('Séminaire Entreprise', '2024-08-15', 2, 'Prestataire Y', 'En attente'); 