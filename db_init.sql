-- Création de la base de données
CREATE DATABASE IF NOT EXISTS eventdb;
USE eventdb;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYE', 'PARTICIPANT', 'PRESTATAIRE') NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    description TEXT,
    event_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    location VARCHAR(200),
    client_id INT,
    provider VARCHAR(100),
    status ENUM('PLANIFIE', 'EN_COURS', 'TERMINE', 'ANNULE') DEFAULT 'PLANIFIE',
    max_participants INT DEFAULT 100,
    price DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- Table des participants
CREATE TABLE IF NOT EXISTS participants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    type ENUM('PARTICIPANT', 'INTERVENANT') DEFAULT 'PARTICIPANT',
    company VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des inscriptions
CREATE TABLE IF NOT EXISTS inscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    participant_id INT NOT NULL,
    inscription_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('INSCRIT', 'CONFIRME', 'PRESENT', 'ABSENT') DEFAULT 'INSCRIT',
    payment_status ENUM('PENDING', 'PAID', 'REFUNDED') DEFAULT 'PENDING',
    amount_paid DECIMAL(10,2),
    notes TEXT,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE,
    UNIQUE KEY unique_inscription (event_id, participant_id)
);

-- Table des ressources
CREATE TABLE IF NOT EXISTS resources (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type ENUM('SALLE', 'MATERIEL', 'VEHICULE') NOT NULL,
    capacity INT,
    description TEXT,
    price_per_hour DECIMAL(10,2),
    status ENUM('DISPONIBLE', 'RESERVE', 'MAINTENANCE') DEFAULT 'DISPONIBLE',
    location VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des réservations de ressources
CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    resource_id INT NOT NULL,
    start_datetime DATETIME NOT NULL,
    end_datetime DATETIME NOT NULL,
    status ENUM('RESERVE', 'CONFIRME', 'ANNULE') DEFAULT 'RESERVE',
    cost DECIMAL(10,2),
    notes TEXT,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources(id)
);

-- Table des notifications
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT,
    recipient_email VARCHAR(100) NOT NULL,
    type ENUM('INVITATION', 'RAPPEL', 'CONFIRMATION', 'ANNULATION') NOT NULL,
    subject VARCHAR(200),
    message TEXT,
    sent_at TIMESTAMP,
    status ENUM('PENDING', 'SENT', 'FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-- Données de test pour les utilisateurs
INSERT INTO users (username, password, role, email, phone) VALUES
('admin', 'admin123', 'ADMIN', 'admin@eventmanager.com', '0601020304'),
('employe', 'employe123', 'EMPLOYE', 'employe@eventmanager.com', '0605060708');

-- Données de test pour les clients
INSERT INTO clients (name, email, phone) VALUES
('Client A', 'clienta@mail.com', '0600000000'),
('Client B', 'clientb@mail.com', '0611111111'),
('Entreprise Tech Solutions', 'contact@techsolutions.com', '0622333444');

-- Données de test pour les participants
INSERT INTO participants (name, email, phone, type, company) VALUES
('Jean Dupont', 'jean.dupont@email.com', '0612345678', 'PARTICIPANT', 'TechCorp'),
('Marie Martin', 'marie.martin@email.com', '0623456789', 'INTERVENANT', 'ConseilPro'),
('Pierre Durand', 'pierre.durand@email.com', '0634567890', 'PARTICIPANT', 'Innovation Ltd'),
('Sophie Laurent', 'sophie.laurent@email.com', '0645678901', 'INTERVENANT', 'Expert Consulting');

-- Données de test pour les ressources
INSERT INTO resources (name, type, capacity, description, price_per_hour, location) VALUES
('Salle de Conférence A', 'SALLE', 50, 'Salle équipée avec vidéoprojecteur et système audio', 75.00, 'Bâtiment Principal - 1er étage'),
('Salle de Réunion B', 'SALLE', 20, 'Salle intimiste pour réunions', 40.00, 'Bâtiment Principal - 2ème étage'),
('Vidéoprojecteur HD', 'MATERIEL', NULL, 'Projecteur haute définition avec cables', 25.00, 'Local technique'),
('Système Audio', 'MATERIEL', NULL, 'Micros sans fil et enceintes', 35.00, 'Local technique'),
('Van de Transport', 'VEHICULE', 8, 'Véhicule pour transport de matériel', 50.00, 'Parking');

-- Données de test pour les événements (mise à jour)
INSERT INTO events (title, description, event_date, start_time, end_time, location, client_id, provider, status, max_participants, price) VALUES
('Mariage Dupont', 'Cérémonie de mariage avec réception', '2024-07-01', '14:00:00', '23:00:00', 'Château de la Rose', 1, 'Prestataire X', 'PLANIFIE', 150, 2500.00),
('Séminaire Entreprise', 'Formation en management d\'équipe', '2024-08-15', '09:00:00', '17:00:00', 'Centre de Formation TechSolutions', 2, 'Prestataire Y', 'PLANIFIE', 30, 800.00),
('Conférence Innovation 2024', 'Conférence sur les nouvelles technologies', '2024-09-20', '08:30:00', '18:00:00', 'Centre des Congrès', 3, 'Event Pro', 'PLANIFIE', 200, 150.00);

-- Données de test pour les inscriptions
INSERT INTO inscriptions (event_id, participant_id, status, payment_status, amount_paid, notes) VALUES
(1, 1, 'CONFIRME', 'PAID', 150.00, 'Inscription VIP avec cocktail'),
(1, 2, 'INSCRIT', 'PENDING', 0.00, 'En attente de confirmation'),
(2, 3, 'CONFIRME', 'PAID', 800.00, 'Formation complète 2 jours'),
(2, 4, 'PRESENT', 'PAID', 800.00, 'Formateur principal'),
(3, 1, 'INSCRIT', 'PENDING', 150.00, 'Participant général'),
(3, 3, 'CONFIRME', 'PAID', 150.00, 'Accès premium');

-- Données de test pour les réservations de ressources
INSERT INTO reservations (event_id, resource_id, start_datetime, end_datetime, status, cost, notes) VALUES
(1, 1, '2024-07-01 13:00:00', '2024-07-02 01:00:00', 'CONFIRME', 900.00, 'Salle principale pour réception'),
(1, 4, '2024-07-01 18:00:00', '2024-07-01 23:00:00', 'CONFIRME', 175.00, 'Système audio pour la soirée'),
(2, 2, '2024-08-15 08:00:00', '2024-08-15 18:00:00', 'CONFIRME', 400.00, 'Salle de formation'),
(2, 3, '2024-08-15 09:00:00', '2024-08-15 17:00:00', 'CONFIRME', 200.00, 'Projecteur pour présentations'),
(3, 1, '2024-09-20 08:00:00', '2024-09-20 19:00:00', 'RESERVE', 825.00, 'Grande salle de conférence'); 