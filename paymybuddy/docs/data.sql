USE mydb;

INSERT INTO users (username, email, password, balance)
VALUES
('Rebecca', 'rebecca@test.com', 'password123', 500.00),
('Guto', 'guto@test.com', 'password123', 300.00),
('Sarah', 'sarah@test.com', 'password123', 200.00);

INSERT INTO users_connections (user_id, friend_id)
VALUES
(1, 2),
(1, 3);

INSERT INTO transactions
(sender_id, receiver_id, description, amount)
VALUES
(1, 2, 'Restaurant', 20.00),
(2, 1, 'Cinema', 15.00),
(1, 3, 'Vacances', 50.00);