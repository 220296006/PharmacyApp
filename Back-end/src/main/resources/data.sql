USE pharmacyapp;

--INSERT INTO Users (id, image_url, first_name, middle_name, last_name, email, password, enabled, not_locked, using_mfa, phone, address)
-- VALUES
--    (1, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png','Chris', 'G', 'Taylor', 'chris.taylor@example.com', 'hashed_password', TRUE, TRUE, FALSE, '222-777-4444', '505 Willow St, Recoveryville'),
--   (2, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png', 'Jane', 'A', 'Johnson', 'jane.johnson@example.com', 'hashed_password', TRUE, TRUE, FALSE, '987-654-3210', '456 Oak St, Townsville'),
--   (3, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png', 'Michael', 'B', 'Brown', 'michael.brown@example.com', 'hashed_password', TRUE, TRUE, FALSE, '555-123-4567', '789 Pine St, Villagetown'),
--   (4, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png','Emily', 'C', 'Davis', 'emily.davis@example.com', 'hashed_password', TRUE, TRUE, FALSE, '333-999-7777', '101 Elm St, Healthcity'),
--   (5, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png','Christopher', 'D', 'Miller', 'christopher.miller@example.com', 'hashed_password', TRUE, TRUE, FALSE, '111-444-8888', '202 Maple St, Remedytown'),
--   (6, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png','Sophia', 'E', 'Wilson', 'sophia.wilson@example.com', 'hashed_password', TRUE, TRUE, FALSE, '777-222-5555', '303 Birch St, Ailmentville'),
--   (7, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png','Matthew', 'F', 'Moore', 'matthew.moore@example.com', 'hashed_password', TRUE, TRUE, FALSE, '999-666-3333', '404 Cedar St, Cureburg'),
--   (8, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png','Olivia', 'G', 'Taylor', 'olivia.taylor@example.com', 'hashed_password', TRUE, TRUE, FALSE, '222-777-4444', '505 Willow St, Recoveryville'),
--   (9, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png','Emma', 'H', 'Clark', 'emma.clark@example.com', 'hashed_password', TRUE, TRUE, FALSE, '444-111-9999', '606 Spruce St, Medicineville'),
--   (10, 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png','William', 'I', 'Hall', 'william.hall@example.com', 'hashed_password', TRUE, TRUE, FALSE, '666-333-2222', '707 Sycamore St, Remedytown');
--
--INSERT INTO Customers (id, user_id, city, state, zip_code)
--VALUES
--  (1,1,  'Ailmentville', 'State6', '44444'),
--  (2,2, 'Townsville', 'State2', '67890'),
--  (3,3, 'Villagetown', 'State3', '11111'),
--  (4,4, 'Healthcity', 'State4', '22222'),
--  (5,5, 'Remedytown', 'State5', '33333'),
--  (6,6, 'Ailmentville', 'State6', '44444'),
--  (7,7, 'Cureburg', 'State7', '55555'),
--  (8,8, 'Recoveryville', 'State8', '66666'),
--  (9,9, 'Medicineville', 'State9', '77777'),
--  (10,10,'Remedytown', 'State10', '88888');
--
--  INSERT INTO Prescriptions (id, customer_id, doctor_name, doctor_address, issue_date) VALUES
--    (1,1, 'Dr. Matsaba', '421 Makhaza Ave, Cityville', '2023-02-10'),
--    (2,2, 'Dr. Matsaba', '421 Makhaza Ave, Cityville', '2023-02-10'),
--    (3,3, 'Dr. Brown', '789 Wellness Blvd, Villagetown', '2023-03-10'),
--    (4,4, 'Dr. Davis', '101 Healing Rd, Healthcity', '2023-04-05'),
--    (5,5, 'Dr. Miller', '202 Cure Lane, Remedytown', '2023-05-12'),
--    (6,6, 'Dr. Wilson', '303 Recovery Dr, Ailmentville', '2023-06-18'),
--    (7,7, 'Dr. Moore', '404 Mend St, Cureburg', '2023-07-25'),
--    (8,8, 'Dr. Taylor', '505 Remedy Ave, Recoveryville', '2023-08-30'),
--    (9,9, 'Dr. Clark', '606 Health Ln, Medicineville', '2023-09-14'),
--    (10,10, 'Dr. Jane', '500 Brrokylm Ln, Lewisville', '2023-09-14');
--
--INSERT INTO Medications (id, prescription_id, name, dosage, frequency)
-- VALUES
--   (1,1, 'Amoxicillin', '10 mg', 'Once a day'),
--  (2,2, 'Amoxicillin', '10 mg', 'Once a day'),
--  (3,3, 'Lisinopril', '15 mg', 'Twice a day'),
--  (4,4, 'Omeprazole', '20 mg', 'Once a day'),
--  (5,5, 'Disprin', '30 mg', 'Three times a day'),
--  (6,6, 'Metformin', '5 mg', 'Once a day'),
--  (7,7, 'Ibuprofen', '30 mg', 'Twice a day'),
--  (8,8, 'Atorvastatin', '40 mg', 'Once a day'),
--  (9,9, 'Amlodipine', '50 mg', 'Three times a day'),
--  (10,10, 'GrandPa', '50 mg', 'Three times a day');
--
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (1, 1, '7.61', '2023-12-18 18:23:12', 'CANCELLED');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (2, 2, '7.61', '2023-12-18 18:23:12', 'CANCELLED');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (3, 3, '6.84', '2023-06-05 03:20:00', 'PENDING');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (4, 4, '1.46', '2023-03-02 00:23:18', 'PAID');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (5, 5, '9.85', '2023-11-26 18:54:44', 'PAID');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (6, 6, '7.50', '2023-10-29 16:36:24', 'OVERDUE');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (7, 7, '9.84', '2023-12-11 09:45:54', 'CANCELLED');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (8, 8, '7.33', '2023-04-06 12:26:14', 'OVERDUE');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (9, 9, '3.69', '2023-12-30 14:35:47', 'PAID');
--insert into Invoices (id, customer_id, amount, due_date, payment_status) values (10, 10, '9.50', '2024-12-30 14:35:47', 'PAID');


INSERT INTO Inventory (id, medication_id, name, description, quantity, price)
VALUES
  (1,1, 'Amoxicillin', 'Antibiotic', 50, '12.50'),
  (2,2, 'Amoxicillin', 'Antibiotic', 50, '12.50'),
  (3,3, 'Lisinopril', 'Blood pressure medication', 30, '8.75'),
  (4,4, 'Omeprazole', 'Acid reflux medication', 60, '20.50'),
  (5,5, 'Disprin', 'Pain medication', 60, '20.50'),
  (6,6, 'Metformin', 'Diabetes medication', 25, '18.90'),
  (7,7, 'Ibuprofen', 'Anti-inflammatory', 35, '10.75'),
  (8,8, 'Atorvastatin', 'Cholesterol medication', 45, '25.40'),
  (9,9, 'Amlodipine', 'Blood pressure medication', 55, '14.60'),
  (10,10, 'GrandPa', 'Pain medication', 55, '14.60');



